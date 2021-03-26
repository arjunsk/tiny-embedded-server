package com.arjunsk.server.ck.sample.handlers;

import com.arjunsk.server.ck.domain.CkHttpExchange;
import com.arjunsk.server.ck.enums.ContentType;
import com.arjunsk.server.ck.enums.HttpStatusCode;
import com.arjunsk.server.ck.handler.Handler;
import java.io.IOException;
import java.io.OutputStream;

public class RootHandler implements Handler {

  @Override
  public void handle(CkHttpExchange he) throws IOException {

    // Write message
    String response = "<h1>Server start success if you see this message</h1>";

    he.sendResponseHeaders(HttpStatusCode.OK, ContentType.HTML);

    OutputStream os = he.getResponseBody();
    os.write(response.getBytes());
    os.close();
  }
}
