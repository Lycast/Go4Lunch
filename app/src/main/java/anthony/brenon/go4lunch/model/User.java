package anthony.brenon.go4lunch.model;

import androidx.annotation.NonNull;

/**
 * Created by Lycast on 29/01/2022.
 */

public class User {
    private final String TAG = "my logs";

    private String uid;
    private String username;
    private String urlPicture;
    private String restaurantChosenId;

    public User() {}

    public User(String uid, String username, String urlPicture) {
        this.setUid(uid);
        this.setUsername(username);
        this.setUrlPicture(urlPicture);
    }

    // GETTERS
    public String getUid() { return uid; }
    public String getUsername() { return username; }
    public String getUrlPicture() { return urlPicture; }
    public String getRestaurantChosenId() { return restaurantChosenId; }

    // SETTERS
    public void setUsername(String username) { this.username = username; }
    public void setUid(String uid) { this.uid = uid; }
    public void setUrlPicture(String urlPicture) { this.urlPicture = urlPicture; }
    public void setRestaurantChosenId(String restaurantChosenId) { this.restaurantChosenId = restaurantChosenId; }

    @NonNull
    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                '}';
    }
}
