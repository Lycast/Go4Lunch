package anthony.brenon.go4lunch.ui.navigation_bottom.map;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;
import java.util.Objects;

import anthony.brenon.go4lunch.R;
import anthony.brenon.go4lunch.model.Location;
import anthony.brenon.go4lunch.model.Restaurant;
import anthony.brenon.go4lunch.viewmodel.SharedViewModel;

public class MapViewFragment extends SupportMapFragment implements OnMapReadyCallback, LocationListener {
    private final String TAG = "my_logs";
    private final String LOG_INFO = "MapViewFragment ";

    private static final int PERMS_CALL_ID = 101;
    private LocationManager locationManager;
    private GoogleMap googleMap;
    private SharedViewModel sharedViewModel;
    private final LatLng POS_DEFAULT = new LatLng(47.060234, -0.884324);
    private LatLng position;
    Location locationUser;


    @Override
    public void onMapReady(@NonNull final GoogleMap googleMap) {
        this.googleMap = googleMap;
        if (checkFineLocationPermission()) {
            getPositionButton();
        } else {
            checkPermissions();
        }
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
            locationManager.requestLocationUpdates( LocationManager.GPS_PROVIDER, 30000, 20, this);
        }
        if (locationManager.isProviderEnabled( LocationManager.PASSIVE_PROVIDER)) {
            locationManager.requestLocationUpdates(LocationManager.PASSIVE_PROVIDER, 30000, 20, this);
        }
        if (locationManager.isProviderEnabled( LocationManager.NETWORK_PROVIDER)) {
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 30000, 20, this);
        }
    }


    private boolean checkFineLocationPermission() {
        String permission = Manifest.permission.ACCESS_FINE_LOCATION;
        int res = Objects.requireNonNull(getContext()).checkCallingOrSelfPermission(permission);
        return (res == PackageManager.PERMISSION_GRANTED);
    }


    @Override
    public void onLocationChanged(@NonNull android.location.Location location) {
        position = new LatLng ( location.getLatitude(), location.getLongitude());
        locationUser = new Location(location.getLatitude(), location.getLongitude());
        sharedViewModel.setLocationUser(locationUser);
        if (getView() != null)
            //TODO fix problem location for wait a good location before call api
            sharedViewModel.getRestaurantsLiveData().observe(getViewLifecycleOwner(), this::displayRestaurants);
        if (googleMap != null)
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(getPosition(), 13));
    }


    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) { }


    @SuppressLint("MissingPermission")
    private void getPositionButton() {
        googleMap.setMyLocationEnabled(true);
        googleMap.getUiSettings().setMyLocationButtonEnabled(true);
    }


    private void displayRestaurants(List<Restaurant> restaurants) {
        for (Restaurant restaurant : restaurants) {
            LatLng restaurantLocation = new LatLng(restaurant.getGeometryPlace().getLocationPlace().getLat(), restaurant.getGeometryPlace().getLocationPlace().getLng());
            googleMap.addMarker(new MarkerOptions()
                    .icon(BitmapFromVector(getContext(), R.drawable.marker_dinner_orange_34))
                    .position(restaurantLocation)
                    .title(restaurant.getName()));
        }
    }


    private LatLng getPosition() {
        if (position == null)
            return POS_DEFAULT;
            return position;
    }

    private BitmapDescriptor BitmapFromVector(Context context, int vectorResId) {
        // below line is use to generate a drawable.
        Drawable vectorDrawable = ContextCompat.getDrawable(context, vectorResId);

        // below line is use to set bounds to our vector drawable.
        vectorDrawable.setBounds(0, 0, vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight());

        // below line is use to create a bitmap for our
        // drawable which we have added.
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);

        // below line is use to add bitmap in our canvas.
        Canvas canvas = new Canvas(bitmap);

        // below line is use to draw our
        // vector drawable in canvas.
        vectorDrawable.draw(canvas);

        // after generating our bitmap we are returning our bitmap.
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }
}