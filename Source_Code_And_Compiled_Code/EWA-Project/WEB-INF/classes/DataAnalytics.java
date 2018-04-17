
import java.util.Map;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.WebServlet;
import java.io.*;
import java.util.*;

@WebServlet("/DataAnalytics")

public class DataAnalytics extends HttpServlet {
  HttpSession session;
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
                        throws ServletException, IOException {
    session = request.getSession();

    PrintWriter pw = response.getWriter();

    Utilities utility = new Utilities(pw,request,response);
    utility.printHtml("header.html");
    if (session.getAttribute("role") == null) {
      pw.print("Please login as Store Manager");
    } else {

      if (session.getAttribute("role").equals("Admin")) {

        String typeOfCharts[] = {"inventory_chart",
                                "inventory_table",
                                "products_on_sale_table",
                                // "products_rebate_chart",
                                "sales_by_date_chart",
                                "sales_each_product_chart",
                                "sales_each_product_detail_chart",
        };
        String chartHeading[] = {"Inventory Chart",
                                "Inventory Table",
                                "Products on Sale",
                                // "Products with Manufacturer Rebates",
                                "Sales by Date",
                                "Sales Each Product",
                                "Sales Each Product Detailed"
        };


        pw.print("<div class='panel panel-primary ' id='formcontainer' style='width: 70%;margin-left:200px; margin-top:180px;overflow: hidden;'>");
        pw.print("<div class='panel-heading'> Select From Following </div>"+
                "<div class='panel-body'>"+
                // "<form method='get' action='DataVisualizationCharts'>"+
                "<div class='form-group text-center'>");
        for (int i =0; i < typeOfCharts.length; i++) {
          pw.print("<form method='post' action='DataVisualizationCharts' style='padding: 5px; display: inline;'>" +
                  "<input type='hidden' name='typeOfChart' value='"+typeOfCharts[i]+"'>"+
                  "<input type='hidden' name='chartHeading' value='"+chartHeading[i]+"'>"+
                  "<input type='submit' class='btn btn-default managerbuttons' value='"+chartHeading[i]+"'></form>");
        }
        pw.print("</div></div></div>");
      } else {
        pw.print("Please log in as Store Manager to access Data ANalytics Part");
      }
    }
    utility.printHtml("footer.html");
  }
}
