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

public class RequestRouter implements Runnable {

  private final Socket conn;

  private final HandlerManager handlerManager;

  public RequestRouter(Socket conn, HandlerManager handlerManager) {
    this.conn = conn;
    this.handlerManager = handlerManager;
  }

  @Override
  public void run() {

    try (BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        OutputStream out = new BufferedOutputStream(conn.getOutputStream())) {

      CkHttpExchange exchange = createExchange(in, out);

      String path = exchange.getPath();
      HttpMethod method = exchange.getMethod();
      Handler handler = handlerManager.getHandler(path, method);

      handler.handle(exchange);

      out.flush();
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

  public CkHttpExchange createExchange(BufferedReader in, OutputStream out) throws IOException {

    StringBuilder requestBuilder = new StringBuilder();

    String line;
    while (!(line = in.readLine()).trim().isEmpty()) {
      requestBuilder.append(line).append("\r\n");
    }

    String request = requestBuilder.toString();

    String[] requestsLines = request.split("\r\n");
    String[] requestLine = requestsLines[0].split(" ");
    String method = requestLine[0];
    String path = requestLine[1];
    String version = requestLine[2];

    List<String> headers = Arrays.asList(requestsLines).subList(2, requestsLines.length);

    return new CkHttpExchange(path, HttpMethod.valueOf(method), version, headers, out);
  }
}
