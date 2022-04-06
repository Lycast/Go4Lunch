package anthony.brenon.go4lunch.ui.navigation_bottom;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;

import anthony.brenon.go4lunch.R;
import anthony.brenon.go4lunch.model.Restaurant;
import anthony.brenon.go4lunch.viewmodel.RestaurantViewModel;

public class MapViewFragment extends SupportMapFragment implements OnMapReadyCallback {
    private final String TAG = "my_logs";
    private final String LOG_INFO = "MapViewFragment ";

    private GoogleMap googleMap;
    private RestaurantViewModel restaurantViewModel;

    @SuppressLint("MissingPermission")
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        //Log.d(TAG,LOG_INFO + "onCreate");
        super.onCreate(savedInstanceState);
        //checkPermissions();
//        if (checkFineLocationPermission()) {
//            //startLocationManager();
//        }
        restaurantViewModel = new ViewModelProvider(requireActivity()).get(RestaurantViewModel.class);
    }


    @Override
    public void onMapReady(@NonNull final GoogleMap googleMap) {
        Log.d(TAG,LOG_INFO + "onMapReady");
        this.googleMap = googleMap;
        googleMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(requireContext(), R.raw.style_map));
            restaurantViewModel.getLatLngUser().observe(getViewLifecycleOwner(), pos-> {
                    googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(pos, 15));
                    getPositionButton();
                    displayRestaurants();
            });

    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //Log.d(TAG,LOG_INFO + "onViewCreated");
        getMapAsync(this);
    }


    @Override
    public void onResume() {
        super.onResume();
        //Log.d(TAG,LOG_INFO + "onResume");
        //displayRestaurants();
    }


    @SuppressLint("MissingPermission")
    @Override
    public void onPause() {
        super.onPause();
        //Log.d(TAG,LOG_INFO + "onPause");
//        if(locationManager != null) {
//            locationManager.removeUpdates(this);
//        }
    }


    @SuppressLint("MissingPermission")
    private void getPositionButton() {
        //Log.d(TAG,LOG_INFO + "getPositionButton");
        googleMap.setMyLocationEnabled(true);
        googleMap.getUiSettings().setMyLocationButtonEnabled(true);
    }

//    private boolean checkFineLocationPermission() {
//        //Log.d(TAG,LOG_INFO + "checkFineLocationPermission");
//        String permission = Manifest.permission.ACCESS_FINE_LOCATION;
//        int res = Objects.requireNonNull(getContext()).checkCallingOrSelfPermission(permission);
//        return (res == PackageManager.PERMISSION_GRANTED);
//    }

//    // --------------------- POSITION GPS START ----------------------------- //
//
//    @SuppressLint("MissingPermission")
//    private void checkPermissions() {
//        //Log.d(TAG,LOG_INFO + "checkPermission");
//        if (ActivityCompat.checkSelfPermission(Objects.requireNonNull(getContext()), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
//                ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            ActivityCompat.requestPermissions(Objects.requireNonNull(getActivity()), new String[]{
//                    Manifest.permission.ACCESS_FINE_LOCATION,
//                    Manifest.permission.ACCESS_COARSE_LOCATION
//            }, PERMS_CALL_ID);
//        }
//    }
//
//
//    @SuppressLint("MissingPermission")
//    private void startLocationManager() {
//        locationManager = (LocationManager) Objects.requireNonNull(getActivity()).getSystemService( Context.LOCATION_SERVICE );
//        if (locationManager.isProviderEnabled( LocationManager.GPS_PROVIDER)) {
//            locationManager.requestLocationUpdates( LocationManager.GPS_PROVIDER, 30000, 0, this);
//        }
//        if (locationManager.isProviderEnabled( LocationManager.PASSIVE_PROVIDER)) {
//            locationManager.requestLocationUpdates(LocationManager.PASSIVE_PROVIDER, 30000, 0, this);
//        }
//        if (locationManager.isProviderEnabled( LocationManager.NETWORK_PROVIDER)) {
//            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 30000, 0, this);
//        }
//    }
//
//
//    private boolean checkFineLocationPermission() {
//        //Log.d(TAG,LOG_INFO + "checkFineLocationPermission");
//        String permission = Manifest.permission.ACCESS_FINE_LOCATION;
//        int res = Objects.requireNonNull(getContext()).checkCallingOrSelfPermission(permission);
//        return (res == PackageManager.PERMISSION_GRANTED);
//    }
//
//
//    @Override
//    public void onLocationChanged(@NonNull android.location.Location location) {
//        //Log.d(TAG,LOG_INFO + "onLocationChanged");
//        Location locationUser = new Location(location.getLatitude(), location.getLongitude());
//        //Log.d(TAG,LOG_INFO + "onLocationChanged locUser: " + locationUser);
//        restaurantViewModel.setLocationUser(locationUser);
//    }
//
//
//    @Override
//    public void onStatusChanged(String provider, int status, Bundle extras) { }
//
//    // --------------------- POSITION GPS END ----------------------------- //


//        String newStatus = "";
//        switch (status) {
//            case LocationProvider.OUT_OF_SERVICE:
//                newStatus = "OUT_OF_SERVICE";
//                break;
//            case LocationProvider.TEMPORARILY_UNAVAILABLE:
//                newStatus = "TEMPORARILY_UNAVAILABLE";
//                break;
//            case LocationProvider.AVAILABLE:
//                newStatus = "AVAILABLE";
//                break;
//        }
//        Log.i(TAG, "onStatusChanged " + newStatus);

    private void displayRestaurants() {
        restaurantViewModel.getRestaurantsInstance().observe(this, restaurantsInstance -> {
            //Log.d(TAG,LOG_INFO + "displayRestaurant" + restaurantsInstance);
            for(Restaurant restaurant : restaurantsInstance) {
                    if (restaurant.getUsersChoice().isEmpty()) {
                        LatLng restaurantLocation = new LatLng(restaurant.getGeometryPlace().getLocationPlace().getLat(), restaurant.getGeometryPlace().getLocationPlace().getLng());
                        googleMap.addMarker(new MarkerOptions()
                                .icon(BitmapFromVector(getContext(), R.drawable.ic_marker))
                                .position(restaurantLocation)
                                .title(restaurant.getName()));
                    } else {
                        LatLng restaurantLocation = new LatLng(restaurant.getGeometryPlace().getLocationPlace().getLat(), restaurant.getGeometryPlace().getLocationPlace().getLng());
                        googleMap.addMarker(new MarkerOptions()
                                .icon(BitmapFromVector(getContext(), R.drawable.ic_marker_green))
                                .position(restaurantLocation)
                                .title(restaurant.getName()));
                    }
                }
        });
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