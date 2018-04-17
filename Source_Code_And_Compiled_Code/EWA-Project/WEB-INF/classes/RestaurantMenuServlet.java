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

public class RestaurantMenuServlet  extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        process(request,response);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        process(request,response);
    }

    private void process(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        if(request.getParameter("addToCart")!=null){
            if(addToCart(request.getParameter("addToCart"),request,response)){
                showRestaurantMenu(response,request);
            }
        }else if(request.getParameter("restaurantId")!=null){
            showRestaurantMenu(response,request);
        }
    }

    public static boolean addToCart(String menuItemId,HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        HttpSession session = request.getSession();
        if(session.getAttribute("username")==null){
            response.setContentType("text/html");
            PrintWriter printWriter = response.getWriter();
            Utilities utility = new Utilities(printWriter,request,response);
            utility.printHtml("headersignin.html");
            List<MenuItems> menuItemsList = MySQLDataStoreUtilities.getRestaurantMenu(request.getParameter("restaurantId"));
            Restaurant restaurant = MySQLDataStoreUtilities.getRestaurantById(request.getParameter("restaurantId"));
            createRestaurantData(restaurant,printWriter);
            createRestaurantMenu(restaurant, menuItemsList,printWriter);
            utility.printHtml("footer.html");
            return false;
        }else{
            LinkedHashMap<String,Integer> cartItems = new LinkedHashMap<String,Integer>();
            if (session.getAttribute("cartItems") == null) {
                cartItems.put(menuItemId,1);
            } else {
                cartItems = (LinkedHashMap<String,Integer>) session.getAttribute("cartItems");
                if(cartItems.containsKey(menuItemId)){
                    Integer value = cartItems.get(menuItemId);
                    cartItems.put(menuItemId,value+1);
                }else{
                    cartItems.put(menuItemId,1);
                }
            }
            session.setAttribute("cartItems", cartItems);
        }
        return true;
    }


    private void showRestaurantMenu(HttpServletResponse response, HttpServletRequest request) throws IOException, ServletException {
        response.setContentType("text/html");
        PrintWriter printWriter = response.getWriter();
        Utilities utility = new Utilities(printWriter,request,response);
        utility.printHtml("header.html");
        List<MenuItems> menuItemsList = MySQLDataStoreUtilities.getRestaurantMenu(request.getParameter("restaurantId"));
        Restaurant restaurant = MySQLDataStoreUtilities.getRestaurantById(request.getParameter("restaurantId"));
        createRestaurantData(restaurant,printWriter);
        createRestaurantMenu(restaurant, menuItemsList,printWriter);
        utility.printHtml("footer.html");
        HttpSession session = request.getSession(true);
        if(session.getAttribute("restaurantId")!=null && !session.getAttribute("restaurantId").equals(request.getParameter("restaurantId"))){
            session.setAttribute("cartItems",new LinkedHashMap<String,Integer>());
        }
        request.getSession(true).setAttribute("restaurantId",request.getParameter("restaurantId"));
    }

    public static void createRestaurantData(Restaurant restaurant, PrintWriter printWriter) {
        StringBuilder htmlContentBuilder = new StringBuilder();
        htmlContentBuilder.append("<div class=\" bg-overlay\"><div style=\"margin-top: 170px;\"><div class=\"restaurantImage\"><img src=\""+restaurant.getImage()+"\" alt=\"...\" width=\"100px\" height=\"100px\" style=\"margin-left: 2px;;margin-right: 10px;\"></div><div class=\"restaurantNameMenu text-info\" style=\"color: white; font-size: 37px;\">"+restaurant.getName()+"\n" +
                "<div class=\"restaurantTaglineMenu\" style=\"color: white\">"+restaurant.getTagLine()+"<br><span style=\"font-weight: 400;color:white\">"+restaurant.getTime()+"</span><span><a href=\"ReviewServlet?viewReview="+restaurant.getId()+"\" style=\"text-decoration: none;\">\n" +
                "<i style=\"font-size: 16px;font-weight: 400;color:lightblue;\">View Reviews     </i>\n" +
                "</a><a href=\"ReviewServlet?writeReview="+restaurant.getId()+"\" style=\"font-size: 16px;font-weight: 400;color:lightblue;text-decoration: none\">, Write review</a></span></div></div><div class=\"restaurantTaglineMenu\"></div></div></div>");
        printWriter.write(htmlContentBuilder.toString());
    }

    public static void createRestaurantMenu(Restaurant restaurant, List<MenuItems> menuItemsList, PrintWriter printWriter) {
        StringBuilder htmlContentBuilder = new StringBuilder();
        if(menuItemsList.size()==0){
            htmlContentBuilder.append("<h3 style=\"\">    No Menu Items Available Currently. Please Try Later.\n\n\n</h3>\n");
        }
        htmlContentBuilder.append("<div id=\"menucontent\" style=\"overflow-y: scroll\">\n <div class=\"row\">");
        for(int i = 0; i < menuItemsList.size(); i++){
            MenuItems menuItem = menuItemsList.get(i);
            htmlContentBuilder.append("<div class=\"menuCard col-md-6 col-ls-6 col-sm-6\"> <div class=\"restaurantNameMenu text-info\"><a href=\"RestaurantMenuServlet?restaurantId="+restaurant.getId()+"&addToCart="+menuItem.getId()+"\">"+menuItem.getName()+" </a><div class=\"restaurantTaglineMenu\">\n" +
                    "<span style=\"font-weight: 700\">Ingredients List<br></span>"+menuItem.getIngredients()+"<br></div></div><div class=\"minimumAmountMenu\"><div class=\"restaurantTaglineMenu\">\n" +
                    "<span style=\"font-weight: 700;\" class=\"text-primary\">Price</span></div><span>"+menuItem.getPrice()+"$</span></div></div>");
        }

        htmlContentBuilder.append("</div></div> ");
        printWriter.print(htmlContentBuilder.toString());
    }


}
