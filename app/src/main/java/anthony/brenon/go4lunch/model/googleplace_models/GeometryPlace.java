package anthony.brenon.go4lunch.model.googleplace_models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Lycast on 10/03/2022.
 */
public class GeometryPlace {

    @SerializedName("location")
    private final LocationPlace locationPlace;

    public GeometryPlace(LocationPlace locationPlace) {
        this.locationPlace = locationPlace;
    }

    public LocationPlace getLocationPlace() {
        return locationPlace;
    }
}
