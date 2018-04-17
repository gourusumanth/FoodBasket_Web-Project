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

@WebServlet("/VendorUploadFood")

public class VendorUploadFood extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        process(request,response);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        process(request,response);
    }

    private void process(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
      HttpSession session = request.getSession();
      String restaurant = session.getAttribute("vendorRestaurant").toString();
      String itemName = request.getParameter("name");
      String ingredients = request.getParameter("ingredients");
      float price = Float.parseFloat(request.getParameter("price"));
      String image = request.getParameter("image");
      int availability = Integer.parseInt(request.getParameter("inventory"));

      int newMenuItemId = MySQLDataStoreUtilities.getNewMenuItemId();
      MenuItems item = new MenuItems();
      item.setId(newMenuItemId+"");
      item.setRestaurantId(restaurant);
      ArrayList<String> list = new ArrayList<String>();
      list.add(" ");
      item.setSimilarMenuItems(list);
      item.setName(itemName);
      item.setIngredients(ingredients);
      item.setPrice(price);
      item.setImage("images/"+image);
      item.setSale(true);
      item.setAvailability(availability);
      List<MenuItems> menuItemsList = new ArrayList<>();
      menuItemsList.add(item);
      MySQLDataStoreUtilities.saveMenuItems(menuItemsList);
      String role = session.getAttribute("role").toString();
      if(role.equals("Customer")){
          session.setAttribute("cartItems",new LinkedHashMap<String,Integer>());
          response.sendRedirect("MainPageServlet");
      }else if(role.equals("FoodVendor")){
          response.sendRedirect("FoodVendorServlet");
      }else if(role.equals("Admin")){
          response.sendRedirect("AdminServlet");
      }
    }
  }
