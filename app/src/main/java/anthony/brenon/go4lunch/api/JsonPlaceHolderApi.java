package anthony.brenon.go4lunch.api;


import anthony.brenon.go4lunch.BuildConfig;
import anthony.brenon.go4lunch.model.googleplace_models.PlaceNearbyResponse;
import anthony.brenon.go4lunch.model.googleplace_models.PlaceResponse;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Lycast on 24/02/2022.
 */
public interface JsonPlaceHolderApi {


    @GET("nearbysearch/json?type=restaurant&key=" + BuildConfig.MAPS_API_KEY)
    Call<PlaceNearbyResponse> getApiNearbyRestaurantResponse(@Query("location") String location, @Query("radius") String radius);


    @GET("details/json?key=" + BuildConfig.MAPS_API_KEY)
    Call<PlaceResponse> getApiDetailsResponse(@Query("place_id") String place_id );


    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("https://maps.googleapis.com/maps/api/place/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();
}