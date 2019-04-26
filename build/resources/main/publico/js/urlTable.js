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
                    {
                        targets: 3,
                        data: 'id',
                        "render": function (data, type, row, meta) {
                            return '<button class="btn btn-info btn-sm" id=val_' + data + ' onclick="openStat(this.id)"> Stats!</button>'
                        },
                    }
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