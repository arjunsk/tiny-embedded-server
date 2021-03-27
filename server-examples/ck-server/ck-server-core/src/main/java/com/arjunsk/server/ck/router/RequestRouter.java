package com.arjunsk.server.ck.router;

import com.arjunsk.server.ck.domain.CkHttpExchange;
import com.arjunsk.server.ck.enums.HttpMethod;
import com.arjunsk.server.ck.handler.Handler;
import com.arjunsk.server.ck.handler.HandlerManager;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/** Creates the exchange and passes it to the corresponding handler. */
public class RequestRouter implements Runnable {

  // Connection Socket
  private final Socket conn;

  // Holds all the handlers
  private final HandlerManager handlerManager;

  // NOTE: We are passing the hanlder from CKHttpServer to ensure that there is only one
  // hanlderManager for 1 server instance.
  public RequestRouter(Socket conn, HandlerManager handlerManager) {
    this.conn = conn;
    this.handlerManager = handlerManager;
  }

  @Override
  public void run() {

    // in: requestBody
    // out: responseBody
    try (BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        OutputStream out = new BufferedOutputStream(conn.getOutputStream())) {

      Optional<CkHttpExchange> exchange = createExchange(in, out);

      if (exchange.isPresent()) {

        String path = exchange.get().getPath();
        HttpMethod method = exchange.get().getMethod();

        // Fetch handler using path and method.
        Handler handler = handlerManager.getHandler(path, method);

        // Invoke handler
        handler.handle(exchange.get());

        // Finally flush the responseBody.
        out.flush();
      }
    } catch (Exception ex) {
      ex.printStackTrace();
    } finally {
      try {
        conn.close(); // close the socket
      } catch (Exception e) {
        System.err.println("Error closing stream : " + e.getMessage());
      }
    }
  }

  /**
   * Create a exchange using requestBody and include responseBody as well to the Exchange.
   *
   * <p>Sample request body (From chrome):
   *
   * <pre>
   * GET /echoHeader HTTP/1.1
   * Host: localhost:8080
   * Connection: keep-alive
   * Cache-Control: max-age=0
   * sec-ch-ua: "Google Chrome";v="89", "Chromium";v="89", ";Not A Brand";v="99"
   * sec-ch-ua-mobile: ?0
   * Upgrade-Insecure-Requests: 1
   * User-Agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/89.0.4389.90 Safari/537.36
   * Sec-Fetch-Site: none
   * Sec-Fetch-Mode: navigate
   * Sec-Fetch-User: ?1
   * Sec-Fetch-Dest: document
   * Accept-Encoding: gzip, deflate, br
   * Accept-Language: en-GB,en-US;q=0.9,en;q=0.8
   * </pre>
   *
   * @param in Request Body
   * @param out Response Body
   * @return Exchange
   */
  public Optional<CkHttpExchange> createExchange(BufferedReader in, OutputStream out)
      throws IOException {

    StringBuilder requestBuilder = new StringBuilder();
    String line;
    while (((line = in.readLine()) != null) && !line.trim().isEmpty()) {
      requestBuilder.append(line).append("\r\n");
    }
    String requestBodyString = requestBuilder.toString();

    /*
     * In browser, they might send 2 requests for 1 browser call.
     *
     * 1. with empty request body
     * 2. with correct request body.
     *
     * To avoid null pointer exception we are doing this check.
     */
    if (requestBodyString.isEmpty()) return Optional.empty();

    String[] requestsLines = requestBodyString.split("\r\n");

    // In the first request line, we get method, path and version.
    // Eg: GET /echoHeader HTTP/1.1
    String[] firstRequestLineArray = requestsLines[0].split(" ");
    String method = firstRequestLineArray[0];
    String path = firstRequestLineArray[1];
    String version = firstRequestLineArray[2];

    // In the second line, we get the host name
    // From 3rd line onwards, we only get headers.
    List<String> headers = Arrays.asList(requestsLines).subList(2, requestsLines.length);

    return Optional.of(new CkHttpExchange(path, HttpMethod.valueOf(method), version, headers, out));
  }
}
