import com.mongodb.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MongoDBDataStoreUtilities {
    private static DBCollection restaurantReviews;

    public static void getConnection() throws Exception {
        MongoClient mongo;
        mongo = new MongoClient("localhost", 27017);
        DB db = mongo.getDB("food_basket");
        restaurantReviews = db.getCollection("restaurantReviews");
    }

    public static void insertRestaurantReview(String restaurantId, String reviewSummary, String userId, String reviewRating, String reviewDate, String reviewText, String zipcode)
    {
        try{
            getConnection();
            BasicDBObject doc = new BasicDBObject("title", "restaurantReviews")
                    .append("restaurantId", restaurantId)
                    .append("reviewSummary", reviewSummary)
                    .append("userId", userId)
                    .append("reviewRating", Double.parseDouble(reviewRating))
                    .append("reviewDate", reviewDate)
                    .append("reviewText", reviewText)
                    .append("zipcode", zipcode);
            restaurantReviews.insert(doc);
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public static List<RestaurantReviews> readRestaurantReview(String restaurantId){
        Map<String, List<RestaurantReviews>> restaurantAndReviewMap = getAllRestaurantAndReviewsMap();
        List<RestaurantReviews> restaurantReviewList = restaurantAndReviewMap.get(restaurantId);
        return restaurantReviewList;
    }

    public static Map<String, List<RestaurantReviews>> getAllRestaurantAndReviewsMap(){
        Map<String, List<RestaurantReviews>> restaurantReviewMap = new HashMap<String, List<RestaurantReviews>>();
        try{
            getConnection();
            DBCursor cursor = restaurantReviews.find();
            while (cursor.hasNext()) {
                BasicDBObject obj = (BasicDBObject) cursor.next();
                if (!restaurantReviewMap.containsKey(obj.getString("restaurantId"))) {
                    List<RestaurantReviews> restaurantReviewList = new ArrayList<RestaurantReviews>();
                    restaurantReviewMap.put(obj.getString("restaurantId"), restaurantReviewList);
                }
                List<RestaurantReviews> listReview = restaurantReviewMap.get(obj.getString("restaurantId"));
                listReview.add(populateDbObjectToRestaurantReview(obj));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return restaurantReviewMap;
    }

    public static RestaurantReviews populateDbObjectToRestaurantReview(BasicDBObject obj) {
        RestaurantReviews restaurantReview = new RestaurantReviews();
        restaurantReview.setRestaurantId(obj.getString("restaurantId"));
        restaurantReview.setUserId(obj.getString("userId"));
        restaurantReview.setReviewSummary(obj.getString("reviewSummary"));
        restaurantReview.setReviewRating(Double.parseDouble(obj.getString("reviewRating")));
        restaurantReview.setReviewDate(obj.getString("reviewDate"));
        restaurantReview.setReviewText(obj.getString("reviewText"));
        return restaurantReview;
    }

    public static List<Trending> getTopFiveLikedRestaurants() {
        List<Trending> trendingRestaurantList = new ArrayList<Trending>();
        try {
            getConnection();
            DBObject groupFields= new BasicDBObject("_id", 0);
            groupFields.put("_id", "$restaurantId");
            groupFields.put("rating", new BasicDBObject("$avg", "$reviewRating"));
            DBObject group = new BasicDBObject("$group", groupFields);
            DBObject projectFields = new BasicDBObject();
            projectFields.put("value", "$_id");
            projectFields.put("rating","$rating");
            DBObject project = new BasicDBObject("$project", projectFields);

            DBObject sort = new BasicDBObject();
            sort.put("rating",-1);
            DBObject orderby=new BasicDBObject("$sort",sort);

            DBObject limit=new BasicDBObject("$limit",5);
            AggregationOutput aggregate = restaurantReviews.aggregate(group,orderby,limit);

            for(DBObject obj : aggregate.results()){
                Trending trending = new Trending();
                trending.setRestaurantId(obj.get("_id").toString());
                trending.setReviewRating(Double.parseDouble(obj.get("rating").toString()));
                trendingRestaurantList.add(trending);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return trendingRestaurantList;
    }

    public static List<Trending> getTopSoldRestaurants() {

        List<Trending> trendingProductList = new ArrayList<Trending>();
        try{
            getConnection();
            DBObject groupFields= new BasicDBObject("_id", 0);
            groupFields.put("_id", "$restaurantId");
            groupFields.put("count", new BasicDBObject("$sum", 1));
            DBObject group = new BasicDBObject("$group", groupFields);
            DBObject projectFields = new BasicDBObject();
            projectFields.put("value", "$_id");
            projectFields.put("ReviewValue","$count");
            DBObject project = new BasicDBObject("$project", projectFields);
            DBObject sort = new BasicDBObject();
            sort.put("ReviewValue",-1);
            DBObject orderby=new BasicDBObject("$sort",sort);
            DBObject limit=new BasicDBObject("$limit",5);
            AggregationOutput aggregate = restaurantReviews.aggregate(group,project,orderby,limit);
            for(DBObject obj : aggregate.results()){
                Trending trending = new Trending();
                trending.setRestaurantId(obj.get("value").toString());
                trending.setReviewCount(obj.get("ReviewValue").toString());
                trendingProductList.add(trending);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return trendingProductList;
    }

    public static List<Trending> getMostReviewsInZipCode() {
        List<Trending> trendingProductList = new ArrayList<Trending>();
        try {
            getConnection();
            DBObject groupFields= new BasicDBObject("_id", 0);
            groupFields.put("_id", "$zipcode");
            groupFields.put("count",new BasicDBObject("$sum",1));
            DBObject group = new BasicDBObject("$group", groupFields);

            DBObject sort = new BasicDBObject();
            sort.put("ReviewCount",-1);

            DBObject projectFields = new BasicDBObject();
            projectFields.put("value", "$_id");
            projectFields.put("ReviewCount","$count");
            DBObject project = new BasicDBObject("$project", projectFields);

            DBObject orderby=new BasicDBObject("$sort",sort);
            DBObject limit=new BasicDBObject("$limit",5);
            AggregationOutput aggregate = restaurantReviews.aggregate(group,project,orderby,limit);
            for(DBObject obj : aggregate.results()){
                Trending trending = new Trending();
                trending.setZipcode(obj.get("value").toString());
                trending.setReviewCount(obj.get("ReviewCount").toString());
                trendingProductList.add(trending);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return trendingProductList;
    }

}