package com.arjunsk.server.ck;

import com.arjunsk.server.ck.enums.HttpMethod;
import com.arjunsk.server.ck.handler.Handler;
import com.arjunsk.server.ck.handler.HandlerManager;
import com.arjunsk.server.ck.router.RequestRouter;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/** Main Server Public class. */
public class CkHttpServer {

  // Holds all the handlers registered in the server
  private final HandlerManager handlerManager;

  // Contains hostname and port
  private final InetSocketAddress host;

  // Custom Executor from callee.
  private Executor executor;

  public CkHttpServer(InetSocketAddress host) {
    this.handlerManager = new HandlerManager();
    this.host = host;
  }

  /**
   * Add a new handler to the server.
   *
   * @param path url
   * @param method http method
   * @param handler handler function
   */
  public void addHandler(String path, HttpMethod method, Handler handler) {
    handlerManager.addHandler(path, method, handler);
  }

  /**
   * Setting our custom executor
   *
   * @param executor Thread Executor
   */
  public void setExecutor(Executor executor) {
    this.executor = executor;
  }

  /** Starts our server. */
  @SuppressWarnings("java:S2189")
  public void start() {

    // Setting a default executor if executor is not passed.
    if (executor == null) {
      executor = Executors.newSingleThreadExecutor();
    }

    int port = this.host.getPort();

    // Creating a connection for the port.
    try (ServerSocket serverSocket = new ServerSocket(port)) {

      while (true) {

        // serverSocket.accept() waits the thread till a new connection request.
        RequestRouter requestRouter = new RequestRouter(serverSocket.accept(), handlerManager);

        // submit the new request to a thread using executor
        executor.execute(requestRouter);
      }
    } catch (IOException e) {
      System.err.println("Could not start server: " + e);
      System.exit(-1);
    }
  }
}
