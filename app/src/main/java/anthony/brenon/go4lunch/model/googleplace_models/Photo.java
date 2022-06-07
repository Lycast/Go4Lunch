package anthony.brenon.go4lunch.model.googleplace_models;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Lycast on 14/03/2022.
 */
public class Photo {

    @SerializedName("photo_reference")
    private String photoReference;

    public Photo() {}

    // --GETTER--
    public String getPhotoReference() {
        return photoReference;
    }

    // --SETTER--
    public void setPhotoReference(String photoReference) { this.photoReference = photoReference; }

    @NonNull
    @Override
    public String toString() {
        return photoReference;
    }
}
