package anthony.brenon.go4lunch.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lycast on 29/01/2022.
 */

public class User {


    private String uid;
    private String username;
    private String urlPicture;
    private String restaurantChosenName;
    private String restaurantChosenId;
    private List<String> restaurantsLiked = new ArrayList<>();


    public User() {}


    public User(String uid, String username, String urlPicture) {
        this.setUid(uid);
        this.setUsername(username);
        this.setUrlPicture(urlPicture);
    }


    public User(String uid, String username, String urlPicture, String restaurantChosenName, String restaurantChosenId, List<String> restaurantsLiked) {
        this.uid = uid;
        this.username = username;
        this.urlPicture = urlPicture;
        this.restaurantChosenName = restaurantChosenName;
        this.restaurantChosenId = restaurantChosenId;
        this.restaurantsLiked = restaurantsLiked;
    }


    // GETTERS
    public String getUid() { return uid; }
    public String getUsername() { return username; }
    public String getUrlPicture() { return urlPicture; }
    public String getRestaurantChosenName() {
        if (restaurantChosenName == null) return "";
        return restaurantChosenName; }
    public String getRestaurantChosenId() {
        if (restaurantChosenId == null) return "";
        else return restaurantChosenId; }
    public List<String> getRestaurantsLiked() { return restaurantsLiked; }


    // SETTERS
    public void setUsername(String username) { this.username = username; }
    public void setUid(String uid) { this.uid = uid; }
    public void setUrlPicture(String urlPicture) { this.urlPicture = urlPicture; }
    public void setRestaurantChosenName (String restaurantChosenName) {this.restaurantChosenName = restaurantChosenName;}
    public void setRestaurantChosenId(String restaurantChosenId) { this.restaurantChosenId = restaurantChosenId; }
    public void setRestaurantsLiked(List<String> restaurantsLiked) {
        if (restaurantsLiked == null) this.restaurantsLiked = new ArrayList<>();
        else this.restaurantsLiked = restaurantsLiked;
    }


    @Override
    public String toString() {
        return "User{" +
                "uid='" + uid + '\'' +
                ", username='" + username + '\'' +
                ", urlPicture='" + urlPicture + '\'' +
                ", restaurantChosenId='" + restaurantChosenId + '\'' +
                ", restaurantsLiked=" + restaurantsLiked +
                '}';
    }
}
