package com.arjunsk.components.httpserver.handlers;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class EchoHeaderHandler implements HttpHandler {

  @Override
  public void handle(HttpExchange he) throws IOException {

    // Read
    Headers headers = he.getRequestHeaders();
    Set<Entry<String, List<String>>> entries = headers.entrySet();
    StringBuilder response = new StringBuilder();
    for (Map.Entry<String, List<String>> entry : entries) {
      response.append(entry.toString()).append("\n");
    }

    // Write
    he.sendResponseHeaders(200, response.length());
    OutputStream os = he.getResponseBody();
    os.write(response.toString().getBytes());
    os.close();
  }
}
