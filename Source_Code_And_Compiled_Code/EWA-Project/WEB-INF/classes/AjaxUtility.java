import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

import java.sql.Connection;

public class AjaxUtility {

    public static HashMap<String, Restaurant> getRestaurantData() {
        HashMap<String, Restaurant> restaurantIdRestaurantMap = new HashMap<>();
        try {
            Connection connection = MySQLDataStoreUtilities.conn;
            Statement statement = connection.createStatement();
            String selectRestaurants = "select * from restaurants";
            ResultSet rs = statement.executeQuery(selectRestaurants);
            while (rs.next()) {
                Restaurant restaurant = new Restaurant();
                restaurant.setId(rs.getString("id"));
                restaurant.setName(rs.getString("name"));
                restaurantIdRestaurantMap.put(restaurant.getId(), restaurant);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return restaurantIdRestaurantMap;
    }

    public static StringBuffer readRestaurantData(String restaurantSearchId) {
        StringBuffer sb = new StringBuffer();
        if (!restaurantSearchId.isEmpty()) {
            HashMap<String, Restaurant> restaurantIdRestaurantMap = getRestaurantData();
            for (Map.Entry<String, Restaurant> entry : restaurantIdRestaurantMap.entrySet()) {
                Restaurant restaurant = entry.getValue();
                if (restaurant.getName().toLowerCase().startsWith(restaurantSearchId.toLowerCase())) {
                    sb.append("<restaurant>");
                    sb.append("<id>" + restaurant.getId() + "</id>");
                    sb.append("<name>" + restaurant.getName() + "</name>");
                    sb.append("</restaurant>");
                }
            }
        }
        return sb;
    }

}
