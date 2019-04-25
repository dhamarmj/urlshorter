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
        <div class="py-3 text-center">
            <h2>Stats</h2>
        </div>
    </div>
    <div class="card-group">
        <div class="card text-center" style="width: 18rem;">
            <div class="card-body">
                <h5 class="card-title">${clickNum} Visits</h5>
            </div>
        </div>
        <div class="card text-center" style="width: 18rem;">
            <div class="card-body">
                <h5 class="card-title">${clickNum} Visits</h5>
            </div>
        </div>
    </div>
    <div align="left" id="piechart" style="width: 500px; height: 500px;"></div>

</div>
<script src="../js/jQuery.js"></script>
<script src="../js/table.js"></script>
<script src="../js/googleCharts.js"></script>
<script src="../js/chartPage.js"></script>
</body>
</html