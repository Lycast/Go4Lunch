package anthony.brenon.go4lunch.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import anthony.brenon.go4lunch.BuildConfig;
import anthony.brenon.go4lunch.model.googleplace_models.GeometryPlace;
import anthony.brenon.go4lunch.model.googleplace_models.OpeningHours;
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
    private float rating;
    @SerializedName("opening_hours")
    private OpeningHours opening_hours;

    private double distance;

    public Restaurant() { }

    // --GETTERS--
    public String getId() { return id; }
    public String getName() { return name; }
    public String getAddress() { return address; }
    public GeometryPlace getGeometryPlace() { return geometryPlace; }
    public List<Photo> getPhotosUrl() { return photosUrl; }
    public float getRating() { return rating; }
    public double getDistance() { return distance; }
    public OpeningHours getOpening_hours() { return opening_hours; }

    // --SETTERS--
    public void setId(String id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setAddress(String address) { this.address = address; }
    public void setGeometryPlace(GeometryPlace geometryPlace) { this.geometryPlace = geometryPlace; }
    public void setPhotosUrl(List<Photo> photosUrl) { this.photosUrl = photosUrl; }
    public void setRating(float rating) { this.rating = rating; }
    public void setOpening_hours(OpeningHours opening_hours) { this.opening_hours = opening_hours; }
    public void setDistance(double distance) { this.distance = distance; }


    public void setDistance(Location locationUser) {

        android.location.Location place = new android.location.Location("Restaurant");
        place.setLatitude(getGeometryPlace().getLocationPlace().getLat());
        place.setLongitude(getGeometryPlace().getLocationPlace().getLng());

        android.location.Location user = new android.location.Location("User");
        user.setLatitude(locationUser.getLat());
        user.setLongitude(locationUser.getLng());
        setDistance(user.distanceTo(place));
    }

    public String getPhoto(int size) {
        if (photosUrl != null && photosUrl.size() > 0) {
            return "https://maps.googleapis.com/maps/api/place/photo?maxwidth=" + size
                    + "&maxheight=" + size
                    + "&photoreference=" + photosUrl.get(0).getPhotoReference()
                    + "&key=" + BuildConfig.MAPS_API_KEY;
        } else return "";
    }

//    public String setPhoto(int size) {
//        if (photosUrl != null && photosUrl.size() > 0) {
//            return "https://maps.googleapis.com/maps/api/place/photo?maxwidth=" + size
//                    + "&maxheight=" + size
//                    + "&photoreference=" + photosUrl.get(0).getPhotoReference()
//                    + "&key=" + BuildConfig.MAPS_API_KEY;
//        } else return "";
//    }
//
//    public ImageView getPhoto2 (Restaurant restaurant) {
//        Glide.with()
//                .load(restaurant.setPhoto(600))
//                .placeholder(R.drawable.ic_image_not_supported)
//                .transform(new CenterCrop(), new RoundedCorners(8))
//                .into(itemBinding.restaurantImage);
//    }
}
