package anthony.brenon.go4lunch.ui;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
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
import anthony.brenon.go4lunch.viewmodel.DetailsActivityViewModel;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Details activity
 * Our view of restaurant details
 */
public class DetailsRestaurantActivity extends AppCompatActivity {
    private final String TAG = "DEBUG_LOG";
    private final String LOG_INFO = "DetailsRestaurantActivity ";
    private static final int REQUEST_CALL = 123;

    private DetailsActivityViewModel viewModel;
    private ActivityDetailsRestaurantBinding binding;
    private String placeId;
    private Workmate currentWorkmate;
    private Restaurant currentRestaurant;
    private final WorkmatesAdapter adapter = new WorkmatesAdapter(false);
    private final Observer<List<Workmate>> workmateObserver = adapter::updateDataWorkmates;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDetailsRestaurantBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        viewModel = new ViewModelProvider(this).get(DetailsActivityViewModel.class);

        // set placeId
        placeId = getIntent().getStringExtra("place_id");

        // set current user
        viewModel.getCurrentWorkmateData().addOnSuccessListener(workmate -> {
            this.currentWorkmate = workmate;
            setLikeIcon();
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

    @Override
    protected void onResume() {
        super.onResume();
        viewModel.getWorkmatesLiveData().observe(this, workmateObserver);
    }

    @Override
    protected void onPause() {
        super.onPause();
        viewModel.removeObserver(workmateObserver);
    }

    //The retrofit call of the google api to retrieve the detail of a restaurant from an id
    private void getRestaurantDetails(final String placeId) {
        viewModel.getRestaurantFS(placeId).addOnSuccessListener(restaurant -> viewModel.getRestaurantDetailsApi(placeId, new Callback<PlaceResponse>() {
            @Override
            public void onResponse(@NonNull Call<PlaceResponse> call, @NonNull Response<PlaceResponse> response) {
                if (response.isSuccessful()) {
                    currentRestaurant = Objects.requireNonNull(response.body()).getResult();

                    if (restaurant != null) {
                        currentRestaurant.setUsersChoice(restaurant.getUsersChoice());
                    }
                    viewModel.updateRestaurantIntoFS(currentRestaurant);

                    populateDetailsRestaurant(currentRestaurant);
                    setEnableButton(currentRestaurant);
                    getWorkmatesData(currentRestaurant.getUsersChoice());
                }
            }

            @Override
            public void onFailure(@NonNull Call<PlaceResponse> call, @NonNull Throwable t) {
                Log.d(TAG, LOG_INFO + " - getRestaurantDetails - onFailure: " + t.getMessage());
            }
        }));
    }

    private void populateDetailsRestaurant(Restaurant restaurant) {
        binding.tvDetailsName.setText(restaurant.getName());
        binding.tvDetailsAddress.setText(restaurant.getAddress());
        binding.restaurantDetailsRatingBar.setRating(restaurant.getRating());
        Glide.with(binding.ivDetailsRestaurant.getContext())
                .load(restaurant.getPhoto(1000))
                .placeholder(R.drawable.ic_image_not_supported)
                .transform(new CenterCrop(), new RoundedCorners(8))
                .into(binding.ivDetailsRestaurant);
    }

    private void setLikeIcon() {
        if (!currentWorkmate.getRestaurantsLiked().contains(placeId)) {
            binding.btnLike.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_star_border, 0, 0);
        } else {
            binding.btnLike.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_star, 0, 0);
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
        if (!currentWorkmate.getRestaurantChosenId().equals("") && !currentWorkmate.getRestaurantChosenId().equals(placeId)) {
            // alert if user want to select 2 restaurants
            Toast.makeText(this, "You can't chosen 2 restaurant", Toast.LENGTH_SHORT).show();
        } else {
            // change choice for this restaurant
            List<String> usersChoice = currentRestaurant.getUsersChoice();
            if (currentRestaurant.getUsersChoice().contains(currentWorkmate.getUid())) {
                usersChoice.remove(currentWorkmate.getUid());
                currentWorkmate.setRestaurantChosenId("");
                currentWorkmate.setRestaurantChosenName("");
            } else {
                usersChoice.add(currentWorkmate.getUid());
                currentWorkmate.setRestaurantChosenId(currentRestaurant.getId());
                currentWorkmate.setRestaurantChosenName(currentRestaurant.getName());
            }
            // update view and data after choice change
            currentRestaurant.setUsersChoice(usersChoice);
            updateCurrentWorkmate();
            updateCurrentRestaurant();
            setImageChoice();
            getWorkmatesData(currentRestaurant.getUsersChoice());
        }
    }

    private void showAlertDialog() {
        new AlertDialog.Builder(this)
                .setIcon(R.drawable.ic_phone_enabled)
                .setTitle(R.string.title_call)
                .setMessage(currentRestaurant.getName() + "  " + currentRestaurant.getPhoneNumber() + " ?")
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
        if (currentRestaurant != null && currentRestaurant.getPhoneNumber().trim().length() > 0) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, REQUEST_CALL);
            } else {
                String dial = "tel:" + currentRestaurant.getPhoneNumber();
                startActivity(new Intent(Intent.ACTION_CALL, Uri.parse(dial)));
            }
        }
    }


    // UPDATES
    private void updateCurrentWorkmate() {
        viewModel.updateWorkmate(currentWorkmate);
    }

    private void updateCurrentRestaurant() {
        viewModel.updateRestaurantIntoFS(currentRestaurant);
    }

    private void getWorkmatesData(final List<String> workmateIds) {
        viewModel.getWorkmatesFromList(workmateIds);
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
            setLikeIcon();
            updateCurrentWorkmate();
        });
    }

    private void onClickListenerBtnWebsite() {
        binding.btnWebsite.setOnClickListener(view -> {
            if (currentRestaurant != null) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(currentRestaurant.getWebsite()));
                startActivity(browserIntent);
            }
        });
    }

    // request permission for make a call
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