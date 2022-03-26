package anthony.brenon.go4lunch.model.googleplace_models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Lycast on 14/03/2022.
 */
public class Photo {
    private final String TAG = "my logs";

    @SerializedName("photo_reference")
    private String photoReference;

    // --GETTER--
    public String getPhotoReference() {
        return photoReference;
    }
}
