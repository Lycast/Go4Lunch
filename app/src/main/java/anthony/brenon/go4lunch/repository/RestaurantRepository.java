package anthony.brenon.go4lunch.repository;

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
import anthony.brenon.go4lunch.model.googleplace_models.PlacesNearbyResponse;
import anthony.brenon.go4lunch.model.googleplace_models.PlaceResponse;
import anthony.brenon.go4lunch.utils.SortMethod;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Lycast on 24/02/2022.
 * The repository layer of our MVVM architecture Who will get the necessary information from the restaurants
 */
public class RestaurantRepository {
    private static final String TAG = "DEBUG_LOG";
    private final String LOG_INFO = "RestaurantRepository ";

    private static final String COLLECTION_RESTAURANTS = "restaurants";
    private final JsonPlaceHolderApi jsonPlaceHolderApi;
    private final MutableLiveData<List<Restaurant>> liveDataRestaurant = new MutableLiveData<>();
    private List<Restaurant> restaurantList = new ArrayList<>();
    private List<Restaurant> restaurantListFiltered = new ArrayList<>();

    public RestaurantRepository() {
        jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi.class);
    }

    // GOOGLE API
    // The restaurant details call to the google api which returns the details of a restaurant
    public void callDetailsRestaurantApi(String place_id, Callback<PlaceResponse> callback) {
        Call<PlaceResponse> restaurantCall = jsonPlaceHolderApi.getApiDetailsResponse(place_id);
        restaurantCall.enqueue(callback);
    }

    // The call of nearby restaurants to the google api which returns all the restaurants in a perimeter
    public void callNearbyRestaurantsApi(final Location locationUser, String radius) {
        if(locationUser != null) {
            getRestaurantsDatabase().addOnSuccessListener(restaurantsDb -> {
                Call<PlacesNearbyResponse> call = jsonPlaceHolderApi.getApiNearbyRestaurantResponse(locationUser.toString(), radius);
                call.enqueue(new Callback<PlacesNearbyResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<PlacesNearbyResponse> call,
                                           @NonNull Response<PlacesNearbyResponse> response) {
                        if (response.isSuccessful()) {
                            try {
                                List<Restaurant> fbRestaurants = Objects.requireNonNull(response.body()).getResults();
                                // We are going to implement some attributes to the restaurants returned from our google api which are saved in our firebase
                                // and then return our list as livedata
                                setUserChoiceToRestaurants(fbRestaurants, locationUser, restaurantsDb);
                                updateRestaurantsDatabase(fbRestaurants);
                                liveDataRestaurant.postValue(fbRestaurants);
                                restaurantList = new ArrayList<>(fbRestaurants);
                            } catch (Exception e) {
                                call.cancel();
                            }
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<PlacesNearbyResponse> call, @NonNull Throwable t) {
                        Log.d(TAG, LOG_INFO + "onFailure: " + t.getMessage());
                    }
                });
            });
        }
    }

    public void setUserChoiceToRestaurants(List<Restaurant> fbRestaurants, Location locationUser, List<Restaurant> restaurantsDb) {
        Comparator<Restaurant> c = (u1, u2) -> u1.getId().compareTo(u2.getId());
        for (Restaurant restaurant : fbRestaurants) {
            // We add the distance attribute to a restaurant
            restaurant.setDistance(locationUser);
            int indexRestaurant = Arrays.binarySearch(restaurantsDb.toArray(new Restaurant[restaurantsDb.size()]), restaurant, c);
            if(indexRestaurant >= 0) {
                // We add the list of users who have chosen this restaurant
                restaurant.setUsersChoice(restaurantsDb.get(indexRestaurant).getUsersChoice());
            } else {
                restaurant.setUsersChoice(Collections.emptyList());
            }
        }
    }

    // Updating restaurants from a list
    private void updateRestaurantsDatabase(List<Restaurant> restaurantList) {
        for (Restaurant restaurant : restaurantList) {
            getRestaurantDatabase(restaurant.getId()).addOnSuccessListener(data -> {
                if (data == null) {
                    getRestaurantsCollection().document(restaurant.getId()).set(restaurant);
                } else if (data.getUsersChoice().size() > 0 && restaurant.getUsersChoice().isEmpty()) {
                    restaurant.setUsersChoice(data.getUsersChoice());
                }
                getRestaurantsCollection().document(restaurant.getId()).set(restaurant);
            });
        }
    }

    private Task<List<Restaurant>> getRestaurantsDatabase() {
        return getRestaurantsCollection().get().continueWith(data ->
                data.getResult().toObjects(Restaurant.class));
    }

    private CollectionReference getRestaurantsCollection() {
        return FirebaseFirestore.getInstance().collection(COLLECTION_RESTAURANTS);
    }


    // The methods that will be called by our application
    public LiveData<List<Restaurant>> getLiveDataRestaurants(){
        return liveDataRestaurant;
    }

    public Task<Restaurant> getRestaurantDatabase(String placeId) {
        return getRestaurantsCollection().document(placeId).get().continueWith(data ->
                data.getResult().toObject(Restaurant.class));
    }

    public void updateRestaurantDatabase(Restaurant restaurantUpdate) {
        getRestaurantsCollection().document(restaurantUpdate.getId()).set(restaurantUpdate);
    }

    public void sortMethodRestaurantsList(int sortOption) {
        switch (sortOption) {
            case SortMethod.BY_DISTANCE:
                Collections.sort(restaurantList, Restaurant.compareDistance);
                liveDataRestaurant.postValue(restaurantList);
                break;
            case SortMethod.BY_RATING:
                Collections.sort(restaurantList, Restaurant.compareRating);
                liveDataRestaurant.postValue(restaurantList);
                break;
            case SortMethod.BY_WORKMATES:
                Collections.sort(restaurantList, Restaurant.compareWorkmate);
                liveDataRestaurant.postValue(restaurantList);
                break;
            case SortMethod.BY_OPENING:
                Collections.sort(getRestaurantListWithOpeningTime(), Restaurant.compareOpening);
                liveDataRestaurant.postValue(restaurantListFiltered);
                break;
        }
    }

    // It's possible restaurant don't have opening time we delete them form the list
    private List<Restaurant> getRestaurantListWithOpeningTime() {
        restaurantListFiltered = new ArrayList<>();
        for (Restaurant restaurant : restaurantList) {
            if (restaurant.getOpeningHours() != null) {
                restaurantListFiltered.add(restaurant);
            }
        }
        return restaurantListFiltered;
    }
}