package anthony.brenon.go4lunch.ui.navigation_bottom;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
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
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import anthony.brenon.go4lunch.R;
import anthony.brenon.go4lunch.model.Restaurant;
import anthony.brenon.go4lunch.ui.DetailsRestaurantActivity;
import anthony.brenon.go4lunch.viewmodel.MainActivityViewModel;

public class MapViewFragment extends SupportMapFragment implements OnMapReadyCallback {

    private GoogleMap googleMap;
    private MainActivityViewModel viewModel;


    @SuppressLint("MissingPermission")
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(requireActivity()).get(MainActivityViewModel.class);
    }


    @Override
    public void onMapReady(@NonNull final GoogleMap googleMap) {
        this.googleMap = googleMap;
        googleMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(requireContext(), R.raw.style_map));
        viewModel.getLatLngLiveData().observe(getViewLifecycleOwner(), pos -> {
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(pos, 15));
            getPositionButton();
            displayRestaurants();
        });

        setOnClickInfoWindowListenerMarker();
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getMapAsync(this);
    }

    private void setOnClickInfoWindowListenerMarker() {
        Intent intent = new Intent(requireActivity(), DetailsRestaurantActivity.class);
        googleMap.setOnInfoWindowClickListener(marker -> {
                intent.putExtra("place_id", marker.getTag().toString());
                MapViewFragment.this.startActivity(intent);
        });
    }

    @SuppressLint("MissingPermission")
    private void getPositionButton() {
        googleMap.setMyLocationEnabled(true);
        googleMap.getUiSettings().setMyLocationButtonEnabled(true);
    }

    private void displayRestaurants() {
        viewModel.getLiveDataListRestaurants().observe(this, restaurantsInstance -> {
            for (Restaurant restaurant : restaurantsInstance) {
                if (restaurant.getUsersChoice().isEmpty()) {
                    LatLng restaurantLocation = new LatLng(restaurant.getGeometryPlace().getLocationPlace().getLat(), restaurant.getGeometryPlace().getLocationPlace().getLng());
                    Marker marker = googleMap.addMarker(new MarkerOptions()
                            .icon(BitmapFromVector(getContext(), R.drawable.ic_marker_orange))
                            .position(restaurantLocation)
                            .title(restaurant.getName()));
                    if (marker != null) { marker.setTag(restaurant.getId()); }
                } else {
                    LatLng restaurantLocation = new LatLng(restaurant.getGeometryPlace().getLocationPlace().getLat(), restaurant.getGeometryPlace().getLocationPlace().getLng());
                    Marker marker = googleMap.addMarker(new MarkerOptions()
                            .icon(BitmapFromVector(getContext(), R.drawable.ic_marker_green))
                            .position(restaurantLocation)
                            .title(restaurant.getName()));
                    if (marker != null) { marker.setTag(restaurant.getId()); }
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