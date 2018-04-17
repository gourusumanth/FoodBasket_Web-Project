function loadBarChartPackage() {
  google.charts.load('current', {'packages':['bar']});
}

function loadTableChartPackage() {
  google.charts.load('current', {'packages':['table']});
}

function inventory_chart (data) {
  loadBarChartPackage();
  google.charts.setOnLoadCallback(function() {
    customChart(data);
  });
}

function inventory_table (data) {
  loadTableChartPackage();
  google.charts.setOnLoadCallback(function() {
    customInventoryTableChart(data);
  });
}
// function products_rebate_chart (data) {
//   loadTableChartPackage();
//   google.charts.setOnLoadCallback(function() {
//     customProductsRebateTableChart(data);
//   });
// }
function products_on_sale_table (data) {
  loadTableChartPackage();
  google.charts.setOnLoadCallback(function() {
    customProductsOnSaleTableChart(data);
  });
}
function sales_by_date_chart (data) {
  loadTableChartPackage();
  google.charts.setOnLoadCallback(function() {
    customSalesByDateChart(data);
  });
}
function sales_each_product_chart(data) {
  loadBarChartPackage();
  google.charts.setOnLoadCallback(function() {
    customSalesEachProductChart(data);
  });
}
function sales_each_product_detail_chart(data) {
  loadTableChartPackage();
  google.charts.setOnLoadCallback(function() {
    customSalesEachProductDetailChart(data);
  });
}

function customSalesEachProductDetailChart(salesData) {
  var data = new google.visualization.DataTable();
      data.addColumn('string', 'Product Name');
      data.addColumn('string', 'Product Price ($)');
      data.addColumn('string', 'Number of Sales');
      data.addColumn('string', 'Total Sales ($)');

      for (var i = 0; i< salesData.length; i++) {
        data.addRow(salesData[i]);
      }


      var table = new google.visualization.Table(document.getElementById('chart_div'));

      table.draw(data, {showRowNumber: true, width: '100%', height: '100%'});

}
function customChart(data) {

  var productNameArray = new Array(data.length);
  var products = new Array(data.length);
  products[0] = new Array(2);
  products[0][0] = "Product Name";
  products[0][1] = "Number of available Items";

  for (var i = 0; i< data.length; i++) {
  products[i+1] = new Array(2);
  productNameArray[i] = data[i].name;
  products[i+1][0] = data[i].name;
  products[i+1][1] = data[i].availability.toString();
  }
  console.log(products[0][0]);
  products = products.sort(function(a, b) {
    return a[1] - b[1];
  })
      var data = google.visualization.arrayToDataTable(products);
      var options = {
        'width': 800,
        'height': 1500,
        chart: {
          title: 'Inventory',
          subtitle: productNameArray,
        },
        bars: 'horizontal',
      };

      var materialChart = new google.charts.Bar(document.getElementById('chart_div'));
      materialChart.draw(data, options);
    }
function customSalesByDateChart(salesData) {
  var data = new google.visualization.DataTable();
      data.addColumn('string', 'Date');
      data.addColumn('string', 'Number Sales');
      for (var i = 0; i< salesData.length; i++) {
        data.addRow(salesData[i]);
      }


      var table = new google.visualization.Table(document.getElementById('chart_div'));

      table.draw(data, {showRowNumber: true, width: '100%', height: '100%'});

    }
function customSalesEachProductChart(data) {
  console.log(data);
  var productNameArray = new Array(data.length);
  var sales = new Array(data.length);
  sales[0] = new Array(2);
  sales[0][0] = "Product Name";
  sales[0][1] = "Total Sales";

  for (var i = 0; i< data.length; i++) {
  sales[i+1] = new Array(2);
  productNameArray[i] = data[i][0];
  sales[i+1][0] = data[i][0];
  sales[i+1][1] = data[i][1];
  }
  sales = sales.sort(function(a, b) {
    return a[1] - b[1];
  })
  console.log(sales[1][0]);
  var chartData = google.visualization.arrayToDataTable(sales);
  var options = {
    'width': 800,
    'height': 1500,
    chart: {
      title: 'Total Sales',
      subtitle: productNameArray,
    },
    bars: 'horizontal',
  };

  var materialChart = new google.charts.Bar(document.getElementById('chart_div'));
  materialChart.draw(chartData, options);
}
function customInventoryTableChart(pData) {
  console.log(pData);
  var data = new google.visualization.DataTable();
  data.addColumn('string', 'Product Name');
  data.addColumn('number', 'Product Price ($)');
  data.addColumn('number', 'Number of Available');

  for (var i = 0; i< pData.length; i++) {
    data.addRow([pData[i].name, pData[i].price, pData[i].availability]);
  }

  var table = new google.visualization.Table(document.getElementById('chart_div'));

  table.draw(data, {showRowNumber: true, width: '100%', height: '100%'});

}
function customProductsOnSaleTableChart(pData) {
  var data = new google.visualization.DataTable();
  data.addColumn('string', 'Product Name');
  data.addColumn('number', 'Product Price ($)');
  console.log(pData);
  for (var i = 0; i< pData.length; i++) {
    if (pData[i].sale == true) {
      data.addRow([pData[i].name, pData[i].price]);
    }
  }
  var table = new google.visualization.Table(document.getElementById('chart_div'));
  table.draw(data, {showRowNumber: true, width: '100%', height: '100%', title: 'Products On Sale'});
}
// function customProductsRebateTableChart(pData) {
//
//   var data = new google.visualization.DataTable();
//   data.addColumn('string', 'Product Name');
//   data.addColumn('number', 'Product Price ($)');
//
//   for (var i = 0; i< pData.length; i++) {
//     if (pData[i].rebate == true) {
//       data.addRow([pData[i].smartName, pData[i].smartPrice]);
//     }
//   }
//   var table = new google.visualization.Table(document.getElementById('chart_div'));
//   table.draw(data, {showRowNumber: true, width: '100%', height: '100%', title: 'Products On Sale'});
// }
