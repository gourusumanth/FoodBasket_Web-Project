import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import javax.servlet.annotation.WebServlet;

@WebServlet("/TrackOrder")

public class TrackOrder extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
      response.setContentType("text/html");

      String orderId = request.getParameter("orderid");

      PrintWriter pw = response.getWriter();
      Utilities utility = new Utilities(pw,request,response);

      Order od = MySQLDataStoreUtilities.getOrderById(orderId);
      utility.printHtml("trackOrder.html");
      // pw.print("<script>$(window).on('load',function(){$('#myModal').modal('show');});</script>");
      if (Utilities.getDate(od.getDeliveryDate()).getTime()- new Date().getTime()<0 && !od.getStatus().equals("Delivered")) {
          MySQLDataStoreUtilities.updateOrderStatus("Delivered",od.getOrderId());
          od.setStatus("Delivered");
      } else if(Utilities.getDate(od.getDeliveryDate()).getTime()- new Date().getTime()<15*60*1000  && !od.getStatus().equals("Shipped")){
          MySQLDataStoreUtilities.updateOrderStatus("Shipped",od.getOrderId());
          od.setStatus("Shipped");
      }
      pw.print("<div class='modal fade' id='myModal' role='dialog'>"+
          "<div class='modal-dialog'>"+
              "<!-- Modal content-->"+
              "<div class='modal-content'>"+
                  "<div class='modal-header'>"+
                      "<!--<button type='button' class='close' data-dismiss='modal'>&times;</button>-->"+
                      "<a href='#' type='btn' class='close'>&times;</a>"+
                      "<h4 class='modal-title ' style='color:#f63440;font-weight: 700'>Order Details</h4>"+
                  "</div>"+
                  "<div class='modal-body'>"+
                      "<form class='form-horizontal' id='addressdetailsform' action='ViewOrdersServlet' role='form' style='magin-left:10px; padding: 10px;width: 500px'>"+
                          "<h3>Order Status</h3>"+
                          "<p>Order Id -> "+orderId+" </p>"+
                          "<div> Order Time: "+ od.getOrderDate() +"<div>"+
                          "<div> Delivery Time: "+ od.getDeliveryDate() +"<div>"+
                          "<div> Stauts: "+ od.getStatus() +"<div>"+
                          "<div class='form-group'>"+
                              "<div class='col-sm-offset-2 col-sm-10'>"+
                                  "<div class='pull-right'>"+
                                      "<a type='button'  href='ViewOrdersServlet' class='btn btn-default'>Cancel</a>"+
                                      "<input type='submit' class='btn btn-default' style='background-color:#f63440;color: white;font-weight: 700' value='Ok'/>"+
                                  "</div>"+
                              "</div>"+
                          "</div>"+
                      "</form>"+
                  "</div>"+
              "</div>"+
          "</div>"+
      "</div></body></html>");
    }
}
