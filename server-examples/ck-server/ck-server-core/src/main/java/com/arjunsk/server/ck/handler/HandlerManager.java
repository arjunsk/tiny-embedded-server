package com.arjunsk.server.ck.handler;

import com.arjunsk.server.ck.enums.HttpMethod;
import java.util.EnumMap;
import java.util.Map;
import java.util.TreeMap;

/** We are using this class to manage all the handlers registered to the server. */
public class HandlerManager {
  private final Map<String, Map<HttpMethod, Handler>> handlerMap;

  public HandlerManager() {
    // NOTE: Using Tree Map to keep sorted order of endpoints.
    this.handlerMap = new TreeMap<>();
  }

  public void addHandler(String path, HttpMethod method, Handler handler) {
    // NOTE: Using Enum map.
    handlerMap.computeIfAbsent(path, arg -> new EnumMap<>(HttpMethod.class));
    handlerMap.get(path).put(method, handler);
  }

  public Handler getHandler(String path, HttpMethod method) {
    return handlerMap.get(path).get(method);
  }
}
