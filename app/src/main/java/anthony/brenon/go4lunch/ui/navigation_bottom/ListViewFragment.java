package anthony.brenon.go4lunch.ui.navigation_bottom;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import anthony.brenon.go4lunch.databinding.FragmentListViewBinding;
import anthony.brenon.go4lunch.ui.DetailsRestaurantActivity;
import anthony.brenon.go4lunch.ui.adapter.RestaurantsAdapter;
import anthony.brenon.go4lunch.viewmodel.RestaurantViewModel;

public class ListViewFragment extends Fragment {
    private final String TAG = "my_logs";
    private final String LOG_INFO = "ListViewFragment ";

    private FragmentListViewBinding binding;
    RestaurantViewModel restaurantViewModel;
    private final RestaurantsAdapter adapter = new RestaurantsAdapter();


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentListViewBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RecyclerView recyclerView = binding.restaurantsRecyclerView;
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));
        recyclerView.setAdapter(adapter);
        restaurantViewModel = new ViewModelProvider(requireActivity()).get(RestaurantViewModel.class);

        restaurantViewModel.getRestaurantsInstance().observe(this, restaurantsInstance-> {
            //Log.d(TAG,LOG_INFO + "displayRestaurant" + restaurantsInstance);
            adapter.updateDataRestaurants(restaurantsInstance);
        });

//        restaurantViewModel.getRestaurantListDto().observe(this, restaurants -> restaurantsDB = restaurants);
//        sharedViewModel.getNearbyRestaurantsApi().observe(this, restaurants -> {
//            for (Restaurant restaurant : restaurants) {
//                for (Restaurant restaurant1 : restaurantsDB)
//                    if(restaurant.getId().equals(restaurant1.getId()))
//                        restaurant.setUsersChoice(restaurant1.getUsersChoice());
//            }
//            adapter.updateDataRestaurants(restaurants);
//        });

        adapter.setOnItemClickListener(placeId -> {
            Intent intent = new Intent(getActivity(), DetailsRestaurantActivity.class);
            intent.putExtra("place_id", placeId);
            startActivity(intent);
        });
    }
}