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
import javax.servlet.annotation.WebServlet;

@WebServlet("/FeedbackDisplay")

public class FeedbackDisplay extends HttpServlet {
    HttpSession session;
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
      session = request.getSession(true);

      response.setContentType("text/html");
      PrintWriter printWriter = response.getWriter();
      Utilities utility = new Utilities(printWriter,request,response);
      utility.printHtml("header.html");
      utility.printHtml("complaintsView.html");
      LinkedHashMap<Integer, Feedback> allFeedbacks = MySQLDataStoreUtilities.getFeedbacks();

      for (Integer key : allFeedbacks.keySet()) {
        Feedback f = allFeedbacks.get(key);
        ArrayList<Comment> comments = MySQLDataStoreUtilities.getComments(key);
        printWriter.print("<ul id='comments-list' class='comments-list'>"+
            "<li>"+
                "<div class='comment-main-level'>"+
                    "<div class='comment-avatar'><img src='http://i9.photobucket.com/albums/a88/creaticode/avatar_1_zps8e1c80cd.jpg' alt=''></div>"+
                    "<!-- Contenedor del Comentario -->"+
                    "<div class='comment-box'>"+
                        "<div class='comment-head'>"+
                            "<h6 class='comment-name by-author'><a href='#'>"+f.getFeedbackUserName()+"</a></h6>"+
                            "<i class='fa fa-reply' data-toggle='modal' data-target='#usermodal'></i>"+
                        "</div>"+
                        "<div class='comment-content'>"+f.getFeedbackText()+
                        "</div>"+
                    "</div>"+
                "</div>");
        for(Comment c: comments) {
          printWriter.print("<ul class='comments-list reply-list'>"+
                              "<li>"+
                                  "<div class='comment-avatar'><img src='http://i9.photobucket.com/albums/a88/creaticode/avatar_2_zps7de12f8b.jpg' alt=''></div>"+
                                  "<div class='comment-box'>"+
                                      "<div class='comment-head'>"+
                                          "<h6 class='comment-name text-primary'><a href='#'>"+c.getCommentUserName()+"</a></h6>"+
                                          "<i class='fa fa-reply' data-toggle='modal' data-target='#adminmodal'></i>"+
                                          "<!--<i class='fa fa-heart'></i>-->"+
                                      "</div>"+
                                      "<div class='comment-content'>"+c.getCommentText()+
                                      "</div>"+
                                  "</div>"+
                              "</li>"+
                          "</ul>");
        }
        printWriter.print("</li>"+
        "</ul>");
      }
      printWriter.print("</div>"+
      "</div>"+
      "</div>");
      utility.printHtml("footer.html");
    }
}
