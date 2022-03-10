package anthony.brenon.go4lunch.model.googleplace_models;

/**
 * Created by Lycast on 01/03/2022.
 */
public class LocationPlace {

    private Double lat = 0d;
    private Double lng = 0d;

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

    @Override
    public String toString() {
        return lat + "," + lng;
    }
}
