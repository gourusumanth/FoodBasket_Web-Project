import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

public class MySqlInit {

  // Change Following according to your system
  private static String dbUsername = "root";
  private static String dbPassword = "";
  private static String dbName = "food_basket";
  private static String port = "3306";

  private static String dbUrl = "jdbc:mysql://localhost:";
  private static String useSSL = "useSSL=false";

  private static String[] tables = {"restaurants","users","orders", "menu_items", "feedbacks", "comments"}; // Names of the tables

  private static Connection conn;

  static void getConnection() {
    try {
      Class.forName("com.mysql.jdbc.Driver").newInstance();

      conn = DriverManager.getConnection(dbUrl+port+"/"+dbName+"?"+useSSL, dbUsername, dbPassword);
    } catch (Exception e) {
      System.out.println(e);
    }
  }

  static void initTables() {
    String createTableRestaurants = "CREATE TABLE "+tables[0]+"("+
                                          "id varchar(40) NOT NULL," +
                                          "name varchar(255) NOT NULL," +
                                          "tagline varchar(255) NOT NULL," +
                                          "time varchar(255) DEFAULT NULL," +
                                          "expensiveMeter varchar(50) DEFAULT NULL," +
                                          "minimumAmount float DEFAULT NULL," +
                                          "image varchar(50) DEFAULT NULL," +
                                          "deliveryFee float DEFAULT NULL," +
                                          "zipcode varchar(50) DEFAULT NULL," +
                                          "PRIMARY KEY (id));";

    String createTableUsers = "CREATE TABLE "+tables[1]+"("+
                                      "name varchar(255) NOT NULL," +
                                      "username varchar(255) NOT NULL," +
                                      "password varchar(255) DEFAULT NULL," +
                                      "role varchar(50) DEFAULT NULL," +
                                      "PRIMARY KEY (username));";


    String createTableOrders = "CREATE TABLE "+tables[2]+"("+
                                      "order_id int NOT NULL,"+
                                      "cust_user_name varchar(40) NOT NULL,"+
                                      "cust_address varchar(100) NOT NULL,"+
                                      "cust_zip_code varchar(10) NOT NULL,"+
                                      "cust_phone varchar(10) NOT NULL,"+
                                      "cust_credit_card varchar(20) NOT NULL,"+
                                      "order_items varchar(100) NOT NULL,"+
                                      "order_price double NOT NULL,"+
                                      "order_status varchar(20) NOT NULL,"+
                                      "PRIMARY KEY (order_id));";

    String createTableMenuItems = "CREATE TABLE "+tables[3]+"("+
                                      "menu_item_id varchar(40) NOT NULL," +
                                      "menu_item_name varchar(40) NOT NULL," +
                                      "menu_item_rest_id varchar(40)  NOT NULL," +
                                      "menu_item_ingredients varchar(255)," +
                                      "menu_item_image varchar(50) NOT NULL," +
                                      "menu_item_price float," +
                                      "similar_menu_items varchar(100) NOT NULL," +
                                      "menu_item_sale boolean"+
                                      "PRIMARY KEY (menu_item_id));";

  String createTableFeedback = "CREATE TABLE "+tables[4]+"("+
                                "feedback_id int AUTO_INCREMENT,"+
                                "feedback_text varchar(500) NOT NULL,"+
                                "feedback_customer_username varchar(50) NOT NULL,"+
                                "PRIMARY KEY (feedback_id))";

   String createTableComments = "CREATE TABLE "+tables[5]+"("+
                                  "comment_id int AUTO_INCREMENT,"+
                                  "comment_feedback_id int NOT NULL,"+
                                  "comment_text varchar(500) NOT NULL,"+
                                  "comment_user_name varchar(20)  NOT NULL,"+
                                  "PRIMARY KEY (comment_id))";
   try {
     PreparedStatement pst;
     pst = conn.prepareStatement(createTableRestaurants);
     pst.executeUpdate();

     pst = conn.prepareStatement(createTableUsers);
     pst.executeUpdate();

     pst = conn.prepareStatement(createTableOrders);
     pst.executeUpdate();

     pst = conn.prepareStatement(createTableMenuItems);
     pst.executeUpdate();

     pst = conn.prepareStatement(createTableFeedback);
     pst.executeUpdate();

     pst = conn.prepareStatement(createTableComments);
     pst.executeUpdate();

   } catch (Exception e) {
     System.out.println(e);
   }
  }

  static void closeConnection() {
    try {
      conn.close();
    } catch(Exception e) {
      System.out.println(e);
    }
  }

  public static void main(String args[]) {
    getConnection();
    initTables();
    closeConnection();
  }
}
