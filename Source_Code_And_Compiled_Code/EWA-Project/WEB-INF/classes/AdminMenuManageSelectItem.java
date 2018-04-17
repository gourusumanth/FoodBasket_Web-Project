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

@WebServlet("/AdminMenuManageSelectItem")

public class AdminMenuManageSelectItem extends HttpServlet {
    HttpSession session;
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
      session = request.getSession(true);
      response.setContentType("text/html");

      PrintWriter pw = response.getWriter();
      Utilities utility = new Utilities(pw,request,response);

      String req = request.getParameter("req");
      String restId = request.getParameter("restaurant");


      if(req.equals("Add")) {
        session.setAttribute("vendorRestaurant", restId);
        utility.printHtml("uploadFoodScreen.html");
      } else {
        utility.printHtml("header.html");

        List<MenuItems> items = MySQLDataStoreUtilities.getRestaurantMenu(restId);

        pw.print("<div class='panel panel-primary ' id='formcontainer' style='width: 70%;margin-left:200px; margin-top:180px;overflow: hidden;'>"+
          "<div class='panel-heading'> Select Menu Item </div>"+
            "<div class='panel-body'>"+
            "<form action='EditMenuItem'><div>"+
            "<input type='hidden' name='req' id ='req' value='"+req+"'>"+
        "<select name='menuItemId'><option value='0' selected>(please select:)</option>");

        for (int i =0; i< items.size(); i++) {
          pw.print("<option value='"+items.get(i).getId()+"'>"+items.get(i).getName()+"</option>");
        }

        pw.print("</select>"+
        "<div><input type='submit' value='submit'></div>"+
        "</form></div></div></div>");

        utility.printHtml("footer.html");
      }

    }
}
