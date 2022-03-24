package anthony.brenon.go4lunch.Repository;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;
import java.util.List;

import anthony.brenon.go4lunch.api.JsonPlaceHolderApi;
import anthony.brenon.go4lunch.model.Location;
import anthony.brenon.go4lunch.model.Restaurant;
import anthony.brenon.go4lunch.model.googleplace_models.GooglePlaceResponse;
import anthony.brenon.go4lunch.model.googleplace_models.PlaceDetails;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Lycast on 24/02/2022.
 */
public class RestaurantRepository {

    private static final String TAG = "repository_restaurant";
    private List<Restaurant> restaurants = new ArrayList<>();
    JsonPlaceHolderApi jsonPlaceHolderApi;

    //TODO put there variable into shared view model radius setting
    String radius = "2000";


    public RestaurantRepository () {

        Log.d("getExtra", "construct ");
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://maps.googleapis.com/maps/api/place/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();
        jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi.class);
    }









    public List<Restaurant> getAllRestaurantsList(Location locationUser) {

        List<Restaurant> result = new ArrayList<>();
        Call<GooglePlaceResponse> call = jsonPlaceHolderApi.getApiNearbyRestaurantResponse(locationUser.toString(), radius);

        call.enqueue(new Callback<GooglePlaceResponse>() {

            @Override
            public void onResponse(@NonNull Call<GooglePlaceResponse> call,
                                   @NonNull Response<GooglePlaceResponse> response) {
                if(response.isSuccessful()){
                    restaurants = response.body().getResults();
                    for (Restaurant restaurant : restaurants) {
                        restaurant.setDistance(locationUser);
                    }
                    result.addAll(restaurants);
                }
            }
            @Override
            public void onFailure(@NonNull Call<GooglePlaceResponse> call, @NonNull Throwable t) {
                Log.d(TAG, "onFailure: " + t.getMessage());
            }
        });
        return result;
    }

    public MutableLiveData<Restaurant> getDetailsRestaurant(String place_id) {

        MutableLiveData<Restaurant> restaurant = new MutableLiveData<>();

        Call<PlaceDetails> restaurantCall = jsonPlaceHolderApi.getApiDetailsResponse(place_id);

        restaurantCall.enqueue(new Callback<PlaceDetails>() {
            @Override
            public void onResponse(Call<PlaceDetails> call, Response<PlaceDetails> response) {
                Log.d("getExtra", "onResponse ");
                if (response.isSuccessful()) {
                    restaurant.postValue(response.body().getResults());
                }
            }
            @Override
            public void onFailure(Call<PlaceDetails> call, Throwable t) {
                Log.d("getExtra", "onFailure: " + t.getMessage());
            }
        });
        return restaurant;
    }
}
