package anthony.brenon.go4lunch.model.googleplace_models;

import java.util.List;

import anthony.brenon.go4lunch.model.Restaurant;

/**
 * Created by Lycast on 03/03/2022.
 */
public class GooglePlaceNearbyResponse {

    private final List<Restaurant> results;

    public GooglePlaceNearbyResponse(List<Restaurant> results) {
        this.results = results;
    }

    public List<Restaurant> getResults() {
        return results;
    }
}
