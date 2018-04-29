package com.kodcu;

/*
 * Created by hakdogan on 28.04.2018
 */

import com.kodcu.verticle.VerticleRestServer;
import io.vertx.core.Vertx;
import io.vertx.ext.unit.Async;
import io.vertx.ext.unit.TestContext;
import io.vertx.ext.unit.junit.VertxUnitRunner;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(VertxUnitRunner.class)
public class VerticleIntegrationTest {

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
    public void saveDocument(TestContext testContext) {
        Async async = testContext.async();
        vertx.createHttpClient().getNow(8080, "localhost", "/api/articles/save/1/title/content/author",
                response -> {
                    response.handler(responseBody -> {
                    testContext.assertTrue(responseBody.toString().contains("recorded..."));
                    async.complete();
                 });
        });
    }

    @Test
    public void removeDocument(TestContext testContext) {
        Async async = testContext.async();
        vertx.createHttpClient()
                .getNow(8080, "localhost", "/api/articles/remove/1",
                    response -> {
                        response.handler(responseBody -> {
                                testContext.assertTrue(responseBody.toString().contains("deleted"));
                                async.complete();
                        });
                    });
    }
}
