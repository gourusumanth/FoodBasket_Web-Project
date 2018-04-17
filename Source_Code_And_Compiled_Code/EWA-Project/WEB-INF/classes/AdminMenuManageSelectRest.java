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

@WebServlet("/AdminMenuManageSelectRest")

public class AdminMenuManageSelectRest extends HttpServlet {
    HttpSession session;
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
      session = request.getSession(true);
      response.setContentType("text/html");

      PrintWriter pw = response.getWriter();
      Utilities utility = new Utilities(pw,request,response);
      String req = request.getParameter("req").toString();
      List<Restaurant> restaurants = MySQLDataStoreUtilities.getAllRestaurants();
      utility.printHtml("header.html");

      pw.print("<div class='panel panel-primary ' id='formcontainer' style='width: 70%;margin-left:200px; margin-top:180px;overflow: hidden;'>"+
        "<div class='panel-heading'> Select Restaurant </div>"+
          "<div class='panel-body'>"+
          "<form action='AdminMenuManageSelectItem'><div>"+
          "<input type='hidden' name='req' value='"+req+"' id='req'>"+
      "<select name='restaurant'><option value='0' selected>(please select:)</option>");

      for (int i =0; i< restaurants.size(); i++) {
        pw.print("<option value='"+restaurants.get(i).getId()+"'>"+restaurants.get(i).getName()+"</option>");
      }

      pw.print("</select>"+
      "<div><input type='submit' value='submit'></div>"+
      "</form></div></div></div>");

      utility.printHtml("footer.html");
    }
}
