package com.arjunsk.server.ck.sample.handlers;

import com.arjunsk.server.ck.domain.CkHttpExchange;
import com.arjunsk.server.ck.enums.ContentType;
import com.arjunsk.server.ck.enums.HttpStatusCode;
import com.arjunsk.server.ck.handler.Handler;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;

public class EchoGetHandler implements Handler {

  @Override
  public void handle(CkHttpExchange he) throws IOException {
    // Read
    URI requestUri = he.getRequestUri();

    // Write
    he.sendResponseHeaders(HttpStatusCode.OK, ContentType.TEXT);
    OutputStream os = he.getResponseBody();
    os.write(requestUri.getRawQuery().getBytes());
    os.close();
  }
}
