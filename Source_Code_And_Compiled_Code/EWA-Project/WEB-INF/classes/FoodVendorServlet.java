import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class FoodVendorServlet extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        process(request,response);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        process(request,response);
    }

    private void process(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        response.setContentType("text/html");
        HttpSession session = request.getSession(true);
        if(request.getParameter("editItem")!=null){
            displayMenuItemsListForEdit(request,response);
        }else if(request.getParameter("editMenuItem")!=null){
            displayMenuItemToEdit(request,response);
        }else if(request.getParameter("updateDetails")!=null){
            updateMenuItemDetails(request,response);
        }else if(request.getParameter("deleteItem")!=null){
            displayMenuItemsListForDelete(request,response);
        }else if(request.getParameter("deleteMenuItem")!=null){
            displayMenuItemToDelete(request,response);
        }else if (session.getAttribute("username") != null) {
          PrintWriter pw = response.getWriter();
          String restaurant = MySQLDataStoreUtilities.getVendor(session.getAttribute("username").toString());
          session.setAttribute("vendorRestaurant", restaurant);
          Utilities utility = new Utilities(pw,request,response);
          utility.printHtml("header.html");
          utility.printHtml("foodVendorScreen.html");
          utility.printHtml("footer.html");
        }

    }

    private void displayMenuItemToDelete(HttpServletRequest request, HttpServletResponse response) throws IOException {
        MySQLDataStoreUtilities.deleteMenuItem(request.getParameter("deleteMenuItemId"));
        response.sendRedirect("FoodVendorServlet");
    }


    private void displayMenuItemsListForDelete(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        HttpSession session = request.getSession(true);
        PrintWriter printWriter = response.getWriter();
        String restaurant = MySQLDataStoreUtilities.getVendor(session.getAttribute("username").toString());
        List<MenuItems> menuItems = MySQLDataStoreUtilities.getRestaurantMenu(restaurant);
        session.setAttribute("vendorRestaurant", restaurant);
        Utilities utility = new Utilities(printWriter,request,response);
        utility.printHtml("header.html");
        StringBuilder htmlContentBuilder = new StringBuilder();
        htmlContentBuilder.append("<div class=\"panel panel-primary\" id=\"formcontainer\" style=\"width:500px;margin-top:30px;margin-left:30%\">\n" +
                "    <div class=\"panel-heading\">Update Menu Item</div>\n" +
                "    <div class=\"panel-body\">\n" +
                "        <form method=\"post\" action=\"FoodVendorServlet?deleteMenuItem=true\">\n" +
                "            <div class=\"form-group\">\n" +
                "               <select name=\"deleteMenuItemId\" id=\"itemId\" class=\"form-control\">" );

        for (int i=0; i <menuItems.size(); i++){
            htmlContentBuilder.append("<option value=\""+menuItems.get(i).getId()+"\">"+menuItems.get(i).getName()+"</option> ");
        }
        htmlContentBuilder.append("                </select>\n" +
                "            </div>\n" +
                "            <button type=\"submit\" class=\"btn btn-success pull-right\" >Submit</button>\n" +
                "\n" +
                "        </form>\n" +
                "    </div>\n" +
                "</div>");
        printWriter.print(htmlContentBuilder.toString());
        utility.printHtml("footer.html");
    }

    private void updateMenuItemDetails(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        HttpSession session = request.getSession(true);
        String restaurant = MySQLDataStoreUtilities.getVendor(session.getAttribute("username").toString());
        MenuItems menuItems = MySQLDataStoreUtilities.getMenuItemById(request.getParameter("updateDetails"));
        session.setAttribute("vendorRestaurant", restaurant);
        MySQLDataStoreUtilities.deleteMenuItem(menuItems.getId());

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

        response.sendRedirect("FoodVendorServlet");

    }

    private void displayMenuItemToEdit(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(true);
        PrintWriter printWriter = response.getWriter();
        String restaurant = MySQLDataStoreUtilities.getVendor(session.getAttribute("username").toString());
        MenuItems menuItems = MySQLDataStoreUtilities.getMenuItemById(request.getParameter("editMenuItemId"));
        session.setAttribute("vendorRestaurant", restaurant);
        Utilities utility = new Utilities(printWriter,request,response);
        utility.printHtml("header.html");
        printWriter.print("<div class=\"middlePage\" style=\"margin-top: 100px;\">\n" +
                "    <div class=\"page-header\">\n" +
                "        <h1 class=\"logo\"> <small>Upload Food Item</small></h1>\n" +
                "    </div>\n" +
                "\n" +
                "    <div class=\"panel panel-info\">\n" +
                "        <div class=\"panel-heading\">\n" +
                "            <h3 class=\"panel-title\">Fill the fields</h3>\n" +
                "        </div>\n" +
                "        <div class=\"panel-body\">\n" +
                "\n" +
                "            <div class=\"row\">\n" +
                "\n" +
                "                <div class=\"col-md-3\" >\n" +
                "                    <h3 style=\"text-align: center;margin-top: 150px;color:#f63440;font-weight: 700\">FOODBASKET</h3>\n" +
                "                </div>\n" +
                "\n" +
                "                <div class=\"col-md-9\" style=\"border-left:1px solid #ccc;\">\n" +
                "                    <form class=\"form-horizontal\" action=\"FoodVendorServlet?updateDetails="+menuItems.getId()+"\" method=\"post\" >\n" +
                "                        <fieldset>\n" +
                "\n" +
                "\n" +
                "                            <div class=\"form-group\">\n" +
                "                            <label class=\"control-label col-sm-2\" for=\"name\">Name:</label>\n" +
                "                            <div class=\"col-sm-10\">\n" +
                "                            <input type=\"text\" class=\"form-control\" id=\"name\" name=\"name\" value=\""+menuItems.getName()+"\" required>\n" +
                "                            </div>\n" +
                "                            </div>\n" +
                "\n" +
                "                            <div class=\"form-group\">\n" +
                "                            <label class=\"control-label col-sm-2\" for=\"ingredients\">ingredients:</label>\n" +
                "                            <div class=\"col-sm-10\">\n" +
                "                            <input type=\"text\" class=\"form-control\" id=\"ingredients\" name=\"ingredients\" value=\""+menuItems.getIngredients()+"\" required>\n" +
                "                            </div>\n" +
                "                            </div>\n" +
                "\n" +
                "                            <div class=\"form-group\">\n" +
                "                            <label class=\"control-label col-sm-2 \" for=\"price\">price:</label>\n" +
                "                            <div class=\"col-sm-10\">\n" +
                "                            <input type=\"number\" class=\"form-control\" id=\"price\" name=\"price\" value=\""+menuItems.getPrice()+"\" required>\n" +
                "                            </div>\n" +
                "                            </div>\n" +
                "\n" +
                "\n" +
                "                            <div class=\"form-group\">\n" +
                "                            <label class=\"control-label col-sm-2 \" for=\"image\">Image:</label>\n" +
                "                            <div class=\"col-sm-10\">\n" +
                "                            <input type=\"text\" class=\"form-control\" id=\"image\" name=\"image\" value=\""+menuItems.getImage()+"\" required>\n" +
                "                            </div>\n" +
                "                            </div>\n" +
                "\n" +
                "                            <div class=\"form-group\">\n" +
                "                            <label class=\"control-label col-sm-2 \" for=\"inventory\">No of items available:</label>\n" +
                "                            <div class=\"col-sm-10\">\n" +
                "                            <input type=\"number\" class=\"form-control\" id=\"inventory\" name=\"inventory\" value=\""+menuItems.getAvailability()+"\" required>\n" +
                "                            </div>\n" +
                "                            </div>\n" +
                "\n" +
                "\n" +
                "\n" +
                "                            <div class=\"form-group\">\n" +
                "                            <div class=\"col-sm-offset-2 col-sm-10\">\n" +
                "                            <button type=\"submit\" class=\"btn btn-success pull-right\">Upload</button>\n" +
                "                            </div>\n" +
                "                            </div>\n" +
                "\n" +
                "                        </fieldset>\n" +
                "                    </form>\n" +
                "                </div>\n" +
                "\n" +
                "            </div>\n" +
                "\n" +
                "        </div>\n" +
                "    </div>\n" +
                "\n" +
                "\n" +
                "</div>");
        //utility.printHtml("footer.html");
    }

    private void displayMenuItemsListForEdit(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        HttpSession session = request.getSession(true);
        PrintWriter printWriter = response.getWriter();
        String restaurant = MySQLDataStoreUtilities.getVendor(session.getAttribute("username").toString());
        List<MenuItems> menuItems = MySQLDataStoreUtilities.getRestaurantMenu(restaurant);
        session.setAttribute("vendorRestaurant", restaurant);
        Utilities utility = new Utilities(printWriter,request,response);
        utility.printHtml("header.html");
        StringBuilder htmlContentBuilder = new StringBuilder();
        htmlContentBuilder.append("<div class=\"panel panel-primary\" id=\"formcontainer\" style=\"width:500px;margin-top:30px;margin-left:30%\">\n" +
                "    <div class=\"panel-heading\">Update Menu Item</div>\n" +
                "    <div class=\"panel-body\">\n" +
                "        <form method=\"post\" action=\"FoodVendorServlet?editMenuItem=true\">\n" +
                "            <div class=\"form-group\">\n" +
                "               <select name=\"editMenuItemId\" id=\"itemId\" class=\"form-control\">" );

        for (int i=0; i <menuItems.size(); i++){
            htmlContentBuilder.append("<option value=\""+menuItems.get(i).getId()+"\">"+menuItems.get(i).getName()+"</option> ");
        }
        htmlContentBuilder.append("                </select>\n" +
                "            </div>\n" +
                "            <button type=\"submit\" class=\"btn btn-success pull-right\" >Submit</button>\n" +
                "\n" +
                "        </form>\n" +
                "    </div>\n" +
                "</div>");
        printWriter.print(htmlContentBuilder.toString());
        utility.printHtml("footer.html");
    }

}
