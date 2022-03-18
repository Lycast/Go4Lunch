package anthony.brenon.go4lunch.Repository;

import android.util.Log;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import anthony.brenon.go4lunch.api.JsonPlaceHolderApi;
import anthony.brenon.go4lunch.model.Restaurant;
import anthony.brenon.go4lunch.model.googleplace_models.GooglePlaceNearbyResponse;
import anthony.brenon.go4lunch.model.googleplace_models.LocationPlace;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Lycast on 24/02/2022.
 */
public class RestaurantRepository {

    //"47.089505," + "-1.286154"

    private static final String TAG = "repository_restaurant";
    private List<Restaurant> restaurants = new ArrayList<>();



    //TODO put there variable into model and get the position and radius setting
    //Location location = new Location(47.089505,-1.286154);
    String radius = "2000";



    public RestaurantRepository () {}

    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("https://maps.googleapis.com/maps/api/place/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    JsonPlaceHolderApi jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi.class);

    public List<Restaurant> getAllRestaurantsList(LocationPlace locationPlace) {

        Log.d(TAG, "getAllRestaurants");

        List<Restaurant> result = new ArrayList<>();
        Call<GooglePlaceNearbyResponse> call = jsonPlaceHolderApi.getApiNearbyRestaurantResponse(locationPlace.toString(), radius);

        call.enqueue(new Callback<GooglePlaceNearbyResponse>() {

            @Override
            public void onResponse(@NonNull Call<GooglePlaceNearbyResponse> call,
                                   @NonNull Response<GooglePlaceNearbyResponse> response) {
                if(response.isSuccessful()){
                    Log.d(TAG, "onResponse: " + Objects.requireNonNull(response.body()).getResults());
                    restaurants = response.body().getResults();
                    result.addAll(restaurants);
                }
            }

            @Override
            public void onFailure(@NonNull Call<GooglePlaceNearbyResponse> call, @NonNull Throwable t) {
                Log.d(TAG, "onFailure: " + t.getMessage());
            }
        });
        return result;
    }
}
