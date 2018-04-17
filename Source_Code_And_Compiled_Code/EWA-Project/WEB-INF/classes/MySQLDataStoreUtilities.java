import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.sql.*;
import java.util.*;
import java.util.Date;
import java.io.*;

public class MySQLDataStoreUtilities {

    public static Connection conn = null;

    private static String port = "3306";
    private static String dbUrl = "jdbc:mysql://localhost:";
    private static String dbUsername = "root";
    private static String dbPassword = "";
    private static String dbName = "food_basket";
    private static String useSSL = "useSSL=false";

    static{
        try{
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            // conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/ewa_project","root","");
            conn = DriverManager.getConnection(dbUrl+port+"/"+dbName+"?"+useSSL, dbUsername, dbPassword);
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    private static Map<String, MenuItems> allMenuItems;

    public static void insertUser(String name,String username,String password,String role){
        try{
            String insertIntoCustomerRegisterQuery = "INSERT INTO users(name,username,password,role) VALUES (?,?,?,?);";
            PreparedStatement pst = conn.prepareStatement(insertIntoCustomerRegisterQuery);
            pst.setString(1,name);
            pst.setString(2,username);
            pst.setString(3,password);
            pst.setString(4,role);
            pst.execute();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public static String getRestaurantId(String name) {
      String restaurantId = "";

      try {
        String getRestId = "select * from restaurants where name = '"+name+"';";
        PreparedStatement pst = conn.prepareStatement(getRestId);
        ResultSet rs = pst.executeQuery();
        while (rs.next()) {
            restaurantId = rs.getString("id");
        }

      } catch (Exception e) {
        System.out.println(e);
      }
      return restaurantId;
    }

    public static void insertFoodVendor(String username,String password,String restaurantname){
        try{
            String rId = getRestaurantId(restaurantname);
            String insertIntoFoodVendorQuery = "INSERT INTO restaurant_vendors(vendor_username,vendor_password,vendor_restaurant_id) VALUES (?,?,?);";
            PreparedStatement pst = conn.prepareStatement(insertIntoFoodVendorQuery);
            pst.setString(1,username);
            pst.setString(2,password);
            pst.setString(3,rId);
            pst.execute();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public static String getVendor(String username) {
      String restaurant = "";

      try {
        String getVendor = "select * from restaurant_vendors where vendor_username = '"+username+"';";
        PreparedStatement pst = conn.prepareStatement(getVendor);
        ResultSet rs = pst.executeQuery();
        while (rs.next()) {
            restaurant = rs.getString("vendor_restaurant_id");
        }

      } catch (Exception e) {
        System.out.println(e);
      }
      return restaurant;

    }

    public static boolean isValidUser(String username, String password,String role) {
        boolean isValidUser = false;
        try {
            String selectUserQuery = "Select count(username) from users where username=? and password=? and role=?";
            PreparedStatement pst = conn.prepareStatement(selectUserQuery);
            pst.setString(1, username);
            pst.setString(2, password);
            pst.setString(3, role);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                int numberOfRows = rs.getInt(1);
                if (numberOfRows > 0) {
                    isValidUser = true;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return isValidUser;
    }

    public static void clearRestaurants(){
        try{
            String dropProductQuery = "DELETE FROM restaurants";
            PreparedStatement pst = conn.prepareStatement(dropProductQuery);
            pst.execute();
        }catch(SQLException e){
            e.printStackTrace();
        }

    }

    public static void saveRestaurants(Map<String,Restaurant> restaurantMap){
        for(Map.Entry<String,Restaurant> entry: restaurantMap.entrySet()){
            saveRestaurant(entry.getValue());
        }
    }

    private static void saveRestaurant(Restaurant restaurant) {
        try{
            String insertIntoRestaurant = "INSERT INTO restaurants(id,name,tagline,time,expensiveMeter,minimumAmount,image,deliveryFee,zipcode) VALUES (?,?,?,?,?,?,?,?,?);";
            PreparedStatement pst = conn.prepareStatement(insertIntoRestaurant);
            pst.setString(1,restaurant.getId());
            pst.setString(2,restaurant.getName());
            pst.setString(3,restaurant.getTagLine());
            pst.setString(4,restaurant.getTime());
            pst.setString(5,restaurant.getExpensiveMeter());
            pst.setFloat(6,restaurant.getMinimumAmount());
            pst.setString(7,restaurant.getImage());
            pst.setFloat(8,restaurant.getDeliverFee());
            pst.setString(9,restaurant.getZipCode());
            pst.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static List<Restaurant> getRestaurant(String zipcode) {
        List<Restaurant> result = new ArrayList<>();
        if(zipcode.length()>2) {
            String match = zipcode.substring(0, zipcode.length() - 2);
            try {
                String selectRestaurant = "SELECT * FROM restaurants where zipcode LIKE '" + match + "%'";
                PreparedStatement pst = conn.prepareStatement(selectRestaurant);
                ResultSet rs = pst.executeQuery();
                while (rs.next()) {
                    Restaurant restaurant = new Restaurant();
                    restaurant.setName(rs.getString("name"));
                    restaurant.setId(rs.getString("id"));
                    restaurant.setTagLine(rs.getString("tagline"));
                    restaurant.setTime(rs.getString("time"));
                    restaurant.setExpensiveMeter(rs.getString("expensiveMeter"));
                    restaurant.setMinimumAmount(rs.getFloat("minimumAmount"));
                    restaurant.setImage(rs.getString("image"));
                    restaurant.setDeliverFee(rs.getFloat("deliveryFee"));
                    restaurant.setZipCode(rs.getString("zipcode"));
                    int zipCodeLength = restaurant.getZipCode().length();
                    if (zipCodeLength > 2 &&
                            restaurant.getZipCode().substring(0, zipCodeLength - 2).equals(match)) {
                        result.add(restaurant);
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    public static List<Restaurant> getAllRestaurants() {
        List<Restaurant> result = new ArrayList<>();
            try {
                String selectRestaurant = "SELECT * FROM restaurants";
                PreparedStatement pst = conn.prepareStatement(selectRestaurant);
                ResultSet rs = pst.executeQuery();
                while (rs.next()) {
                    Restaurant restaurant = new Restaurant();
                    restaurant.setName(rs.getString("name"));
                    restaurant.setId(rs.getString("id"));
                    restaurant.setTagLine(rs.getString("tagline"));
                    restaurant.setTime(rs.getString("time"));
                    restaurant.setExpensiveMeter(rs.getString("expensiveMeter"));
                    restaurant.setMinimumAmount(rs.getFloat("minimumAmount"));
                    restaurant.setImage(rs.getString("image"));
                    restaurant.setDeliverFee(rs.getFloat("deliveryFee"));
                    restaurant.setZipCode(rs.getString("zipcode"));
                    int zipCodeLength = restaurant.getZipCode().length();
                    result.add(restaurant);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        return result;
    }


    public static void clearMenuItems() {
        try{
            String dropProductQuery = "DELETE FROM menu_items";
            PreparedStatement pst = conn.prepareStatement(dropProductQuery);
            pst.execute();
        }catch(SQLException e){
            e.printStackTrace();
        }
    }


    public static void saveMenuItems(List<MenuItems> menuItemsList) {
        for(int i = 0 ;i < menuItemsList.size(); i++){
            saveMenuItem(menuItemsList.get(i));
        }
    }

    public static int getNewMenuItemId() {
      int id = 0;
      try {
        String getId = "SELECT MAX(menu_item_id) from menu_items;";
        PreparedStatement pst = conn.prepareStatement(getId);
        ResultSet rs = pst.executeQuery();
        while (rs.next()) {
            id = Integer.parseInt(rs.getString("MAX(menu_item_id)"));
        }

      } catch (Exception e) {
        System.out.println(e);
      }
      int newId = id +1;
      return newId;
    }

    private static void saveMenuItem(MenuItems menuItem) {
        try{
            String insertIntoRestaurant = "INSERT INTO menu_items(menu_item_id,menu_item_name,menu_item_rest_id,menu_item_ingredients,menu_item_image,menu_item_price,similar_menu_items,menu_item_sale, menu_item_quantity) VALUES (?,?,?,?,?,?,?,?,?);";
            PreparedStatement pst = conn.prepareStatement(insertIntoRestaurant);
            pst.setString(1,menuItem.getId());
            pst.setString(2,menuItem.getName());
            pst.setString(3,menuItem.getRestaurantId());
            pst.setString(4,menuItem.getIngredients());
            pst.setString(5,menuItem.getImage());
            pst.setFloat(6,menuItem.getPrice());
            String similarItems="";
            for(int i=0;i<menuItem.getSimilarMenuItems().size();i++){
                similarItems=similarItems.concat(String.valueOf(menuItem.getSimilarMenuItems().get(i)));
                if(i!=menuItem.getSimilarMenuItems().size()-1){
                    similarItems=similarItems.concat(" ");
                }
            }
            pst.setString(7,similarItems);
            pst.setBoolean(8, menuItem.getSale());
            pst.setInt(9, menuItem.getAvailability());
            pst.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void updateMenuItem(MenuItems menuItem) {
        try{
            String updateMenu = "UPDATE menu_items set menu_item_name = ?, menu_item_ingredients = ?, menu_item_image = ?, menu_item_price = ?, menu_item_sale = ?, menu_item_quantity = ? WHERE menu_item_id = ? ; ";

            PreparedStatement pst = conn.prepareStatement(updateMenu);
            pst.setString(1,menuItem.getName());
            pst.setString(2,menuItem.getIngredients());
            pst.setString(3,menuItem.getImage());
            pst.setFloat(4,menuItem.getPrice());
            pst.setBoolean(5, menuItem.getSale());
            pst.setInt(6, menuItem.getAvailability());
            pst.setString(7,menuItem.getId());
            pst.executeUpdate();
        }
        catch (SQLException e) {

            e.printStackTrace();
        }
    }

    public static List<MenuItems> getRestaurantMenu(String restaurantId) {
        List<MenuItems> resultList = new ArrayList<>();
        try {
            String selectRestaurant = "SELECT * FROM menu_items where menu_item_rest_id ="+restaurantId;
            PreparedStatement pst = conn.prepareStatement(selectRestaurant);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {

                MenuItems menuItem = new MenuItems();
                menuItem.setName(rs.getString("menu_item_name"));
                menuItem.setId(rs.getString("menu_item_id"));
                menuItem.setRestaurantId(rs.getString("menu_item_rest_id"));
                menuItem.setIngredients(rs.getString("menu_item_ingredients"));
                menuItem.setPrice(rs.getFloat("menu_item_price"));
                menuItem.setImage(rs.getString("menu_item_image"));
                menuItem.setSale(rs.getBoolean("menu_item_sale"));
                ArrayList<String> similarItems = new ArrayList<String>(Arrays.asList(rs.getString("similar_menu_items").split(" ")));
                menuItem.setSimilarMenuItems(similarItems);
                resultList.add(menuItem);

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resultList;
    }

    public static Restaurant getRestaurantById(String restaurantId) {
        try {
            String selectRestaurant = "SELECT * FROM restaurants where id="+ restaurantId ;
            PreparedStatement pst = conn.prepareStatement(selectRestaurant);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                Restaurant restaurant = new Restaurant();
                restaurant.setName(rs.getString("name"));
                restaurant.setId(rs.getString("id"));
                restaurant.setTagLine(rs.getString("tagline"));
                restaurant.setTime(rs.getString("time"));
                restaurant.setExpensiveMeter(rs.getString("expensiveMeter"));
                restaurant.setMinimumAmount(rs.getFloat("minimumAmount"));
                restaurant.setImage(rs.getString("image"));
                restaurant.setDeliverFee(rs.getFloat("deliveryFee"));
                restaurant.setZipCode(rs.getString("zipcode"));
                return restaurant;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static MenuItems getMenuItemById(String menuitemId) {
        try {
            String selectMenuItem = "SELECT * FROM menu_items where menu_item_id ="+menuitemId;
            PreparedStatement pst = conn.prepareStatement(selectMenuItem);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                MenuItems menuItem = new MenuItems();
                menuItem.setName(rs.getString("menu_item_name"));
                menuItem.setId(rs.getString("menu_item_id"));
                menuItem.setRestaurantId(rs.getString("menu_item_rest_id"));
                menuItem.setIngredients(rs.getString("menu_item_ingredients"));
                menuItem.setPrice(rs.getFloat("menu_item_price"));
                menuItem.setImage(rs.getString("menu_item_image"));
                menuItem.setSale(rs.getBoolean("menu_item_sale"));
                menuItem.setAvailability(rs.getInt("menu_item_quantity"));
                ArrayList<String> similarItems = new ArrayList<String>(Arrays.asList(rs.getString("similar_menu_items").split(" ")));
                menuItem.setSimilarMenuItems(similarItems);
                return menuItem;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static List<Order> insertOrder(HttpServletRequest request,boolean discount) {
        HttpSession session = request.getSession(true);
        List<Order> orderList = new ArrayList<Order>();
        LinkedHashMap<String,Integer> cartItems = (LinkedHashMap<String,Integer>) session.getAttribute("cartItems");
        String orderId = "OD"+Utilities.generateRandomNumber();
        Date orderDate = new Date();
        for(Map.Entry<String,Integer> entry : cartItems.entrySet()) {
            try {
                MenuItems menuItem = MySQLDataStoreUtilities.getMenuItemById(entry.getKey());
                String insertIntoRestaurant = "INSERT INTO orders(order_id,cust_user_name,cust_address_line1,cust_address_line2,cust_address_city,cust_address_state,cust_address_country" +
                        ",cust_zip_code,order_delivery_date,order_status,order_date,order_item_price,order_item_quantity,order_item_id,order_item_total_price,restaurantid,isDiscountApplied) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?);";
                PreparedStatement pst = conn.prepareStatement(insertIntoRestaurant);
                pst.setString(1, orderId);
                Order order = new Order();
                order.setOrderId(orderId);
                pst.setString(2, (String) session.getAttribute("username"));
                order.setUsername((String) session.getAttribute("username"));
                pst.setString(3, (String) session.getAttribute("addressline1"));
                order.setAddressLine1((String) session.getAttribute("addressline1"));
                pst.setString(4, (String) session.getAttribute("addressline2"));
                order.setAddressLine2((String) session.getAttribute("addressline2"));
                pst.setString(5, (String) session.getAttribute("city"));
                order.setCity((String) session.getAttribute("city"));
                pst.setString(6, (String) session.getAttribute("state"));
                order.setState((String) session.getAttribute("state"));
                pst.setString(7, (String) session.getAttribute("country"));
                order.setCountry((String) session.getAttribute("country"));
                pst.setString(8, (String) session.getAttribute("zipcode"));
                order.setZipcode((String) session.getAttribute("zipcode"));
                /*pst.setString(9, Utilities.addHours(orderDate,2));
                order.setDeliveryDate(Utilities.addHours(orderDate,2));*/
                pst.setString(9, Utilities.addMinutes(orderDate,30));
                order.setDeliveryDate(Utilities.addMinutes(orderDate,30));
                pst.setString(10, "Accepted");
                order.setStatus("Accepted");
                pst.setString(11, Utilities.addMinutes(orderDate,0));
                order.setOrderDate(Utilities.addMinutes(orderDate,0));
                pst.setFloat(12, menuItem.getPrice());
                order.setItemPrice(menuItem.getPrice());
                pst.setInt(13, entry.getValue());
                order.setItemQuantity(entry.getValue());
                pst.setString(14, menuItem.getId());
                order.setItemId(menuItem.getId());
                pst.setFloat(15, entry.getValue()*menuItem.getPrice());
                order.setItemTotalPrice(entry.getValue()*menuItem.getPrice());
                pst.setString(16, (String) session.getAttribute("restaurantId"));
                order.setRestaurantId((String) session.getAttribute("restaurantId"));
                pst.setBoolean(17,discount);
                pst.execute();
                orderList.add(order);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        session.setAttribute("cartItems",new LinkedHashMap<String,Integer>());
        session.removeAttribute("addressline1");
        session.removeAttribute("addressline2");
        session.removeAttribute("city");
        session.removeAttribute("state");
        session.removeAttribute("country");
        session.removeAttribute("zipcode");
        return orderList;
    }

    public static Order getOrderById(String id) {
        Order order = new Order();
        try {
            String selectRestaurant = "SELECT * FROM orders where order_id=?";
            PreparedStatement pst = conn.prepareStatement(selectRestaurant);
            pst.setString(1,id);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                order.setOrderId(rs.getString("order_id"));
                order.setUsername(rs.getString("cust_user_name"));
                order.setAddressLine1(rs.getString("cust_address_line1"));
                order.setAddressLine2(rs.getString("cust_address_line2"));
                order.setCity(rs.getString("cust_address_city"));
                order.setState(rs.getString("cust_address_state"));
                order.setCountry(rs.getString("cust_address_country"));
                order.setZipcode(rs.getString("cust_zip_code"));
                order.setDeliveryDate(rs.getString("order_delivery_date"));
                order.setStatus(rs.getString("order_status"));
                order.setOrderDate(rs.getString("order_date"));
                order.setItemPrice(rs.getFloat("order_item_price"));
                order.setItemQuantity(rs.getInt("order_item_quantity"));
                order.setItemId(rs.getString("order_item_id"));
                order.setItemTotalPrice(rs.getFloat("order_item_total_price"));
                order.setRestaurantId(rs.getString("restaurantId"));
                order.setDiscountApplied(rs.getBoolean("isDiscountApplied"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return order;
    }
    public static LinkedHashMap<String,ArrayList<Order>> getAllOrders(){
        LinkedHashMap<String,ArrayList<Order>> resultMap = new LinkedHashMap<String,ArrayList<Order>>();
        try {
            String selectRestaurant = "SELECT * FROM orders order by order_date DESC ";
            PreparedStatement pst = conn.prepareStatement(selectRestaurant);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                Order order = new Order();
                order.setOrderId(rs.getString("order_id"));
                order.setAddressLine1(rs.getString("cust_address_line1"));
                order.setAddressLine2(rs.getString("cust_address_line2"));
                order.setCity(rs.getString("cust_address_city"));
                order.setState(rs.getString("cust_address_state"));
                order.setCountry(rs.getString("cust_address_country"));
                order.setZipcode(rs.getString("cust_zip_code"));
                order.setDeliveryDate(rs.getString("order_delivery_date"));
                order.setStatus(rs.getString("order_status"));
                order.setOrderDate(rs.getString("order_date"));
                order.setItemPrice(rs.getFloat("order_item_price"));
                order.setItemQuantity(rs.getInt("order_item_quantity"));
                order.setItemId(rs.getString("order_item_id"));
                order.setItemTotalPrice(rs.getFloat("order_item_total_price"));
                order.setRestaurantId(rs.getString("restaurantId"));
                order.setDiscountApplied(rs.getBoolean("isDiscountApplied"));
                if(resultMap.containsKey(order.getOrderId())){
                    ArrayList<Order> orderList = resultMap.get(order.getOrderId());
                    orderList.add(order);
                }else{
                    ArrayList<Order> orderList = new ArrayList<Order>();
                    orderList.add(order);
                    resultMap.put(order.getOrderId(),orderList);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resultMap;
    }

    public static LinkedHashMap<String,ArrayList<Order>> getOrders(String username){
        LinkedHashMap<String,ArrayList<Order>> resultMap = new LinkedHashMap<String,ArrayList<Order>>();
        try {
            String selectRestaurant = "SELECT * FROM orders where cust_user_name =? order by order_date DESC ";
            PreparedStatement pst = conn.prepareStatement(selectRestaurant);
            pst.setString(1,username);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                Order order = new Order();
                order.setOrderId(rs.getString("order_id"));
                order.setUsername(username);
                order.setAddressLine1(rs.getString("cust_address_line1"));
                order.setAddressLine2(rs.getString("cust_address_line2"));
                order.setCity(rs.getString("cust_address_city"));
                order.setState(rs.getString("cust_address_state"));
                order.setCountry(rs.getString("cust_address_country"));
                order.setZipcode(rs.getString("cust_zip_code"));
                order.setDeliveryDate(rs.getString("order_delivery_date"));
                order.setStatus(rs.getString("order_status"));
                order.setOrderDate(rs.getString("order_date"));
                order.setItemPrice(rs.getFloat("order_item_price"));
                order.setItemQuantity(rs.getInt("order_item_quantity"));
                order.setItemId(rs.getString("order_item_id"));
                order.setItemTotalPrice(rs.getFloat("order_item_total_price"));
                order.setRestaurantId(rs.getString("restaurantId"));
                order.setDiscountApplied(rs.getBoolean("isDiscountApplied"));
                if(resultMap.containsKey(order.getOrderId())){
                    ArrayList<Order> orderList = resultMap.get(order.getOrderId());
                    orderList.add(order);
                }else{
                    ArrayList<Order> orderList = new ArrayList<Order>();
                    orderList.add(order);
                    resultMap.put(order.getOrderId(),orderList);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resultMap;
    }

    public static void deleteOrder(String deleteOrderId) {
        try {
            String deleteOrder = "DELETE FROM orders where order_id = ?";
            PreparedStatement pst = conn.prepareStatement(deleteOrder);
            pst.setString(1,deleteOrderId);
            pst.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static LinkedHashMap<Integer, Feedback> getFeedbacks(){
        LinkedHashMap<Integer, Feedback> allFeedbacks = new LinkedHashMap<Integer, Feedback>();
        try {
            String selectFeedbacks = "SELECT * FROM feedbacks";
            PreparedStatement pst = conn.prepareStatement(selectFeedbacks);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                Feedback feedback = new Feedback();
                feedback.setFeedbackId(rs.getInt("feedback_id"));
                feedback.setFeedbackText(rs.getString("feedback_text"));
                feedback.setFeedbackUserName(rs.getString("feedback_customer_username"));
                allFeedbacks.put(new Integer(rs.getInt("feedback_id")), feedback);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return allFeedbacks;
    }

    public static void insertFeedback(String feedbackText, String customerUsername){
        try {
            String insertFeedback = "INSERT INTO feedbacks(feedback_text, feedback_customer_username) VALUES (?,?)";
            PreparedStatement pst = conn.prepareStatement(insertFeedback);
            pst.setString(1,feedbackText);
            pst.setString(2,customerUsername);
            pst.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void insertComment(int commentFeedbackId, String commentText, String customerUsername){
        try {
            String insertFeedback = "INSERT INTO comments(comment_feedback_id, comment_text, comment_user_name) VALUES (?,?,?)";
            PreparedStatement pst = conn.prepareStatement(insertFeedback);
            pst.setInt(1,commentFeedbackId);
            pst.setString(2,commentText);
            pst.setString(3,customerUsername);
            pst.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static ArrayList<Comment> getComments(int feedbackId){
        ArrayList<Comment> comments = new ArrayList<Comment>();

        try {
            String selectFeedbacks = "SELECT * FROM comments where comment_feedback_id="+feedbackId+";";
            PreparedStatement pst = conn.prepareStatement(selectFeedbacks);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
              Comment cm = new Comment();
              cm.setCommentId(rs.getInt("comment_id"));
              cm.setCommentFeedbackId(rs.getInt("comment_feedback_id"));
              cm.setCommentText(rs.getString("comment_text"));
              cm.setCommentUserName(rs.getString("comment_user_name"));
              comments.add(cm);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return comments;
    }

    public static String[][] salesByDate() {
      String[][] salesByDate = null;
      try {
        ArrayList<String> allDates = new ArrayList<String>();

        String getSalesByDate = "select order_date from orders order by order_date DESC;";
        PreparedStatement pst = conn.prepareStatement(getSalesByDate);
        ResultSet rs = pst.executeQuery();
        while(rs.next()) {
          allDates.add(rs.getString("order_date").split("T")[0]);
        }

        HashMap<String, Integer> uniqueDates = new HashMap<String, Integer>();
        for (String date: allDates) {
          if(uniqueDates.containsKey(date)) {
            uniqueDates.put(date, uniqueDates.get(date) + 1);
          } else {
            uniqueDates.put(date, 1);
          }
        }

        salesByDate = new String[uniqueDates.size()][2];
        int i = 0;
        for(Map.Entry<String, Integer> entry : uniqueDates.entrySet()) {
          salesByDate[i][0] = entry.getKey();
          salesByDate[i][1] = entry.getValue()+"";
          i++;
        }
      } catch (Exception e) {
        System.out.println(e);
      }
      return salesByDate;
    }

    public static String[][] salesEveryProduct() {
      String[][] salesEachProduct = null;
      try {
        String getSalesByDate = "select * from orders;";
        PreparedStatement pst = conn.prepareStatement(getSalesByDate);
        ResultSet rs = pst.executeQuery();
        HashMap<String, Integer> prd = new HashMap<String, Integer>();

        while (rs.next()) {
          String itemId = rs.getString("order_item_id");
            if (itemId != null && !itemId.isEmpty()) {
              if (prd.containsKey(itemId)) {
                prd.put(itemId, prd.get(itemId) +1);
              } else {
                prd.put(itemId, 1);
              }
          }
        }
        String getAllProducts = "select * from menu_items;";
        pst = conn.prepareStatement(getAllProducts);
        ResultSet newRs = pst.executeQuery();
        while (newRs.next()) {
          if (!prd.containsKey(newRs.getString("menu_item_id"))) {
            prd.put(newRs.getString("menu_item_id"), 0);
          }
        }

        salesEachProduct = new String[prd.size()][2];
        int k =0;
        for (Map.Entry<String, Integer> entry : prd.entrySet()) {
          String pname = getMenuItemName(entry.getKey());
          salesEachProduct[k][0] = pname;
          salesEachProduct[k][1] = entry.getValue()+"";
          k++;
        }
      } catch (Exception e) {
        System.out.println(e);
      }
      return salesEachProduct;
    }

  public static String[][] salesDetailsEachProduct() {
    String[][] salesEveryProduct = salesEveryProduct();
    String[][] salesDetailsEachProduct = new String[salesEveryProduct.length][4]; //[0][0] name, [0][1] price, [0][2] sales, [0][3] total sales
    for (int i =0; i< salesEveryProduct.length; i++) {
      salesDetailsEachProduct[i][0] =  salesEveryProduct[i][0];
      salesDetailsEachProduct[i][1] = getMenuItemPrice(salesEveryProduct[i][0])+"";
      salesDetailsEachProduct[i][2] = salesEveryProduct[i][1];
      salesDetailsEachProduct[i][3] = (Double.parseDouble(salesDetailsEachProduct[i][1]) *  Double.parseDouble(salesEveryProduct[i][1])) +"";
    }
    return salesDetailsEachProduct;
  }

  public static String getMenuItemName(String pid) {
    String pname = "";
    try {
      String getSalesByDate = "select * from menu_items where menu_item_id='"+pid+"';";
      PreparedStatement pst = conn.prepareStatement(getSalesByDate);
      ResultSet rs = pst.executeQuery();

      while (rs.next()) {
        pname = rs.getString("menu_item_name");
      }

    } catch (Exception e) {
      System.out.println(e);
    }
    return pname;
  }
  public static double getMenuItemPrice(String menu_name) {
    float price = 0f;
    try {
      String getSalesByDate = "select * from menu_items where menu_item_name='"+menu_name+"';";
      PreparedStatement pst = conn.prepareStatement(getSalesByDate);
      ResultSet rs = pst.executeQuery();

      while (rs.next()) {
        price = rs.getFloat("menu_item_price");
      }

    } catch (Exception e) {
      System.out.println(e);
    }
    return price;
  }

  public static ArrayList<MenuItems> getMenuItemsAvailability() {
  ArrayList<MenuItems> mnt = new ArrayList<MenuItems>();
  try {
    String getAvailavle = "SELECT * FROM menu_items;";
    PreparedStatement pst = conn.prepareStatement(getAvailavle);
    ResultSet rs = pst.executeQuery();
    while (rs.next()) {
      MenuItems menuItem = new MenuItems();
      menuItem.setName(rs.getString("menu_item_name"));
      menuItem.setId(rs.getString("menu_item_id"));
      menuItem.setRestaurantId(rs.getString("menu_item_rest_id"));
      menuItem.setIngredients(rs.getString("menu_item_ingredients"));
      menuItem.setPrice(rs.getFloat("menu_item_price"));
      menuItem.setImage(rs.getString("menu_item_image"));
      menuItem.setSale(rs.getBoolean("menu_item_sale"));
      ArrayList<String> similarItems = new ArrayList<String>(Arrays.asList(rs.getString("similar_menu_items").split(" ")));
      menuItem.setSimilarMenuItems(similarItems);
      menuItem.setAvailability(rs.getInt("menu_item_quantity"));
      mnt.add(menuItem);
    }
  } catch (Exception e) {
    System.out.println(e);
  }
  return mnt;
}

    public static void updateOrderStatus(String status,String orderId) {
        String updateOrderStatus = "UPDATE orders SET order_status=? WHERE order_id=?";
        PreparedStatement pst = null;
        try {
            pst = conn.prepareStatement(updateOrderStatus);
            pst.setString(1,status);
            pst.setString(2,orderId);
            pst.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void savePaymentDetails(HttpServletRequest request) {
        Enumeration<String> parameters = request.getParameterNames();
        while(parameters.hasMoreElements()){
            String k = parameters.nextElement();

        }
        HttpSession session = request.getSession(true);
        String savePaymentDetails = "INSERT INTO payment_details VALUES (?, ?,?,?) ON DUPLICATE KEY UPDATE cardnumber=?, month=?,year=?;";
        PreparedStatement pst = null;
        try {
            pst = conn.prepareStatement(savePaymentDetails);
            pst.setString(1,(String) session.getAttribute("username"));
            pst.setInt(2,Integer.parseInt(request.getParameter("cardNumber")));
            pst.setInt(3,Integer.parseInt(request.getParameter("expiryMonth")));
            pst.setInt(4,Integer.parseInt(request.getParameter("expiryYear")));
            pst.setInt(5,Integer.parseInt(request.getParameter("cardNumber")));
            pst.setInt(6,Integer.parseInt(request.getParameter("expiryMonth")));
            pst.setInt(7,Integer.parseInt(request.getParameter("expiryYear")));
            pst.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static List<Order> getOrdersById(String id) {
        List<Order> orderList = new ArrayList<>();
        try {
            String selectRestaurant = "SELECT * FROM orders where order_id=?";
            PreparedStatement pst = conn.prepareStatement(selectRestaurant);
            pst.setString(1,id);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                Order order = new Order();
                order.setOrderId(rs.getString("order_id"));
                order.setUsername(rs.getString("cust_user_name"));
                order.setAddressLine1(rs.getString("cust_address_line1"));
                order.setAddressLine2(rs.getString("cust_address_line2"));
                order.setCity(rs.getString("cust_address_city"));
                order.setState(rs.getString("cust_address_state"));
                order.setCountry(rs.getString("cust_address_country"));
                order.setZipcode(rs.getString("cust_zip_code"));
                order.setDeliveryDate(rs.getString("order_delivery_date"));
                order.setStatus(rs.getString("order_status"));
                order.setOrderDate(rs.getString("order_date"));
                order.setItemPrice(rs.getFloat("order_item_price"));
                order.setItemQuantity(rs.getInt("order_item_quantity"));
                order.setItemId(rs.getString("order_item_id"));
                order.setItemTotalPrice(rs.getFloat("order_item_total_price"));
                order.setRestaurantId(rs.getString("restaurantid"));
                order.setDiscountApplied(rs.getBoolean("isDiscountApplied"));
                orderList.add(order);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return orderList;
    }

    public static PaymentDetails getPaymentDetails(String username) {
        PaymentDetails paymentDetails = new PaymentDetails();
        try {
            String selectPaymentDetails = "SELECT * FROM payment_details where username=?";
            PreparedStatement pst = conn.prepareStatement(selectPaymentDetails);
            pst.setString(1,username);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                paymentDetails.setRestaurantId(rs.getString("username"));
                paymentDetails.setCrediCardNumber(rs.getInt("cardnumber"));
                paymentDetails.setExpiryMonth(rs.getInt("month"));
                paymentDetails.setExpiryYear(rs.getInt("year"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return paymentDetails;
    }

    public static LinkedHashMap<String,ArrayList<Order>> getOrdersByRestaurant(String foodVendor){
        String restaurantId = getVendor(foodVendor);

        LinkedHashMap<String,ArrayList<Order>> resultMap = new LinkedHashMap<String,ArrayList<Order>>();
        try {
            String selectRestaurant = "SELECT * FROM orders where restaurantId =? order by order_date DESC ";
            PreparedStatement pst = conn.prepareStatement(selectRestaurant);
            pst.setString(1,restaurantId);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                Order order = new Order();
                order.setOrderId(rs.getString("order_id"));
                order.setUsername(rs.getString("cust_user_name"));
                order.setAddressLine1(rs.getString("cust_address_line1"));
                order.setAddressLine2(rs.getString("cust_address_line2"));
                order.setCity(rs.getString("cust_address_city"));
                order.setState(rs.getString("cust_address_state"));
                order.setCountry(rs.getString("cust_address_country"));
                order.setZipcode(rs.getString("cust_zip_code"));
                order.setDeliveryDate(rs.getString("order_delivery_date"));
                order.setStatus(rs.getString("order_status"));
                order.setOrderDate(rs.getString("order_date"));
                order.setItemPrice(rs.getFloat("order_item_price"));
                order.setItemQuantity(rs.getInt("order_item_quantity"));
                order.setItemId(rs.getString("order_item_id"));
                order.setItemTotalPrice(rs.getFloat("order_item_total_price"));
                order.setRestaurantId(rs.getString("restaurantId"));
                order.setDiscountApplied(rs.getBoolean("isDiscountApplied"));
                if(resultMap.containsKey(order.getOrderId())){
                    ArrayList<Order> orderList = resultMap.get(order.getOrderId());
                    orderList.add(order);
                }else{
                    ArrayList<Order> orderList = new ArrayList<Order>();
                    orderList.add(order);
                    resultMap.put(order.getOrderId(),orderList);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resultMap;
    }

    public static Map<String, MenuItems> getAllMenuItems() {
        Map<String, MenuItems> menuItemsMap = new HashMap<String, MenuItems>();
        try {
            String selectMenuItem = "SELECT * FROM menu_items";
            PreparedStatement pst = conn.prepareStatement(selectMenuItem);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                MenuItems menuItem = new MenuItems();
                menuItem.setName(rs.getString("menu_item_name"));
                menuItem.setId(rs.getString("menu_item_id"));
                menuItem.setRestaurantId(rs.getString("menu_item_rest_id"));
                menuItem.setIngredients(rs.getString("menu_item_ingredients"));
                menuItem.setPrice(rs.getFloat("menu_item_price"));
                menuItem.setImage(rs.getString("menu_item_image"));
                menuItem.setSale(rs.getBoolean("menu_item_sale"));
                ArrayList<String> similarItems = new ArrayList<String>(Arrays.asList(rs.getString("similar_menu_items").split(" ")));
                menuItem.setSimilarMenuItems(similarItems);
                menuItem.setAvailability(rs.getInt("menu_item_quantity"));
                menuItemsMap.put(menuItem.getId(),menuItem);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return menuItemsMap;
    }

    public static void deleteMenuItem(String menuItemId) {
        try {
            String deleteMenu = "DELETE FROM menu_items where menu_item_id = ?";
            PreparedStatement pst = conn.prepareStatement(deleteMenu);
            pst.setString(1,menuItemId);
            pst.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
