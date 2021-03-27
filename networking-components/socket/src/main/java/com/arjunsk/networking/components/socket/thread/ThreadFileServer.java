package com.arjunsk.networking.components.socket.thread;

import java.io.IOException;
import java.net.ServerSocket;

@SuppressWarnings("java:S2189")
public class ThreadFileServer {

  public static void main(String[] args) {

    if (args.length != 2) {
      System.out.println("Usage: java FileServer <port> <wwwRoot>");
      System.exit(-1);
    }

    int port = Integer.parseInt(args[0]);
    String wwwRoot = args[1];

    try (ServerSocket serverSocket = new ServerSocket(port)) {
      while (true) {

        ServerThread serverThread = new ServerThread(serverSocket.accept(), wwwRoot);

        Thread thread = new Thread(serverThread);
        thread.start();
      }
    } catch (IOException e) {
      System.err.println("Could not start server: " + e);
      System.exit(-1);
    }
  }
}
