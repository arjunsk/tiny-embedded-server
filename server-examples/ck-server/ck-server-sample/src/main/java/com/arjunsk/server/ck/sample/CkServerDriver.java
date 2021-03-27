package com.arjunsk.server.ck.sample;

import com.arjunsk.server.ck.CkHttpServer;
import com.arjunsk.server.ck.enums.HttpMethod;
import com.arjunsk.server.ck.sample.handlers.EchoHeaderHandler;
import com.arjunsk.server.ck.sample.handlers.EchoPostHandler;
import com.arjunsk.server.ck.sample.handlers.RootHandler;
import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

public class CkServerDriver {

  public static void main(String[] args) {
    int port = 8080;
    CkHttpServer server = new CkHttpServer(new InetSocketAddress(port));

    server.addHandler("/", HttpMethod.GET, new RootHandler());
    server.addHandler("/echoHeader", HttpMethod.GET, new EchoHeaderHandler());
    server.addHandler("/echoPost", HttpMethod.POST, new EchoPostHandler());

    // In Chrome, there is an extra call for favicon.
    server.addHandler("/favicon.ico", HttpMethod.GET, new RootHandler());

    server.setExecutor(Executors.newCachedThreadPool());

    server.start();
  }
}
