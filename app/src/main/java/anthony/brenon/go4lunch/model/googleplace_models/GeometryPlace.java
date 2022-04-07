package anthony.brenon.go4lunch.model.googleplace_models;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import anthony.brenon.go4lunch.model.Location;

/**
 * Created by Lycast on 10/03/2022.
 */
public class GeometryPlace {

    @SerializedName("location")
    private Location locationPlace;

    public GeometryPlace() {}

    public GeometryPlace(Location locationPlace) {
        this.locationPlace = locationPlace;
    }


    public Location getLocationPlace() {
        return locationPlace;
    }

    @NonNull
    @Override
    public String toString() {
        return "GeometryPlace{" +
                "locationPlace=" + locationPlace +
                '}';
    }
}
