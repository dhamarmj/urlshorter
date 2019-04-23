<html lang="en">
<head>

    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">

    <title>Blog</title>

    <!-- Bootstrap core CSS -->
    <link rel="stylesheet" type="text/css" href="/css/bootstrap.css">
</head>

<body>
<nav class="navbar navbar-expand-lg navbar-dark bg-dark fixed-top" id="mainNav">
    <div class="container">
        <a class="navbar-brand" href="/">Short URL</a>
        <button class="navbar-toggler navbar-toggler-right" type="button" data-toggle="collapse"
                data-target="#navbarResponsive" aria-controls="navbarResponsive" aria-expanded="false"
                aria-label="Toggle navigation">
            Menu
            <i class="fas fa-bars"></i>
        </button>
        <div class="collapse navbar-collapse" id="navbarResponsive">
            <ul class="navbar-nav ml-auto">
                <li class="nav-item">
                    <a class="nav-link" href="/">My URL's</a>
                </li>
                <#if usuario == "admin">
                    <li class="nav-item">
                        <a class="nav-link" href="/Admin/Compose/">All URL's</a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" href="/Register/">Register User</a>
                    </li>
                </#if>
                <li class="nav-item">
                    <a class="nav-link" href="/LogOut/">Log Out</a>
                </li>
            </ul>
        </div>
    </div>
</nav>
</body>
</html>