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