package anthony.brenon.go4lunch.model.googleplace_models;

import java.util.List;

import anthony.brenon.go4lunch.model.Restaurant;

/**
 * Created by Lycast on 03/03/2022.
 */
public class GooglePlaceNearbyResponse {

    private List<Restaurant> results;

    public List<Restaurant> getResults() {
        return results;
    }

    public void setResult(List<Restaurant> results) {
        this.results = results;
    }
}
