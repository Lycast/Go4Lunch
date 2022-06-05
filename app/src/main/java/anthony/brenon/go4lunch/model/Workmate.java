package anthony.brenon.go4lunch.model;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lycast on 29/01/2022.
 */

public class Workmate {


    private String uid;
    private String username;
    private String urlPicture;
    private String email;
    private String restaurantChosenName;
    private String restaurantChosenId;
    private List<String> restaurantsLiked = new ArrayList<>();
    private boolean enableNotification;
    private String researchRadius;


    public Workmate() {}


    public Workmate(String uid, String username, String urlPicture, String email, boolean enableNotification, String researchRadius) {
        this.setUid(uid);
        this.setUsername(username);
        this.setUrlPicture(urlPicture);
        this.email = email;
        this.enableNotification = enableNotification;
        this.researchRadius = researchRadius;
    }

    // constructor for tests
    public Workmate(String uid, String username, String urlPicture, String email) {
        this.setUid(uid);
        this.setUsername(username);
        this.setUrlPicture(urlPicture);
        this.email = email;
    }

    // GETTERS
    public String getUid() { return uid; }
    public String getUsername() { return username; }
    public String getUrlPicture() { return urlPicture; }
    public String getEmail() { return email; }
    public String getRestaurantChosenName() {
        if (restaurantChosenName == null) return "";
        return restaurantChosenName; }
    public String getRestaurantChosenId() {
        if (restaurantChosenId == null) return "";
        else return restaurantChosenId; }
    public List<String> getRestaurantsLiked() { return restaurantsLiked; }
    public boolean isEnableNotification() { return enableNotification; }
    public String getResearchRadius() { return researchRadius; }

    // SETTERS
    public void setUsername(String username) { this.username = username; }
    public void setUid(String uid) { this.uid = uid; }
    public void setUrlPicture(String urlPicture) { this.urlPicture = urlPicture; }
    public void setEmail(String email) { this.email = email; }
    public void setRestaurantChosenName (String restaurantChosenName) {this.restaurantChosenName = restaurantChosenName;}
    public void setRestaurantChosenId(String restaurantChosenId) { this.restaurantChosenId = restaurantChosenId; }
    public void setRestaurantsLiked(List<String> restaurantsLiked) {
        if (restaurantsLiked == null) this.restaurantsLiked = new ArrayList<>();
        else this.restaurantsLiked = restaurantsLiked;
    }
    public void setEnableNotification(boolean enableNotification) { this.enableNotification = enableNotification; }
    public void setResearchRadius(String researchRadius) { this.researchRadius = researchRadius; }

    @NonNull
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
