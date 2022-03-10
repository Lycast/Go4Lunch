package anthony.brenon.go4lunch.ui.bottom_navigation.list_view;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import anthony.brenon.go4lunch.Repository.RestaurantRepository;
import anthony.brenon.go4lunch.model.Restaurant;
import anthony.brenon.go4lunch.model.googleplace_models.LocationPlace;

/**
 * Created by Lycast on 24/02/2022.
 */
public class ListViewViewModel extends AndroidViewModel {

    private final RestaurantRepository restaurantRepository;
    private LiveData<List<Restaurant>> allRestaurants;

    public ListViewViewModel(Application application) {
        super(application);
        restaurantRepository = new RestaurantRepository(application);

    }

    public LiveData<List<Restaurant>> getAllRestaurants(LocationPlace locationPlace) {
        allRestaurants = restaurantRepository.getAllRestaurants(locationPlace);
        return allRestaurants;}
}
