package com.arjunsk.server.ck.sample;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsStringIgnoringCase;
import static org.hamcrest.Matchers.equalTo;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class CkServerDriverTest {

  private static Thread serverThread;
  private static int port;

  @BeforeClass
  public static void setUp() {
    port = 8080;

    // Start server in a separate thread. Else socket.accept() will block the test-process thread.
    serverThread = new Thread(() -> CkServerDriver.main(new String[0]));
    serverThread.start();
  }

  @AfterClass
  public static void tearDown() throws Exception {
    // After completion stop the server thread to prevent further listening on that port.
    serverThread.stop();
  }

  @Test
  public void test_root_handler() {
    given()
        .port(port)
        .when()
        .get("/")
        .then()
        .statusCode(200)
        .body(equalTo("<h1>Server start success if you see this message</h1>"));
  }

  @Test
  public void test_echoHeader_handler() {
    given()
        .port(port)
        .when()
        .get("/echoHeader")
        .then()
        .statusCode(200)
        .body(containsStringIgnoringCase("Connection = keep-alive"));
  }

  @Test
  public void test_echoGet_handler() {
    given()
        .port(port)
        .when()
        .get("/echoGet?name=Arjun")
        .then()
        .statusCode(200)
        .body(equalTo("name=Arjun"));
  }

  @Test
  public void test_echoPost_handler() {
    given()
        .port(port)
        .body("body-content")
        .when()
        .post("/echoPost")
        .then()
        .statusCode(200)
        .body(equalTo("body-content"));
  }
}
