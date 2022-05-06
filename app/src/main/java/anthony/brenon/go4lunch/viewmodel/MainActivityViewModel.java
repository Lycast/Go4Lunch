package anthony.brenon.go4lunch.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

import anthony.brenon.go4lunch.Repository.RestaurantRepository;
import anthony.brenon.go4lunch.Repository.WorkmateRepository;
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

    public MainActivityViewModel() {
        super();
        restaurantRepository = new RestaurantRepository();
        workmateRepository = new WorkmateRepository();
    }


    public LiveData<LatLng> getLatLngLiveData() {
        return latLngLiveData;
    }

    public LiveData<List<Restaurant>> getLiveDataListRestaurants() {
        return restaurantRepository.getLiveDataListRestaurant();
    }

    private void setLatLngUser(Location locationUser) {
        if (locationUser != null) {
            LatLng latLng1 = new LatLng(locationUser.getLat(), locationUser.getLng());
            latLngLiveData.postValue(latLng1);
        }
    }

    public void setLocationUser(Location locationUser) {
        if(locationUser != null) {
            this.callNearbyRestaurantsApi(locationUser);
            this.setLatLngUser(locationUser);
            this.locationUser = locationUser;
        }
    }

    //TODO double call to the api change for only one call into mainActivity when onResume
    public void callNearbyRestaurantsApi(Location locationUser) {
        if(locationUser != null)
            restaurantRepository.callNearbyRestaurantsApi(locationUser);
    }

    public void callNearbyRestaurantsApi() {
        callNearbyRestaurantsApi(locationUser);
    }

    public Task<Workmate> createWorkmateIntoDb() {
        FirebaseUser fbUser = workmateRepository.getCurrentFirebaseUser();
        if (fbUser == null) {
            throw new NullPointerException("FirebaseUser is not defined");
        }
        String urlPicture = (fbUser.getPhotoUrl() != null) ? fbUser.getPhotoUrl().toString() : null;
        String username = fbUser.getDisplayName();
        String uid = fbUser.getUid();
        String email = fbUser.getEmail();
        return workmateRepository.getWorkmateData()
                .addOnSuccessListener(dbUser -> {
                    // User exist in database -> update user
                    dbUser.setUsername(username);
                    dbUser.setUid(uid);
                    dbUser.setUrlPicture(urlPicture);
                    dbUser.setEmail(email);
                    workmateRepository.updateWorkmateIntoFS(dbUser);
                })
                .addOnFailureListener(notExistException -> {
                    // User doesn't exist in database -> create user
                    Workmate workmateToCreate = new Workmate(uid, username, urlPicture, email);
                    workmateRepository.createWorkmateIntoFS(workmateToCreate);
                });
    }

    public Task<Workmate> getCurrentWorkmateData(){
        return workmateRepository.getWorkmateData();
    }

    // list for workmates list view
    public LiveData<List<Workmate>> getWorkmatesList() {
        return workmateRepository.getWorkmatesListData();
    }

    // list for notification
    public void getWorkmatesFromList(List<String> workmateIds) {
        workmateRepository.getWorkmatesFromList(workmateIds);
    }

    public LiveData<List<Workmate>> getWorkmatesLiveData(){
        return workmateRepository.getWorkmatesLiveData();
    }

    public void sortMethodRestaurantsList(int sortOption) { restaurantRepository.sortMethodRestaurantsList(sortOption); }

    public Task<Restaurant> getRestaurantFS(String placeId) {
        return restaurantRepository.getRestaurantFS(placeId);
    }
}
