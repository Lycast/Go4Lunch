package anthony.brenon.go4lunch.ui.bottom_navigation.map;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnSuccessListener;

import pub.devrel.easypermissions.EasyPermissions;

public class MapViewFragment extends SupportMapFragment implements OnMapReadyCallback {

    private GoogleMap googleMap;
    private FusedLocationProviderClient fusedLocationClient;
    private static final String TAG = "Fragment_map";


    //3
    @Override
    public void onMapReady(@NonNull final GoogleMap googleMap) {
        this.googleMap = googleMap;

        getPositionButton();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(getActivity());


    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getMapAsync(this);
    }

    @Override
    public void onResume() {
        super.onResume();

        permissionPosition();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //Handle the request result
        Log.d(TAG, "method : onRequestPermissionsResult");
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults,
                this);
    }


    private void permissionPosition() {
        Log.d(TAG, "method : Permission Position");
        // When permission already granted
        // Display snackBar
        if (EasyPermissions.hasPermissions(getContext(), Manifest.permission.ACCESS_FINE_LOCATION)) {
            // Show Snack Bar with a message
            Toast.makeText(getContext(),"Gps position already", Toast.LENGTH_SHORT).show();
            getLocationUser();
        } else {
            // When permission not granted
            // Request permission
            EasyPermissions.requestPermissions(getActivity(),
                    "App needs access to your position",
                    101,
                    Manifest.permission.ACCESS_FINE_LOCATION);
        }
    }


    private void getLocationUser() {
        Log.d(TAG, "method : get Location User");
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return; }
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener( new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        // Got last known location. In some rare situations this can be null.
                        if (location != null) {
                            // Logic to handle location object
                            LatLng position = new LatLng ( location.getLatitude(), location.getLongitude());
                            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom( position , 12));
                        }
                    }
                });
    }

    private void getPositionButton() {
        Log.d(TAG, "method : get position button");
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return; }
        googleMap.setMyLocationEnabled(true);
        googleMap.getUiSettings().setMyLocationButtonEnabled(true);
    }



    // ----- STATES TEST START ùez-----------------------------------------------------------------------------------
    @Override
    public void onPause() {
        super.onPause();
    }
    @Override
    public void onAttach(@NonNull Activity activity) {
        super.onAttach(activity);
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
    }
    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }
    // ----- STATES TEST END -------------------------------------------------------------------------------------
}