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

@WebServlet("/UpdateMenuItem")

public class UpdateMenuItem extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        process(request,response);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        process(request,response);
    }

    private void process(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
      HttpSession session = request.getSession();

      String menuItemId = request.getParameter("menuId");
      String restaurant = request.getParameter("restaurantId");
      String itemName = request.getParameter("name");
      String ingredients = request.getParameter("ingredients");
      float price = Float.parseFloat(request.getParameter("price"));
      String image = request.getParameter("image");
      int availability = Integer.parseInt(request.getParameter("inventory"));

      MenuItems item = MySQLDataStoreUtilities.getMenuItemById(menuItemId);
      item.setId(menuItemId);
      item.setRestaurantId(restaurant);
      item.setName(itemName);
      item.setIngredients(ingredients);
      item.setPrice(price);
      item.setImage(image);
      item.setSale(true);
      item.setAvailability(availability);

      MySQLDataStoreUtilities.updateMenuItem(item);

      response.sendRedirect("AdminServlet");

    }
  }
