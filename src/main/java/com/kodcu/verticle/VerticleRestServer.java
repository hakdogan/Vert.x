package com.kodcu.verticle;


/*
 * Created by hakdogan on 28.04.2018
 */

import com.kodcu.util.Constants;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.MongoClient;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.templ.FreeMarkerTemplateEngine;
import lombok.extern.slf4j.Slf4j;

import static com.kodcu.util.Constants.*;

@Slf4j
public class VerticleRestServer extends AbstractVerticle {

    private MongoClient mongoClient;
    private final FreeMarkerTemplateEngine templateEngine = FreeMarkerTemplateEngine.create();

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

        router.route("/*").handler(BodyHandler.create());
        router.get("/").handler(this::welcomePage);
        router.get("/api/articles").handler(this::getArticles);
        router.get("/api/articles/save").handler(this::getSavePage);
        router.post("/api/articles/save").handler(this::saveDocument);
        router.get("/api/articles/remove").handler(this::getDeletePage);
        router.post("/api/articles/remove").handler(this::removeDocument);
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
    private void welcomePage(RoutingContext routingContext){
        routingContext.put("h1", "Welcome the Vert.x tutorial");
        pageRender(routingContext, "/index.ftl", "homeActive", HTML_PRODUCE, HTTP_STATUS_CODE_OK);
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
            routingContext.put("articles", result);
            pageRender(routingContext, "/list.ftl", "allArticlesActive", HTML_PRODUCE, statusCode);
        });
    }

    /**
     *
     * @param routingContext
     */
    private void getSavePage(RoutingContext routingContext){
        pageRender(routingContext, "/save.ftl", "saveArticleActive", HTML_PRODUCE, HTTP_STATUS_CODE_OK);
    }

    /**
     *
     * @param routingContext
     */
    private void saveDocument(RoutingContext routingContext){

        mongoClient.save(COLLECTION_NAME, routingContext.getBodyAsJson(), req -> {
            String result = "";
            int statusCode = HTTP_STATUS_CODE_OK;

            if(req.succeeded()){
               result = req.result().isEmpty()?Json.encodePrettily("Document could not be inserted"):Json.encodePrettily("Inserted doc id: " + req.result());
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
    private void getDeletePage(RoutingContext routingContext){
        pageRender(routingContext, "/delete.ftl", "deleteArticleActive", HTML_PRODUCE, HTTP_STATUS_CODE_OK);
    }

    /**
     *
     * @param routingContext
     */
    private void removeDocument(RoutingContext routingContext) {

        log.info(routingContext.getBodyAsJson().toString());
        mongoClient.removeDocuments(COLLECTION_NAME, routingContext.getBodyAsJson(), req -> {
            String result = "";
            int statusCode = HTTP_STATUS_CODE_OK;

            if (req.succeeded()) {
                result = req.result().getRemovedCount() > 0?Json.encodePrettily(req.result().getRemovedCount() + " document was deleted")
                        :Json.encodePrettily("No document was deleted");
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
     * @param statusCode
     * @param result
     */
    private void responseHandle(RoutingContext routingContext, int statusCode, String result){
        routingContext.response()
                .putHeader(CONTENT_TYPE, JSON_PRODUCER)
                .setStatusCode(statusCode)
                .end(result);
    }

    /**
     *
     * @param routingContext
     * @param url
     * @param activePage
     * @param produceType
     * @param statusCode
     */
    private void pageRender(RoutingContext routingContext, String url, String activePage, String produceType, int statusCode){
        Constants.getMenuItems().forEach(item -> {
            String status = item.equals(activePage)?"active":"";
            routingContext.put(item, status);
        });

        templateEngine.render(routingContext, TEMPLATES_DIRECTORY, url, page -> {
            if (page.succeeded()) {
                routingContext.response().putHeader(CONTENT_TYPE, produceType)
                .setStatusCode(statusCode)
                .end(page.result());
            } else {
                routingContext.fail(page.cause());
            }
        });
    }

}
