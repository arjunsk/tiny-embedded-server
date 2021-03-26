package com.arjunsk.eg.socket.fileserver;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@SuppressWarnings("java:S2189")
public class FileServer {

  private static int port;
  private static String wwwRoot;

  public static void main(String[] args) {

    if (args.length != 2) {
      System.out.println("Usage: java FileServer <port> <wwwRoot>");
      System.exit(-1);
    }

    port = Integer.parseInt(args[0]);
    wwwRoot = args[1];

    try (ServerSocket serverSocket = new ServerSocket(port)) {
      while (true) {
        // Wait for request
        Socket connection = serverSocket.accept();

        // Handle request
        handleRequest(connection);

        // Close the socket
        connection.close();
      }
    } catch (IOException e) {
      System.err.println("Could not start server: " + e);
      System.exit(-1);
    }
  }

  private static void handleRequest(Socket connection) throws IOException {
    BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
    OutputStream out = new BufferedOutputStream(connection.getOutputStream());

    StringBuilder requestBuilder = new StringBuilder();
    String line;
    while (!(line = in.readLine()).trim().isEmpty()) {
      requestBuilder.append(line).append("\r\n");
    }

    String request = requestBuilder.toString();

    String[] requestsLines = request.split("\r\n");
    String[] requestLine = requestsLines[0].split(" ");
    String method = requestLine[0];
    String path = requestLine[1];
    String version = requestLine[2];
    String host = requestsLines[1].split(" ")[1];

    List<String> headers = Arrays.asList(requestsLines).subList(2, requestsLines.length);

    String accessLog = String.format("method %s, path %s, host %s", method, path, host);
    System.out.println(accessLog);

    if (method.equalsIgnoreCase("GET")) {
      appendOkHeader(out);
      appendResponse(out, path);
      out.flush();
    }
  }

  private static void appendOkHeader(OutputStream os) throws IOException {
    os.write(("HTTP/1.1 200 OK" + "\r\n").getBytes());
    os.write(("ContentType: " + "text/json" + "\r\n").getBytes());
    os.write(("Date: " + new Date() + "\r\n").getBytes());
    os.write("\r\n\r\n".getBytes());
  }

  private static void appendResponse(OutputStream out, String path) throws IOException {
    File file = new File(wwwRoot, path);
    try (DataInputStream dataIS = new DataInputStream(new FileInputStream(file))) {
      int fileLength = (int) file.length();
      byte[] buffer = new byte[fileLength];
      dataIS.readFully(buffer);
      out.write(buffer, 0, fileLength);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
