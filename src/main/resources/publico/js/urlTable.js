$("#form").submit(function () {

    $.ajax({
        type: "POST",
        url: '/generateUrl',
        contentType: 'application/json',
        data: JSON.stringify(data),
        dataType: 'json'
    })
    // var db = new Dexie("form");
    // db.version(1).stores({
    //     form: '++id,url,redirect'
    // });
    // db.form.add({
    //     url: $('#firstName').val(),
    //     redirect: $('#redirect').val(),
    // });
    // document.getElementById("form").reset();
    // loadTabla()
    return false;
});

function URlSaved() {
    $.ajax({
        type: "POST",
        url: '/generateUrl',
        contentType: 'application/json',
        data: JSON.stringify(data),
        dataType: 'json'
    })
}

function loadSavedTable() {
    $.ajax({
        url: '/rest/urlUser',
        async: false,
        success: function (response) {
            var tabla = $('#short_urls').DataTable({
                destroy: true,
                data: response,
                columns: [
                    {
                        data: 'id',
                        visible: false
                    },
                    {data: 'url'},
                    {data: 'redirect'},
                    {
                        targets: 2,
                        data: 'id',
                        "render": function (data, type, row, meta) {
                            return '<button class="btn btn-info btn-sm" id=editar_' + data + ' onclick="openEdit(this.id)"> Stats!</button>'
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

function loadTabla() {
    var json = [];
    var forms = getData().then(function (results) {
        results.forEach(function (data) {
            json.push({
                id: data.id,
                name: data.name,
                sector: data.sector,
                education: data.education,
            });
        });
        reloadTabla(json);
    });
}

function getData() {
    var db = new Dexie("form");
    db.version(1).stores({
        form: '++id,url,redirect'
    });
    return db.form.toArray();
}

function reloadTabla(nuevo) {

    var tabla = $('#short_urls').DataTable({
        destroy: true,
        data: nuevo,
        columns: [
            {targets: 0, data: 'url'},
            {targets: 1, data: 'redirect'}
        ],
        searchable: false,
        buttons: [],
        language: {
            search: "Buscar: ",
            paginate: {
                previous: "Anterior ",
                next: " Siguiente"
            },
            emptyTable: "No hay datos disponibles",
            info: "Mostrando del _START_ al _END_ de _TOTAL_ registros"
        },

        createdRow: function (row, data, index) {
            $('td', row).eq(5).addClass('letra');
        }

    });

    tabla.columns.adjust().draw();
}

