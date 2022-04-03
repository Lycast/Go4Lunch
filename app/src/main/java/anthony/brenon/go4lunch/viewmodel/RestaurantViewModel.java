package anthony.brenon.go4lunch.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.tasks.Task;

import anthony.brenon.go4lunch.Repository.RestaurantRepository;
import anthony.brenon.go4lunch.model.Restaurant;

/**
 * Created by Lycast on 25/02/2022.
 */
public class RestaurantViewModel extends ViewModel {

    private final RestaurantRepository restaurantRepository;


    public RestaurantViewModel() {
        super();
        restaurantRepository = new RestaurantRepository();
    }


    public MutableLiveData<Restaurant> getRestaurantDetails(String place_id) {
        return restaurantRepository.getDetailsRestaurant(place_id);
    }


    public void updateRestaurantDto(Restaurant restaurantUpdate) {
        restaurantRepository.updateRestaurant(restaurantUpdate);
    }


    public Task<Restaurant> getRestaurantDto(String placeId) {
        return restaurantRepository.getRestaurantDto(placeId);
    }
}
