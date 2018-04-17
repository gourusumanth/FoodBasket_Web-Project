import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;


public class TrendingServlet extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        process(request,response);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        process(request,response);
    }

    private void process(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        StringBuilder htmlContentBuilder = new StringBuilder();
        response.setContentType("text/html");
        PrintWriter printWriter = response.getWriter();
        Utilities utility = new Utilities(printWriter,request,response);
        utility.printHtml("header.html");
        createTrendingData(request,response);
        utility.printHtml("footer.html");
    }

    private void createTrendingData(HttpServletRequest request, HttpServletResponse response) throws IOException {
        createTopFiveLikedRestaurants(request,response);
        createTopFiveSoldRestaurants(request,response);
        createTopFiveSoldRestaurantsZipcode(request,response);
    }

    private void createTopFiveSoldRestaurantsZipcode(HttpServletRequest request, HttpServletResponse response) throws IOException {
        StringBuilder htmlContentBuilder =new StringBuilder();
        PrintWriter printWriter = response.getWriter();
        List<Trending> trendingZipCodeProductList = MongoDBDataStoreUtilities.getMostReviewsInZipCode();
        htmlContentBuilder.append("<div class=\"header_bottom_right\"><h4>Top Five Zip Code With Maximum Number Of Products Sold: </h4>");
        htmlContentBuilder.append("<table style=\"width:80%;border: 3px solid #EEE;text-align:center;\"><thead style=\"background-color:#f63440;font-weight:bold;font-size:120%;text-align:center;color:#FFF\"><tr><th style=\"border: 3px solid #EEE;\">  ZipCode  </th><th style=\"border: 3px solid #EEE;\">  Restaurants Visited  </th></tr></thead>");

        for(int i=0;i<trendingZipCodeProductList.size();i++){
            Trending t = trendingZipCodeProductList.get(i);
            htmlContentBuilder.append("<tr style=\"border: 3px solid #EEE;\"><td style=\"border: 3px solid #EEE;\"> "+t.getZipcode()+" </td><td style=\"border: 3px solid #EEE;\"> "+ t.getReviewCount() +" </td></tr>");
        }
        htmlContentBuilder.append("</table>");
        htmlContentBuilder.append("</div><div class=\"header_bottom_right\">");
        htmlContentBuilder.append("</div>");
        printWriter.print(htmlContentBuilder.toString());

    }

    private void createTopFiveSoldRestaurants(HttpServletRequest request, HttpServletResponse response) throws IOException {
        StringBuilder s =new StringBuilder();
        PrintWriter printWriter = response.getWriter();
        s.append("<div class=\"container\">");
        s.append("<fieldset> <legend>Top Five Visited Restaurants </legend>");
        s.append("<div id=\"carousel-example-generic\" class=\"carousel slide\" data-ride=\"carousel\">\n"

                +"<ol class=\"carousel-indicators\">\n"
                +"<li data-target=\"#carousel-example-generic\" data-slide-to=\"0\" class=\"active\"></li>\n"
                +"<li data-target=\"#carousel-example-generic\" data-slide-to=\"1\"></li>\n"
                +"<li data-target=\"#carousel-example-generic\" data-slide-to=\"2\"></li>\n"
                +"</ol>\n"
                +"<div class=\"carousel-inner\">\n");
        int cnt = 0;
        List<Trending> trendingList = MongoDBDataStoreUtilities.getTopSoldRestaurants();

        for(int i=0;i<trendingList.size();i++)
        {
            Trending trending = trendingList.get(i);
            Restaurant restaurant = MySQLDataStoreUtilities.getRestaurantById(trending.getRestaurantId());


            if(cnt==0 ){
                s.append("<div class=\"row item active\">\n");
            }
            if(cnt==3){
                s.append("<div class=\"row item\">\n");
            }
            s.append("<div class=\" col-md-4 col-sm-4 col-xs-4 col-lg-4\">\n"
                    +"<div class=\"thumbnail bg-primary\">\n"
                    +"<img src=\""+restaurant.getImage()+"\" alt=\"First slide\">\n"
                    +"<!-- Static Header -->\n"
                    +"<div  style=\"text-align: center\">\n"
                    +"<b class=\"text-primary\" style=\"font-size: 22px;margin-left: 25px\">"+restaurant.getName()+"</b><br>\n"
                    +"<ul>"
                    +"<li><a type=\"button\" class=\"btn btn-default disabled\" style=\"margin-bottom:5px;\" href=\"#\">Visited Count : "+ trending.getReviewCount()+"</a><br></li>\n"
                    +"</ul>"
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
        printWriter.print(s.toString());
    }

    private void createTopFiveLikedRestaurants(HttpServletRequest request, HttpServletResponse response) throws IOException {
        StringBuilder s =new StringBuilder();
        PrintWriter printWriter = response.getWriter();
        s.append("<div class=\"container\">");
        s.append("<fieldset> <legend>Top Five Liked Restaurants </legend>");
        s.append("<div id=\"carousel-example-generic\" class=\"carousel slide\" data-ride=\"carousel\">\n"

                +"<ol class=\"carousel-indicators\">\n"
                +"<li data-target=\"#carousel-example-generic\" data-slide-to=\"0\" class=\"active\"></li>\n"
                +"<li data-target=\"#carousel-example-generic\" data-slide-to=\"1\"></li>\n"
                +"<li data-target=\"#carousel-example-generic\" data-slide-to=\"2\"></li>\n"
                +"</ol>\n"
                +"<div class=\"carousel-inner\">\n");
        int cnt = 0;
        List<Trending> trendingList = MongoDBDataStoreUtilities.getTopFiveLikedRestaurants();

        for(int i=0;i<trendingList.size();i++)
        {
            Trending trending = trendingList.get(i);
            Restaurant restaurant = MySQLDataStoreUtilities.getRestaurantById(trending.getRestaurantId());


            if(cnt==0 ){
                s.append("<div class=\"row item active\">\n");
            }
            if(cnt==3){
                s.append("<div class=\"row item\">\n");
            }
            s.append("<div class=\" col-md-4 col-sm-4 col-xs-4 col-lg-4\">\n"
                    +"<div class=\"thumbnail bg-primary\">\n"
                    +"<img src=\""+restaurant.getImage()+"\" alt=\"First slide\">\n"
                    +"<!-- Static Header -->\n"
                    +"<div  style=\"text-align: center\">\n"
                    +"<b class=\"text-primary\" style=\"font-size: 22px;margin-left: 25px\">"+restaurant.getName()+"</b><br>\n"
                    +"<ul>"
                    +"<li><a type=\"button\" class=\"btn btn-default disabled\" style=\"margin-bottom:5px;\" href=\"#\">Average Rating : "+ trending.getReviewRating()+"</a><br></li>\n"
                    +"</ul>"
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
        printWriter.print(s.toString());
    }

}
