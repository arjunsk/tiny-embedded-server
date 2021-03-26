package com.arjunsk.eg.socket.fileserver;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
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

    try (ServerSocket serverSocket = new ServerSocket(8080)) {
      while (true) {
        try (Socket client = serverSocket.accept()) {
          handleRequest(client);
        }
      }
    } catch (IOException e) {
      System.err.println("Could not start server: " + e);
      System.exit(-1);
    }
  }

  private static void handleRequest(Socket connection) throws IOException {
    // wait for request
    BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));

    OutputStream out = new BufferedOutputStream(connection.getOutputStream());
    PrintStream pout = new PrintStream(out);

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

    String accessLog =
        String.format("method %s, path %s, version %s, host %s", method, path, version, host);
    System.out.println(accessLog);

    // parse the line
    if (method.equalsIgnoreCase("GET")) {
      File f = new File(wwwRoot, path);

      try (InputStream file = new FileInputStream(f)) {
        // send file
        okHeader(pout);

        byte[] buffer = new byte[1000];
        while (file.available() > 0) {
          out.write(buffer, 0, file.read(buffer));
        }

      } catch (FileNotFoundException e) {
        e.printStackTrace();
      } finally {
        out.flush();
      }
    }
  }

  private static void okHeader(PrintStream pout) {
    pout.print(
        "HTTP/1.0 200 OK\r\n"
            + "Content-Type: "
            + "text/json"
            + "\r\n"
            + "Date: "
            + new Date()
            + "\r\n"
            + "Server: FileServer 1.0\r\n\r\n");
  }
}
