package anthony.brenon.go4lunch.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.firebase.ui.auth.AuthUI;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;

import anthony.brenon.go4lunch.R;
import anthony.brenon.go4lunch.databinding.ActivityMainBinding;
import anthony.brenon.go4lunch.model.Workmate;
import anthony.brenon.go4lunch.ui.navigation_bottom.list_view.ListViewFragment;
import anthony.brenon.go4lunch.ui.navigation_bottom.map.MapViewFragment;
import anthony.brenon.go4lunch.ui.navigation_bottom.workmates.WorkmatesFragment;
import anthony.brenon.go4lunch.viewmodel.WorkmateViewModel;

public class MainActivity extends AppCompatActivity{
    private final String TAG = "my_logs";

    private DrawerLayout drawer;
    private BottomNavigationView bottomNavMenu;
    private ActivityMainBinding binding;
    private WorkmateViewModel workmateViewModel;
    private String chosenRestaurant;
    private Workmate currentWorkmate;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        // Init bottom navigation
        bottomNavMenu = binding.appBarMain.bottomNavigation;
        // init user view model
        workmateViewModel = new ViewModelProvider(this).get(WorkmateViewModel.class);

        // setup toolbar
        Toolbar toolbar = binding.appBarMain.toolbar;
        setSupportActionBar(toolbar);
        // bind drawer
        drawer = binding.drawerLayout;

        // Bind and listener fab
        binding.appBarMain.fabChat.setOnClickListener(view ->
            Snackbar.make(view, "Replace action to open a chat", Snackbar.LENGTH_LONG).setAction("Action", null).show()
        );
        setupNavigationBottom();
    }


    @Override
    protected void onResume() {
        super.onResume();
        final MapViewFragment mapsFragment = new MapViewFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment_content_main, mapsFragment).commit();
        bottomNavMenu.setSelectedItemId(R.id.page_1_map_view);
        setupDrawerUIWithUserData();
    }


    // Bind and listener navigation bottom
    private void setupNavigationBottom() {
        bottomNavMenu.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            final MapViewFragment mapsFragment = new MapViewFragment();
            final ListViewFragment listViewFragment = new ListViewFragment();
            final WorkmatesFragment workmatesFragment = new WorkmatesFragment();
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.page_1_map_view) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment_content_main, mapsFragment).commit();
                    return true;
                } else if (id == R.id.page_2_list_view) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment_content_main, listViewFragment).commit();
                    return true;
                } else if (id == R.id.page_3_workmates) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment_content_main, workmatesFragment).commit();
                    return true;
                } else return id == R.id.page_4_invisible;
            }
        });
    }


    // Content of drawer menu
    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                menuItem -> {
                    selectDrawerItem(menuItem);
                    return true;
                });
    }


    // Open/close drawer
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            drawer.openDrawer(GravityCompat.START);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    private void setupDrawerUIWithUserData(){
        NavigationView nvDrawer = binding.navView;
        setupDrawerContent(nvDrawer);
        View hView = nvDrawer.getHeaderView(0);
        workmateViewModel.getCurrentWorkmateData().observe(this, workmate -> {
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


    // Bind and listener drawer menu
    public void selectDrawerItem(MenuItem menuItem) {
        // Specify the fragment to show based on nav item clicked
            int id = menuItem.getItemId(); {
                if ( id == R.id.dv_your_lunch ) {
                    openYourLunchDetails();
                drawer.closeDrawer(GravityCompat.START);
                } else if ( id == R.id.dv_settings) {
                    Log.d(TAG, "settings");
                    deselectBottomNav();
                    drawer.closeDrawer(GravityCompat.START);
                } else if ( id == R.id.dv_logout) {
                    signOut();
                }
        }
    }


    // Logout application
    private void signOut(){
        AuthUI.getInstance().signOut(this).addOnSuccessListener(unused -> {
            Intent intent = new Intent(this, AuthActivity.class);
            startActivity(intent);
            finish();
        });
    }


    // deselect bottom navigation
    private void deselectBottomNav(){
        // select and invisible item
        bottomNavMenu.setSelectedItemId(R.id.page_4_invisible);
    }


    private void openYourLunchDetails() {
        workmateViewModel.getCurrentWorkmateData().observe(this, workmate -> {
            if(!workmate.getRestaurantChosenId().equals("")) {
                Intent intent = new Intent(this, DetailsRestaurantActivity.class);
                intent.putExtra("place_id", workmate.getRestaurantChosenId());
                startActivity(intent);
            } else Toast.makeText(this,"You need to have chosen a restaurant", Toast.LENGTH_SHORT).show();
        });
    }
}