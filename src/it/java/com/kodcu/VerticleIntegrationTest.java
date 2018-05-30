package com.kodcu;

/*
 * Created by hakdogan on 28.04.2018
 */

import com.kodcu.util.Constants;
import com.kodcu.verticle.VerticleRestServer;
import io.vertx.core.Vertx;
import io.vertx.ext.unit.Async;
import io.vertx.ext.unit.TestContext;
import io.vertx.ext.unit.junit.VertxUnitRunner;
import org.junit.*;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;

@RunWith(VertxUnitRunner.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class VerticleIntegrationTest {

    private final String DOCUMENT_ID = "1";
    private final String DOCUMENT_TITLE = "title";
    private final String DOCUMENT_CONTENT = "content";
    private final String DOCUMENT_AUTHOR = "hakdogan";

    private Vertx vertx;

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
        vertx.createHttpClient().getNow(8080, "localhost", "/",
                response -> {
                    response.handler(responseBody -> {
                        testContext.assertTrue(responseBody.toString().contains("Welcome"));
                        async.complete();
                    });
                });
    }

    @Test
    public void testA(TestContext testContext) {
        Async async = testContext.async();
        vertx.createHttpClient().getNow(8080, "localhost", "/api/collection/drop/" + Constants.COLLECTION_NAME,
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
        String url = String.join("/", DOCUMENT_ID, DOCUMENT_TITLE, DOCUMENT_CONTENT, DOCUMENT_AUTHOR);
        vertx.createHttpClient().getNow(8080, "localhost", "/api/articles/save/" + url,
                response -> {
                    response.handler(responseBody -> {
                    testContext.assertTrue(responseBody.toString().contains("Inserted"));
                    async.complete();
                 });
        });
    }

    @Test
    public void testC(TestContext testContext) {
        Async async = testContext.async();
        vertx.createHttpClient().getNow(8080, "localhost", "/api/articles/article/" + DOCUMENT_ID,
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
        vertx.createHttpClient()
                .getNow(8080, "localhost", "/api/articles/remove/" + DOCUMENT_ID,
                    response -> {
                        response.handler(responseBody -> {
                                testContext.assertTrue(responseBody.toString().contains("deleted"));
                                async.complete();
                        });
                    });
    }
}
