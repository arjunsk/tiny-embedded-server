package com.arjunsk.server.ck.sample.handlers;

import com.arjunsk.server.ck.domain.CkHttpExchange;
import com.arjunsk.server.ck.enums.ContentType;
import com.arjunsk.server.ck.enums.HttpStatusCode;
import com.arjunsk.server.ck.handler.Handler;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

public class EchoHeaderHandler implements Handler {

  @Override
  public void handle(CkHttpExchange he) throws IOException {

    // Read
    List<String> headers = he.getRequestHeaders();
    StringBuilder response = new StringBuilder();
    for (String header : headers) {
      response.append(header).append("\n");
    }

    // Write
    he.sendResponseHeaders(HttpStatusCode.OK, ContentType.TEXT);
    OutputStream os = he.getResponseBody();
    os.write(response.toString().getBytes());
    os.close();
  }
}
