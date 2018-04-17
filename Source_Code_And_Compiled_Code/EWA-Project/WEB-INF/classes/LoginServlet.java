import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

public class LoginServlet extends HttpServlet{

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        process(request,response);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        process(request,response);
    }

    public void process(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        if(request.getParameter("signup")!=null){
            HttpSession session = request.getSession(true);
            if (session.getAttribute("role") != null && session.getAttribute("role").equals("Admin")) {
              registerUserWithoutLogin(request, response);
            } else {
              registerUser(request,response);
            }
        }else if(request.getParameter("signin")!=null){
            signInUser(request,response);
        }else if(request.getParameter("logout")!=null){
            Utilities.clearSessionVariables(request);
            response.sendRedirect("MainPageServlet");
        }
    }

    private void signInUser(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String role = request.getParameter("role");
        if(username==null || username.isEmpty() || password==null || password.isEmpty() || role==null || role.isEmpty()){
            showPage(response,"Login Failure, Enter complete details and try again.");
        }else if(MySQLDataStoreUtilities.isValidUser(username.trim(),password.trim(),role.trim())){
            HttpSession session = request.getSession(true);
            session.setAttribute("username", username.trim());
            session.setAttribute("role", role.trim());
            sendRedirect(session,role.trim(),response);
        }else{
            showPage(response,"Login Failure. Username, password and role do not match.");
        }
    }

    private void registerUser(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String name = request.getParameter("signupname");
        String username = request.getParameter("signupusername");
        String password = request.getParameter("signuppassword");
        String confirmPassword = request.getParameter("confirmpassword");
        String role = request.getParameter("role");
        if(name == null || name.isEmpty() || username == null || username.isEmpty() || password == null || password.isEmpty() ||
                confirmPassword==null || confirmPassword.isEmpty() || role==null || role.isEmpty() || !password.equals(confirmPassword)){
            showPage(response,"Registration Failure");
        }else{
            if (role.equals("FoodVendor")) {
              String restaurantName = request.getParameter("signuprestaurantname");
              MySQLDataStoreUtilities.insertUser(name.trim(),username.trim(),password.trim(),role);
              MySQLDataStoreUtilities.insertFoodVendor(username.trim(),password.trim(), restaurantName.trim());
              HttpSession session = request.getSession(true);
              session.setAttribute("username", username.trim());
              session.setAttribute("role", role.trim());
              sendRedirect(session,role.trim(),response);
            } else {
              MySQLDataStoreUtilities.insertUser(name.trim(),username.trim(),password.trim(),role);
              HttpSession session = request.getSession(true);
              session.setAttribute("username", username.trim());
              session.setAttribute("role", role.trim());
              sendRedirect(session,role.trim(),response);
            }
        }
    }

    private void registerUserWithoutLogin(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String name = request.getParameter("signupname");
        String username = request.getParameter("signupusername");
        String password = request.getParameter("signuppassword");
        String confirmPassword = request.getParameter("confirmpassword");
        String role = request.getParameter("role");
        if(name == null || name.isEmpty() || username == null || username.isEmpty() || password == null || password.isEmpty() ||
                confirmPassword==null || confirmPassword.isEmpty() || role==null || role.isEmpty() || !password.equals(confirmPassword)){
            showPage(response,"Registration Failure");
        }else{
          if (role.equals("FoodVendor")) {
            String restaurantName = request.getParameter("signuprestaurantname");
            HttpSession session = request.getSession(true);
            MySQLDataStoreUtilities.insertUser(name.trim(),username.trim(),password.trim(),role);
            MySQLDataStoreUtilities.insertFoodVendor(username.trim(),password.trim(), restaurantName.trim());
            sendRedirect(session,"Admin",response);
          } else {
            String restaurantName = request.getParameter("signuprestaurantname");
            HttpSession session = request.getSession(true);
            MySQLDataStoreUtilities.insertUser(name.trim(),username.trim(),password.trim(),role);
            sendRedirect(session,"Admin",response);
          }
        }
    }

    private void sendRedirect(HttpSession session, String role, HttpServletResponse response) throws IOException {
        if(role.equals("Customer")){
            session.setAttribute("cartItems",new LinkedHashMap<String,Integer>());
            response.sendRedirect("MainPageServlet");
        }else if(role.equals("FoodVendor")){
            response.sendRedirect("FoodVendorServlet");
        }else if(role.equals("Admin")){
            response.sendRedirect("AdminServlet");
        }
    }

    protected void showPage(HttpServletResponse response, String message) throws ServletException, java.io.IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        out.println("<html>");
        out.println("<head>");
        out.println("<title>Login Servlet Response</title>");
        out.println("</head>");
        out.println("<body>");
        out.println("<h2>" + message + "</h2>");
        out.println("</body>");
        out.println("</html>");
        out.close();
    }

}
