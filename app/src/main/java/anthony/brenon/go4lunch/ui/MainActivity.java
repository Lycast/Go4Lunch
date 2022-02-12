package anthony.brenon.go4lunch.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.firebase.ui.auth.AuthUI;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Objects;

import anthony.brenon.go4lunch.R;
import anthony.brenon.go4lunch.databinding.ActivityMainBinding;
import anthony.brenon.go4lunch.ui.drawer.YourLunchFragment;
import anthony.brenon.go4lunch.ui.bottom_navigation.ListViewFragment;
import anthony.brenon.go4lunch.ui.bottom_navigation.MapViewFragment;
import anthony.brenon.go4lunch.ui.bottom_navigation.WorkmatesFragment;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "Activity_Main";
    private ActivityMainBinding binding;

    private DrawerLayout drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Log.d(TAG, "onCreate");


// [Drawer setup]
        // Bind action bar
        Toolbar toolbar = binding.appBarMain.toolbar;
        // Init toolbar
        setSupportActionBar(toolbar);
        // Setup toolbar
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        // Bind layout
        drawer = binding.drawerLayout;
        // Bind and init drawer view
        NavigationView nvDrawer = binding.navView;
        setupDrawerContent(nvDrawer);
        // Init header view
        View hView = nvDrawer.getHeaderView(0);

        // Bind and listener fab
        binding.appBarMain.fab.setOnClickListener(view ->
                        Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG).setAction("Action", null).show()
                //signOut()
        );

        setupNavigationBottom();
        updateUIWithUserData(hView);
    }

    // Content of drawer menu
    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                menuItem -> {
                    selectDrawerItem(menuItem);
                    return true;
                });
    }

    // Bind and listener drawer menu
    @SuppressLint("NonConstantResourceId")
    public void selectDrawerItem(MenuItem menuItem) {
        // Specify the fragment to show based on nav item clicked
        final YourLunchFragment firstFragment = new YourLunchFragment();

        switch(menuItem.getItemId()) {
            case R.id.dv_your_lunch:
                getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment_content_main, firstFragment).commit();
                break;
            case R.id.dv_settings:
                Log.d(TAG, "settings");
                break;
            case R.id.dv_logout:
                signOut();
                break;
            default:
                break;
        }
    }

    // he action bar home/up action should open or close the drawer.
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // The action bar home/up action should open or close the drawer.
        if (item.getItemId() == android.R.id.home) {
            drawer.openDrawer(GravityCompat.START);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // Populate header
    private void updateUIWithUserData(View viewHeader){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user != null) {
            ImageView imageUser = viewHeader.findViewById(R.id.imageUser);
            TextView firstName = viewHeader.findViewById(R.id.firstName);
            TextView addressMail = viewHeader.findViewById(R.id.addressMail);


            if (user.getPhotoUrl() != null) {
                Glide.with(this)
                        .load(user.getPhotoUrl())
                        .apply(RequestOptions.circleCropTransform())
                        .into(imageUser);
            }
            firstName.setText(user.getDisplayName());
            addressMail.setText(user.getEmail());
        }
    }

    // Bind and listener navigation bottom
    private void setupNavigationBottom() {
        binding.appBarMain.bottomNavigation.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            final MapViewFragment firstFragment = new MapViewFragment();
            final ListViewFragment secondFragment = new ListViewFragment();
            final WorkmatesFragment thirdFragment = new WorkmatesFragment();
            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                switch(id){
                    case R.id.page_1_map_view:
                        getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment_content_main, firstFragment).commit();
                        return true;

                    case R.id.page_2_list_view:
                        getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment_content_main, secondFragment).commit();
                        return true;

                    case R.id.page_3_workmates:
                        getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment_content_main, thirdFragment).commit();
                        return true;
                }
                return true;
            }
        });
    }

    // Logout application
    public void signOut(){
        AuthUI.getInstance().signOut(this).addOnSuccessListener(unused -> {
            Intent intent = new Intent(this, AuthActivity.class);
            startActivity(intent);
            finish();
        });
    }
}