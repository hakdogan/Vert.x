[![Build Status](https://travis-ci.org/hakdogan/Vert.x.svg?branch=master)](https://travis-ci.org/hakdogan/Vert.x)
[![Coverage Status](https://coveralls.io/repos/github/hakdogan/Vert.x/badge.svg?branch=master)](https://coveralls.io/github/hakdogan/Vert.x?branch=master)

# Vert.x
This repository shows how to use the Vertx Mongo client with the RESTful Web Service.

## How it's run?
```
sh run.sh
```

## For save a document
```
http://localhost:8080/api/articles/save/{id}/{title}/{content}/{author}
```

## For get a document
```
http://localhost:8080/api/articles/article/{id}
```

## For get all documents
```
http://localhost:8080/api/articles
```

## For remove a document
```
http://localhost:8080/api/articles/remove/{id}
```