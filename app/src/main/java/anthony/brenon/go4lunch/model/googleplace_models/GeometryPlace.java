package anthony.brenon.go4lunch.model.googleplace_models;

import com.google.gson.annotations.SerializedName;

import anthony.brenon.go4lunch.model.Location;

/**
 * Created by Lycast on 10/03/2022.
 */
public class GeometryPlace {
    private final String TAG = "my logs";

    @SerializedName("location")
    private final Location locationPlace;

    public GeometryPlace(Location locationPlace) {
        this.locationPlace = locationPlace;
    }


    public Location getLocationPlace() {
        return locationPlace;
    }

    @Override
    public String toString() {
        return "GeometryPlace{" +
                "locationPlace=" + locationPlace +
                '}';
    }
}
