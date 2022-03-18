package anthony.brenon.go4lunch.model.googleplace_models;

import androidx.annotation.NonNull;

/**
 * Created by Lycast on 01/03/2022.
 */
public class LocationPlace {

    private Double lat;
    private Double lng;

    public LocationPlace(Double lat, Double lng) {
        this.lat = lat;
        this.lng = lng;
    }

    //GETTERS
    public Double getLat() { return lat; }
    public Double getLng() { return lng; }

    //SETTERS
    public void setLat(Double lat) { this.lat = lat; }
    public void setLng(Double lng) { this.lng = lng; }

    @NonNull
    @Override
    public String toString() {
        return lat + "," + lng;
    }
}
