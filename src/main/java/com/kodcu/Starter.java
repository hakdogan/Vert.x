package com.kodcu;

/*
 * Created by hakdogan on 28.04.2018
 */

import com.kodcu.verticle.VerticleRestServer;
import io.vertx.core.Vertx;

public class Starter {

    public static void main(String[] args){
        Vertx vertx = Vertx.vertx();
        vertx.deployVerticle(new VerticleRestServer());
    }
}
