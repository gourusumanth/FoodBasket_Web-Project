import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

public class ReviewServlet extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        process(request,response);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        process(request,response);
    }

    private void process(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        HttpSession session = request.getSession(true);
        if(request.getParameter("writeReview")!=null){
            writeReview(request,response);
        } else if(request.getParameter("submitReview")!=null){
            submitReview(request,response);
        } else if (request.getParameter("viewReview")!=null){
            viewReview(request,response);
        }
    }

    private void viewReview(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        StringBuilder htmlContentBuilder = new StringBuilder();
        response.setContentType("text/html");
        PrintWriter printWriter = response.getWriter();
        Utilities utility = new Utilities(printWriter,request,response);
        utility.printHtml("header.html");
        String restaurantId = request.getParameter("viewReview");
        List<RestaurantReviews> restaurantReviews = MongoDBDataStoreUtilities.readRestaurantReview(restaurantId);
        if(restaurantReviews==null || restaurantReviews.size()==0){
            printWriter.print("<br><br><h4> No reviews made yet for this restaurant.   </h4>");
        } else{
            htmlContentBuilder.append("<div class=\"container\"><fieldset><legend>Reviews</legend>");
            for(int j=restaurantReviews.size()-1;j>=0;j--){
                RestaurantReviews restaurantReview = restaurantReviews.get(j);
                htmlContentBuilder.append("<table class=\"table table-hover table-condensed cart\"><thead><tr><th style=\"width:50%\">   By "+restaurantReview.getUserId()+"</th></tr></thead>\n" +
                        " <tbody><tr><td data-th=\"Product Reviews\"><div class=\"row\"><div class=\"col-sm-2 hidden-xs\"> <img src=\" \" alt=\"...\" class=\"img-responsive\"/></div>\n" +
                        "     <div class=\"col-sm-10\"><h4 class=\"nomargin\"><span>Rating : "+restaurantReview.getReviewRating()+"/5.0</span></h4><h5>Summary: "+restaurantReview.getReviewSummary()+"</h5><h6>Details: "+restaurantReview.getReviewText()+"</h6></div></div></td></tr></tbody></table>\n");
            }
            htmlContentBuilder.append("</fieldset></div>");
            printWriter.print(htmlContentBuilder.toString());
        }
        utility.printHtml("footer.html");
    }

    private void submitReview(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        StringBuilder htmlContentBuilder = new StringBuilder();
        MongoDBDataStoreUtilities.insertRestaurantReview(request.getParameter("submitReview"),request.getParameter("reviewsummary"),request.getParameter("userid"),request.getParameter("reviewrating"),request.getParameter("reviewdate"),request.getParameter("reviewtext"),request.getParameter("zipcode"));
        response.setContentType("text/html");
        PrintWriter printWriter = response.getWriter();
        Utilities utility = new Utilities(printWriter,request,response);
        utility.printHtml("header.html");
        htmlContentBuilder.append("<h3 style=\"padding-left:25px;padding-right:10px;text-align:center\">Review added for resturant.\n Thank you for your time.</h3>");
        response.getWriter().write(htmlContentBuilder.toString());
        utility.printHtml("footer.html");
    }

    private void writeReview(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        HttpSession session = request.getSession();
        response.setContentType("text/html");
        PrintWriter printWriter = response.getWriter();
        Utilities utility = new Utilities(printWriter,request,response);
        String restaurantId = request.getParameter("writeReview");
        utility.printHtml("header.html");
        printWriter.print("<div class=\"panel panel-primary \" id=\"formcontainer\" style=\"width: 500px;margin-left: 32%;margin-top: 10px\"><div class=\"panel-heading\">Review</div><div class=\"panel-body\">\n" +
                "<form method=\"post\" action=\"ReviewServlet?submitReview="+restaurantId+"\"><div class=\"form-group\"><label for=\"reviewsummary\">Review Summary:</label><input type=\"text\" class=\"form-control\" id=\"reviewsummary\" name=\"reviewsummary\"></div>\n" +
                "<div class=\"form-group\"><label for=\"userid\">UserId:</label><input type=\"text\" class=\"form-control\" id=\"userid\" name=\"userid\"></div><div class=\"form-group\"><label for=\"reviewrating\">ReviewRating:</label><input type=\"number\" min=\"0.0\" max=\"5.0\" step=\"0.01\"  class=\"form-control\" id=\"reviewrating\" name=\"reviewrating\"></div>\n" +
                "<div class=\"form-group\"><label for=\"reviewdate\">ReviewDate:</label><input type=\"date\" class=\"form-control\" id=\"reviewdate\" name=\"reviewdate\"></div>\n" +
                "<div class=\"form-group\"><label for=\"zipcode\">Zipcode:</label><input type=\"text\" class=\"form-control\" id=\"zipcode\" name=\"zipcode\"></div>\n" +
                "<div class=\"form-group\"><label for=\"reviewtext\">ReviewText:</label><textarea class=\"form-control\" rows=\"5\" id=\"reviewtext\" name=\"reviewtext\"></textarea></div>\n" +
                "<button type=\"submit\" class=\"btn btn-success pull-right\" name=\"submitReview\">Submit Review</button></form></div></div>");
        utility.printHtml("footer.html");
    }

}
