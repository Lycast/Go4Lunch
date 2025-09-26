package anthony.brenon.go4lunch.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

import anthony.brenon.go4lunch.repository.RestaurantRepository;
import anthony.brenon.go4lunch.repository.WorkmateRepository;
import anthony.brenon.go4lunch.model.Location;
import anthony.brenon.go4lunch.model.Restaurant;
import anthony.brenon.go4lunch.model.Workmate;

/**
 * Created by Lycast on 22/04/2022.
 */
public class MainActivityViewModel extends ViewModel {

    private final RestaurantRepository restaurantRepository;
    private final WorkmateRepository workmateRepository;
    private final MutableLiveData<LatLng> latLngLiveData = new MutableLiveData<>();
    private static Location locationUser;
    private String radius;

    public MainActivityViewModel() {
        super();
        restaurantRepository = new RestaurantRepository();
        workmateRepository = new WorkmateRepository();
    }


    public LiveData<LatLng> getLatLngLiveData() {
        return latLngLiveData;
    }

    public LiveData<List<Restaurant>> getLiveDataListRestaurants() {
        return restaurantRepository.getLiveDataRestaurants();
    }

    private void setLatLngUser(Location locationUser) {
        if (locationUser != null) {
            LatLng latLng1 = new LatLng(locationUser.getLat(), locationUser.getLng());
            latLngLiveData.postValue(latLng1);
        }
    }

    public void setLocationUser(Location locationUser) {
        if(locationUser != null) {
            this.setLatLngUser(locationUser);
            MainActivityViewModel.locationUser = locationUser;
        }
    }

    public void callNearbyRestaurantsApi(Location locationUser) {
        if(locationUser != null)
            workmateRepository.getCurrentUserDatabase().addOnSuccessListener(workmate -> {
                radius = workmate.getResearchRadius();
                restaurantRepository.callNearbyRestaurantsApi(locationUser, radius);
            });
    }

    public void callNearbyRestaurantsApi() {
        callNearbyRestaurantsApi(locationUser);
    }

    public Task<Workmate> updateCurrentUserDatabase() {
        FirebaseUser fbUser = workmateRepository.getCurrentFirebaseUser();
        String urlPicture = (fbUser.getPhotoUrl() != null) ? fbUser.getPhotoUrl().toString() : null;
        String username = fbUser.getDisplayName();
        String uid = fbUser.getUid();
        String email = fbUser.getEmail();

        return workmateRepository.getCurrentUserDatabase()
                .addOnSuccessListener(dbUser -> {
                    if (dbUser != null) {
                        // User exist in database -> update user
                        dbUser.setUsername(username);
                        dbUser.setUid(uid);
                        dbUser.setUrlPicture(urlPicture);
                        dbUser.setEmail(email);
                        workmateRepository.updateCurrentUserDatabase(dbUser);
                    } else {
                        // If the user does not exist -> we pass him some default parameters
                        Workmate workmateToCreate = new Workmate(uid, username, urlPicture, email, false, "500");
                        workmateRepository.updateCurrentUserDatabase(workmateToCreate);
                    }
                });
    }

    public Task<Workmate> getCurrentWorkmateData(){
        return workmateRepository.getCurrentUserDatabase();
    }

    public Boolean isCurrentUserLogged() {
        return (workmateRepository.getCurrentUserId() != null);
    }

    public LiveData<List<Workmate>> getWorkmatesDatabase() {
        return workmateRepository.getWorkmatesDatabase();
    }

    public void getWorkmatesFromList(List<String> workmateIds) {
        workmateRepository.getWorkmatesFromList(workmateIds);
    }

    public LiveData<List<Workmate>> getWorkmatesLiveData(){
        return workmateRepository.getWorkmatesLiveData();
    }

    public void sortMethodRestaurantsList(int sortOption) { restaurantRepository.sortMethodRestaurantsList(sortOption); }

    public Task<Restaurant> getRestaurantFS(String placeId) {
        return restaurantRepository.getRestaurantDatabase(placeId);
    }
}
