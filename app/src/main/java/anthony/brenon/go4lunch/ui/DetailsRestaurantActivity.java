package anthony.brenon.go4lunch.ui;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;

import java.util.List;
import java.util.Objects;

import anthony.brenon.go4lunch.R;
import anthony.brenon.go4lunch.databinding.ActivityDetailsRestaurantBinding;
import anthony.brenon.go4lunch.model.Restaurant;
import anthony.brenon.go4lunch.model.Workmate;
import anthony.brenon.go4lunch.model.googleplace_models.PlaceResponse;
import anthony.brenon.go4lunch.ui.adapter.WorkmatesAdapter;
import anthony.brenon.go4lunch.viewmodel.RestaurantViewModel;
import anthony.brenon.go4lunch.viewmodel.WorkmateViewModel;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailsRestaurantActivity extends AppCompatActivity {
    private final String TAG = "my_logs";
    private final String LOG_INFO = "DetailsRestaurantActivity ";

    private RestaurantViewModel restaurantViewModel;
    private WorkmateViewModel workmateViewModel;
    private ActivityDetailsRestaurantBinding binding;
    private static final int REQUEST_CALL = 123;
    private String placeId;
    private Workmate currentWorkmate;
    private Restaurant restaurantDB;
    private final WorkmatesAdapter adapter = new WorkmatesAdapter(false);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDetailsRestaurantBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        restaurantViewModel = new ViewModelProvider(this).get(RestaurantViewModel.class);
        workmateViewModel = new ViewModelProvider(this).get(WorkmateViewModel.class);

        // set placeId
        placeId = getIntent().getStringExtra("place_id");

        // set current user
        workmateViewModel.getCurrentWorkmateData().observe(this, workmate -> {
            this.currentWorkmate = workmate;
            setLikeImage();
            setImageChoice();
        });

        // populate restaurant data
        if (placeId != null) {
            getRestaurantDetails(placeId);
        }

        // setup recycler view
        RecyclerView recyclerView = binding.detailsRecyclerView;
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(adapter);

        // set all buttons listeners
        onClickListenerBtnWebsite();
        onClickListenerBtnCall();
        onClickListenerBtnLike();
        onClickListenerFabChoice();
    }

    private void getRestaurantDetails(final String placeId) {

        restaurantViewModel.getRestaurantFS(placeId).addOnSuccessListener(restaurant -> {

            restaurantViewModel.getRestaurantDetailsApi(placeId, new Callback<PlaceResponse>() {
                @Override
                public void onResponse(@NonNull Call<PlaceResponse> call, @NonNull Response<PlaceResponse> response) {
                    if (response.isSuccessful()) {

                        restaurantDB = Objects.requireNonNull(response.body()).getResult();
                        restaurantDB.setUsersChoice(restaurant.getUsersChoice());
                        populateDetailsRestaurant(restaurantDB);
                        setEnableButton(restaurantDB);
                        updateRecyclerList(restaurantDB.getUsersChoice());
                    }
                }

                @Override
                public void onFailure(@NonNull Call<PlaceResponse> call, @NonNull Throwable t) {
                    Log.d(TAG, LOG_INFO + " - getRestaurantDetails - onFailure: " + t.getMessage());
                }
            });
        });
    }


    private void populateDetailsRestaurant(Restaurant restaurant) {
        binding.tvDetailsName.setText(restaurant.getName());
        binding.tvDetailsAddress.setText(restaurant.getAddress());
        Glide.with(binding.ivDetailsRestaurant.getContext())
                .load(restaurant.getPhoto(1000))
                .placeholder(R.drawable.ic_image_not_supported)
                .transform(new CenterCrop(), new RoundedCorners(8))
                .into(binding.ivDetailsRestaurant);
    }

    private void setLikeImage() {
        if (currentWorkmate.getRestaurantsLiked().contains(placeId)) {
            binding.imgLike.setVisibility(View.VISIBLE);
        } else {
            binding.imgLike.setVisibility(View.GONE);
        }
    }

    private void setLikeList() {
        List<String> restaurantsIds = currentWorkmate.getRestaurantsLiked();
        if (currentWorkmate.getRestaurantsLiked().contains(placeId))
            restaurantsIds.remove(placeId);
        else restaurantsIds.add(placeId);
        currentWorkmate.setRestaurantsLiked(restaurantsIds);
    }

    private void setImageChoice() {
        if (currentWorkmate.getRestaurantChosenId().equals(placeId)) {
            binding.fabRestaurantChoice.setImageResource(R.drawable.ic_check_circle);
        } else {
            binding.fabRestaurantChoice.setImageResource(R.drawable.ic_restaurant_menu);
        }
    }

    private void setUserChoice() {
        if (currentWorkmate.getRestaurantChosenId().equals(placeId) || currentWorkmate.getRestaurantChosenId().equals("")) {
            // set new choice
            List<String> userChoice = restaurantDB.getUsersChoice();

            if (restaurantDB.getUsersChoice().contains(currentWorkmate.getUid())) {
                userChoice.remove(currentWorkmate.getUid());
                currentWorkmate.setRestaurantChosenId("");
                currentWorkmate.setRestaurantChosenName("");
            } else {
                userChoice.add(currentWorkmate.getUid());
                currentWorkmate.setRestaurantChosenId(restaurantDB.getId());
                currentWorkmate.setRestaurantChosenName(restaurantDB.getName());
            }
            updateCurrentWorkmate();
            updateCurrentRestaurant();
            setImageChoice();
            updateRecyclerList(restaurantDB.getUsersChoice());
        } else {
            // alert if user want to select 2 restaurants
            Toast.makeText(this, "Take can't chosen 2 restaurant", Toast.LENGTH_SHORT).show();
        }
    }

    private void showAlertDialog() {
        new AlertDialog.Builder(this)
                .setIcon(R.drawable.ic_phone_enabled)
                .setTitle(" call")
                .setMessage(restaurantDB.getName() + "  " + restaurantDB.getPhoneNumber() + " ?")
                .setPositiveButton(android.R.string.yes, (dialog, which) -> makePhoneCall())
                .setNegativeButton(android.R.string.no, null)
                .show();
    }

    private void setEnableButton(Restaurant restaurant) {
        if (restaurant.getWebsite() != null) {
            binding.btnWebsite.setEnabled(true);
        }
        if (restaurant.getPhoneNumber() != null) {
            binding.btnCall.setEnabled(true);
        }
    }

    // should not be called if restaurantDb is null
    private void makePhoneCall() {
        if (restaurantDB != null && restaurantDB.getPhoneNumber().trim().length() > 0) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, REQUEST_CALL);
            } else {
                String dial = "tel:" + restaurantDB.getPhoneNumber();
                startActivity(new Intent(Intent.ACTION_CALL, Uri.parse(dial)));
            }
        }
    }


    // UPDATES
    private void updateCurrentWorkmate() {
        workmateViewModel.updateWorkmate(currentWorkmate);
    }

    private void updateCurrentRestaurant() {
        restaurantViewModel.updateRestaurantIntoFS(restaurantDB);
    }

    private void updateRecyclerList(final List<String> workmateIds) {
        workmateViewModel.getWorkmateListForDetails(workmateIds).observe(this, adapter::updateDataWorkmates);
    }


    // SET ON CLICK LISTENER METHODS
    private void onClickListenerFabChoice() {
        binding.fabRestaurantChoice.setOnClickListener(view -> setUserChoice());
    }

    private void onClickListenerBtnCall() {
        binding.btnCall.setOnClickListener(view -> showAlertDialog());
    }

    private void onClickListenerBtnLike() {
        binding.btnLike.setOnClickListener(view -> {
            setLikeList();
            setLikeImage();
            updateCurrentWorkmate();
        });
    }

    private void onClickListenerBtnWebsite() {
        binding.btnWebsite.setOnClickListener(view -> {
            if (restaurantDB != null) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(restaurantDB.getWebsite()));
                startActivity(browserIntent);
            }
        });
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CALL) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                makePhoneCall();
            } else {
                Toast.makeText(this, "Permission DENIED", Toast.LENGTH_SHORT).show();
            }
        }
    }
}