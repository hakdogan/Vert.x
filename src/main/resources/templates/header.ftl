<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <meta http-equiv="x-ua-compatible" content="ie=edge">
    <title>Vert.x tutorial | Simple tutorial about Vert.x</title>
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.1.1/css/bootstrap.min.css" integrity="sha384-WskhaSGFgHYWDcbwN70/dfYBj47jz9qbsMId/iRN3ewGhXQFZCSftd1LZCfmhktB" crossorigin="anonymous">
    <link href="https://fonts.googleapis.com/icon?family=Material+Icons" rel="stylesheet">

    <style>
        body {
            margin: 5px;
        }

        .centralizing {
            width: 600px !important;
            margin: 20px auto auto auto;
            padding: 5px;
        }

    </style>
</head>
<body>
<nav class="navbar navbar-expand-lg navbar-light bg-light">
    <a class="navbar-brand" href="#">Vert.x</a>
    <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarSupportedContent" aria-controls="navbarSupportedContent" aria-expanded="false" aria-label="Toggle navigation">
        <span class="navbar-toggler-icon"></span>
    </button>

    <div class="collapse navbar-collapse" id="navbarSupportedContent">
        <ul class="navbar-nav mr-auto">
            <li class="nav-item ${context.homeActive}">
                <a class="nav-link" href="/">Home</a>
            </li>
            <li class="nav-item ${context.allArticlesActive}">
                <a class="nav-link" href="/api/articles">All Articles</a>
            </li>
            <li class="nav-item ${context.saveArticleActive}">
                <a class="nav-link" href="/api/articles/save">Save Article</a>
            </li>
        </ul>
    </div>
</nav>