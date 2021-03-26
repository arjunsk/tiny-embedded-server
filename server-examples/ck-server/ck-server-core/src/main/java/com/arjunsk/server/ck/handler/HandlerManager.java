package com.arjunsk.server.ck.handler;

import com.arjunsk.server.ck.enums.HttpMethod;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class HandlerManager {
  private final Map<String, Map<HttpMethod, Handler>> handlerMap;

  public HandlerManager() {
    this.handlerMap = new TreeMap<>();
  }

  public void addHandler(String path, HttpMethod method, Handler handler) {
    handlerMap.computeIfAbsent(path, arg -> new HashMap<>());
    handlerMap.get(path).put(method, handler);
  }

  public Handler getHandler(String path, HttpMethod method) {
    return handlerMap.get(path).get(method);
  }
}
