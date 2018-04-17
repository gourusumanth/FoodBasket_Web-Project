import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class DealMatchesServlet extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        process(request,response);
    }


    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        process(request,response);
    }

    private void process(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        HttpSession session = request.getSession(true);
        PrintWriter printWriter = response.getWriter();
        Utilities utility = new Utilities(printWriter,request,response);
        utility.printHtml("header.html");
        StringBuilder htmlContentBuilder = new StringBuilder();
        htmlContentBuilder.append("<div class=\"header_bottom_right\">");
        htmlContentBuilder.append("<br><p>      </p><div width=\"100%\"><a href=\"#\"><img src=\"images/best_deals.jpg\" padding-left=\"5%\" width=\"90%\"/></a></div><br>");
        htmlContentBuilder.append("<h3>         Welcome to Best Deals</h3><h3>          We beat our competitors in all aspects. Price-Match Guaranteed</h3>");

        String catalinaHome = System.getProperty("catalina.home").replace("\\", "/");
        String dealMatchesFileName = catalinaHome+"/webapps/EWA-Project/DealMatches.txt";
        Map<String,MenuItems> selectedMenuItems = new LinkedHashMap<String,MenuItems>();
        Map<String,MenuItems> menuItemMap = MySQLDataStoreUtilities.getAllMenuItems();
        for(Map.Entry<String,MenuItems> entrySet: menuItemMap.entrySet()){
            if(selectedMenuItems.size()<2 && !selectedMenuItems.containsKey(entrySet.getKey())){
                BufferedReader reader = new BufferedReader(new FileReader(new File(dealMatchesFileName)));
                String line=reader.readLine();
                if(line==null && selectedMenuItems.size()==0) {
                    htmlContentBuilder.append("<h5 style=\"color:#B81D22\">No Offers Found</h5>");
                    htmlContentBuilder.append("<h3>Deal Matches</h3>");
                    htmlContentBuilder.append("<h5 style=\"color:#B81D22\">No Deals Found</h5>");
                    printWriter.print(htmlContentBuilder.toString());
                    utility.printHtml("footer.html");
                    return;
                }else{
                    do {
                        if(line.toLowerCase().contains(entrySet.getValue().getName().toLowerCase()) && !selectedMenuItems.containsKey(entrySet.getKey())) {
                            if(line.indexOf("http")!=-1) {
                                htmlContentBuilder.append("<h6>" + line.substring(0, line.indexOf("http")) + "</h6>");
                                htmlContentBuilder.append("<a href=" + line.substring(line.indexOf("http"), line.length()) + ">" + line.substring(line.indexOf("http"), line.length()) + "</a>");
                                htmlContentBuilder.append("<br>");
                            }else if(line.indexOf("https")!=-1) {
                                htmlContentBuilder.append("<h6>" + line.substring(0, line.indexOf("https")) + "</h6>");
                                htmlContentBuilder.append("<a href=" + line.substring(line.indexOf("https"), line.length()) + ">" + line.substring(line.indexOf("https"), line.length()) + "</a>");
                                htmlContentBuilder.append("<br>");
                            }else{
                                htmlContentBuilder.append("<h6>" + line + "</h6><br>");
                            }
                            selectedMenuItems.put(entrySet.getKey(),entrySet.getValue());
                        }
                    } while((line = reader.readLine()) != null && selectedMenuItems.size()<2);
                }
            }
        }
        if(selectedMenuItems.size()==0) {
            htmlContentBuilder.append("<h5 style=\"color:#B81D22\" >No Offers Found</h5>");
            htmlContentBuilder.append("<h3>Deal Matches</h3>");
            htmlContentBuilder.append("<h5 style=\"color:#B81D22\" >No Deals Found</h5>");
            printWriter.print(htmlContentBuilder.toString());
            utility.printHtml("footer.html");
            return;
        }
        htmlContentBuilder.append("<br><br><br><h3>Deal Matches</h3>");
        for(Map.Entry<String,MenuItems> entrySet: selectedMenuItems.entrySet()){
            MenuItems items = entrySet.getValue();
            float finalPrice = items.getPrice();
            htmlContentBuilder.append("<div class=\"grid_1_of_2 images_1_of_2\"><img src=\""+items.getImage()+"\"alt=\"\" />  <h2>"+items.getName()+" </h2>"+
                    "<div class=\"price-details\"></div><div class=\"price-number\"><p><span class=\"rupees\">$"+finalPrice+"</span></p>"+
                    "</div><div class=\"preview\"><h4><a href=\"RestaurantMenuServlet?restaurantId="+items.getRestaurantId()+"\">Preview</a></h4></div><div class=\"clear\"></div></div>");

        }

        htmlContentBuilder.append("</div>");
        printWriter.print(htmlContentBuilder.toString());
        utility.printHtml("footer.html");
    }

}
