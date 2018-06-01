package com.kodcu.util;

/*
 * Created by hakdogan on 30.04.2018
 */

import java.util.ArrayList;
import java.util.List;

public class Constants {

    public static final String DB_URI = "mongodb://localhost:27017";
    public static final String DB_NAME = "vertx";
    public static final String COLLECTION_NAME = "articles";
    public static final String TEMPLATES_DIRECTORY = "templates";
    public static final String CONTENT_TYPE = "content-type";
    public static final String HTML_PRODUCE = "text/html";
    public static final String JSON_PRODUCER = "application/json";
    public static final String ERROR_MESSAGE = "Internal server error: ";
    public static final int HTTP_PORT = 8080;
    public static final int HTTP_STATUS_CODE_OK = 200;
    public static final int INTERNEL_SERVER_ERROR_CODE = 500;

    protected static final List<String> menuItems = new ArrayList<>();

    static {
        menuItems.add("homeActive");
        menuItems.add("allArticlesActive");
        menuItems.add("saveArticleActive");
        menuItems.add("deleteArticleActive");
    }

    private Constants(){}

    public static List<String> getMenuItems(){
        return menuItems;
    }
}
