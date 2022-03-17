package anthony.brenon.go4lunch.ui.bottom_navigation.map;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;
import java.util.Objects;

import anthony.brenon.go4lunch.model.Restaurant;
import anthony.brenon.go4lunch.model.googleplace_models.LocationPlace;

public class MapViewFragment extends SupportMapFragment implements OnMapReadyCallback, LocationListener {

    private static final int PERMS_CALL_ID = 101;
    private LocationManager locationManager;
    private GoogleMap googleMap;
    private SharedViewModel sharedViewModel;
    private static final String TAG = "Fragment_map";


    @Override
    public void onMapReady(@NonNull final GoogleMap googleMap) {
        this.googleMap = googleMap;

        getPositionButton();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getMapAsync(this);
    }

    @Override
    public void onResume() {
        super.onResume();

        checkPermissions();
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onPause() {
        super.onPause();

        if(locationManager != null) {
            locationManager.removeUpdates(this);
        }
    }

    @SuppressLint("MissingPermission")
    private void checkPermissions() {
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
        LatLng position = new LatLng ( location.getLatitude(), location.getLongitude());
        LocationPlace locationPlace = new LocationPlace(location.getLatitude(), location.getLongitude());
        sharedViewModel.setLocation(locationPlace);
        sharedViewModel.getRestaurantsLiveData().observe(getViewLifecycleOwner(), this::displayRestaurants);
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom( position , 15));
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) { }

    @SuppressLint("MissingPermission")
    private void getPositionButton() {
        if (ActivityCompat.checkSelfPermission(Objects.requireNonNull(getContext()), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return; }
        googleMap.setMyLocationEnabled(true);
        googleMap.getUiSettings().setMyLocationButtonEnabled(true);
    }

    private void displayRestaurants(List<Restaurant> restaurants) {
        for (Restaurant restaurant : restaurants) {
            LatLng restaurantLocation = new LatLng(restaurant.getGeometryPlace().getLocationPlace().getLat(), restaurant.getGeometryPlace().getLocationPlace().getLng());
            googleMap.addMarker(new MarkerOptions()
                    .position(restaurantLocation)
                    .title(restaurant.getName()));
        }
    }
}