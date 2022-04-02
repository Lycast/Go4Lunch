package anthony.brenon.go4lunch.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import anthony.brenon.go4lunch.Repository.RestaurantRepository;
import anthony.brenon.go4lunch.model.Location;
import anthony.brenon.go4lunch.model.Restaurant;

/**
 * Created by Lycast on 24/02/2022.
 */
public class SharedViewModel extends ViewModel {
    private final String TAG = "my_logs";

    private final RestaurantRepository restaurantRepository;
    private Location locationUser;

    public SharedViewModel() {
        super();
        restaurantRepository = new RestaurantRepository();
    }

    public LiveData<List<Restaurant>> getRestaurantsLiveData() {
        return restaurantRepository.getNearbyRestaurants(locationUser);
    }

    public void setLocationUser(Location locationUser) {
        this.locationUser = locationUser;
    }

}
