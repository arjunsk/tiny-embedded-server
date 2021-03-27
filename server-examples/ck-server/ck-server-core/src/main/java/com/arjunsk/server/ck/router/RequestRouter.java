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
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/** Creates the exchange and passes it to the corresponding handler. */
public class RequestRouter implements Runnable {

  // Connection Socket
  private final Socket conn;

  // Holds all the handlers
  private final HandlerManager handlerManager;

  // NOTE: We are passing the handler from CKHttpServer to ensure that there is only one
  // handlerManager for 1 server instance.
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

        String path = exchange.get().getRequestUri().getPath();
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
   *   POST /echo HTTP/1.1 ################ 1. Status Line (Block 1) ###################
   *   Host: www.example.com ################ 2. Host ###################
   *   Accept: text/html,/*;q=0.5 ################ 3. Headers[] ###################
   *   User-Agent: BrowserName/1.0
   *   Referer: http://www.example.com/
   *   Content-type: application/x-www-form-urlencoded; charset=utf-8
   *
   *   foo=1&bar=2 ################ 4. Body (Block 2) ###################
   * </pre>
   *
   * @param in Request Body
   * @param out Response Body
   * @return Exchange
   */
  public Optional<CkHttpExchange> createExchange(BufferedReader in, OutputStream out)
      throws IOException, URISyntaxException {

    // 1.1 Code to read status line & headers.
    StringBuilder requestBlock1Builder = new StringBuilder();
    String line;
    while (((line = in.readLine()) != null) && !line.trim().isEmpty()) {
      requestBlock1Builder.append(line).append("\r\n");
    }
    String requestBlock1 = requestBlock1Builder.toString();

    /*
     * In browser, they might send 2 requests for 1 browser call.
     *
     * a. with empty request headers.
     * b. with correct request header.
     *
     * To avoid null pointer exception we are doing this check.
     */
    if (requestBlock1.isEmpty()) return Optional.empty();

    String[] requestBlock1Lines = requestBlock1.split("\r\n");

    // In the first request line, we get method, path and version.
    // Eg: GET /echoHeader HTTP/1.1
    String[] statusLineArray = requestBlock1Lines[0].split(" ");
    HttpMethod method = HttpMethod.valueOf(statusLineArray[0]);
    URI pathUri = new URI(statusLineArray[1]);
    String version = statusLineArray[2];

    // 1.2 In the second line, we get the host name
    // From 3rd line onwards, we only get headers.
    List<String> headers = Arrays.asList(requestBlock1Lines).subList(2, requestBlock1Lines.length);

    // Convert List of Headers to Map<Header Key, Header Value>
    Map<String, String> headersMap =
        headers.stream()
            .filter(header -> !header.trim().isEmpty())
            .map(header -> header.split(":", 2))
            .collect(Collectors.toMap(e -> e[0].trim(), e -> e[1].trim()));

    // 2. Code to read body.
    StringBuilder requestBlock2Builder = new StringBuilder();
    while (in.ready()) {
      requestBlock2Builder.append((char) in.read());
    }
    String requestBody = requestBlock2Builder.toString();

    return Optional.of(new CkHttpExchange(pathUri, method, version, headersMap, out, requestBody));
  }
}
