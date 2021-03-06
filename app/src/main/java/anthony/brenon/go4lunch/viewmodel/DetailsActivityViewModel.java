package anthony.brenon.go4lunch.viewmodel;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.tasks.Task;

import java.util.List;

import anthony.brenon.go4lunch.repository.RestaurantRepository;
import anthony.brenon.go4lunch.repository.WorkmateRepository;
import anthony.brenon.go4lunch.model.Restaurant;
import anthony.brenon.go4lunch.model.Workmate;
import anthony.brenon.go4lunch.model.googleplace_models.PlaceResponse;
import retrofit2.Callback;

/**
 * Created by Lycast on 22/04/2022.
 */
public class DetailsActivityViewModel extends ViewModel {

    private final RestaurantRepository restaurantRepository;
    private final WorkmateRepository workmateRepository;

    public DetailsActivityViewModel() {
        super();
        restaurantRepository = new RestaurantRepository();
        workmateRepository = new WorkmateRepository();
    }


    public void getRestaurantDetailsApi(String placeId, Callback<PlaceResponse> callback) {
        restaurantRepository.callDetailsRestaurantApi(placeId, callback);
    }

    public Task<Restaurant> getRestaurantFirestore(String placeId) {
        return restaurantRepository.getRestaurantDatabase(placeId);
    }

    public void updateRestaurantIntoFirestore(Restaurant restaurantUpdate) {
        restaurantRepository.updateRestaurantDatabase(restaurantUpdate);
    }

    public void updateWorkmate(Workmate workmateUpdate) {
        workmateRepository.getCurrentUserDatabase()
                .addOnSuccessListener(data -> {
                            // User exist in database -> update user
                            workmateRepository.updateCurrentUserDatabase(workmateUpdate);
                        }
                )
                .addOnFailureListener(notExistException ->
                        // User doesn't exist in database -> create user
                        Log.e("TAG", "updateWorkmate :" + notExistException.getMessage())
                );
    }

    public Task<Workmate> getCurrentWorkmateData(){
        return workmateRepository.getCurrentUserDatabase();
    }

    public LiveData<List<Workmate>> getWorkmatesLiveData(){
        return workmateRepository.getWorkmatesLiveData();
    }

    public void getWorkmatesFromList(List<String> workmateIds) {
        workmateRepository.getWorkmatesFromList(workmateIds);
    }

    public void removeObserver(Observer<List<Workmate>> observer){
        workmateRepository.removeObserver(observer);
    }

}
