package anthony.brenon.go4lunch.ui;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.common.api.Status;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.TypeFilter;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import anthony.brenon.go4lunch.BuildConfig;
import anthony.brenon.go4lunch.R;
import anthony.brenon.go4lunch.databinding.ActivityMainBinding;
import anthony.brenon.go4lunch.model.Location;
import anthony.brenon.go4lunch.ui.navigation_bottom.ListViewFragment;
import anthony.brenon.go4lunch.ui.navigation_bottom.MapViewFragment;
import anthony.brenon.go4lunch.ui.navigation_bottom.WorkmatesFragment;
import anthony.brenon.go4lunch.utils.SortMethod;
import anthony.brenon.go4lunch.viewmodel.MainActivityViewModel;

public class MainActivity extends AppCompatActivity implements LocationListener {

    private final static int AUTOCOMPLETE_REQUEST_CODE = 1;
    private static final int PERMS_CALL_ID = 101;

    private final MapViewFragment mapsFragment = new MapViewFragment();
    private final ListViewFragment listViewFragment = new ListViewFragment();
    private final WorkmatesFragment workmatesFragment = new WorkmatesFragment();

    private DrawerLayout drawer;
    private BottomNavigationView bottomNavMenu;
    private ActivityMainBinding binding;
    private MainActivityViewModel viewModel;
    private LocationManager locationManager;
    private int menuPosition;
    private Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        bottomNavMenu = binding.appBarMain.bottomNavigation;
        viewModel = new ViewModelProvider(this).get(MainActivityViewModel.class);
        toolbar = binding.appBarMain.toolbar;
        drawer = binding.drawerLayout;

        populateBottomMenu(mapsFragment, 0, R.string.main_toolbar_title_hungry);
        bottomNavMenu.setSelectedItemId(R.id.page_1_map_view);

        onNavigationBottom();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // check position gps is enable
        checkPermissions();
        if(checkFineLocationPermission()) {
            startLocationManager();
        } else checkPermissions();

        setupDrawerUIWithUserData();
        viewModel.callNearbyRestaurantsApi();
    }

    @SuppressLint("MissingPermission")
    @Override
    protected void onPause() {
        super.onPause();
        if(locationManager != null) {
            locationManager.removeUpdates(this);
        }
    }

    // research
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Place place = Autocomplete.getPlaceFromIntent(Objects.requireNonNull(data));
                Log.i("TAG", "Place: " + place.getName() + ", " + place.getId());
                Intent intent = new Intent(this, DetailsRestaurantActivity.class);
                intent.putExtra("place_id", place.getId());
                startActivity(intent);
            } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
                Status status = Autocomplete.getStatusFromIntent(Objects.requireNonNull(data));
                Log.i("TAG", status.getStatusMessage());
            } else if (resultCode == RESULT_CANCELED) {
                Log.i("TAG", "The user canceled the operation");
            }
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void autoCompleteLauncher() {
        if(!Places.isInitialized()) {
            Places.initialize(this, BuildConfig.MAPS_API_KEY);
        } else {
            List<Place.Field> fields = Arrays.asList(Place.Field.ID, Place.Field.NAME);

            Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY, fields)
                    .setHint(getString(R.string.autocomplete_hint))
                    .setTypeFilter(TypeFilter.ESTABLISHMENT)
                    .setCountry("FR")
                    .build(this);
            startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE);
        }
    }

