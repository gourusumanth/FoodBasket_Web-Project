import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.WebServlet;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/Error")

public class Error extends HttpServlet {
  HttpSession session;

  protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    PrintWriter pw = response.getWriter();

    session = request.getSession();
    String errorString = request.getParameter("error");

    pw.print(errorString);

  }
}
