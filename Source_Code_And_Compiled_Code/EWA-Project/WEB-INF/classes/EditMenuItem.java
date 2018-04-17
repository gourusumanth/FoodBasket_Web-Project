import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import javax.servlet.annotation.WebServlet;

@WebServlet("/EditMenuItem")

public class EditMenuItem extends HttpServlet {
    HttpSession session;
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
      session = request.getSession(true);

      response.setContentType("text/html");
      PrintWriter pw = response.getWriter();
      Utilities utility = new Utilities(pw,request,response);

      String menuitemId = request.getParameter("menuItemId");
      String req = request.getParameter("req");

      if(req.equals("Delete")) {
        MySQLDataStoreUtilities.deleteMenuItem(menuitemId);
        response.sendRedirect("AdminServlet");
      } else {
        MenuItems item = MySQLDataStoreUtilities.getMenuItemById(menuitemId);

        utility.printHtml("header.html");

        pw.print("<div class='middlePage' style='margin-top: 100px;'>"+
            "<div class='page-header'>"+
            "<h1 class='logo'> <small>Update Menu Item</small></h1>"+
            "</div>"+
            "<div class='panel panel-info'>"+
                "<div class='panel-heading'>"+
                    "<h3 class='panel-title'>Fill the fields</h3>"+
                "</div>"+
                "<div class='panel-body'>"+
                    "<div class='row'>"+
                        "<div class='col-md-3' >"+
                            "<h3 style='text-align: center;margin-top: 150px;color:#f63440;font-weight: 700'>FOODBASKET</h3>"+
                        "</div>"+
                        "<div class='col-md-9' style='border-left:1px solid #ccc;'>"+
                            "<form class='form-horizontal' action='UpdateMenuItem' method='post' >"+
                            "<input type='hidden' name='restaurantId' value='"+item.getRestaurantId()+"'>"+
                            "<input type='hidden' name='menuId' value='"+item.getId()+"'>"+
                                "<fieldset>"+
                                    "<div class='form-group'>"+
                                    "<label class='control-label col-sm-2' for='name'>Name:</label>"+
                                    "<div class='col-sm-10'>"+
                                    "<input type='text' class='form-control' id='name' name='name' value='"+item.getName()+"' required>"+
                                    "</div>"+
                                    "</div>"+
                                    "<div class='form-group'>"+
                                    "<label class='control-label col-sm-2' for='ingredients'>ingredients:</label>"+
                                    "<div class='col-sm-10'>"+
                                    "<input type='text' class='form-control' id='ingredients' name='ingredients' value='"+item.getIngredients()+"' required>"+
                                    "</div>"+
                                    "</div>"+
                                    "<div class='form-group'>"+
                                    "<label class='control-label col-sm-2 ' for='price'>price:</label>"+
                                    "<div class='col-sm-10'>"+
                                    "<input type='number' class='form-control' id='price' name='price' value='"+item.getPrice()+"' required>"+
                                    "</div>"+
                                    "</div>"+
                                    "<div class='form-group'>"+
                                    "<label class='control-label col-sm-2 ' for='image'>Image:</label>"+
                                    "<div class='col-sm-10'>"+
                                    "<input type='text' class='form-control' id='image' name='image' value='"+item.getImage()+"' required>"+
                                    "</div>"+
                                    "</div>"+
                                    "<div class='form-group'>"+
                                    "<label class='control-label col-sm-2 ' for='inventory'>No of items available:</label>"+
                                    "<div class='col-sm-10'>"+
                                    "<input type='number' class='form-control' id='inventory' name='inventory' value='"+item.getAvailability()+"' required>"+
                                    "</div>"+
                                    "</div>"+
                                    "<div class='form-group'>"+
                                    "<div class='col-sm-offset-2 col-sm-10'>"+
                                    "<button type='submit' class='btn btn-success pull-right'>Update</button>"+
                                    "</div>"+
                                    "</div>"+
                                "</fieldset>"+
                            "</form>"+
                        "</div>"+
                    "</div>"+
                "</div>"+
            "</div>");

        utility.printHtml("footer.html");
      }


    }
}
