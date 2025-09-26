package anthony.brenon.go4lunch.api;


import anthony.brenon.go4lunch.model.googleplace_models.PlacesNearbyResponse;
import anthony.brenon.go4lunch.model.googleplace_models.PlaceResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Lycast on 24/02/2022.
 * This is our interface that will allow us to communicate with our google api.
 * We use Retrofit to make our calls
 */
public interface GooglePlacesApiService {

    @GET("nearbysearch/json")
    Call<PlacesNearbyResponse> getNearbyRestaurants(
            @Query("location") String location,
            @Query("radius") String radius,
            @Query("type") String type,
            @Query("key") String apiKey
    );

    @GET("details/json") //
    Call<PlaceResponse> getPlaceDetails(
            @Query("place_id") String placeId,
            @Query("key") String apiKey
    );
}