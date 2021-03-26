package com.arjunsk.server.ck.domain;

import com.arjunsk.server.ck.enums.ContentType;
import com.arjunsk.server.ck.enums.HttpMethod;
import com.arjunsk.server.ck.enums.HttpStatusCode;
import java.io.OutputStream;
import java.util.Date;
import java.util.List;

public class CkHttpExchange {

  private final String path;

  private final HttpMethod method;

  private final String version;

  private final List<String> headers;

  private final OutputStream responseBody;

  public CkHttpExchange(
      String path,
      HttpMethod method,
      String version,
      List<String> headers,
      OutputStream responseBody) {
    this.path = path;
    this.method = method;
    this.version = version;
    this.headers = headers;
    this.responseBody = responseBody;
  }

  public String getPath() {
    return path;
  }

  public HttpMethod getMethod() {
    return method;
  }

  public String getVersion() {
    return version;
  }

  public List<String> getRequestHeaders() {
    return headers;
  }

  public OutputStream getResponseBody() {
    return responseBody;
  }

  public void sendResponseHeaders(HttpStatusCode responseStatus, ContentType contentType) {
    try {
      responseBody.write(("HTTP/1.1 " + responseStatus.getValue() + " " + responseStatus.getMessage() + "\r\n").getBytes());
      responseBody.write(("ContentType: " + contentType.getValue() + "\r\n").getBytes());
      responseBody.write(("Date: " + new Date() + "\r\n").getBytes());
      responseBody.write("\r\n\r\n".getBytes());
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }
}
