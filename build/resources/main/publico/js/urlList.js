$(document).ready(function () {
    loadSavedTable()
})

function loadSavedTable() {
    $.ajax({
        url: '/rest/urls',
        success: function (response) {
            var tabla = $('#short_urls').DataTable({
                destroy: true,
                data: response,
                columns: [
                    {targets: 0, data: 'id', visible: false},
                    {
                        targets: 1,
                        data: 'url',
                        "render": function (data, type, row, meta) {
                            return '<a href="' + data + '"> ' + data + '</a>'
                        },
                    },
                    {
                        targets: 2, data: 'redirect',
                        "render": function (data, type, row, meta) {
                            return '<a href="' + data + '"> ' + data + '</a>'
                        }
                    },
                    {
                        targets: 3,
                        data: 'id',
                        "render": function (data, type, row, meta) {
                            return '<button class="btn btn-info btn-sm" id=val_' + data + ' onclick="openStat(this.id)"> Stats! </button>' + '<button class="btn btn-danger btn-sm" id=val_' + data + ' onclick="deleteForm(this.id)">Eliminar</button>'
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

function deleteForm(id) {
    var num = id.replace('val_', '');
    window.location.href = '/rest/url/' + num;
}

function openStat(id){
    var num = id.replace('val_', '');
    window.location.href = '/StatsUrl/' + num;
}

