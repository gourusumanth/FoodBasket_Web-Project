import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

public class FastCheckoutServlet extends HttpServlet{

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        process(request,response);
    }


    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        process(request,response);
    }

    private void process(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        response.setContentType("text/html");
        HttpSession session = request.getSession(true);
        PrintWriter printWriter = response.getWriter();
        Utilities utility = new Utilities(printWriter, request, response);

        if( session.getAttribute("username")==null){
            utility.printHtml("header.html");
            printWriter.print("<h3 style=\"\">    Sign In to perform FastCheckOut.\n</h3>\n");
            utility.printHtml("footer.html");
            return;
        }
        if(request.getParameter("cancel")!=null){
            response.sendRedirect("CartServlet");
        }else if(request.getParameter("checkOut")!=null){
            utility.printHtml("header.html");
            viewOrders(printWriter,session,response);
            utility.printHtml("footer.html");
        }else if(request.getParameter("orderid")!=null){
            saveOrder(request,response);
        }else if(request.getParameter("addressdetails")!=null){
            saveAddressDetails(request,response);
        }
    }
    private void saveAddressDetails(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        HttpSession session = request.getSession(true);
        session.setAttribute("addressline1",request.getParameter("addressline1"));
        session.setAttribute("addressline2",request.getParameter("addressline2"));
        session.setAttribute("city",request.getParameter("city"));
        session.setAttribute("state",request.getParameter("state"));
        session.setAttribute("zipcode",request.getParameter("zipcode"));
        session.setAttribute("country",request.getParameter("country"));
        response.setContentType("text/html");
        PrintWriter printWriter = response.getWriter();
        Utilities utility = new Utilities(printWriter, request, response);
        //utility.printHtml("paymentdetails.html");
        createPaymentDetails(request,response);
    }

    private void createPaymentDetails(HttpServletRequest request, HttpServletResponse response) throws IOException {
        PrintWriter printWriter = response.getWriter();
        HttpSession session = request.getSession(true);
        double total = getTotal(session);
        LinkedHashMap<String,ArrayList<Order>> orders = MySQLDataStoreUtilities.getOrders((String) session.getAttribute("username"));
        if(orders.size()%5==0){
            total = total*0.8;
        }
        PaymentDetails paymentDetails = MySQLDataStoreUtilities.getPaymentDetails((String)session.getAttribute("username"));
        printWriter.print("<!DOCTYPE html>\n" +
                "<html lang=\"en\">\n" +
                "<head>\n" +
                "    <meta charset=\"UTF-8\">\n" +
                "    <title>Payment Details</title>\n" +
                "    <link rel=\"stylesheet\" href=\"https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css\">\n" +
                "    <script src=\"https://ajax.googleapis.com/ajax/libs/jquery/3.2.1/jquery.min.js\"></script>\n" +
                "    <script src=\"https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js\"></script>\n" +
                "    <link rel=\"stylesheet\" href=\"css/paymentdetailscss.css\">\n" +
                "    <link rel=\"stylesheet\" href=\"css/headercss.css\">\n" +
                "    <script>\n" +
                "        $(window).on('load',function(){\n" +
                "            $('#myModal').modal('show');\n" +
                "        });\n" +
                "    </script>\n" +
                "</head>\n" +
                "<body>\n" +
                "\n" +
                "\n" +
                "<div id=\"foodbasket-navbar\">\n" +
                "    <div class=\"container\">\n" +
                "        <div class=\"row row1\">\n" +
                "            <ul class=\"largenav pull-right\">\n" +
                "                <li class=\"upper-links\"><a class=\"links\" href=\"http://clashhacks.in/\">AboutUs</a></li>\n" +
                "                <li class=\"upper-links\"><a class=\"links\" href=\"http://clashhacks.in/\">Sell on Foodbasket</a></li>\n" +
                "                <li class=\"upper-links\"><a class=\"links\" href=\"https://campusbox.org/\">Contact US</a></li>\n" +
                "                <li class=\"upper-links\"><a class=\"links\" href=\"http://clashhacks.in/\">View Orders</a></li>\n" +
                "                <li class=\"upper-links\">\n" +
                "                    <a class=\"links\" href=\"http://clashhacks.in/\">\n" +
                "                        Track Order\n" +
                "                    </a>\n" +
                "                </li>\n" +
                "                <li class=\"upper-links\"><a class=\"links\" href=\"http://clashhacks.in/\">My Account</a></li>\n" +
                "\n" +
                "                <li class=\"upper-links\"><a class=\"links\" href=\"http://clashhacks.in/\">Logout</a></li>\n" +
                "                <li class=\"upper-links dropdown\"><a class=\"links\" href=\"http://clashhacks.in/\">Dropdown</a>\n" +
                "                    <ul class=\"dropdown-menu\">\n" +
                "                        <li class=\"profile-li\"><a class=\"profile-links\" href=\"http://yazilife.com/\">Link</a></li>\n" +
                "                        <li class=\"profile-li\"><a class=\"profile-links\" href=\"http://hacksociety.tech/\">Link</a></li>\n" +
                "                        <li class=\"profile-li\"><a class=\"profile-links\" href=\"http://clashhacks.in/\">Link</a></li>\n" +
                "                        <li class=\"profile-li\"><a class=\"profile-links\" href=\"http://clashhacks.in/\">Link</a></li>\n" +
                "                        <li class=\"profile-li\"><a class=\"profile-links\" href=\"http://clashhacks.in/\">Link</a></li>\n" +
                "                        <li class=\"profile-li\"><a class=\"profile-links\" href=\"http://clashhacks.in/\">Link</a></li>\n" +
                "                        <li class=\"profile-li\"><a class=\"profile-links\" href=\"http://clashhacks.in/\">Link</a></li>\n" +
                "                    </ul>\n" +
                "                </li>\n" +
                "            </ul>\n" +
                "        </div>\n" +
                "        <div class=\"row row2\">\n" +
                "            <div class=\"col-sm-3\">\n" +
                "                <h2 style=\"margin:0px;\"><span class=\"smallnav menu\" onclick=\"openNav()\">☰ Brand</span></h2>\n" +
                "                <h1 style=\"margin:0px;\"><span class=\"largenav\">FOODBASKET</span></h1>\n" +
                "            </div>\n" +
                "            <div class=\"foodbasket-navbar-search smallsearch col-sm-7 col-xs-10\">\n" +
                "                <div class=\"row\">\n" +
                "                    <input class=\"foodbasket-navbar-input col-xs-11\"  id=\"search\" name=\"search\" type=\"\" placeholder=\"Search \" name=\"\">\n" +
                "                    <button class=\"foodbasket-navbar-button col-xs-1\">\n" +
                "                        <svg width=\"15px\" height=\"15px\">\n" +
                "\n" +
                "                            <path d=\"M11.618 9.897l4.224 4.212c.092.09.1.23.02.312l-1.464 1.46c-.08.08-.222.072-.314-.02L9.868 11.66M6.486 10.9c-2.42 0-4.38-1.955-4.38-4.367 0-2.413 1.96-4.37 4.38-4.37s4.38 1.957 4.38 4.37c0 2.412-1.96 4.368-4.38 4.368m0-10.834C2.904.066 0 2.96 0 6.533 0 10.105 2.904 13 6.486 13s6.487-2.895 6.487-6.467c0-3.572-2.905-6.467-6.487-6.467 \"></path>\n" +
                "                        </svg>\n" +
                "                    </button>\n" +
                "                </div>\n" +
                "                <ul id=\"search-suggestions\">\n" +
                "\n" +
                "                </ul>\n" +
                "            </div>\n" +
                "            <div class=\"cart largenav col-sm-2\">\n" +
                "                <a class=\"cart-button\">\n" +
                "                    <svg class=\"cart-svg \" width=\"16 \" height=\"16 \" viewBox=\"0 0 16 16 \">\n" +
                "                        <path d=\"M15.32 2.405H4.887C3 2.405 2.46.805 2.46.805L2.257.21C2.208.085 2.083 0 1.946 0H.336C.1 0-.064.24.024.46l.644 1.945L3.11 9.767c.047.137.175.23.32.23h8.418l-.493 1.958H3.768l.002.003c-.017 0-.033-.003-.05-.003-1.06 0-1.92.86-1.92 1.92s.86 1.92 1.92 1.92c.99 0 1.805-.75 1.91-1.712l5.55.076c.12.922.91 1.636 1.867 1.636 1.04 0 1.885-.844 1.885-1.885 0-.866-.584-1.593-1.38-1.814l2.423-8.832c.12-.433-.206-.86-.655-.86 \" fill=\"#fff \"></path>\n" +
                "                    </svg> Cart\n" +
                "                    <span class=\"item-number \">0</span>\n" +
                "                </a>\n" +
                "            </div>\n" +
                "        </div>\n" +
                "    </div>\n" +
                "</div>\n" +
                "<div id=\"mySidenav\" class=\"sidenav\">\n" +
                "    <div class=\"container\" style=\"background-color: #2874f0; padding-top: 10px;\">\n" +
                "        <span class=\"sidenav-heading\">Home</span>\n" +
                "        <a href=\"javascript:void(0)\" class=\"closebtn\" onclick=\"closeNav()\">×</a>\n" +
                "    </div>\n" +
                "    <a href=\"http://clashhacks.in/\">Link</a>\n" +
                "    <a href=\"http://clashhacks.in/\">Link</a>\n" +
                "    <a href=\"http://clashhacks.in/\">Link</a>\n" +
                "    <a href=\"http://clashhacks.in/\">Link</a>\n" +
                "</div>\n" +
                "\n" +
                "\n" +
                "<!-------------------------------------------------------------Payment Details Logic-------------------------------------------------------------------------------------------->\n" +
                "\n" +
                "\n" +
                "    <div class=\"modal fade\" id=\"myModal\" role=\"dialog\">\n" +
                "        <div class=\"modal-dialog\">\n" +
                "\n" +
                "            <!-- Modal content-->\n" +
                "            <div class=\"modal-content\">\n" +
                "                <div class=\"modal-header\">\n" +
                "                    <a href=\"FastCheckoutServlet?cancel=true\" type=\"btn\" class=\"close\">&times;</a>\n" +
                "                    <h4 class=\"modal-title \" style=\"color:#f63440;font-weight: 700\">Payment Details</h4>\n" +
                "                </div>\n" +
                "                <div class=\"modal-body\">\n" +
                "                    <div class=\"row\" style=\"margin-left: 10px;\">\n" +
                "                        <div class=\"col-xs-12 col-md-8\">\n" +
                "                            <form role=\"form\" action=\"CheckoutServlet?paymentdetails=true\" method=\"post\">\n" +
                "\n" +
                "                            <div class=\"panel panel-default\">\n" +
                "                                <div class=\"panel-heading\">\n" +
                "                                    <h3 class=\"panel-title\">\n" +
                "                                        Payment Details\n" +
                "                                    </h3>\n" +
                "                                    <div class=\"checkbox pull-right\">\n" +
                "                                        <label>\n" +
                "                                            <input type=\"checkbox\" name=\"rememberme\" id=\"rememberme\"/>\n" +
                "                                            Remember\n" +
                "                                        </label>\n" +
                "                                    </div>\n" +
                "                                </div>\n" +
                "                                <div class=\"panel-body\">\n" +
                "                                        <div class=\"form-group\">\n" +
                "                                            <label for=\"cardNumber\">\n" +
                "                                                CARD NUMBER</label>\n" +
                "                                            <div class=\"input-group\">\n" +
                "                                                <input type=\"number\" value=\""+paymentDetails.getCrediCardNumber()+"\" class=\"form-control\" id=\"cardNumber\" name=\"cardNumber\" placeholder=\"Valid Card Number\"\n" +
                "                                                       required autofocus />\n" +
                "                                                <span class=\"input-group-addon\"><span class=\"glyphicon glyphicon-lock\"></span></span>\n" +
                "                                            </div>\n" +
                "                                        </div>\n" +
                "                                        <div class=\"row\">\n" +
                "                                            <div class=\"col-xs-7 col-md-7\">\n" +
                "                                                <div class=\"form-group\">\n" +
                "                                                    <label for=\"expityMonth\">\n" +
                "                                                        EXPIRY DATE</label>\n" +
                "                                                    <div class=\"col-xs-6 col-lg-6 pl-ziro\">\n" +
                "                                                        <input type=\"number\" value=\"" + paymentDetails.getExpiryMonth() + "\" class=\"form-control\" id=\"expityMonth\" name=\"expiryMonth\" placeholder=\"MM\" required />\n" +
                "                                                    </div>\n" +
                "                                                    <div class=\"col-xs-6 col-lg-6 pl-ziro\">\n" +
                "                                                        <input type=\"number\" value=\"" + paymentDetails.getExpiryYear() + "\" class=\"form-control\" id=\"expityYear\" name=\"expiryYear\" placeholder=\"YY\" required /></div>\n" +
                "                                                </div>\n" +
                "                                            </div>\n" +
                "                                            <div class=\"col-xs-5 col-md-5 pull-right\">\n" +
                "                                                <div class=\"form-group\">\n" +
                "                                                    <label for=\"cvCode\">\n" +
                "                                                        CV CODE</label>\n" +
                "                                                    <input type=\"password\" class=\"form-control\" id=\"cvCode\" name=\"cvCode\" placeholder=\"CV\" required />\n" +
                "                                                </div>\n" +
                "                                            </div>\n" +
                "                                        </div>\n" +
                "                                        <ul class=\"nav nav-pills nav-stacked\">\n" +
                "                                            <li class=\"active\"><a href=\"#\"><span class=\"badge pull-right\"><span class=\"glyphicon glyphicon-usd\"></span>"+String.format("%.2f",total)+"</span> Final Payment</a>\n" +
                "                                            </li>\n" +
                "                                        </ul>\n" +
                "                                        <br/>\n" +
                "                                        <input type=\"submit\" class=\"btn btn-success btn-lg btn-block\" role=\"button\" value=\"pay\"></a>\n" +
                "                                    </form>\n" +
                "                                </div>\n" +
                "                            </div>\n" +
                "\n" +
                "                        </div>\n" +
                "                    </div>\n" +
                "                </div>\n" +
                "\n" +
                "            </div>\n" +
                "\n" +
                "        </div>\n" +
                "    </div>\n" +
                "\n" +
                "\n" +
                "</body>\n" +
                "</html>\n");
    }

    private double getTotal( HttpSession session) {
        double total = 0;
        LinkedHashMap<String,Integer> cartItems = (LinkedHashMap<String,Integer>) session.getAttribute("cartItems");
        for(Map.Entry<String,Integer> entry : cartItems.entrySet()){
            MenuItems menuItems = MySQLDataStoreUtilities.getMenuItemById(entry.getKey());
            total+=entry.getValue()*menuItems.getPrice();
        }
        return total;
    }

    private void saveOrder(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        PrintWriter printWriter = response.getWriter();
        HttpSession session = request.getSession(true);
        Utilities utility = new Utilities(printWriter, request, response);
        List<Order> orderList = MySQLDataStoreUtilities.getOrdersById(request.getParameter("orderid"));
        session.setAttribute("cartItems",new LinkedHashMap<String,Integer>());
        LinkedHashMap<String,Integer> cartItems = (LinkedHashMap<String,Integer>) session.getAttribute("cartItems");
        for(int i=0;i<orderList.size();i++){
            if(cartItems.containsKey(orderList.get(i).getItemId())){
                Integer value = cartItems.get(orderList.get(i).getItemId());
                cartItems.put(orderList.get(i).getItemId(),value+1);
            }else{
                cartItems.put(orderList.get(i).getItemId(),1);
            }
        }
        session.setAttribute("cartItems",cartItems);
        session.setAttribute("restaurantId",orderList.get(0).getRestaurantId());
        prepopulateAddress(printWriter,request,response,orderList);
    }

    private void prepopulateAddress(PrintWriter printWriter, HttpServletRequest request, HttpServletResponse response, List<Order> orderList) {
        Order order = orderList.get(0);
        printWriter.print("<!DOCTYPE html>\n" +
                "<html lang=\"en\">\n" +
                "<head>\n" +
                "    <title>Bootstrap Example</title>\n" +
                "    <meta charset=\"utf-8\">\n" +
                "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1\">\n" +
                "    <script src=\"https://ajax.googleapis.com/ajax/libs/jquery/3.2.1/jquery.min.js\"></script>\n" +
                "    <link rel=\"stylesheet\" href=\"https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css\">\n" +
                "\n" +
                "    <script src=\"https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js\"></script>\n" +
                "    <link rel=\"stylesheet\" href=\"css/headercss.css\">\n" +
                "\n" +
                "    <script>\n" +
                "\n" +
                "        $(window).on('load',function(){\n" +
                "            $('#myModal').modal('show');\n" +
                "        });\n" +
                "    </script>\n" +
                "</head>\n" +
                "<body>\n" +
                "\n" +
                "    <div id=\"foodbasket-navbar\">\n" +
                "        <div class=\"container\">\n" +
                "            <div class=\"row row1\">\n" +
                "                <ul class=\"largenav pull-right\">\n" +
                "                    <li class=\"upper-links\"><a class=\"links\" href=\"http://clashhacks.in/\">AboutUs</a></li>\n" +
                "                    <li class=\"upper-links\"><a class=\"links\" href=\"http://clashhacks.in/\">Sell on Foodbasket</a></li>\n" +
                "                    <li class=\"upper-links\"><a class=\"links\" href=\"https://campusbox.org/\">Contact US</a></li>\n" +
                "                    <li class=\"upper-links\"><a class=\"links\" href=\"http://clashhacks.in/\">View Orders</a></li>\n" +
                "                    <li class=\"upper-links\">\n" +
                "                        <a class=\"links\" href=\"http://clashhacks.in/\">\n" +
                "                            Track Order\n" +
                "                        </a>\n" +
                "                    </li>\n" +
                "                    <li class=\"upper-links\"><a class=\"links\" href=\"http://clashhacks.in/\">My Account</a></li>\n" +
                "\n" +
                "                    <li class=\"upper-links\"><a class=\"links\" href=\"http://clashhacks.in/\">Logout</a></li>\n" +
                "                    <li class=\"upper-links dropdown\"><a class=\"links\" href=\"http://clashhacks.in/\">Dropdown</a>\n" +
                "                        <ul class=\"dropdown-menu\">\n" +
                "                            <li class=\"profile-li\"><a class=\"profile-links\" href=\"http://yazilife.com/\">Link</a></li>\n" +
                "                            <li class=\"profile-li\"><a class=\"profile-links\" href=\"http://hacksociety.tech/\">Link</a></li>\n" +
                "                            <li class=\"profile-li\"><a class=\"profile-links\" href=\"http://clashhacks.in/\">Link</a></li>\n" +
                "                            <li class=\"profile-li\"><a class=\"profile-links\" href=\"http://clashhacks.in/\">Link</a></li>\n" +
                "                            <li class=\"profile-li\"><a class=\"profile-links\" href=\"http://clashhacks.in/\">Link</a></li>\n" +
                "                            <li class=\"profile-li\"><a class=\"profile-links\" href=\"http://clashhacks.in/\">Link</a></li>\n" +
                "                            <li class=\"profile-li\"><a class=\"profile-links\" href=\"http://clashhacks.in/\">Link</a></li>\n" +
                "                        </ul>\n" +
                "                    </li>\n" +
                "                </ul>\n" +
                "            </div>\n" +
                "            <div class=\"row row2\">\n" +
                "                <div class=\"col-sm-3\">\n" +
                "                    <h2 style=\"margin:0px;\"><span class=\"smallnav menu\" onclick=\"openNav()\">☰ Brand</span></h2>\n" +
                "                    <h1 style=\"margin:0px;\"><span class=\"largenav\">FOODBASKET</span></h1>\n" +
                "                </div>\n" +
                "                <div class=\"foodbasket-navbar-search smallsearch col-sm-7 col-xs-10\">\n" +
                "                    <div class=\"row\">\n" +
                "                        <input class=\"foodbasket-navbar-input col-xs-11\"  id=\"search\" name=\"search\" type=\"\" placeholder=\"Search \" name=\"\">\n" +
                "                        <button class=\"foodbasket-navbar-button col-xs-1\">\n" +
                "                            <svg width=\"15px\" height=\"15px\">\n" +
                "\n" +
                "                                <path d=\"M11.618 9.897l4.224 4.212c.092.09.1.23.02.312l-1.464 1.46c-.08.08-.222.072-.314-.02L9.868 11.66M6.486 10.9c-2.42 0-4.38-1.955-4.38-4.367 0-2.413 1.96-4.37 4.38-4.37s4.38 1.957 4.38 4.37c0 2.412-1.96 4.368-4.38 4.368m0-10.834C2.904.066 0 2.96 0 6.533 0 10.105 2.904 13 6.486 13s6.487-2.895 6.487-6.467c0-3.572-2.905-6.467-6.487-6.467 \"></path>\n" +
                "                            </svg>\n" +
                "                        </button>\n" +
                "                    </div>\n" +
                "                    <ul id=\"search-suggestions\">\n" +
                "\n" +
                "                    </ul>\n" +
                "                </div>\n" +
                "                <div class=\"cart largenav col-sm-2\">\n" +
                "                    <a class=\"cart-button\">\n" +
                "                        <svg class=\"cart-svg \" width=\"16 \" height=\"16 \" viewBox=\"0 0 16 16 \">\n" +
                "                            <path d=\"M15.32 2.405H4.887C3 2.405 2.46.805 2.46.805L2.257.21C2.208.085 2.083 0 1.946 0H.336C.1 0-.064.24.024.46l.644 1.945L3.11 9.767c.047.137.175.23.32.23h8.418l-.493 1.958H3.768l.002.003c-.017 0-.033-.003-.05-.003-1.06 0-1.92.86-1.92 1.92s.86 1.92 1.92 1.92c.99 0 1.805-.75 1.91-1.712l5.55.076c.12.922.91 1.636 1.867 1.636 1.04 0 1.885-.844 1.885-1.885 0-.866-.584-1.593-1.38-1.814l2.423-8.832c.12-.433-.206-.86-.655-.86 \" fill=\"#fff \"></path>\n" +
                "                        </svg> Cart\n" +
                "                        <span class=\"item-number \">0</span>\n" +
                "                    </a>\n" +
                "                </div>\n" +
                "            </div>\n" +
                "        </div>\n" +
                "    </div>\n" +
                "    <div id=\"mySidenav\" class=\"sidenav\">\n" +
                "        <div class=\"container\" style=\"background-color: #2874f0; padding-top: 10px;\">\n" +
                "            <span class=\"sidenav-heading\">Home</span>\n" +
                "            <a href=\"javascript:void(0)\" class=\"closebtn\" onclick=\"closeNav()\">×</a>\n" +
                "        </div>\n" +
                "        <a href=\"http://clashhacks.in/\">Link</a>\n" +
                "        <a href=\"http://clashhacks.in/\">Link</a>\n" +
                "        <a href=\"http://clashhacks.in/\">Link</a>\n" +
                "        <a href=\"http://clashhacks.in/\">Link</a>\n" +
                "    </div>\n" +
                "  <!-----------------------------------------------Address Details Logic---------------------------------------------------------------------------------------------------------->\n" +
                "    <div class=\"modal fade\" id=\"myModal\" role=\"dialog\">\n" +
                "        <div class=\"modal-dialog\">\n" +
                "\n" +
                "            <!-- Modal content-->\n" +
                "            <div class=\"modal-content\">\n" +
                "                <div class=\"modal-header\">\n" +
                "                    <!--<button type=\"button\" class=\"close\" data-dismiss=\"modal\">&times;</button>-->\n" +
                "                    <a href=\"FastCheckoutServlet?cancel=true\" type=\"btn\" class=\"close\">&times;</a>\n" +
                "                    <h4 class=\"modal-title \" style=\"color:#f63440;font-weight: 700\">Address Details</h4>\n" +
                "                </div>\n" +
                "                <div class=\"modal-body\">\n" +
                "\n" +
                "\n" +
                "                            <form class=\"form-horizontal\" id=\"addressdetailsform\" method=\"post\" action=\"FastCheckoutServlet?addressdetails=true\" role=\"form\" style=\"magin-left:10px; padding: 10px;width: 500px\">\n" +
                "\n" +
                "                                    <!-- Text input-->\n" +
                "                                    <div class=\"form-group\">\n" +
                "                                        <label class=\"col-sm-2 control-label\" for=\"addressline1\">Line 1</label>\n" +
                "                                        <div class=\"col-sm-10\">\n" +
                "                                            <input type=\"text\" placeholder=\"Address Line 1\" class=\"form-control\" name=\"addressline1\" id=\"addressline1\" required value=\""+order.getAddressLine1()+"\">\n" +
                "                                        </div>\n" +
                "                                    </div>\n" +
                "\n" +
                "                                    <!-- Text input-->\n" +
                "                                    <div class=\"form-group\">\n" +
                "                                        <label class=\"col-sm-2 control-label\" for=\"addressline2\">Line 2</label>\n" +
                "                                        <div class=\"col-sm-10\">\n" +
                "                                            <input type=\"text\" placeholder=\"Address Line 2\" class=\"form-control\" name=\"addressline2\" id=\"addressline2\" required value=\"" + order.getAddressLine2() + "\">\n" +
                "                                        </div>\n" +
                "                                    </div>\n" +
                "\n" +
                "                                    <!-- Text input-->\n" +
                "                                    <div class=\"form-group\">\n" +
                "                                        <label class=\"col-sm-2 control-label\" for=\"city\">City</label>\n" +
                "                                        <div class=\"col-sm-10\">\n" +
                "                                            <input type=\"text\" placeholder=\"City\" class=\"form-control\" name=\"city\" id=\"city\" required value=\"" + order.getCity() + "\">\n" +
                "                                        </div>\n" +
                "                                    </div>\n" +
                "\n" +
                "                                    <!-- Text input-->\n" +
                "                                    <div class=\"form-group\">\n" +
                "                                        <label class=\"col-sm-2 control-label\" for=\"state\">State</label>\n" +
                "                                        <div class=\"col-sm-4\">\n" +
                "                                            <input type=\"text\" placeholder=\"State\" class=\"form-control\" name=\"state\" id=\"state\" required value=\"" + order.getState() + "\">\n" +
                "                                        </div>\n" +
                "\n" +
                "                                        <label class=\"col-sm-2 control-label\" for=\"zipcode\">Postcode</label>\n" +
                "                                        <div class=\"col-sm-4\">\n" +
                "                                            <input type=\"text\" placeholder=\"Post Code\" class=\"form-control\" name=\"zipcode\" id=\"zipcode\" required value=\"" + order.getZipcode() + "\">\n" +
                "                                        </div>\n" +
                "                                    </div>\n" +
                "\n" +
                "\n" +
                "\n" +
                "                                    <!-- Text input-->\n" +
                "                                    <div class=\"form-group\">\n" +
                "                                        <label class=\"col-sm-2 control-label\" for=\"country\">Country</label>\n" +
                "                                        <div class=\"col-sm-10\">\n" +
                "                                            <input type=\"text\" placeholder=\"Country\" class=\"form-control\" name=\"country\" id=\"country\" required value=\"" + order.getCountry() + "\">\n" +
                "                                        </div>\n" +
                "                                    </div>\n" +
                "\n" +
                "                                    <div class=\"form-group\">\n" +
                "                                        <div class=\"col-sm-offset-2 col-sm-10\">\n" +
                "                                            <div class=\"pull-right\">\n" +
                "                                                <!--<a type=\"button\"  href=\"cartview?quantity=1&item_id=5&category=smartwatches\" class=\"btn btn-default\">Cancel</a>-->\n" +
                "                                                <!--<a type=\"button\"  href=\"PaymentDetails.html\" class=\"btn btn-default\" onclick=\"document.getElementById('addressdetailsform').submit();\"style=\"background-color:#f63440;color: white;font-weight: 700\">Next</a>-->\n" +
                "\n" +
                "                                                <a type=\"button\"  href=\"FastCheckoutServlet?cancel=true\" class=\"btn btn-default\">Cancel</a>\n" +
                "                                                <input type=\"submit\" class=\"btn btn-default\" style=\"background-color:#f63440;color: white;font-weight: 700\" value=\"Proceed to payment\"/> <!--onclick=\"javascript: form.action='actionurl2';\"-->\n" +
                "                                            </div>\n" +
                "                                        </div>\n" +
                "                                    </div>\n" +
                "\n" +
                "                            </form>\n" +
                "\n" +
                "                </div>\n" +
                "\n" +
                "            </div>\n" +
                "\n" +
                "        </div>\n" +
                "    </div>\n" +
                "\n" +
                "\n" +
                "</body>\n" +
                "</html>");
    }

    private void viewOrders(PrintWriter printWriter, HttpSession session, HttpServletResponse response) {
        if(session.getAttribute("username")!=null) {
            LinkedHashMap<String,ArrayList<Order>> orderMap = MySQLDataStoreUtilities.getOrders((String) session.getAttribute("username"));
            if(orderMap.size()==0){
                printWriter.print("<h3 style=\"font-weight: bold; padding-left: 10px\">    No Orders Made Yet. </h3>");
                return;
            }
            StringBuilder htmlContentBuilder = new StringBuilder();
            htmlContentBuilder.append("<div class=\"vieworderscontent\" ><fieldset><legend>Your Orders</legend>\n");
            for(Map.Entry<String,ArrayList<Order>> entry : orderMap.entrySet()){
                String orderId = entry.getKey();
                ArrayList<Order> orderList = entry.getValue();
                double total = 0.0;
                htmlContentBuilder.append("<div style=\"display:inline-block\"><h4>Order: "+orderId+"</h4><a type=\"button\" href=\"FastCheckoutServlet?orderid="+orderId+"\" class=\"btn btn-danger btn-sm\">Select Order</a></div><table  class=\"table table-hover table-condensed cart\"><thead><tr><th style=\"width:50%\">    Product</th><th style=\"width:10%\">    Price</th><th style=\"width:9%\">    Quantity</th>\n" +
                            " <th style=\"width:9%\">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Subtotal</th></tr></thead><tbody>\n");

                for(int i=0;i<orderList.size();i++){
                    MenuItems menuItems = MySQLDataStoreUtilities.getMenuItemById(orderList.get(i).getItemId());
                    total+= orderList.get(i).getItemTotalPrice();
                    htmlContentBuilder.append("<tr><td data-th=\"Product\"><div class=\"row\"><div class=\"col-sm-2 hidden-xs\"><img src=\""+menuItems.getImage()+"\" alt=\"...\" class=\"img-responsive viewordersimg\"/></div><div class=\"col-sm-10\"><h4 class=\"nomargin\">    "+menuItems.getName()+"</h4>\n" +
                            " <p> "+menuItems.getIngredients()+" </p></div></div></td><td data-th=\"Price\">    $"+menuItems.getPrice()+"</td><form action=\"HomeServlet\" method=\"get\"><td data-th=\"Quantity\" >   &nbsp;&nbsp;&nbsp;&nbsp; "+orderList.get(i).getItemQuantity()+"</td><td data-th=\"Subtotal\" class=\"text-center\">    $"+orderList.get(i).getItemTotalPrice()+"</td></form></tr>\n");
                }
                htmlContentBuilder.append("</tbody></table><h5>Total: $"+String.format("%.2f",total)+"</h5><hr/>");
            }
            htmlContentBuilder.append("</fieldset></div>");
            printWriter.print(htmlContentBuilder.toString());
        }else{
            printWriter.print("<h3 style=\"font-weight: bold; padding-left: 10px\">    Please Signin To View Orders. </h3>");
        }
    }

}
