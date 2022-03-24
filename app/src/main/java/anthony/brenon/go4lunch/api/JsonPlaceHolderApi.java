package anthony.brenon.go4lunch.api;


import anthony.brenon.go4lunch.BuildConfig;
import anthony.brenon.go4lunch.model.googleplace_models.GooglePlaceResponse;
import anthony.brenon.go4lunch.model.googleplace_models.PlaceDetails;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Lycast on 24/02/2022.
 */
public interface JsonPlaceHolderApi {

    @GET("nearbysearch/json?type=restaurant&key=" + BuildConfig.MAPS_API_KEY)
    Call<GooglePlaceResponse> getApiNearbyRestaurantResponse(@Query("location") String location, @Query("radius") String radius);

    @GET("details/json?key=" + BuildConfig.MAPS_API_KEY)
    Call<PlaceDetails> getApiDetailsResponse(@Query("place_id") String place_id );
}