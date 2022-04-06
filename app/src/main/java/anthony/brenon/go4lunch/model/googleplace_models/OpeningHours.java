package anthony.brenon.go4lunch.model.googleplace_models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Lycast on 23/03/2022.
 */
public class OpeningHours {
    private final String TAG = "my logs";

    @SerializedName("open_now")
    private boolean open_now;

    public OpeningHours() {}

    public OpeningHours(boolean open_now) {
        this.open_now = open_now;
    }

    public boolean isOpen_now() {
        return open_now;
    }
}
