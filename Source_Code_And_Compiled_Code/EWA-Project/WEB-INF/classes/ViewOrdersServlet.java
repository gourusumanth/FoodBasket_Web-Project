import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

public class ViewOrdersServlet extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        process(request,response);
    }


    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        process(request,response);
    }

    private void process(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        if(request.getParameter("deleteOrder")!=null){
            MySQLDataStoreUtilities.deleteOrder(request.getParameter("deleteOrder"));
        }
        response.setContentType("text/html");
        PrintWriter printWriter = response.getWriter();
        HttpSession session = request.getSession(true);
        Utilities utility = new Utilities(printWriter, request, response);
        utility.printHtml("header.html");
        viewOrders(printWriter,session,response);
        utility.printHtml("footer.html");
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
                  if (Utilities.getDate(orderList.get(0).getDeliveryDate()).getTime()- new Date().getTime()>= 15*60*1000 && orderList.get(0).getStatus().equals("Accepted")) {
                      htmlContentBuilder.append("<div style=\"display:inline-block\"><h4>Order: "+orderId+"</h4><a type=\"button\" href=\"TrackOrder?orderid="+orderId+"\" class=\"btn btn-danger btn-sm\">Track Order</a><a type=\"button\" href=\"ViewOrdersServlet?deleteOrder="+orderId+"\" class=\"btn btn-danger btn-sm\"><i class=\"fa fa-trash-o\"></i></a></div><table  class=\"table table-hover table-condensed cart\"><thead><tr><th style=\"width:50%\">    Product</th><th style=\"width:10%\">    Price</th><th style=\"width:9%\">    Quantity</th>\n" +
                              " <th style=\"width:9%\">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Subtotal</th></tr></thead><tbody>\n");
                  } else {
                      htmlContentBuilder.append("<h4>Order: "+orderId+"</h4><table  class=\"table table-hover table-condensed cart\"><a type=\"button\" href=\"TrackOrder?orderid="+orderId+"\" class=\"btn btn-danger btn-sm\">Track Order</a><thead><tr><th style=\"width:50%\">    Product</th><th style=\"width:10%\">    Price</th><th style=\"width:9%\">    Quantity</th>\n" +
                              " <th style=\"width:9%\">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Subtotal</th></tr></thead><tbody>\n");
                  }
                   for(int i=0;i<orderList.size();i++){
                      MenuItems menuItems = MySQLDataStoreUtilities.getMenuItemById(orderList.get(i).getItemId());
                      total+= orderList.get(i).getItemTotalPrice();
                      htmlContentBuilder.append("<tr><td data-th=\"Product\"><div class=\"row\"><div class=\"col-sm-2 hidden-xs\"><img src=\""+menuItems.getImage()+"\" alt=\"...\" class=\"img-responsive viewordersimg\"/></div><div class=\"col-sm-10\"><h4 class=\"nomargin\">    "+menuItems.getName()+"</h4>\n" +
                                " <p> "+menuItems.getIngredients()+" </p></div></div></td><td data-th=\"Price\">    $"+menuItems.getPrice()+"</td><form action=\"HomeServlet\" method=\"get\"><td data-th=\"Quantity\" >   &nbsp;&nbsp;&nbsp;&nbsp; "+orderList.get(i).getItemQuantity()+"</td><td data-th=\"Subtotal\" class=\"text-center\">    $"+orderList.get(i).getItemTotalPrice()+"</td></form></tr>\n");
                  }
                  if(orderList.get(0).isDiscountApplied()){
                      total = total*0.8;
                      htmlContentBuilder.append("</tbody></table><h5>Total After 20% Discount: $"+String.format("%.2f",total)+"</h5><hr/>");
                  }else{
                      htmlContentBuilder.append("</tbody></table><h5>Total: $"+String.format("%.2f",total)+"</h5><hr/>");
                  }
            }
            htmlContentBuilder.append("</fieldset></div>");
            printWriter.print(htmlContentBuilder.toString());
        }else{
            printWriter.print("<h3 style=\"font-weight: bold; padding-left: 10px\">    Please Signin To View Orders. </h3>");
        }
    }

}
