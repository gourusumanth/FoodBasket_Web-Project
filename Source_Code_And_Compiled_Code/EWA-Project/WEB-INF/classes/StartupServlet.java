import javax.servlet.*;
import javax.servlet.http.*;

public class StartupServlet extends HttpServlet{

    public void init() throws ServletException {
        String catalinaHome = System.getProperty("catalina.home").replace("\\", "/");
        String restaurantXML = catalinaHome+"/webapps/EWA-Project/Restaurants.xml";
        String menuItemsXML = catalinaHome+"/webapps/EWA-Project/MenuItems.xml";
        SaxParserRestaurants.parseXML(restaurantXML);
        SaxParserMenuItems.parseXML(menuItemsXML);
    }

}