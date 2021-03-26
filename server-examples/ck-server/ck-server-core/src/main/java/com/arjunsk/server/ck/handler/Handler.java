package com.arjunsk.server.ck.handler;

import com.arjunsk.server.ck.domain.CkHttpExchange;
import java.io.IOException;

public interface Handler {
  void handle(CkHttpExchange httpExchange) throws IOException;
}
