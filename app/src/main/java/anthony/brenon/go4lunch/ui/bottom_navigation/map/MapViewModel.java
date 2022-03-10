package anthony.brenon.go4lunch.ui.bottom_navigation.map;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import anthony.brenon.go4lunch.Repository.RestaurantRepository;
import anthony.brenon.go4lunch.model.Restaurant;
import anthony.brenon.go4lunch.model.googleplace_models.LocationPlace;

/**
 * Created by Lycast on 24/02/2022.
 */
public class MapViewModel extends AndroidViewModel {

    private final RestaurantRepository restaurantRepository;
    private LiveData<List<Restaurant>> allRestaurants;

    public MapViewModel(@NonNull Application application) {
        super(application);
        restaurantRepository = new RestaurantRepository(application);
    }

    public LiveData<List<Restaurant>> getAllRestaurants(LocationPlace location) {
        allRestaurants = restaurantRepository.getAllRestaurants(location);
        return allRestaurants;}
}
