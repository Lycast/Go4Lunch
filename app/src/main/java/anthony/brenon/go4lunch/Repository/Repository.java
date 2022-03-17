package anthony.brenon.go4lunch.Repository;


import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.LiveData;

import java.util.Objects;

/**
 * Created by Lycast on 16/03/2022.
 */
public class Repository implements LocationListener {
    private static final String TAG = "repository";

    private static final int PERMS_CALL_ID = 101;
    private LocationManager locationManager;
    private LiveData<Location> locationLiveData;

    public Repository (Application application) {}

    @SuppressLint("MissingPermission")
    private void checkPermissions(Activity activity) {
        if (ActivityCompat.checkSelfPermission(Objects.requireNonNull(activity), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(Objects.requireNonNull(activity), new String[] {
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
            }, PERMS_CALL_ID);
            return;
        }

        locationManager = (LocationManager) Objects.requireNonNull(activity).getSystemService( Context.LOCATION_SERVICE );
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
        //locationPlace = new LocationPlace(location.getLatitude(), location.getLongitude());
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) { }
}
