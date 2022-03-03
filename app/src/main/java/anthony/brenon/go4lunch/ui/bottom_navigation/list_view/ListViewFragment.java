package anthony.brenon.go4lunch.ui.bottom_navigation.list_view;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.List;

import anthony.brenon.go4lunch.api.JsonPlaceHolderApi;
import anthony.brenon.go4lunch.databinding.FragmentListViewBinding;
import anthony.brenon.go4lunch.model.Restaurant;
import anthony.brenon.go4lunch.model.placedetails.GooglePlaceNearbyResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ListViewFragment extends Fragment {

    private FragmentListViewBinding binding;

    private static final String TAG = "list_view_fragment";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentListViewBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://maps.googleapis.com/maps/api/place/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        JsonPlaceHolderApi jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi.class);

        Call<GooglePlaceNearbyResponse> call = jsonPlaceHolderApi.getRestaurants();



        call.enqueue(new Callback<GooglePlaceNearbyResponse>() {

            @Override
            public void onResponse(@NonNull Call<GooglePlaceNearbyResponse> call, @NonNull Response<GooglePlaceNearbyResponse> response) {
                Log.d(TAG, "onResponse: ");
                if(!response.isSuccessful()){
                    binding.tvRestaurant.setText("code: " + response.code());
                    return;
                }
                List<Restaurant> restaurants = response.body().getResults();

                for (Restaurant restaurant : restaurants) {
                    String content = "";
                    content += "ID: " + restaurant.getId() + "\n";
                    content += "Name: " + restaurant.getName() + "\n";
                    content += "Address: " + restaurant.getAddress() + "\n\n";

                    binding.tvRestaurant.append(content);
                    Log.d(TAG, "Restaurant: " + restaurant);
                }
            }

            @Override
            public void onFailure(@NonNull Call<GooglePlaceNearbyResponse> call, @NonNull Throwable t) {
                Log.d(TAG, "onFailure: ");
                binding.tvRestaurant.setText(t.getMessage());
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}