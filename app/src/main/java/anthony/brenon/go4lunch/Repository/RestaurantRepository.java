package anthony.brenon.go4lunch.Repository;

import static anthony.brenon.go4lunch.api.JsonPlaceHolderApi.retrofit;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

import anthony.brenon.go4lunch.api.JsonPlaceHolderApi;
import anthony.brenon.go4lunch.model.Location;
import anthony.brenon.go4lunch.model.Restaurant;
import anthony.brenon.go4lunch.model.googleplace_models.PlaceNearbyResponse;
import anthony.brenon.go4lunch.model.googleplace_models.PlaceResponse;
import anthony.brenon.go4lunch.utils.SortMethod;
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
    private MutableLiveData<List<Restaurant>> liveDataRestaurant = new MutableLiveData<>();
    private List<Restaurant> restaurantList = new ArrayList<>();
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
        Log.d("my_logs", " -callNearbyRestaurantsApi- ");
        if(locationUser != null) {
            getListRestaurant().addOnSuccessListener(restaurantsDb -> {
                Call<PlaceNearbyResponse> call = jsonPlaceHolderApi.getApiNearbyRestaurantResponse(locationUser.toString(), radius);
                call.enqueue(new Callback<PlaceNearbyResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<PlaceNearbyResponse> call,
                                           @NonNull Response<PlaceNearbyResponse> response) {
                        if (response.isSuccessful()) {
                            try {
                                List<Restaurant> fbRestaurants = Objects.requireNonNull(response.body()).getResults();
                                Comparator<Restaurant> c = (u1, u2) -> {
                                    return u1.getId().compareTo(u2.getId());
                                };
                                for (Restaurant restaurant : fbRestaurants) {
                                    restaurant.setDistance(locationUser);
                                    int indexRestaurant = Arrays.binarySearch(restaurantsDb.toArray(new Restaurant[restaurantsDb.size()]), restaurant, c);
                                    if(indexRestaurant >= 0) {
                                        restaurant.setUsersChoice(restaurantsDb.get(indexRestaurant).getUsersChoice());
                                    } else {
                                        restaurant.setUsersChoice(Collections.emptyList());
                                    }
                                }
                                updateFirebase(fbRestaurants);
                                liveDataRestaurant.postValue(fbRestaurants);
                                restaurantList = new ArrayList<>(fbRestaurants);
                            } catch (Exception e) {
                                call.cancel();
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
    }


    // GETS
    public LiveData<List<Restaurant>> getLiveDataListRestaurant(){

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
    public void updateRestaurantIntoFS(Restaurant restaurantUpdate) {
        getRestaurantsCollection().document(restaurantUpdate.getId()).set(restaurantUpdate);
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

    public void sortMethodRestaurantsList(int sortOption) {
        switch (sortOption) {
            case SortMethod.BY_DISTANCE:
                Collections.sort(restaurantList, new Restaurant.RestaurantDistanceComp());
                break;
            case SortMethod.BY_RATING:
                Collections.sort(restaurantList, new Restaurant.RestaurantRatingComp());
                break;
            case SortMethod.BY_WORKMATES:
                Collections.sort(restaurantList, new Restaurant.RestaurantWorkmatesComp());
                break;
            case SortMethod.BY_OPENING:
                //Collections.sort(restaurantList, new Restaurant.RestaurantOpeningComp());
                break;
            default:
                return;
        }
        liveDataRestaurant.postValue(restaurantList);
        Log.d("my_logs", " -sortMethodRestaurantsList- " + restaurantList.toString());
    }
}