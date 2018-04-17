import java.util.ArrayList;

public class MenuItems {

    private String id;

    private String restaurantId;

    private String name;

    private String ingredients;

    private String image;

    private float price;

    private ArrayList<String> similarMenuItems;

    private int availability;

    private boolean sale;

    public void setSale(boolean sale) {
      this.sale = sale;
    }

    public boolean getSale() {
      return sale;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(String restaurantId) {
        this.restaurantId = restaurantId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIngredients() {
        return ingredients;
    }

    public void setIngredients(String ingredients) {
        this.ingredients = ingredients;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public ArrayList<String> getSimilarMenuItems() {
        return similarMenuItems;
    }

    public void setSimilarMenuItems(ArrayList<String> similarMenuItems) {
        this.similarMenuItems = similarMenuItems;
    }

    public void setAvailability(int availability) {
      this.availability = availability;
    }

    public int getAvailability() {
      return availability;
    }
}
