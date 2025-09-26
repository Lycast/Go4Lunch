package anthony.brenon.go4lunch.api;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {

    private static final String BASE_URL = "https://maps.googleapis.com/maps/api/place/";

    private static Retrofit retrofitInstance = null;
    private static GooglePlacesApiService apiServiceInstance = null;

    private static Retrofit getRetrofitInstance() {
        if (retrofitInstance == null) {
            retrofitInstance = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create()) // Pour la conversion JSON <-> Objets Java
                    .build();
        }
        return retrofitInstance;
    }

    public static GooglePlacesApiService getGooglePlacesApiService() {
        if (apiServiceInstance == null) {
            apiServiceInstance = getRetrofitInstance().create(GooglePlacesApiService.class);
        }
        return apiServiceInstance;
    }
}
