package anthony.brenon.go4lunch.ui;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import anthony.brenon.go4lunch.Repository.RestaurantRepository;
import anthony.brenon.go4lunch.model.Location;
import anthony.brenon.go4lunch.model.Restaurant;

/**
 * Created by Lycast on 24/02/2022.
 */
public class SharedViewModel extends ViewModel {

    private final RestaurantRepository restaurantRepository;
    private Location locationUser;
    private final MutableLiveData<List<Restaurant>> restaurantsLiveData = new MutableLiveData<>();

    public SharedViewModel() {
        super();
        restaurantRepository = new RestaurantRepository();
    }

    public void getAllRestaurants() {
        restaurantsLiveData.postValue(restaurantRepository.getAllRestaurantsList(locationUser));
    }

    public void setLocationUser(Location locationUser) {
        this.locationUser = locationUser;
        getAllRestaurants();
    }

    public LiveData<List<Restaurant>> getRestaurantsLiveData() {
        return restaurantsLiveData;
    }
}
