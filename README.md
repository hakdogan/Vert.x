[![Build Status](https://travis-ci.org/hakdogan/Vert.x.svg?branch=master)](https://travis-ci.org/hakdogan/Vert.x)
[![Codacy Badge](https://api.codacy.com/project/badge/Grade/5e7c0a2c146e4571865071dc609c87de)](https://www.codacy.com/app/hakdogan/Vert.x?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=hakdogan/Vert.x&amp;utm_campaign=Badge_Grade)
[![Coverage Status](https://coveralls.io/repos/github/hakdogan/Vert.x/badge.svg?branch=master)](https://coveralls.io/github/hakdogan/Vert.x?branch=master)

# Vert.x
This repository shows how to use the Vert.x Mongo client with the RESTful Web Service.

## How it's run?
```
sh run.sh
```

## How to perform CRUD operations?

### Save a document
```
http://localhost:8080/api/articles/save/{id}/{title}/{content}/{author}
```

### Get a document
```
http://localhost:8080/api/articles/article/{id}
```

### Get all documents
```
http://localhost:8080/api/articles
```

### Remove a document
```
http://localhost:8080/api/articles/remove/{id}
```

### Drop a collection
```
http://localhost:8080/api/collection/drop/{name}
```