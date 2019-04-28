<html lang="en">
<head>

    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">

    <title>Blog</title>

    <!-- Bootstrap core CSS -->
    <link rel="stylesheet" type="text/css" href="../css/bootstrap.css">
    <#--<link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css" integrity="sha384-ggOyR0iXCbMQv3Xipma34MD+dH/1fQ784/j6cY/iJTQUOhcWr7x9JvoRxT2MZw1T" crossorigin="anonymous">-->

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
    <div class="align-content-center">
        <button type="button" class="btn btn-lg btn-block btn-success" disabled>${clickNum} Visits</button>
    </div>
    <br>
    <div class="card-group">
        <div class="card text-center" style="width: 18rem;">
            <div class="card-header">
                <h5>QR</h5>
            </div>
            <div class="card-body">
                <img src="https://chart.googleapis.com/chart?cht=qr&chl=${url}&chs=300x300&chld=L|0"
                     class="qr-code img-thumbnail img-responsive" alt="">
            </div>
        </div>
        <div class="card text-center" style="width: 18rem;">
            <div class="card-header">
                <h5>Preview</h5>
            </div>
            <div class="card-body">
                <div id="show_lnk"></div>
            </div>
        </div>
    </div>
    <div class="card-group">
        <div class="card text-center" style="width: 18rem;">
            <div class="card-header">
                <h5>Visits By Date</h5>
            </div>
            <div class="card-body">
                <div id="piechart2"></div>
            </div>
        </div>
        <div class="card text-center" style="width: 18rem;">
            <div class="card-header">
                <h5>Visits By Browser</h5>
            </div>
            <div class="card-body">
                <#--<div align="left" id="piechart1" style="width: 500px; height: 500px;"></div>-->
                <div id="piechart1"></div>
            </div>
        </div>
    </div>
    <div class="card-group">
        <div class="card text-center" style="width: 18rem;">
            <div class="card-header">
                <h5>Visits By OS</h5>
            </div>
            <div class="card-body">
                <div align="left" id="piechart3"></div>
            </div>
        </div>
        <div class="card text-center" style="width: 18rem;">
            <div class="card-header">
                <h5>Visits By Ip</h5>
            </div>
            <div class="card-body">
                <div align="left" id="piechart4"></div>
            </div>
        </div>
    </div>
</div>


</div>
<script src="../js/jQuery.js"></script>
<#--<script src="https://code.jquery.com/jquery-3.3.1.slim.min.js" integrity="sha384-q8i/X+965DzO0rT7abK41JStQIAqVgRVzpbzo5smXKp4YfRvH+8abtTE1Pi6jizo" crossorigin="anonymous"></script>-->
<script src="../js/popper.js"></script>
<#--<script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.7/umd/popper.min.js" integrity="sha384-UO2eT0CpHqdSJQ6hJty5KVphtPhzWj9WO1clHTMGa3JDZwrnQq4sF86dIHNDz0W1" crossorigin="anonymous"></script>-->
<script src="../js/bootstrap.js"></script>
<#--<script src="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/js/bootstrap.min.js" integrity="sha384-JjSmVgyd0p3pXB1rRibZUAYoIIy6OrQ6VrjIEaFf/nJGzIxFDsf4x0xIM+B07jRM" crossorigin="anonymous"></script>-->
<script src="../js/table.js"></script>
<script src="../js/googleCharts.js"></script>
<script src="https://cdn.jsdelivr.net/npm/apexcharts"></script>

<script>
    $(document).ready(function () {
        var target = encodeURI('${url}');
        $.ajax({
            url: "https://api.linkpreview.net",
            dataType: 'jsonp',
            data: {q: target, key: '5a2e292e7d25bb63a2d3b4c63524cd10abe39420dc68c'},
            success: function (response) {
                $("#show_lnk").html('<img src="' + response.image + '" width="300" height="200"><h5>' + response.title + '</h5><p>' + response.description + '</p><a href="' + response.url + '">' + response.url + '</a>');
            }
        });

        drawChartBrowser();
        drawChartDate();
        drawChartOS();
        drawChartip()
    });


    function drawChartBrowser() {
        $.ajax({
            url: '/rest/browserUrl/${urlId}',
            success: function (response) {
                var data = [], labels = [];
                response.forEach(function (val) {
                    data.push(val.value);
                    labels.push(val.name);
                });

                var options = {
                    chart: {
                        type: 'pie',
                    },
                    series: data,
                    labels: labels,
                    responsive: [{
                        breakpoint: 480,
                        options: {
                            chart: {
                                width: 150
                            },
                            legend: {
                                position: 'bottom'
                            }
                        }
                    }]
                };

                var chart = new ApexCharts(document.querySelector("#piechart1"), options);
                chart.render();
            }
        });
    }

    function drawChartDate() {
        $.ajax({
            url: '/rest/dateUrl/${urlId}',
            success: function (response) {
                var data = [], labels = [];
                response.forEach(function (val) {
                    data.push(val.value);
                    labels.push(val.name);
                });

                var options = {
                    chart: {
                        type: 'donut',
                    },
                    series: data,
                    labels: labels,
                    responsive: [{
                        breakpoint: 480,
                        options: {
                            chart: {
                                width: 150
                            },
                            legend: {
                                position: 'bottom'
                            }
                        }
                    }]
                };

                var chart = new ApexCharts(document.querySelector("#piechart2"), options);
                chart.render();
            }
        });
    }

    function drawChartOS() {
        $.ajax({
            url: '/rest/osUrl/${urlId}',
            success: function (response) {
                var data = [], labels = [];
                response.forEach(function (val) {
                    data.push(val.value);
                    labels.push(val.name);
                });

                var options = {
                    chart: {
                        type: 'pie',
                    },
                    series: data,
                    labels: labels,
                    fill: {
                        type: 'gradient',
                    },
                    responsive: [{
                        breakpoint: 480,
                        options: {
                            chart: {
                                width: 150
                            },
                            legend: {
                                position: 'bottom'
                            }
                        }
                    }]
                };

                var chart = new ApexCharts(document.querySelector("#piechart3"), options);
                chart.render();
            }
        });
    }

    function drawChartip() {
        $.ajax({
            url: '/rest/ipUrl/${urlId}',
            success: function (response) {
                var data = [], labels = [];
                response.forEach(function (val) {
                    data.push(val.value);
                    labels.push(val.name);
                });

                var options = {
                    chart: {
                        type: 'donut',
                    },
                    series: data,
                    labels: labels,
                    theme: {
                        monochrome: {
                            enabled: true
                        }
                    },
                    responsive: [{
                        breakpoint: 480,
                        options: {
                            chart: {
                                width: 150
                            },
                            legend: {
                                position: 'bottom'
                            }
                        }
                    }]
                };

                var chart = new ApexCharts(document.querySelector("#piechart4"), options);
                chart.render();
            }
        });
    }


</script>
</body>
</html