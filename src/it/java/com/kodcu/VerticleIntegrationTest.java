package com.kodcu;

/*
 * Created by hakdogan on 28.04.2018
 */

import com.kodcu.util.Constants;
import com.kodcu.verticle.VerticleRestServer;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.unit.Async;
import io.vertx.ext.unit.TestContext;
import io.vertx.ext.unit.junit.VertxUnitRunner;
import io.vertx.ext.web.client.WebClient;
import org.junit.*;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;

@RunWith(VertxUnitRunner.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class VerticleIntegrationTest {

    private Vertx vertx;
    private static String DOC_ID;
    private static final int HTTP_PORT = 8080;
    private static final String HOSTNAME = "localhost";

    @Before
    public void setup(TestContext testContext) {
        vertx = Vertx.vertx();
        vertx.deployVerticle(VerticleRestServer.class.getName(), testContext.asyncAssertSuccess());
    }

    @After
    public void tearDown(TestContext testContext) {
        vertx.close(testContext.asyncAssertSuccess());
    }

    @Test
    public void welcomePageTest(TestContext testContext) {
        Async async = testContext.async();
        vertx.createHttpClient().getNow(HTTP_PORT, HOSTNAME, "/",
                response -> {
                    response.handler(responseBody -> {
                        testContext.assertTrue(responseBody.toString().contains("Vert.x"));
                        async.complete();
                    });
                });
    }

    @Test
    public void testA(TestContext testContext) {
        Async async = testContext.async();
        vertx.createHttpClient().getNow(HTTP_PORT, HOSTNAME, "/api/collection/drop/" + Constants.COLLECTION_NAME,
                response -> {
                    response.handler(responseBody -> {
                        testContext.assertTrue(responseBody.toString().contains("dropped"));
                        async.complete();
                    });
                });
    }

    @Test
    public void testB(TestContext testContext) {
        Async async = testContext.async();
        WebClient client = WebClient.create(vertx);

        client.post(HTTP_PORT, HOSTNAME, "/api/articles/save")
                .sendJsonObject(new JsonObject()
                        .put("title", "test title")
                        .put("content", "test content")
                        .put("author", "test author"), req -> {
                    if (req.succeeded()) {
                        DOC_ID = req.result().bodyAsString().substring(req.result().bodyAsString().indexOf(":") + 2)
                                .replace("\"", "").trim();
                        testContext.assertTrue(req.result().bodyAsString().contains("Inserted doc"));
                        async.complete();
                    }
                });
    }


    @Test
    public void testC(TestContext testContext) {
        Async async = testContext.async();
        vertx.createHttpClient().getNow(HTTP_PORT, HOSTNAME, "/api/articles",
                response -> {
                    response.handler(responseBody -> {
                        testContext.assertTrue(responseBody.toString().contains("title"));
                        async.complete();
                    });
                });
    }


    @Test
    public void removeDocument(TestContext testContext) {
        Async async = testContext.async();
        WebClient client = WebClient.create(vertx);

        client.post(HTTP_PORT, HOSTNAME, "/api/articles/remove")
                .sendJsonObject(new JsonObject()
                        .put("id", DOC_ID), req -> {
                    if (req.succeeded()) {
                        testContext.assertTrue(req.result().bodyAsString().contains("document was deleted"));
                        async.complete();
                    }
                });
    }

}
