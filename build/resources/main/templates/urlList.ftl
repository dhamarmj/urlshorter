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
<!-- Main Content -->
<div class="container">
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
                <th>Actions</th>
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
<script src="../js/urlList.js"></script>
</body>
</html>