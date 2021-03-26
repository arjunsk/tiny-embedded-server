package com.arjunsk.server.ck.enums;

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

  @Override
  public String toString() {
    return String.valueOf(this.getValue());
  }
}
