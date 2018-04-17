import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;
import java.util.List;

public class RestaurantServlet extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        process(request,response);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        process(request,response);
    }

    private void process(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        if(request.getParameter("zipcode")!=null){
            findRestaurantsByZipcode(request.getParameter("zipcode"),request,response);
        }
    }

    private void findRestaurantsByZipcode(String zipcode, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        response.setContentType("text/html");
        PrintWriter printWriter = response.getWriter();
        List<Restaurant> restaurantList = MySQLDataStoreUtilities.getRestaurant(zipcode);
        Utilities utility = new Utilities(printWriter,request,response);
        utility.printHtml("header.html");
        createRestaurantsHTML(zipcode,restaurantList,printWriter);
        utility.printHtml("footer.html");
    }

    public static void createRestaurantsHTML(String zipcode, List<Restaurant> restaurantList, PrintWriter printWriter) {
        StringBuilder htmlContentBuilder = new StringBuilder();
        htmlContentBuilder.append("<div id=\"content\">");
        for(int i=0; i < restaurantList.size();i++){
            Restaurant r = restaurantList.get(i);
            htmlContentBuilder.append("<div class=\"restaurantCard\">\n <div class=\"restaurantImage\">\n <img src=\""+r.getImage()+"\" alt=\"...\" width=\"100px\" height=\"100px\">\n" +
                    "</div>\n <div class=\"restaurantName text-info\">\n <a href=\"RestaurantMenuServlet?restaurantId="+r.getId()+"\">"+r.getName()+"</a>\n <div class=\"restaurantTagline\">\n "+r.getTagLine()+"<br>\n" +
                    "<span style=\"font-weight: 400\">7:00am - 9:00pm</span>\n </div>\n </div>\n <div class=\"reviewsHeading text-info\">\n <div class=\"reviewStars\">\n" +
                    "<a href=\"ReviewServlet?viewReview="+r.getId()+"\" style=\"text-decoration: none;float: left\">\n <i style=\"font-size: 13px;font-weight: 400;\">View Reviews    </i></a>\n" +
                    "<a href=\"ReviewServlet?writeReview="+r.getId()+"&zipcode="+zipcode+"\" style=\"text-decoration: none;float: left\">\n<i style=\"font-size: 13px;font-weight: 400;\"> ,Write Reviews</i> \n </a></div>\n </div>\n" +
                    "<div class=\"ExpensiveMeter\">"+r.getExpensiveMeter()+"</div>\n <div class=\"minimumAmount\"  style=\"border-right:1px solid #f1f1f1; \">"+r.getMinimumAmount()+"$ \n <div class=\"restaurantTagline\">\n" +
                    "<span style=\"font-weight: 400;\" >Minimum</span>\n </div>\n </div>\n <div class=\"minimumAmount\" >"+r.getDeliverFee()+"$ \n <div class=\"restaurantTagline\">\n" +
                    "<span style=\"font-weight: 400;\" >Delivery Fee</span>\n </div>\n </div>\n </div>");
        }
        if(restaurantList.size()==0){
            htmlContentBuilder.append("<h3 style=\"\">No Restaurants Found Nearby.\n Please bear with us while we expand.\n</h3>\n");
        }
        htmlContentBuilder.append("</div>\n");
        printWriter.print(htmlContentBuilder.toString());
    }


}
