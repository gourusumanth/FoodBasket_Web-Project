
# EWA-Project
EWA Project: Online Food Ordering

# Abstract
This project aims at creating an “Online Food Ordering” platform where the customer can see food items sold by different food vendors around the location entered by the customer.
The platform has three types of login roles: Customer, Food Vendor and Admin.
Customers can place orders, track orders and do a fast checkout using pre-saved payment, shipping information and their pre-saved menu of most ordered food items.
Food vendor can add/edit/delete food items into the inventory available for customers.
Admin can create and manage Customer and Food Vendor accounts.


Database commands:
create database food_basket;
use food_basket;

CREATE TABLE users (
  name varchar(255) NOT NULL,
  username varchar(255) NOT NULL,
  password varchar(255) DEFAULT NULL,
  role varchar(50) DEFAULT NULL,
  PRIMARY KEY (username)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

INSERT INTO `users` (`name`, `username`, `password`, `role`)
VALUES
	('admin', 'admin@gmail.com', '1234', 'Admin'),
	('Customer', 'customer@gmail.com', '1234', 'Customer'),
	('vendor', 'vendor@gmail.com', '1234', 'FoodVendor');


CREATE TABLE restaurants (
  id varchar(40) NOT NULL,
  name varchar(255) NOT NULL,
  tagline varchar(255) NOT NULL,
  time varchar(255) DEFAULT NULL,
  expensiveMeter varchar(50) DEFAULT NULL,
  minimumAmount float DEFAULT NULL,
  image varchar(50) DEFAULT NULL,
  deliveryFee float DEFAULT NULL,
  zipcode varchar(50) DEFAULT NULL,
  PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE menu_items(
    menu_item_id varchar(40) NOT NULL,
    menu_item_name varchar(40) NOT NULL,
    menu_item_rest_id varchar(40)  NOT NULL,
    menu_item_ingredients varchar(255),
    menu_item_image varchar(50) NOT NULL,
    menu_item_price float,
    similar_menu_items varchar(100) NOT NULL,
    menu_item_sale boolean,
    menu_item_quantity int,
    PRIMARY KEY (menu_item_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE orders (
  id int(11) NOT NULL AUTO_INCREMENT,
  order_id varchar(255) NOT NULL,
  cust_user_name varchar(50) NOT NULL,
  cust_address_line1 varchar(255) NOT NULL,
  cust_address_line2 varchar(255) NOT NULL,
  cust_address_city varchar(50) NOT NULL,
  cust_address_state varchar(50) NOT NULL,
  cust_address_country varchar(50) NOT NULL,
  cust_zip_code varchar(10) NOT NULL,
  order_delivery_date varchar(100) NOT NULL,
  order_status varchar(50) NOT NULL,
  order_date varchar(255) NOT NULL,
  order_item_price float NOT NULL,
  order_item_quantity int NOT NULL,
  order_item_id varchar(50) NOT NULL,
  order_item_total_price float NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=24 DEFAULT CHARSET=utf8;

CREATE TABLE feedbacks (feedback_id int AUTO_INCREMENT,
              feedback_text varchar(500) NOT NULL,
              feedback_customer_username varchar(50) NOT NULL,
              PRIMARY KEY (feedback_id));

CREATE TABLE comments(comment_id int AUTO_INCREMENT,
                                  comment_feedback_id int NOT NULL,
                                  comment_text varchar(500) NOT NULL,
                                  comment_user_name varchar(20)  NOT NULL,
                                  PRIMARY KEY (comment_id));


// Mongodb

use food_basket;
db.createCollection("restaurantReviews");

//Testing git

CREATE TABLE restaurant_vendors(vendor_username varchar(100),
                                vendor_password varchar(100),
                                vendor_restaurant_id varchar(40));                                  

//Testing git

create table payment_details(username varchar(50),cardnumber int, month int, year int,primary key(cust_user_name));

alter table orders add column restaurantid varchar(50);

alter table orders add column isDiscountApplied boolean default false;