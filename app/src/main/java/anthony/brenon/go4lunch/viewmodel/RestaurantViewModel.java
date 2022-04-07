package anthony.brenon.go4lunch.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.Task;

import java.util.List;

import anthony.brenon.go4lunch.Repository.RestaurantRepository;
import anthony.brenon.go4lunch.model.Location;
import anthony.brenon.go4lunch.model.Restaurant;
import anthony.brenon.go4lunch.model.googleplace_models.PlaceResponse;
import retrofit2.Callback;

/**
 * Created by Lycast on 25/02/2022.
 */
public class RestaurantViewModel extends ViewModel {

    private final RestaurantRepository restaurantRepository;
    private final MutableLiveData<LatLng> latLngLiveData = new MutableLiveData<>();
    private static Location locationUser;

    public RestaurantViewModel() {
        super();
        restaurantRepository = new RestaurantRepository();
    }


    // GETS
    public void getRestaurantDetailsApi(String placeId, Callback<PlaceResponse> callback) {
        restaurantRepository.getDetailsRestaurantApi(placeId, callback);
    }

    public Task<Restaurant> getRestaurantFS(String placeId) {
        return restaurantRepository.getRestaurantFS(placeId);
    }

    public LiveData<LatLng> getLatLngLiveData() {
        return latLngLiveData;
    }

    public LiveData<List<Restaurant>> getLiveDataListRestaurants() {
        return this.restaurantRepository.getLiveDataListRestaurant();
    }


    // SETS
    private void setLatLngUser(Location locationUser) {
        if (locationUser != null) {
            LatLng latLng1 = new LatLng(locationUser.getLat(), locationUser.getLng());
            latLngLiveData.setValue(latLng1);
        }
    }

    public void setLocationUser(Location locationUser) {
        if(locationUser != null) {
            this.callNearbyRestaurantsApi(locationUser);
            this.setLatLngUser(locationUser);
            this.locationUser = locationUser;
        }
    }


    // CALLS
    public void callNearbyRestaurantsApi(Location locationUser) {
        restaurantRepository.callNearbyRestaurantsApi(locationUser);
    }

    public void callNearbyRestaurantsApi() {
        callNearbyRestaurantsApi(locationUser);
    }

    public void updateRestaurantIntoFS(Restaurant restaurantUpdate) {
        restaurantRepository.updateRestaurantIntoFS(restaurantUpdate).addOnSuccessListener((result) -> this.callNearbyRestaurantsApi(locationUser));
    }
}
