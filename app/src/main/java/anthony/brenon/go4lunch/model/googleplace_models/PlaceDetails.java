package anthony.brenon.go4lunch.model.googleplace_models;

import anthony.brenon.go4lunch.model.Restaurant;

/**
 * Created by Lycast on 08/03/2022.
 */
public class PlaceDetails {

    private final Restaurant result;

    public PlaceDetails(Restaurant results) {
        this.result = results;
    }

    public Restaurant getResults() {
        return result;
    }
}
