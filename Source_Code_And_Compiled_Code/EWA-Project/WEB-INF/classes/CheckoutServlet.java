import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

public class CheckoutServlet extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        process(request,response);
    }


    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        process(request,response);
    }

    private void process(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        if(request.getParameter("cancel")!=null){
            response.sendRedirect("CartServlet");
        }else if(request.getParameter("addressdetails")!=null){
            saveAddressDetails(request,response);
        }else if(request.getParameter("paymentdetails")!=null){
            saveOrder(request,response);
        }else{
            response.setContentType("text/html");
            PrintWriter printWriter = response.getWriter();
            Utilities utility = new Utilities(printWriter, request, response);
            utility.printHtml("addressdetails.html");
        }
    }

    private void saveOrder(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        HttpSession session = request.getSession(true);
        LinkedHashMap<String,ArrayList<Order>> orders = MySQLDataStoreUtilities.getOrders((String) session.getAttribute("username"));
        boolean discount = false;
        if(orders.size()%5==0){
            discount = true;
        }
        List<Order> orderList = MySQLDataStoreUtilities.insertOrder(request,discount);
        response.setContentType("text/html");
        PrintWriter printWriter = response.getWriter();
        Utilities utility = new Utilities(printWriter, request, response);
        utility.printHtml("header.html");
        createReceipt(orderList,request,response,discount);
        utility.printHtml("footer.html");
        if(request.getParameter("rememberme")!=null){
            MySQLDataStoreUtilities.savePaymentDetails(request);
        }
    }

    private void createReceipt(List<Order> orderList, HttpServletRequest request, HttpServletResponse response, boolean discount) throws IOException {
        StringBuilder htmlContentBuilder = new StringBuilder();
        PrintWriter printWriter = response.getWriter();
        double total=0.0;
        for(int i=0;i<orderList.size();i++){
            total+=orderList.get(i).getItemTotalPrice();
        }
        if(discount){
            total = total*0.8;
            htmlContentBuilder.append("<div class=\"container\" style=\"margin-top: 10px;\"><div class=\"row\"><div class=\"well col-xs-10 col-sm-10 col-md-6 col-xs-offset-1 col-sm-offset-1 col-md-offset-3\"><div class=\"row\">\n" +
                    "<div class=\"col-xs-6 col-sm-6 col-md-6\"><address>"+orderList.get(0).getAddressLine1()+"<br>"+orderList.get(0).getAddressLine2()+"<br>"+orderList.get(0).getCity()+", "+orderList.get(0).getState()+", "+orderList.get(0).getZipcode()+"</address></div><div class=\"col-xs-6 col-sm-6 col-md-6 text-right\" ><p style=\"color: black\"><em>Date: "+orderList.get(0).getOrderDate()+"</em></p><p style=\"color: black\"><em>Order #: "+orderList.get(0).getOrderId()+"</em></p></div></div>\n" +
                    "<div class=\"row\"><div class=\"text-center\"><h1>Receipt</h1></div></span><table class=\"table table-hover\"><tr><td>   </td><td>   </td><td class=\"text-center\"><p style=\"color: black\">You order will be delivered to above address in 30 minutes</p>" +
                    "</td></tr><tr><td>   </td><td>   </td><td class=\"text-right\"><h4><strong>Total After 20% Discount: $"+String.format("%.2f",total)+"</strong></h4></td><td class=\"text-center text-danger\"><h4><strong></strong></h4></td></tr></tbody></table><button type=\"button\" class=\"btn btn-success btn-lg pull-right\" onclick=\"window.print()\"> Print <span class=\"glyphicon glyphicon-print\"></span></button></td></div></div></div></div>\n");

        }else{
            htmlContentBuilder.append("<div class=\"container\" style=\"margin-top: 10px;\"><div class=\"row\"><div class=\"well col-xs-10 col-sm-10 col-md-6 col-xs-offset-1 col-sm-offset-1 col-md-offset-3\"><div class=\"row\">\n" +
                    "<div class=\"col-xs-6 col-sm-6 col-md-6\"><address>"+orderList.get(0).getAddressLine1()+"<br>"+orderList.get(0).getAddressLine2()+"<br>"+orderList.get(0).getCity()+", "+orderList.get(0).getState()+", "+orderList.get(0).getZipcode()+"</address></div><div class=\"col-xs-6 col-sm-6 col-md-6 text-right\" ><p style=\"color: black\"><em>Date: "+orderList.get(0).getOrderDate()+"</em></p><p style=\"color: black\"><em>Order #: "+orderList.get(0).getOrderId()+"</em></p></div></div>\n" +
                    "<div class=\"row\"><div class=\"text-center\"><h1>Receipt</h1></div></span><table class=\"table table-hover\"><tr><td>   </td><td>   </td><td class=\"text-center\"><p style=\"color: black\">You order will be delivered to above address in 30 minutes</p>" +
                    "</td></tr><tr><td>   </td><td>   </td><td class=\"text-right\"><h4><strong>Total: $"+String.format("%.2f",total)+"</strong></h4></td><td class=\"text-center text-danger\"><h4><strong></strong></h4></td></tr></tbody></table><button type=\"button\" class=\"btn btn-success btn-lg pull-right\" onclick=\"window.print()\"> Print <span class=\"glyphicon glyphicon-print\"></span></button></td></div></div></div></div>\n");
        }
        printWriter.print(htmlContentBuilder.toString());
    }

    private void saveAddressDetails(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        HttpSession session = request.getSession(true);
        LinkedHashMap<String,ArrayList<Order>> orders = MySQLDataStoreUtilities.getOrders((String) session.getAttribute("username"));
        boolean discount = false;
        if(orders.size()%5==0){
            discount = true;
        }
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
        String catalinaHome = System.getProperty("catalina.home").replace("\\", "/");
        String htmlFilePath = catalinaHome + "/webapps/EWA-Project/paymentdetails.html";
        String html = Utilities.htmlToString(htmlFilePath);
        int index = html.indexOf("4200");
        double total = getTotal(session);
        if(discount){
            total = total*0.8;
        }
        printWriter.print(html.substring(0,index)+String.format("%.2f",total)+html.substring(index+4));

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

}
