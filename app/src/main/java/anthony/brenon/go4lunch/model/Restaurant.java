package anthony.brenon.go4lunch.model;

import androidx.annotation.NonNull;

import com.google.firebase.firestore.Exclude;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.Comparator;
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
    @SerializedName("website")
    private String website;
    @SerializedName("international_phone_number")
    private String phoneNumber;
    @SerializedName("opening_hours")
    private OpeningHours openingHours;
    @SerializedName("rating")
    private float rating = 0;
    private List<String> usersChoice;
    private int distance;

    // Comparator
    public static Comparator<Restaurant> compareDistance = (left, right) -> left.distance - right.distance;
    public static Comparator<Restaurant> compareRating = (left, right) -> Float.compare(right.rating, left.rating);
    public static Comparator<Restaurant> compareWorkmate = (left, right) -> right.usersChoice.size() - left.usersChoice.size();
    public static Comparator<Restaurant> compareOpening = (left, right) -> Boolean.compare(right.openingHours.isOpen_now(), left.openingHours.isOpen_now());


    public Restaurant() {
    }

    public Restaurant(String id, String name, String address, float rating, int distance, OpeningHours openingHours) {
        this.setId(id);
        this.setName(name);
        this.setAddress(address);
        this.setRating(rating);
        this.setDistance(distance);
        this.setOpeningHours(openingHours);
    }

    public Restaurant(String id, String name, String address, float rating, int distance, OpeningHours openingHours,
                      List<String> usersChoice) {
        this(id, name, address, rating, distance, openingHours);
        this.setUsersChoice(usersChoice);
    }
    public Restaurant(String id, String name, String address, float rating, int distance, OpeningHours openingHours,  List<String> usersChoice ,
                      GeometryPlace geometryPlace, List<Photo> photosUrl, String website, String phoneNumber) {
        this(id, name, address, rating, distance, openingHours, usersChoice);
        this.setGeometryPlace(geometryPlace);
        this.setPhotosUrl(photosUrl);
        this.setWebsite(website);
        this.setPhoneNumber(phoneNumber);
    }

    // --GETTERS--
    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    @Exclude
    public GeometryPlace getGeometryPlace() {
        return geometryPlace;
    }

    @Exclude
    public List<Photo> getPhotosUrl() {
        return photosUrl;
    }

    @Exclude
    public String getWebsite() {
        return website;
    }

    @Exclude
    public String getPhoneNumber() {
        return phoneNumber;
    }

    @Exclude
    public OpeningHours getOpeningHours() {
        return openingHours;
    }

    @Exclude
    public float getRating() {
        return rating;
    }

    @Exclude
    public double getDistance() {
        return distance;
    }

    public List<String> getUsersChoice() {
        if (usersChoice == null) return new ArrayList<>();
        else return usersChoice;
    }

    public String getPhoto(int size) {
        if (photosUrl != null && photosUrl.size() > 0) {
            return "https://maps.googleapis.com/maps/api/place/photo?maxwidth=" + size
                    + "&maxheight=" + size
                    + "&photoreference=" + photosUrl.get(0).getPhotoReference()
                    + "&key=" + BuildConfig.MAPS_API_KEY;
        } else return "";
    }

    // --SETTERS--
    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setGeometryPlace(GeometryPlace geometryPlace) {
        this.geometryPlace = geometryPlace;
    }

    public void setPhotosUrl(List<Photo> photosUrl) {
        this.photosUrl = photosUrl;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setOpeningHours(OpeningHours openingHours) {
        this.openingHours = openingHours;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public void setUsersChoice(List<String> usersChoice) {
        this.usersChoice = usersChoice;
    }

    public void setDistance(Location locationUser) {
        if(getGeometryPlace() != null) {
            android.location.Location place = new android.location.Location("Restaurant");
            place.setLatitude(getGeometryPlace().getLocationPlace().getLat());
            place.setLongitude(getGeometryPlace().getLocationPlace().getLng());
            android.location.Location user = new android.location.Location("User");
            user.setLatitude(locationUser.getLat());
            user.setLongitude(locationUser.getLng());
            setDistance((int) user.distanceTo(place));
        }
    }

    @NonNull
    @Override
    public String toString() {
        return "Restaurant{\n" +
                name + ", " +
                address + ", " +
                distance + ", " +
                rating + ", " +
                usersChoice.size() + " " +
                openingHours + ", " +
                '}';
    }
}
