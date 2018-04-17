import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class MainPageServlet extends HttpServlet{

    private boolean isRelaunch;

    public void init() throws ServletException {
        isRelaunch=true;
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        process(request,response);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        process(request,response);
    }

    public void process(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        if(isRelaunch){
            Utilities.clearSessionVariables(request);
            isRelaunch = false;
        }
        response.setContentType("text/html");
        PrintWriter printWriter = response.getWriter();

        Utilities utility = new Utilities(printWriter,request,response);
        utility.printHtml("header.html");
        utility.printHtml("homepagecontent.html");
        utility.printHtml("footer.html");
    }

}
