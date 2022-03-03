package anthony.brenon.go4lunch.api;


import anthony.brenon.go4lunch.model.placedetails.GooglePlaceNearbyResponse;
import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by Lycast on 24/02/2022.
 */
public interface JsonPlaceHolderApi {

    @GET("nearbysearch/json?keyword=cruise&location=-33.8670522%2C151.1957362&radius=1500&type=restaurant&key=AIzaSyDxwT81XIPCXAkmy5kC6Rr_TgvpMTbTiUw")
    Call<GooglePlaceNearbyResponse> getRestaurants();
}
