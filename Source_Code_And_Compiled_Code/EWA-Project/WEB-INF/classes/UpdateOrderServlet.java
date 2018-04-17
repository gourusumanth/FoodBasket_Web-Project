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
import java.util.Map;

public class UpdateOrderServlet extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        process(request, response);
    }


    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        process(request, response);
    }

    private void process(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        if(request.getParameter("orderStatus")!=null){
            MySQLDataStoreUtilities.updateOrderStatus(request.getParameter("orderStatus"),request.getParameter("ordernumber"));
            response.setContentType("text/html");
            HttpSession session = request.getSession(true);
            PrintWriter printWriter = response.getWriter();
            Utilities utility = new Utilities(printWriter, request, response);
            utility.printHtml("header.html");
            printWriter.print("<h3>Update Order Status for Order: "+request.getParameter("ordernumber")+" to Status: "+request.getParameter("orderStatus")+"</h3>");
            utility.printHtml("footer.html");
        }else {
            response.setContentType("text/html");
            HttpSession session = request.getSession(true);
            PrintWriter printWriter = response.getWriter();
            Utilities utility = new Utilities(printWriter, request, response);
            utility.printHtml("header.html");
            updateOrderStatus(request, response);
            utility.printHtml("footer.html");
        }
    }

    private void updateOrderStatus(HttpServletRequest request, HttpServletResponse response) throws IOException {
        PrintWriter printWriter = response.getWriter();
        HttpSession session = request.getSession(true);
        LinkedHashMap<String,ArrayList<Order>> orders = MySQLDataStoreUtilities.getOrdersByRestaurant((String) session.getAttribute("username"));
        StringBuilder htmlContentBuilder = new StringBuilder();
        htmlContentBuilder.append("<div class=\"panel panel-primary\" id=\"formcontainer\" style=\"width:500px;margin-top:30px;margin-left:30%\">\n" +
                "    <div class=\"panel-heading\">Order Status</div>\n" +
                "    <div class=\"panel-body\">\n" +
                "        <form method=\"get\" action=\"UpdateOrderServlet\">\n" +
                "            <div class=\"form-group\">\n" +
                /*"                <label for=\"ordernumber\">ordernumber:</label>\n" +
                "                <input type=\"number\" class=\"form-control\" id=\"ordernumber\" name=\"ordernumber\">\n" +*/
                "               <select name=\"ordernumber\" id=\"ordernumber\" class=\"form-control\">" );

        for (Map.Entry<String,ArrayList<Order>> entry: orders.entrySet()){
            htmlContentBuilder.append("<option value=\""+entry.getKey()+"\">"+entry.getKey()+"</option> ");
        }
        htmlContentBuilder.append("                </select>\n" +
                "            </div>\n" +
                "            <div class=\"form-group\">\n" +
                "                <label for=\"orderStatus\">Order Status:</label>\n" +
                "                <select name=\"orderStatus\" id=\"orderStatus\" class=\"form-control\">\n" +
                "                    <option value=\"Accepted\">Accepted</option>\n" +
                "                    <option value=\"Shipped\">Shipped</option>\n" +
                "                    <option value=\"Delivered\">Delivered</option>\n" +
                "\n" +
                "                </select>\n" +
                "            </div>\n" +
                "\n" +
                "            <button type=\"submit\" class=\"btn btn-success pull-right\" >Submit</button>\n" +
                "\n" +
                "        </form>\n" +
                "    </div>\n" +
                "</div>");
        printWriter.print(htmlContentBuilder.toString());
    }

}