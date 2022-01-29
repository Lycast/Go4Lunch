package anthony.brenon.go4lunch.ui;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.Menu;

import com.firebase.ui.auth.AuthUI;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Arrays;
import java.util.List;

import anthony.brenon.go4lunch.R;
import anthony.brenon.go4lunch.databinding.ActivityMainBinding;
import anthony.brenon.go4lunch.ui.navigationbar.ListViewFragment;
import anthony.brenon.go4lunch.ui.navigationbar.MapViewFragment;
import anthony.brenon.go4lunch.ui.navigationbar.WorkmatesFragment;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    BottomNavigationView bottomNavigationView;
    private ActivityMainBinding binding;

    //Firebase login
    private static final int RC_SIGN_IN = 123;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //startSignInActivity();

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarMain.toolbar);
        binding.appBarMain.fab.setOnClickListener(view -> Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show());
        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        //permet la navigation depuis le drawer
        //NavigationUI.setupWithNavController(navigationView, navController);

        //setup bottom navigation
        setupNavigationBottom();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    private void startSignInActivity(){

        // Choose authentication providers
        List<AuthUI.IdpConfig> providers = Arrays.asList(
                new AuthUI.IdpConfig.GoogleBuilder().build(),
                new AuthUI.IdpConfig.FacebookBuilder().build(),
                new AuthUI.IdpConfig.EmailBuilder().build());

        // Launch the activity
        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(providers)
                        .setIsSmartLockEnabled(false, true)
                        .build(),
                RC_SIGN_IN);
    }

    private void setupNavigationBottom() {
        binding.appBarMain.bottomNavigation.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            final MapViewFragment firstFragment = new MapViewFragment();
            final ListViewFragment secondFragment = new ListViewFragment();
            final WorkmatesFragment thirdFragment = new WorkmatesFragment();
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
}