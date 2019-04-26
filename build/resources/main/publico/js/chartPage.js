google.charts.load('current', {packages: ['corechart']});
google.charts.setOnLoadCallback(drawChart);

function drawChart() {
    $.ajax({
        url: '/rest/urls',
        success: function (response) {
            var data = new google.visualization.DataTable();
            data.addColumn('string', 'Browser');
            data.addColumn('number', 'Quantity');
            data.addRows(response);

            // Instantiate and draw the chart.
            var chart = new google.visualization.PieChart(document.getElementById('piechart'));
            chart.draw(data, null);
        }
    });
}