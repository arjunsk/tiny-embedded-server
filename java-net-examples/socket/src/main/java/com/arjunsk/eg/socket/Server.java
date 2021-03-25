package com.arjunsk.eg.socket;

import java.io.DataInputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
  public static void main(String[] args) {
    try (ServerSocket serverSocket = new ServerSocket(6666)) {
      Socket socket = serverSocket.accept(); // establishes connection

      DataInputStream inputStream = new DataInputStream(socket.getInputStream());
      String str = inputStream.readUTF();
      System.out.println("Message = " + str);

      inputStream.close();
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }
}
