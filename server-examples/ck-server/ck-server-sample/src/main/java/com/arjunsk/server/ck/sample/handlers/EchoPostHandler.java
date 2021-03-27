package com.arjunsk.server.ck.sample.handlers;

import com.arjunsk.server.ck.domain.CkHttpExchange;
import com.arjunsk.server.ck.enums.ContentType;
import com.arjunsk.server.ck.enums.HttpStatusCode;
import com.arjunsk.server.ck.handler.Handler;
import java.io.IOException;
import java.io.OutputStream;

public class EchoPostHandler implements Handler {

  @Override
  public void handle(CkHttpExchange he) throws IOException {

    // Read
    String body = he.getRequestBody();

    // Write
    he.sendResponseHeaders(HttpStatusCode.OK, ContentType.JSON);
    OutputStream os = he.getResponseBody();
    os.write(body.getBytes());
    os.close();
  }
}
