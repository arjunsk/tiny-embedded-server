package com.arjunsk.server.ck.domain;

import com.arjunsk.server.ck.enums.ContentType;
import com.arjunsk.server.ck.enums.HttpMethod;
import com.arjunsk.server.ck.enums.HttpStatusCode;
import java.io.OutputStream;
import java.util.Date;
import java.util.List;

/**
 * Passed to handlers inside callee. Will contain all the required information about the incoming
 * request.
 */
public class CkHttpExchange {

  private final String path;

  private final HttpMethod method;

  private final String version;

  // TODO: Later change to map
  private final List<String> requestHeaders;

  private final OutputStream responseBody;

  public CkHttpExchange(
      String path,
      HttpMethod method,
      String version,
      List<String> requestHeaders,
      OutputStream responseBody) {
    this.path = path;
    this.method = method;
    this.version = version;
    this.requestHeaders = requestHeaders;
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
    return requestHeaders;
  }

  /**
   * Used for writing the response inside handler implementation.
   *
   * @return Response Body.
   */
  public OutputStream getResponseBody() {
    return responseBody;
  }

  /**
   * Used for setting/appending the response headers.
   *
   * @param responseStatus response status code
   * @param contentType response content-type
   */
  public void sendResponseHeaders(HttpStatusCode responseStatus, ContentType contentType) {
    try {
      responseBody.write(("HTTP/1.1 " + responseStatus.toString() + "\r\n").getBytes());
      responseBody.write(("ContentType: " + contentType.getValue() + "\r\n").getBytes());
      responseBody.write(("Date: " + new Date() + "\r\n").getBytes());
      responseBody.write("\r\n\r\n".getBytes());
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }
}
