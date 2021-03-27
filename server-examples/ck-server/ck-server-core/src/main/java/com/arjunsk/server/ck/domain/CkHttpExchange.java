package com.arjunsk.server.ck.domain;

import com.arjunsk.server.ck.enums.ContentType;
import com.arjunsk.server.ck.enums.HttpMethod;
import com.arjunsk.server.ck.enums.HttpStatusCode;
import java.io.OutputStream;
import java.net.URI;
import java.util.Date;
import java.util.Map;

/**
 * Passed to handlers inside callee. Will contain all the required information about the incoming
 * request.
 */
public class CkHttpExchange {

  private final URI requestUri;

  private final HttpMethod method;

  private final String version;

  private final Map<String, String> requestHeaders;

  private final OutputStream responseBody;

  private final String requestBody;

  public CkHttpExchange(
      URI requestUri,
      HttpMethod method,
      String version,
      Map<String, String> requestHeaders,
      OutputStream responseBody,
      String requestBody) {
    this.requestUri = requestUri;
    this.method = method;
    this.version = version;
    this.requestHeaders = requestHeaders;
    this.responseBody = responseBody;
    this.requestBody = requestBody;
  }

  public URI getRequestUri() {
    return requestUri;
  }

  public HttpMethod getMethod() {
    return method;
  }

  public String getVersion() {
    return version;
  }

  public Map<String, String> getRequestHeaders() {
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

  /** Read the request body of POST as string. */
  public String getRequestBody() {
    return requestBody;
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
