package com.arjunsk.eg.socket;

import java.io.DataOutputStream;
import java.net.Socket;

public class Client {
  public static void main(String[] args) {
    try (Socket socket = new Socket("localhost", 6666)) {

      DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream());
      outputStream.writeUTF("Hello Server");

      outputStream.flush();
      outputStream.close();
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }
}
