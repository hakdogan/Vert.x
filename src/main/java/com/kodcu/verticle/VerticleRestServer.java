package com.kodcu.verticle;


/*
 * Created by hakdogan on 28.04.2018
 */

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.MongoClient;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import lombok.extern.slf4j.Slf4j;

import static com.kodcu.util.Constants.*;
@Slf4j
public class VerticleRestServer extends AbstractVerticle {

    private MongoClient mongoClient;

    @Override
    public void start(Future<Void> future) {

        log.info("Welcome to Vertx");

        Router router = Router.router(vertx);
        router.get("/api/articles/article/:id").handler(this::getOneArticle);
        router.get("/api/articles").handler(this::getArticles);
        router.get("/api/articles/save/:id/:title/:content/:author").handler(this::saveDocument);
        router.get("/api/articles/remove/:id").handler(this::removeDocument);

        vertx.createHttpServer()
                .requestHandler(router::accept)
                .listen(config().getInteger("http.port", HTTP_PORT), result -> {
                    if (result.succeeded()) {
                        future.complete();
                    } else {
                        future.fail(result.cause());
                    }
                });

        prepareMongoDB();
    }

    @Override
    public void stop() {
        log.info("Shutting down application");
    }

    private void prepareMongoDB(){

        JsonObject mongoconfig = new JsonObject()
                .put("connection_string", DB_URI)
                .put("db_name", DB_NAME);

        mongoClient = MongoClient.createShared(vertx, mongoconfig);
    }

    /**
     *
     * @param routingContext
     */
    private void getArticles(RoutingContext routingContext) {
        mongoClient.find(COLLECTION_NAME, new JsonObject(), res ->
                routingContext.response()
                    .putHeader(CONTENT_TYPE, PRODUCER_TYPE)
                    .setStatusCode(HTTP_STATUS_CODE_OK)
                    .end(Json.encodePrettily(res.result())));
    }

    /**
     *
     * @param routingContext
     */
    private void getOneArticle(RoutingContext routingContext) {
        String id = routingContext.request().getParam("id");

        mongoClient.find(COLLECTION_NAME, new JsonObject().put("id", id), res ->
                routingContext.response()
                    .putHeader(CONTENT_TYPE, PRODUCER_TYPE)
                    .setStatusCode(HTTP_STATUS_CODE_OK)
                    .end(Json.encodePrettily(res.result())));
    }

    /**
     *
     * @param routingContext
     */
    private void saveDocument(RoutingContext routingContext){

        String documentId = routingContext.request().getParam("id");
        String title = routingContext.request().getParam("title");
        String content = routingContext.request().getParam("content");
        String author = routingContext.request().getParam("author");

        JsonObject article = new JsonObject().put("id", documentId).put(title, title).put("content", content).put("author", author);
        mongoClient.save(COLLECTION_NAME, article, id -> log.debug("Inserted id: {} ", id.result()));

        routingContext.response()
                .putHeader(CONTENT_TYPE, PRODUCER_TYPE)
                .setStatusCode(HTTP_STATUS_CODE_OK)
                .end("recorded...");
    }

    /**
     *
     * @param routingContext
     */
    private void removeDocument(RoutingContext routingContext) {

        String documentId = routingContext.request().getParam("id");
        JsonObject query = new JsonObject().put("id", documentId);

        mongoClient.removeDocument(COLLECTION_NAME, query, res -> {
            if (res.succeeded()) {
                routingContext.response()
                        .putHeader(CONTENT_TYPE, PRODUCER_TYPE)
                        .setStatusCode(HTTP_STATUS_CODE_OK)
                        .end("The document was deleted...");
            } else {
                routingContext.response()
                        .putHeader(CONTENT_TYPE, PRODUCER_TYPE)
                        .setStatusCode(HTTP_STATUS_CODE_OK)
                        .end("Opps. " + res.cause());
            }
        });
    }

}
