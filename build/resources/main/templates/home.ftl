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
<#--<script src="https://code.jquery.com/jquery-3.3.1.slim.min.js" integrity="sha384-q8i/X+965DzO0rT7abK41JStQIAqVgRVzpbzo5smXKp4YfRvH+8abtTE1Pi6jizo" crossorigin="anonymous"></script>-->
<script src="../js/popper.js"></script>
<#--<script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.7/umd/popper.min.js" integrity="sha384-UO2eT0CpHqdSJQ6hJty5KVphtPhzWj9WO1clHTMGa3JDZwrnQq4sF86dIHNDz0W1" crossorigin="anonymous"></script>-->
<script src="../js/bootstrap.js"></script>
<#--<script src="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/js/bootstrap.min.js" integrity="sha384-JjSmVgyd0p3pXB1rRibZUAYoIIy6OrQ6VrjIEaFf/nJGzIxFDsf4x0xIM+B07jRM" crossorigin="anonymous"></script>-->
<script src="../js/table.js"></script>
<script src="../js/googleCharts.js"></script>

<script>
    $("#form").submit(function () {
        var data = document.getElementById("url").value;

        var salida = {url: data};
        $.ajax({
            type: "POST",
            url: '/generateUrl',
            contentType: 'application/json',
            data: JSON.stringify(salida),
            dataType: 'json'
        }).onsuccess(function () {
            loadSavedTable()
        }).onerror(function () {
            console.log(salida);
        });
        return false;
    });
    $(document).ready(function () {
        loadSavedTable()
    })

    function loadSavedTable() {
        $.ajax({
            url: '/rest/urlUser',
            success: function (response) {
                console.log(response);
                var tabla = $('#short_urls').DataTable({
                    destroy: true,
                    data: response,
                    columns: [
                        {targets: 0, data: 'id', visible: false},
                        {
                            targets: 1,
                            data: 'url',
                            "render": function (data, type, row, meta) {
                                return '<a href="'+data+'"> '+data+'</a>'
                            },
                        },
                        {targets: 2,data: 'redirect',
                            "render": function (data, type, row, meta) {
                                return '<a href="'+data+'"> '+data+'</a>'
                            }
                        },
                        <#if usuario == "admin">
                        {
                            targets: 3,
                            data: "id",
                            render: function(data, type, row, meta) {
                                return (
                                    '<button class="btn btn-info btn-sm" id=val_' +
                                    data +
                                    ' onclick="openStat(this.id)"> Stats!</button>'
                                );
                            }
                        }
                        </#if>
                    ],
                    buttons: [],
                    order: [[0, "desc"]],
                    language: {
                        search: "Buscar: ",
                        paginate: {
                            previous: "Anterior ",
                            next: " Siguiente"
                        },
                        emptyTable: "No hay datos disponibles",
                        info: "Mostrando del _START_ al _END_ de _TOTAL_ registros",
                    }

                });
                tabla.columns.adjust().draw();
            }
        });

    }

    function openStat(id){
        var num = id.replace('val_', '');
        window.location.href = '/StatsUrl/' + num;
    }
</script>
</body>
</html>