// import javax.servlet.*;
// import javax.servlet.http.*;
// import javax.servlet.annotation.WebServlet;
// import java.io.IOException;
// import java.io.PrintWriter;
// import java.util.List;
// import java.util.ArrayList;
//
// @WebServlet("/UploadMenuItem")
//
// public class UploadMenuItem extends HttpServlet {
//   HttpSession session;
//
//   protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//
//     session = request.getSession();
//
//     if (session.getAttribute("username") != null) {
//       String userName = session.getAttribute("username").toString();
//
//       String restId = request.getParameter("restId");
//       String itemName = request.getParameter("itemName");
//       String itemIngredients = request.getParameter("itemIngredients");
//       float itemPrice = Float.parseFloat(request.getParameter("price").toString());
//       String itemImage = request.getParameter("itemImage");
//
//       try {
//         List<MenuItems> menuItems = new ArrayList<MenuItems>();
//
//         MenuItems menuItem = new MenuItems();
//         menuItem.setId("1");
//         menuItem.setRestaurantId(restId);
//         menuItem.setName(itemName);
//         menuItem.setIngredients(itemIngredients);
//         menuItem.setPrice(itemPrice);
//         menuItem.setImage(itemImage);
//
//         menuItems.add(menuItem);
//         MySQLDataStoreUtilities.saveMenuItems(menuItems);
//
//       } catch (Exception e) {
//         response.sendRedirect("Error?er="+e.toString());
//       }
//     }
//   }
// }
