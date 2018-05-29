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
        Future<Void> steps = prepareMongoDB().compose(ar -> createServer());
        steps.setHandler(ar -> {
            if (ar.succeeded()) {
                future.complete();
            } else {
                log.error("Application failed to start {} ", ar.cause());
                future.fail(ar.cause());
            }
        });
    }

    @Override
    public void stop() {
        log.info("Shutting down application");
    }

    private Future<Void> prepareMongoDB(){

        Future<Void> future = Future.future();
        final JsonObject mongoConfig = new JsonObject()
                .put("connection_string", DB_URI)
                .put("db_name", DB_NAME);

        mongoClient = MongoClient.createShared(vertx, mongoConfig);
        mongoClient.getCollections(ar -> {
            if(ar.succeeded()){
                future.complete();
            } else {
                future.fail(ar.cause());
            }
        });

        return future;
    }

    /**
     *
     * @return
     */
    private Future<Void> createServer(){

        final Future<Void> future = Future.future();
        final Router router = Router.router(vertx);

        router.get("/api/articles").handler(this::getArticles);
        router.get("/api/articles/article/:id").handler(this::getOneArticle);
        router.get("/api/articles/save/:id/:title/:content/:author").handler(this::saveDocument);
        router.get("/api/articles/remove/:id").handler(this::removeDocument);
        router.get("/api/collection/drop/:name").handler(this::dropCollection);

        vertx.createHttpServer().requestHandler(router::accept)
                .listen(config().getInteger("http.server.port", HTTP_PORT), result -> {
                    if (result.succeeded()) {
                        log.info("HTTP server running on port " + HTTP_PORT);
                        future.complete();
                    } else {
                        log.error("Could not start a HTTP server", result.cause());
                        future.fail(result.cause());
                    }
                });

        return future;
    }

    /**
     *
     * @param routingContext
     */
    private void getArticles(RoutingContext routingContext) {

        mongoClient.find(COLLECTION_NAME, new JsonObject(), req -> {
            String result = "";
            int statusCode = HTTP_STATUS_CODE_OK;

            if(req.succeeded()) {
                result = req.result().isEmpty()?"No documents found":Json.encodePrettily(req.result());
            } else {
                result = ERROR_MESSAGE + req.cause();
                statusCode = INTERNEL_SERVER_ERROR_CODE;
            }
            responseHandle(routingContext, statusCode, result);
        });
    }

    /**
     *
     * @param routingContext
     */
    private void getOneArticle(RoutingContext routingContext) {

        final String id = routingContext.request().getParam("id");
        mongoClient.find(COLLECTION_NAME, new JsonObject().put("id", id), req -> {
            String result = "";
            int statusCode = HTTP_STATUS_CODE_OK;

            if(req.succeeded()){
                result = req.result().isEmpty()?"Document not found":Json.encodePrettily(req.result());
            } else {
                result = ERROR_MESSAGE + req.cause();
                statusCode = INTERNEL_SERVER_ERROR_CODE;
            }
            responseHandle(routingContext, statusCode, result);
        });
    }

    /**
     *
     * @param routingContext
     */
    private void saveDocument(RoutingContext routingContext){

        final String documentId = routingContext.request().getParam("id");
        final String title = routingContext.request().getParam("title");
        final String content = routingContext.request().getParam("content");
        final String author = routingContext.request().getParam("author");
        final JsonObject article = new JsonObject().put("id", documentId).put(title, title).put("content", content).put("author", author);

        mongoClient.save(COLLECTION_NAME, article, req -> {
            String result = "";
            int statusCode = HTTP_STATUS_CODE_OK;

            if(req.succeeded()){
               result = req.result().isEmpty()?"Document could not be inserted":Json.encodePrettily("Inserted doc id: " + req.result());
            } else {
                result = ERROR_MESSAGE + req.cause();
                statusCode = INTERNEL_SERVER_ERROR_CODE;
            }

            log.debug(result);
            responseHandle(routingContext, statusCode, result);
        });
    }

    /**
     *
     * @param routingContext
     */
    private void removeDocument(RoutingContext routingContext) {

        final String documentId = routingContext.request().getParam("id");
        final JsonObject query = new JsonObject().put("id", documentId);

        mongoClient.removeDocuments(COLLECTION_NAME, query, req -> {
            String result = "";
            int statusCode = HTTP_STATUS_CODE_OK;

            if (req.succeeded()) {
                result = req.result().getRemovedCount() > 0?req.result().getRemovedCount() + " document was deleted":"No document was deleted";
            } else {
                result = ERROR_MESSAGE + req.cause();
                statusCode = INTERNEL_SERVER_ERROR_CODE;
            }
            responseHandle(routingContext, statusCode, result);
        });
    }

    /**
     *
     * @param routingContext
     */
    private void dropCollection(RoutingContext routingContext) {

        final String collectinName = routingContext.request().getParam("name");
        mongoClient.dropCollection(collectinName, req -> {

            String result = "The collection was dropped...";
            int statusCode = HTTP_STATUS_CODE_OK;

            if(req.failed()){
                result = ERROR_MESSAGE + req.cause();
                statusCode = INTERNEL_SERVER_ERROR_CODE;
            }
            responseHandle(routingContext, statusCode, result);
        });
    }

    /**
     *
     * @param routingContext
     */
    private void responseHandle(RoutingContext routingContext, int statusCode, String result){
        routingContext.response()
                .putHeader(CONTENT_TYPE, PRODUCER_TYPE)
                .setStatusCode(statusCode)
                .end(result);
    }

}
