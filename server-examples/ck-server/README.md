## CK Server
This is a http server, build from scratch using `Socket`.

### Usage

> CkServerDriver.java (Register handlers)
```java
public class CkServerDriver {

  public static void main(String[] args) {
    int port = 8080;
    CkHttpServer server = new CkHttpServer(new InetSocketAddress(port));

    server.addHandler("/", HttpMethod.GET,new RootHandler());
    server.addHandler("/echoHeader", HttpMethod.GET,new EchoHeaderHandler());

    server.start();
  }
}
```

> EchoHeaderHandler.java (Sample handler)
```java
public class EchoHeaderHandler implements Handler {

  @Override
  public void handle(CkHttpExchange he) throws IOException {

    // Read
    List<String> headers = he.getRequestHeaders();
    StringBuilder response = new StringBuilder();
    for (String header : headers) {
      response.append(header).append("\n");
    }

    // Write
    he.sendResponseHeaders(HttpStatusCode.OK, ContentType.TEXT);
    OutputStream os = he.getResponseBody();
    os.write(response.toString().getBytes());
    os.close();
  }
}
```

### Output
![Browser Output](misc/screenshots/echo-header-screenshot.PNG)

### Features
- Similar to `com.sun.net.*` HTTPServer.
    - `Exchange` added
    - `Handler` added

### TODO
1. Error handler
2. Performance improvement
2. SSL support