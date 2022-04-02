package anthony.brenon.go4lunch.model.googleplace_models;

import anthony.brenon.go4lunch.model.Restaurant;

/**
 * Created by Lycast on 08/03/2022.
 */
public class PlaceResponse {
    private final String TAG = "my logs";

    private final Restaurant result;

    public PlaceResponse(Restaurant result) {
        this.result = result;
    }

    public Restaurant getResult() {
        return result;
    }
}
