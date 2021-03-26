package com.arjunsk.networking.components.socket.clientserver;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

@SuppressWarnings("java:S2189")
public class SocketServer {
  public static void main(String[] args) throws IOException {

    DataInputStream inputStream = null;

    try (ServerSocket serverSocket = new ServerSocket(6666)) {

      while (true) {
        Socket socket = serverSocket.accept(); // establishes connection

        inputStream = new DataInputStream(socket.getInputStream());
        String str = inputStream.readUTF();
        System.out.println("Message = " + str);
      }

    } catch (Exception ex) {
      ex.printStackTrace();
    } finally {
      inputStream.close();
    }
  }
}
