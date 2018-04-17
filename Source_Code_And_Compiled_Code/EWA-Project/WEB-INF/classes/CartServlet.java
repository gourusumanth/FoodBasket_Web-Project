import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class CartServlet extends HttpServlet  {

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        process(request,response);
    }


    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        process(request,response);
    }


    private void process(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        HttpSession session = request.getSession(true);
        if(request.getParameter("removeitem")!=null){
            LinkedHashMap<String,Integer> cartItems = (LinkedHashMap<String,Integer>) session.getAttribute("cartItems");
            cartItems.remove(request.getParameter("removeitem"));
        }
        response.setContentType("text/html");
        PrintWriter printWriter = response.getWriter();
        Utilities utility = new Utilities(printWriter,request,response);
        utility.printHtml("header.html");
        if( session.getAttribute("cartItems")==null){
            printWriter.print("<h3 style=\"\">    No Items In The Cart.\n</h3>\n");
            utility.printHtml("footer.html");
            return;
        }
        LinkedHashMap<String,Integer> cartItems = (LinkedHashMap<String,Integer>) session.getAttribute("cartItems");
        if(cartItems.size()==0 || session.getAttribute("restaurantId")==null){
            printWriter.print("<h3 style=\"\">    No Items In The Cart.\n</h3>\n");
            utility.printHtml("footer.html");
            return;
        }
        if(request.getParameter("addToCartSimilar")!=null){
           RestaurantMenuServlet.addToCart(request.getParameter("addToCartSimilar"),request,response);
        }
        createCart(printWriter,session,response);
        createCarousel(printWriter,session, request,response);
        utility.printHtml("footer.html");
    }

    private void createCarousel(PrintWriter printWriter, HttpSession session, HttpServletRequest request, HttpServletResponse response) {
        StringBuilder s = new StringBuilder();

        s.append("<div class=\"container\">");
        s.append("<fieldset> <legend>You May Also Like</legend>");
        s.append("<div id=\"carousel-example-generic\" class=\"carousel slide\" data-ride=\"carousel\">\n"

                +"<ol class=\"carousel-indicators\">\n"
                +"<li data-target=\"#carousel-example-generic\" data-slide-to=\"0\" class=\"active\"></li>\n"
                +"<li data-target=\"#carousel-example-generic\" data-slide-to=\"1\"></li>\n"
                +"<li data-target=\"#carousel-example-generic\" data-slide-to=\"2\"></li>\n"
                +"</ol>\n"
                +"<div class=\"carousel-inner\">\n");
        int cnt = 0;

        // ArrayList<Integer> acc1 = refmap.get(item_id).getAccessory();
        //  Iterator<Integer> itr1=acc1.iterator();
        LinkedHashMap<String,Integer> cartItems = (LinkedHashMap<String,Integer>) session.getAttribute("cartItems");
        String lastAddedMenuItemId = "";
        for(Map.Entry<String,Integer> entry: cartItems.entrySet()){
            lastAddedMenuItemId = entry.getKey();
        }
        MenuItems lastAddedMenuItem = MySQLDataStoreUtilities.getMenuItemById(lastAddedMenuItemId);

        for(int i=0;i<lastAddedMenuItem.getSimilarMenuItems().size();i++){
            String similarMenuItemId = lastAddedMenuItem.getSimilarMenuItems().get(i);
            MenuItems similarMenuItem = MySQLDataStoreUtilities.getMenuItemById(similarMenuItemId);

            if(cnt==0 ){
                s.append("<div class=\"row item active\">\n");
            }
            if(cnt==3){
                s.append("<div class=\"row item\">\n");
            }
            s.append("<div class=\" col-md-4 col-sm-4 col-xs-4 col-lg-4\">\n"
                    +"<div class=\"thumbnail bg-primary\">\n"
                    +"<img src="+similarMenuItem.getImage()+" alt=\"First slide\">\n"
                    +"<!-- Static Header -->\n"
                    +"<div style=\"text-align: center\">\n"
                    +"<b class=\"text-primary\" style=\"font-size: 22px;margin-left: 25px\">"+similarMenuItem.getName()+"</b><br>\n"
                    +"<ul>\n"
                    +"<li><a type=\"button\" class=\"btn btn-default disabled\" style=\"margin-bottom:5px;\" href=\"#\">$"+ similarMenuItem.getPrice()+"</a><br></li>\n"
                    +" <li><a type=\"button\" class=\"btn btn-primary\" style=\"margin-left: 5px\" href=\"CartServlet?addToCartSimilar="+similarMenuItem.getId()+"&restaurantId="+session.getAttribute("restaurantId")+"\">Buy Now</a></li>\n"
                    +"</ul>\n"
                    +"</div>\n"

                    +"</div>\n"
                    +"</div>\n");
            cnt++;

            if(cnt%3==0){
                s.append("</div>\n");
            }
        }

        s.append("</div>\n");

        s.append("<a class=\"left carousel-control\" href=\"#carousel-example-generic\" data-slide=\"prev\">\n"
                +"<span class=\"glyphicon glyphicon-chevron-left\"></span>\n"
                +"</a>\n"
                +"<a class=\"right carousel-control\" href=\"#carousel-example-generic\" data-slide=\"next\">\n"
                +"<span class=\"glyphicon glyphicon-chevron-right\"></span>\n"
                +"</a>\n");
        s.append("</div>\n");
        s.append("</fieldset>\n");
        s.append("</div>");
        printWriter.print(s.toString());

    }

    /*private void createCarousel(PrintWriter printWriter, HttpSession session, HttpServletResponse response) {
        StringBuilder htmlContentBuilder = new StringBuilder();
        LinkedHashMap<String,Integer> cartItems = (LinkedHashMap<String,Integer>) session.getAttribute("cartItems");
        String lastAddedMenuItemId = "";
        for(Map.Entry<String,Integer> entry: cartItems.entrySet()){
            lastAddedMenuItemId = entry.getKey();
        }
        MenuItems lastAddedMenuItem = MySQLDataStoreUtilities.getMenuItemById(lastAddedMenuItemId);
        htmlContentBuilder.append("<div class=\"container\"><fieldset> <legend>Similar Products</legend><div id=\"carousel-example-generic\" class=\"carousel slide\" data-ride=\"carousel\"><ol class=\"carousel-indicators\"><li data-target=\"#carousel-example-generic\" data-slide-to=\"0\" class=\"active\"></li><li data-target=\"#carousel-example-generic\" data-slide-to=\"1\"></li><li data-target=\"#carousel-example-generic\" data-slide-to=\"2\"></li></ol><div class=\"carousel-inner\">\n");
        for( int i = 0 ; i <lastAddedMenuItem.getSimilarMenuItems().size(); i++){
            String similarMenuItemId = lastAddedMenuItem.getSimilarMenuItems().get(i);
            MenuItems similarMenuItem = MySQLDataStoreUtilities.getMenuItemById(similarMenuItemId);
            if(i==0){
                htmlContentBuilder.append("<div class=\"row item active\"><div class=\"col-md-4 col-sm-4 col-xs-4 col-lg-4\" class=\"imglayout\"><div class=\"thumbnail bg-primary\" ><img src=\""+similarMenuItem.getImage()+"\" alt=\"Image\"><div style=\"text-align: center\" ><b class=\"text-primary\" style=\"font-size: 22px;margin-left: 25px\">"+similarMenuItem.getName()+"</b><br>\n" +
                        "<ul><li><a type=\"button\" class=\"btn btn-default disabled\" style=\"margin-bottom:5px;\"  href=\"#\">$"+similarMenuItem.getPrice()+"</a><br></li><li><a type=\"button\" class=\"btn btn-primary\" href=\"#\">Buy</a></li><li><a type=\"button\" class=\"btn btn-primary\"  href=\"#\">Reviews</a></li><li><a type=\"button\" class=\"btn btn-primary\" href=\"#\">Write Review</a></li></ul></div></div></div>\n");
            }else{
                htmlContentBuilder.append("<div class=\"col-md-4 col-sm-4 col-xs-4 col-lg-4\" class=\"imglayout\"><div class=\"thumbnail bg-primary\" ><img src=\""+similarMenuItem.getImage()+"\" alt=\"Image\"><div style=\"text-align: center\" ><b class=\"text-primary\" style=\"font-size: 22px;margin-left: 25px\">"+similarMenuItem.getName()+"</b><br><ul><li><a type=\"button\" class=\"btn btn-default disabled\" style=\"margin-bottom:5px;\"  href=\"#\">$"+similarMenuItem.getPrice()+"</a><br></li>\n" +
                        "<li><a type=\"button\" class=\"btn btn-primary\" href=\"#\">Buy</a></li><li><a type=\"button\" class=\"btn btn-primary\"  href=\"#\">Reviews</a></li><li><a type=\"button\" class=\"btn btn-primary\" href=\"#\">Write Review</a></li></ul></div></div></div>\n");
            }
        }
        htmlContentBuilder.append("</div><a class=\"left carousel-control\" href=\"#\" data-slide=\"prev\"><span class=\"glyphicon glyphicon-chevron-left\"></span></a><a class=\"right carousel-control\" href=\"#\" data-slide=\"next\"><span class=\"glyphicon glyphicon-chevron-right\"></span></a></div></div></fieldset></div>");
        printWriter.print(htmlContentBuilder.toString());
    }*/

    private void createCart(PrintWriter printWriter, HttpSession session, HttpServletResponse response) {
        StringBuilder htmlContentBuilder = new StringBuilder();
        LinkedHashMap<String,Integer> menuItemIdList = (LinkedHashMap<String,Integer>) session.getAttribute("cartItems");
        double total=0.0;
        htmlContentBuilder.append("<div id=\"content\"><table id=\"cart\" class=\"table table-hover table-condensed\"><thead><tr><th style=\"width:50%\">Product</th><th style=\"width:10%\">Price</th><th style=\"width:8%\">Quantity</th><th style=\"width:22%\" class=\"text-center\">Subtotal</th><th style=\"width:10%\"></th></tr></thead><tbody>\n");
        for(Map.Entry<String,Integer> entry : menuItemIdList.entrySet()){
            String menuitemId = entry.getKey();
            MenuItems menuItem = MySQLDataStoreUtilities.getMenuItemById(menuitemId);
            htmlContentBuilder.append("<tr><td data-th=\"Product\"><div class=\"row\"><div class=\"col-sm-2 hidden-xs\"><img src=\""+menuItem.getImage()+"\" alt=\"...\" class=\"img-responsive\" height=\"100px\" width=\"100px\" style=\"margin-top:15px \"/></div><div class=\"col-sm-10\"><h4 class=\"nomargin\">"+menuItem.getName()+"</h4>\n" +
                    "<p>"+menuItem.getIngredients()+"</p></div></div></td><td data-th=\"Price\">$"+menuItem.getPrice()+"</td><td data-th=\"Quantity\"><input type=\"number\" class=\"form-control text-center\" value=\""+entry.getValue()+"\"></td><td data-th=\"Subtotal\" class=\"text-center\">$"+menuItem.getPrice()*entry.getValue()+"</td><td class=\"actions\" data-th=\"\">" +
                    "<button class=\"btn btn-info btn-sm glyphicon glyphicon-refresh\"></button><button onclick=\"location.href='/EWA-Project/CartServlet?removeitem="+menuItem.getId()+"';\" class=\"btn btn-danger btn-sm glyphicon glyphicon-trash\"></button></td></tr>\n");
            total+=(menuItem.getPrice()*entry.getValue());

        }
        LinkedHashMap<String,ArrayList<Order>> orders = MySQLDataStoreUtilities.getOrders((String) session.getAttribute("username"));
        boolean discount = false;
        if(orders.size()%5==0){
            total = total*0.8;
            discount = true;
        }
        if(discount){
            htmlContentBuilder.append("</tbody><tfoot><tr class=\"visible-xs\"><td class=\"text-center\"><strong>Total $"+String.format("%.2f",total)+"</strong></td></tr><tr><td><a href=\"MainPageServlet\" class=\"btn btn-warning\"><i class=\"fa fa-angle-left\"></i> Continue Shopping</a></td>\n" +
                    " <td colspan=\"2\" class=\"hidden-xs\"></td><td class=\"hidden-xs text-center\"><strong>Total After 20% Discount $"+String.format("%.2f",total)+"</strong></td><td><a href=\"CheckoutServlet\" class=\"btn btn-success btn-block\">Checkout <i class=\"fa fa-angle-right\"></i></a></td></tr></tfoot></table></div>\n");

        }else{
            htmlContentBuilder.append("</tbody><tfoot><tr class=\"visible-xs\"><td class=\"text-center\"><strong>Total $"+String.format("%.2f",total)+"</strong></td></tr><tr><td><a href=\"MainPageServlet\" class=\"btn btn-warning\"><i class=\"fa fa-angle-left\"></i> Continue Shopping</a></td>\n" +
                    " <td colspan=\"2\" class=\"hidden-xs\"></td><td class=\"hidden-xs text-center\"><strong>Total $"+String.format("%.2f",total)+"</strong></td><td><a href=\"CheckoutServlet\" class=\"btn btn-success btn-block\">Checkout <i class=\"fa fa-angle-right\"></i></a></td></tr></tfoot></table></div>\n");

        }
         printWriter.print(htmlContentBuilder.toString());
    }

}
