import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class AutoCompleteServlet extends HttpServlet {

    public AutoCompleteServlet() {

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            StringBuffer sb = new StringBuffer();
            String restaurantSearchId = request.getParameter("restaurantSearchId");
            sb = AjaxUtility.readRestaurantData(restaurantSearchId);
            if (sb != null && !sb.equals("")) {
                response.setContentType("text/xml");
                response.getWriter().write("<restaurants>" + sb.toString() + "</restaurants>");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
