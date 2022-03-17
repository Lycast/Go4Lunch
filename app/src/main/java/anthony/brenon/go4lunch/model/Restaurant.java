package anthony.brenon.go4lunch.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import anthony.brenon.go4lunch.BuildConfig;
import anthony.brenon.go4lunch.model.googleplace_models.GeometryPlace;
import anthony.brenon.go4lunch.model.googleplace_models.Photo;

/**
 * Created by Lycast on 01/03/2022.
 */
public class Restaurant {

    @SerializedName("place_id")
    private String id;
    @SerializedName("name")
    private String name;
    @SerializedName("vicinity")
    private String address;
    @SerializedName("geometry")
    private GeometryPlace geometryPlace;
    @SerializedName("photos")
    private List<Photo> photosUrl = null;
    @SerializedName("rating")
    private double rating;

    //GETTERS
    public String getId() { return id; }
    public String getName() { return name; }
    public String getAddress() { return address; }
    public GeometryPlace getGeometryPlace() { return geometryPlace; }
    public List<Photo> getPhotosUrl() { return photosUrl; }
    public double getRating() { return rating; }

    public String getPhoto(int size) {
        if (photosUrl != null && photosUrl.size() > 0) {
            return "https://maps.googleapis.com/maps/api/place/photo?maxwidth=" + size
                    + "&maxheight=" + size
                    + "&photoreference=" + photosUrl.get(0).getPhotoReference()
                    + "&key=" + BuildConfig.MAPS_API_KEY;
        } else return "";
    }
}
