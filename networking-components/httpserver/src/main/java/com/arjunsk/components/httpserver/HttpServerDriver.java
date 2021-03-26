package com.arjunsk.components.httpserver;

import com.arjunsk.components.httpserver.handlers.EchoHeaderHandler;
import com.arjunsk.components.httpserver.handlers.RootHandler;
import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

public class HttpServerDriver {

  public static void main(String[] args) throws IOException {
    int port = 8080;
    HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);

    System.out.println("server started at " + port);

    // Register endpoints
    server.createContext("/", new RootHandler());
    server.createContext("/echoHeader", new EchoHeaderHandler());
    server.setExecutor(Executors.newCachedThreadPool());
    server.start();
  }
}
