package com.arjunsk.server.ck;

import com.arjunsk.server.ck.enums.HttpMethod;
import com.arjunsk.server.ck.handler.Handler;
import com.arjunsk.server.ck.handler.HandlerManager;
import com.arjunsk.server.ck.router.RequestRouter;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;

public class CkHttpServer {

  private final HandlerManager handlerManager;

  private final InetSocketAddress host;

  public CkHttpServer(InetSocketAddress host) {
    this.handlerManager = new HandlerManager();
    this.host = host;
  }

  public void addHandler(String path, HttpMethod method, Handler handler) {
    handlerManager.addHandler(path, method, handler);
  }

  @SuppressWarnings("java:S2189")
  public void start() {

    int port = this.host.getPort();

    try (ServerSocket serverSocket = new ServerSocket(port)) {
      while (true) {

        RequestRouter handler = new RequestRouter(serverSocket.accept(), handlerManager);

        Thread thread = new Thread(handler);
        thread.start();
      }
    } catch (IOException e) {
      System.err.println("Could not start server: " + e);
      System.exit(-1);
    }
  }
}