//----------------------------------------------- NAVIGATION VIEW START -----------------------------------------------//
    // Content of drawer menu
    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                menuItem -> {
                    selectDrawerItem(menuItem);
                    return true;
                });
    }

    public boolean onCreateOptionsMenu(Menu menu) {
            switch (menuPosition) {
                case 0:
                    getMenuInflater().inflate(R.menu.main_toolbar_map, menu);
                    break;
                case 1:
                    getMenuInflater().inflate(R.menu.main_toolbar_listview, menu);
                    break;
                case 2:
                    getMenuInflater().inflate(R.menu.main_toolbar_workmate, menu);
                    break;
            }
            invalidateOptionsMenu();
        return true;
    }

    private void onNavigationBottom() {
        bottomNavMenu.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.page_1_map_view) {
                populateBottomMenu(mapsFragment, 0, R.string.main_toolbar_title_hungry);
                return true;
            } else if (id == R.id.page_2_list_view) {
                populateBottomMenu(listViewFragment, 1, R.string.main_toolbar_title_hungry);
                return true;
            } else if (id == R.id.page_3_workmates) {
                populateBottomMenu(workmatesFragment, 2, R.string.main_toolbar_title_workmates);
                return true;
            } else return false;
        });
    }

    private void populateBottomMenu(Fragment fragment, int menuPos, int title) {
        getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment_content_main, fragment).commit();
        menuPosition = menuPos;
        setSupportActionBar(toolbar);
        toolbar.setTitle(title);
    }

    // Bind and listener drawer menu
    public void selectDrawerItem(MenuItem menuItem) {
        // Specify the fragment to show based on nav item clicked
        int id = menuItem.getItemId(); {
            if ( id == R.id.dv_your_lunch ) {
                openYourLunchDetails();
                drawer.closeDrawer(GravityCompat.START);
            } else if ( id == R.id.dv_settings) {
                drawer.closeDrawer(GravityCompat.START);
            } else if ( id == R.id.dv_logout) {
                signOut();
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            drawer.openDrawer(GravityCompat.START);
        } else if (id == R.id.restaurant_search) {
            Log.d("my_logs", " -onOptionsItemSelected- restaurant_search");
            autoCompleteLauncher();
        } else if (id == R.id.sort_by_distance) {
            viewModel.sortMethodRestaurantsList(SortMethod.BY_DISTANCE);
            Log.d("my_logs", " -onOptionsItemSelected- BY_DISTANCE");
        } else if (id == R.id.sort_by_rating) {
            viewModel.sortMethodRestaurantsList(SortMethod.BY_RATING);
            Log.d("my_logs", " -onOptionsItemSelected- BY_RATING");
        } else if (id == R.id.sort_by_workmates) {
                viewModel.sortMethodRestaurantsList(SortMethod.BY_WORKMATES);
            Log.d("my_logs", " -onOptionsItemSelected- BY_WORKMATES");
        } else if (id == R.id.sort_by_opening) {
            viewModel.sortMethodRestaurantsList(SortMethod.BY_OPENING);
            Log.d("my_logs", " -onOptionsItemSelected- BY_OPENING");
        }
        return super.onOptionsItemSelected(item);
    }

    // Logout application
    private void signOut(){
        AuthUI.getInstance().signOut(this).addOnSuccessListener(unused -> {
            Intent intent = new Intent(this, AuthActivity.class);
            startActivity(intent);
            finish();
        });
    }
//----------------------------------------------- NAVIGATION VIEW END -----------------------------------------------//

//----------------------------------------------- POSITION GPS START -----------------------------------------------//
    private boolean checkFineLocationPermission() {
        String permission = Manifest.permission.ACCESS_FINE_LOCATION;
        int res = this.checkCallingOrSelfPermission(permission);
        return (res == PackageManager.PERMISSION_GRANTED);
    }

    @SuppressLint("MissingPermission")
    private void checkPermissions() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
            }, PERMS_CALL_ID);
        }
    }

    @SuppressLint("MissingPermission")
    private void startLocationManager() {
        locationManager = (LocationManager) this.getSystemService( Context.LOCATION_SERVICE );
        if (locationManager.isProviderEnabled( LocationManager.GPS_PROVIDER)) {
            locationManager.requestLocationUpdates( LocationManager.GPS_PROVIDER, 120000, 50, this);
        }
        if (locationManager.isProviderEnabled( LocationManager.PASSIVE_PROVIDER)) {
            locationManager.requestLocationUpdates(LocationManager.PASSIVE_PROVIDER, 120000, 50, this);
        }
        if (locationManager.isProviderEnabled( LocationManager.NETWORK_PROVIDER)) {
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 120000, 50, this);
        }
    }

    @Override
    public void onLocationChanged(@NonNull android.location.Location location) {
        Location locationUser = new Location(location.getLatitude(), location.getLongitude());
        viewModel.setLocationUser(locationUser);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) { }
//----------------------------------------------- POSITION GPS END -----------------------------------------------//

    private void setupDrawerUIWithUserData(){
        NavigationView nvDrawer = binding.navView;
        setupDrawerContent(nvDrawer);
        View hView = nvDrawer.getHeaderView(0);
        viewModel.getCurrentWorkmateData().addOnSuccessListener(workmate -> {
            if (workmate != null) {
                ImageView imageUser = hView.findViewById(R.id.imageUser);
                TextView firstName = hView.findViewById(R.id.firstName);
                TextView addressMail = hView.findViewById(R.id.addressMail);
                if (workmate.getUrlPicture() != null) {
                    Glide.with(this)
                            .load(workmate.getUrlPicture())
                            .apply(RequestOptions.circleCropTransform())
                            .into(imageUser);
                }
                firstName.setText(workmate.getUsername());
                addressMail.setText(workmate.getEmail());
            }
        });
    }

    private void openYourLunchDetails() {
        viewModel.getCurrentWorkmateData().addOnSuccessListener(workmate -> {
            if(!workmate.getRestaurantChosenId().equals("")) {
                Intent intent = new Intent(this, DetailsRestaurantActivity.class);
                intent.putExtra("place_id", workmate.getRestaurantChosenId());
                startActivity(intent);
            } else Toast.makeText(this,"You need to have chosen a restaurant", Toast.LENGTH_SHORT).show();
        });
    }
}