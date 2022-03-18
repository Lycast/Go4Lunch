package anthony.brenon.go4lunch.ui;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import anthony.brenon.go4lunch.Repository.RestaurantRepository;
import anthony.brenon.go4lunch.model.Restaurant;
import anthony.brenon.go4lunch.model.googleplace_models.LocationPlace;

/**
 * Created by Lycast on 24/02/2022.
 */
public class SharedViewModel extends ViewModel {

    private final RestaurantRepository restaurantRepository;
    private LocationPlace locationPlace;
    private final MutableLiveData<List<Restaurant>> restaurantsLiveData = new MutableLiveData<>();

    public SharedViewModel() {
        super();
        restaurantRepository = new RestaurantRepository();
    }

    public void getAllRestaurants() {
        restaurantsLiveData.postValue(restaurantRepository.getAllRestaurantsList(locationPlace));
    }

    public void setLocation(LocationPlace locationPlace) {
        this.locationPlace = locationPlace;
        getAllRestaurants();
    }

    public LiveData<List<Restaurant>> getRestaurantsLiveData() {
        return restaurantsLiveData;
    }
}
