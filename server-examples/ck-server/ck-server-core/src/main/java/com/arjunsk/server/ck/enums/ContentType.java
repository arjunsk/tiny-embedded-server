package com.arjunsk.server.ck.enums;

public enum ContentType {
  JSON("text/json"),
  TEXT("text/plain"),
  HTML("text/html");

  private final String value;

  ContentType(String value) {
    this.value = value;
  }

  public String getValue() {
    return value;
  }
}
