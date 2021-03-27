package com.arjunsk.server.ck.handler;

import com.arjunsk.server.ck.domain.CkHttpExchange;
import com.arjunsk.server.ck.enums.HttpMethod;
import java.io.IOException;

/**
 * Would be implemented by the callee. We register handler for each http path, and http method using
 * the {@link com.arjunsk.server.ck.CkHttpServer#addHandler(String, HttpMethod, Handler)}
 */
public interface Handler {
  void handle(CkHttpExchange httpExchange) throws IOException;
}
