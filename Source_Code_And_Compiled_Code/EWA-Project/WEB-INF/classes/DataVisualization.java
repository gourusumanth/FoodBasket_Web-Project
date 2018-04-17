
import java.sql.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import javax.servlet.annotation.WebServlet;
import java.util.*;
import com.google.gson.Gson;

@WebServlet("/DataVisualization")
public class DataVisualization extends HttpServlet {
    HttpSession session;
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
      session = request.getSession();

      PrintWriter pw = response.getWriter();

      String reqType = request.getParameter("reqType");
      String resultedJson = "";

      if (reqType.equals("inventory_chart")
                        || reqType.equals("inventory_table")
                        || reqType.equals("products_on_sale_table")
                        || reqType.equals("products_rebate_chart")) {
        ArrayList<MenuItems> products = MySQLDataStoreUtilities.getMenuItemsAvailability();
        resultedJson = new Gson().toJson(products);
      }
      if (reqType.equals("sales_by_date_chart")) {
        String[][] salesByDate = MySQLDataStoreUtilities.salesByDate();
        resultedJson = new Gson().toJson(salesByDate);
      }
      if (reqType.equals("sales_each_product_chart")) {
        String[][] salesByDate = MySQLDataStoreUtilities.salesEveryProduct();
        resultedJson = new Gson().toJson(salesByDate);
      }
      if (reqType.equals("sales_each_product_detail_chart")) {
        String[][] salesByDate = MySQLDataStoreUtilities.salesDetailsEachProduct();
        resultedJson = new Gson().toJson(salesByDate);
      }

      response.setContentType("application/JSON");
      response.setCharacterEncoding("UTF-8");
      response.getWriter().write(resultedJson);

  }

}
