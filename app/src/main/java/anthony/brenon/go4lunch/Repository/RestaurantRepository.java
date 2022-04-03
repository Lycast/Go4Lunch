package anthony.brenon.go4lunch.Repository;

import static anthony.brenon.go4lunch.api.JsonPlaceHolderApi.retrofit;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import anthony.brenon.go4lunch.api.JsonPlaceHolderApi;
import anthony.brenon.go4lunch.model.Location;
import anthony.brenon.go4lunch.model.Restaurant;
import anthony.brenon.go4lunch.model.googleplace_models.PlaceNearbyResponse;
import anthony.brenon.go4lunch.model.googleplace_models.PlaceResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Lycast on 24/02/2022.
 */
public class RestaurantRepository {
    private final String TAG = "my_logs";
    private final String LOG_INFO = "RestaurantRepository ";


    private static final String COLLECTION_RESTAURANTS = "restaurants";
    private List<Restaurant> restaurants = new ArrayList<>();
    private final JsonPlaceHolderApi jsonPlaceHolderApi;
    //TODO put there variable into shared view model radius setting
    String radius = "2000";


    public RestaurantRepository () {
        Log.d(TAG, LOG_INFO + "construct");
        jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi.class);
    }


    public MutableLiveData<List<Restaurant>> getNearbyRestaurants(Location locationUser) {
        MutableLiveData<List<Restaurant>> result = new MutableLiveData<>();
        Call<PlaceNearbyResponse> call = jsonPlaceHolderApi.getApiNearbyRestaurantResponse(locationUser.toString(), radius);
        call.enqueue(new Callback<PlaceNearbyResponse>() {
            @Override
            public void onResponse(@NonNull Call<PlaceNearbyResponse> call,
                                   @NonNull Response<PlaceNearbyResponse> response) {
                if(response.isSuccessful()){
                    restaurants = Objects.requireNonNull(response.body()).getResults();
                    for (Restaurant restaurant : restaurants) {
                        restaurant.setDistance(locationUser);
                        addNewRestaurantIntoFirebase(restaurant);
                    }
                    result.postValue(restaurants);
                }
            }
            @Override
            public void onFailure(@NonNull Call<PlaceNearbyResponse> call, @NonNull Throwable t) {
                Log.d(TAG,  LOG_INFO + "onFailure: " + t.getMessage());
            }
        });
        return result;
    }


    public MutableLiveData<Restaurant> getDetailsRestaurant(String place_id) {
        MutableLiveData<Restaurant> restaurant = new MutableLiveData<>();
        Call<PlaceResponse> restaurantCall = jsonPlaceHolderApi.getApiDetailsResponse(place_id);
        restaurantCall.enqueue(new Callback<PlaceResponse>() {
            @Override
            public void onResponse(@NonNull Call<PlaceResponse> call, @NonNull Response<PlaceResponse> response) {
                Log.d(TAG,  LOG_INFO + "onResponse ");
                if (response.isSuccessful()) {
                    restaurant.postValue(Objects.requireNonNull(response.body()).getResult());
                }
            }
            @Override
            public void onFailure(@NonNull Call<PlaceResponse> call, @NonNull Throwable t) {
                Log.d(TAG,  LOG_INFO + "onFailure: " + t.getMessage());
            }
        });
        return restaurant;
    }


    public Task<Restaurant> getRestaurantDto(String placeId) {
        return getRestaurantsCollection().document(placeId).get().continueWith(data ->
                data.getResult().toObject(Restaurant.class));
    }


    private CollectionReference getRestaurantsCollection() {
        return FirebaseFirestore.getInstance().collection(COLLECTION_RESTAURANTS);
    }


    private void addNewRestaurantIntoFirebase(Restaurant restaurantAdd) {
        // if document doesn't exist into collection set new document
        Log.d(TAG,"addIntoFirebase condition result: " + getRestaurantsCollection().document(restaurantAdd.getId()).get().isSuccessful() + " / placeID: " + restaurantAdd.getId());
        //TODO change this condition it's not correct
        if (getRestaurantsCollection().document(restaurantAdd.getId()).get().isSuccessful())
            getRestaurantsCollection().document(restaurantAdd.getId()).set(restaurantAdd);
    }


    public void updateRestaurant(Restaurant restaurantUpdate) {
        Log.d(TAG,  LOG_INFO + "update restaurant");
        getRestaurantsCollection().document(restaurantUpdate.getId()).set(restaurantUpdate);
    }
}
