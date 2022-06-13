package anthony.brenon.go4lunch.api;


import anthony.brenon.go4lunch.BuildConfig;
import anthony.brenon.go4lunch.model.googleplace_models.PlacesNearbyResponse;
import anthony.brenon.go4lunch.model.googleplace_models.PlaceResponse;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Lycast on 24/02/2022.
 * This is our interface that will allow us to communicate with our google api.
 * We use Retrofit to make our calls
 */
public interface JsonPlaceHolderApi {

    @GET("nearbysearch/json?type=restaurant&key=" + BuildConfig.MAPS_API_KEY)
    Call<PlacesNearbyResponse> getApiNearbyRestaurantResponse(@Query("location") String location, @Query("radius") String radius);

    @GET("details/json?key=" + BuildConfig.MAPS_API_KEY)
    Call<PlaceResponse> getApiDetailsResponse(@Query("place_id") String place_id);

    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("https://maps.googleapis.com/maps/api/place/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();
}