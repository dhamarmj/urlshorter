<html lang="en">
<head>

    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">

    <title>Blog</title>

    <!-- Bootstrap core CSS -->
    <link rel="stylesheet" type="text/css" href="/css/bootstrap.css">
</head>
<body>
<#if userSigned == false>
    <#include "firstNavBar.ftl">
<#else>
    <#include "navbar.ftl">
</#if>
<br>
<br>
<br>
<br>
<!-- Main Content -->
<div class="container">
    <div>
        <#--<form class="needs-validation" id="form" name="form" method="post" action="/generateUrl">-->
        <form class="needs-validation" id="form">
            <div class="row text-center">
                <div class="col-10">
                    <div name="redirect" id="redirect"></div>
                    <input type="text" class="form-control" placeholder="Paste a link to shorten it" id="url"
                           name="url"
                           width="col-md-auto" required>
                    <div class="invalid-feedback">
                        Valid URL is required.
                    </div>
                </div>
                <div class="col-2">
                    <button class="btn btn-warning" type="submit" id="sendButton">Shorten!</button>
                </div>
            </div>
        </form>
    </div>
    <div class="justify-content-center">
        <br>
        <div class="py-3 text-center">
            <h2>Enlaces</h2>
        </div>
        <table id="short_urls" class="display" style="width:100%">
            <thead>
            <tr>
                <th style="display:none;">Id</th>
                <th>URL</th>
                <th>Redirect</th>
                <#if usuario == "admin">
                    <th>See stats</th>
                </#if>
            </tr>
            </thead>
            <tbody>
            <tr>
            </tr>
            </tbody>
        </table>
    </div>
</div>
<script src="../js/jQuery.js"></script>
<script src="../js/table.js"></script>
<script src="../js/urlTable.js"></script>
</body>
</html>