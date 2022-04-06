package anthony.brenon.go4lunch.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.maps.model.LatLng;

import java.util.List;

import anthony.brenon.go4lunch.Repository.RestaurantRepository;
import anthony.brenon.go4lunch.model.Location;
import anthony.brenon.go4lunch.model.Restaurant;

/**
 * Created by Lycast on 25/02/2022.
 */
public class RestaurantViewModel extends ViewModel {
    private final String TAG = "my_logs";
    private final String LOG_INFO = "RestaurantViewModel ";

    private final RestaurantRepository restaurantRepository;
    private LiveData<List<Restaurant>> restaurantsInstance;
    private Location locationUser;



    public RestaurantViewModel() {
        super();
        restaurantRepository = new RestaurantRepository();
    }


    public MutableLiveData<Restaurant> getRestaurantDetailsApi(String placeId) {
        return restaurantRepository.getRestaurantDetailsApi(placeId);
    }


    public void updateRestaurantIntoDB(Restaurant restaurantUpdate) {
        restaurantRepository.updateRestaurantIntoDB(restaurantUpdate);
    }


    //TODO change that
    public LiveData<Restaurant> getRestaurantDB(String placeId) {
        MutableLiveData<Restaurant> restaurantDB = new MutableLiveData<>();
        restaurantRepository.getRestaurantDB(placeId).addOnSuccessListener(restaurantDB::setValue);
        return restaurantDB;
    }


    public void callNearbyRestaurantsApi() {
        restaurantsInstance = restaurantRepository.callNearbyRestaurantsApi(locationUser);
    }

    public LiveData<LatLng> getLatLngUser() {
        MutableLiveData<LatLng> latLng = new MutableLiveData<>();
        if(locationUser != null) {
            LatLng latLng1 = new LatLng(locationUser.getLat(), locationUser.getLng());
            latLng.setValue(latLng1);
        }
        return latLng;
    }


    public LiveData<List<Restaurant>> getRestaurantsInstance() {
        return restaurantsInstance;
    }


    public void setLocationUser(Location locationUser) {
        this.locationUser = locationUser;
    }

    public void setRestaurantsInstance(LiveData<List<Restaurant>> restaurantsInstance) {
        this.restaurantsInstance = restaurantsInstance;
    }
}
