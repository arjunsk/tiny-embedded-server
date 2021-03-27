package com.arjunsk.server.ck.enums;

/** Contains all the HTTP status code. */
public enum HttpStatusCode {
  OK(200, "OK"),
  BAD_REQUEST(400, "Bad Request"),
  NOT_FOUND(404, "Not Found");

  private final int value;
  private final String message;

  HttpStatusCode(final int value, final String message) {
    this.value = value;
    this.message = message;
  }

  public int getValue() {
    return value;
  }

  public String getMessage() {
    return message;
  }

  /**
   * Stringifies HttpStatus Code.
   *
   * <p>NOTE: Please don't change the format. This is used inside {@link
   * com.arjunsk.server.ck.domain.CkHttpExchange#sendResponseHeaders(HttpStatusCode, ContentType)}
   *
   * @return String value of status code.
   */
  @Override
  public String toString() {
    return getValue() + " " + getMessage();
  }
}
