package anthony.brenon.go4lunch.ui.bottom_navigation.list_view;


import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import anthony.brenon.go4lunch.databinding.FragmentListViewBinding;
import anthony.brenon.go4lunch.model.Restaurant;
import anthony.brenon.go4lunch.ui.adapter.RestaurantsAdapter;
import anthony.brenon.go4lunch.ui.bottom_navigation.map.SharedViewModel;

public class ListViewFragment extends Fragment {

    private FragmentListViewBinding binding;
    SharedViewModel sharedViewModel;
    List< Restaurant> restaurantList;

    private final RestaurantsAdapter adapter = new RestaurantsAdapter();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentListViewBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        Log.d("listView__","onViewCreated");
        super.onViewCreated(view, savedInstanceState);
        //checkPermissions();

        RecyclerView listRestaurants = binding.restaurantsRecyclerView;
        listRestaurants.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));
        listRestaurants.setAdapter(adapter);

        sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
        sharedViewModel.getRestaurantsLiveData().observe(getViewLifecycleOwner(), adapter::updateData);
        //mapViewModel.getRestaurantsLiveData().observe(this, restaurantList);
        //Log.d("TAG__", restaurantList.toString());
    }

    /*@SuppressLint("MissingPermission")
    private void checkPermissions() {
        Log.d("listView__","checkPermissions");
        if (ActivityCompat.checkSelfPermission(Objects.requireNonNull(getContext()), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(Objects.requireNonNull(getActivity()), new String[] {
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
            }, PERMS_CALL_ID);
            return;
        }

        locationManager = (LocationManager) Objects.requireNonNull(getActivity()).getSystemService( Context.LOCATION_SERVICE );
        if (locationManager.isProviderEnabled( LocationManager.GPS_PROVIDER)) {
            locationManager.requestLocationUpdates( LocationManager.GPS_PROVIDER, 30000, 0, this);
        }
        if (locationManager.isProviderEnabled( LocationManager.PASSIVE_PROVIDER)) {
            locationManager.requestLocationUpdates(LocationManager.PASSIVE_PROVIDER, 30000, 0, this);
        }
        if (locationManager.isProviderEnabled( LocationManager.NETWORK_PROVIDER)) {
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 30000, 0, this);
        }
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        Log.d("TAG__","onLocationChanged");
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) { }*/
}