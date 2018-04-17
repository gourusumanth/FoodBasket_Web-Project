# EWA-Project Architecture
EWA Project: Online Food Ordering
FOOD BASKET

## Database Architecture

Steps to replicate following database on your local system:
* Create a database with name of your choice or `food_basket`
* Edit `MySqlInit.java` file inside `WEB-INF\classes\` and add the following to it:
	* `private static String dbUsername = "<db_username>";`
	* `private static String dbPassword = "<db_password>";`
  	* `private static String dbName = "<db_name>";`
  	* `private static String port = "<port_number>";`

### Users Table
* Table Name: `users`

|Field| Type|
|-----------|-------------|
|user_id			|int(11)|
|user_name			|varchar(40)|
|user_address		|varchar(100)|
|user_zip_code		|varchar(10)|
|user_phone			|varchar(10)|
|user_type			|varchar(10)|

### Restaurants table
* Table Name: `restaurants`

|Field| Type|
|-----------|-------------|
|rest_id			|int(11)|
|rest_name			|varchar(40)|
|rest_address		|varchar(100)|
|rest_zip_code		|varchar(10)|
|rest_phone			|varchar(10)|

### Orders table
* Table Name: `orders`

|Field| Type|
|-----------|-------------|
|order_id			|int(11)|
|cust_user_name		|varchar(40)|
|cust_address		|varchar(100)|
|cust_zip_code		|varchar(10)|
|cust_phone			|varchar(10)|
|cust_credit_card	|varchar(20)|
|order_items		|varchar(100)|
|order_price		|double|
|order_status		|varchar(20)|

### Menu Items table
* Table Name: `menu_items`

|Field| Type|
|-----------|-------------|
|menu_items_id			|int(11)|
|menu_items_name		|varchar(40)|
|menu_items_image		|varchar(50)|
|menu_items_rest_id		|int(10)|
|menu_items_ingredients	|varchar(100)|
|menu_items_price		|double|
