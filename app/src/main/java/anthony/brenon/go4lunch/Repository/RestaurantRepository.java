package anthony.brenon.go4lunch.Repository;

import static anthony.brenon.go4lunch.api.JsonPlaceHolderApi.retrofit;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

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
    private final JsonPlaceHolderApi jsonPlaceHolderApi;
    private final MutableLiveData<List<Restaurant>> liveDataRestaurant = new MutableLiveData<>();
    //TODO put there variable into shared view model radius setting 100m - 3000m
    String radius = "1000";


    public RestaurantRepository() {
        jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi.class);
    }


    // GOOGLE API
    public void getDetailsRestaurantApi(String place_id, Callback<PlaceResponse> callback) {
        Call<PlaceResponse> restaurantCall = jsonPlaceHolderApi.getApiDetailsResponse(place_id);
        restaurantCall.enqueue(callback);
    }

    public void callNearbyRestaurantsApi(final Location locationUser) {
        getListRestaurant().addOnSuccessListener(restaurantsDb -> {

            Call<PlaceNearbyResponse> call = jsonPlaceHolderApi.getApiNearbyRestaurantResponse(locationUser.toString(), radius);
            call.enqueue(new Callback<PlaceNearbyResponse>() {

                @Override
                public void onResponse(@NonNull Call<PlaceNearbyResponse> call,
                                       @NonNull Response<PlaceNearbyResponse> response) {
                    if (response.isSuccessful()) {
                        try {
                            List<Restaurant> restaurantsAPI = response.body().getResults();
                            Comparator<Restaurant> c = (u1, u2) -> {
                                return u1.getId().compareTo(u2.getId());
                            };
                            for (Restaurant restaurant : restaurantsAPI) {
                                restaurant.setDistance(locationUser);

                                int indexRestaurant = Arrays.binarySearch(restaurantsDb.toArray(new Restaurant[restaurantsDb.size()]), restaurant, c);
                                if (indexRestaurant >= 0) {
                                    restaurant.setUsersChoice(restaurantsDb.get(indexRestaurant).getUsersChoice());
                                } else {
                                    restaurant.setUsersChoice(Collections.emptyList());
                                }
                            }
                            updateFirebase(restaurantsAPI);
                            liveDataRestaurant.postValue(restaurantsAPI);
                        } catch (Exception e) {
                            call.cancel();
                            Log.d(TAG, LOG_INFO + "call cancel exception: " + e.getMessage());
                        }
                    }
                }

                @Override
                public void onFailure(@NonNull Call<PlaceNearbyResponse> call, @NonNull Throwable t) {
                    Log.d(TAG, LOG_INFO + "onFailure: " + t.getMessage());
                }
            });
        });
    }

    public LiveData<List<Restaurant>> getLiveDataListRestaurant() {
        return liveDataRestaurant;
    }

    public Task<Restaurant> getRestaurantFS(String placeId) {
        return getRestaurantsCollection().document(placeId).get().continueWith(data ->
                data.getResult().toObject(Restaurant.class));
    }

    public Task<List<Restaurant>> getListRestaurant() {
        return getRestaurantsCollection().get().continueWith(data ->
                data.getResult().toObjects(Restaurant.class));
    }

    private CollectionReference getRestaurantsCollection() {
        return FirebaseFirestore.getInstance().collection(COLLECTION_RESTAURANTS);
    }


    // UPDATE
    public Task<Void> updateRestaurantIntoFS(Restaurant restaurantUpdate) {
        return getRestaurantsCollection().document(restaurantUpdate.getId()).set(restaurantUpdate);
    }

    private void updateFirebase(List<Restaurant> restaurantList) {
        for (Restaurant restaurant : restaurantList) {
            getRestaurantFS(restaurant.getId()).addOnSuccessListener(data -> {
                if (data == null) {
                    getRestaurantsCollection().document(restaurant.getId()).set(restaurant);
                } else if (data.getUsersChoice().size() > 0 && restaurant.getUsersChoice().isEmpty()) {
                    restaurant.setUsersChoice(data.getUsersChoice());
                }
                getRestaurantsCollection().document(restaurant.getId()).set(restaurant);
            });
        }
    }
}