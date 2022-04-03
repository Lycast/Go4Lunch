package anthony.brenon.go4lunch.ui;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;

import java.util.List;

import anthony.brenon.go4lunch.R;
import anthony.brenon.go4lunch.databinding.ActivityDetailsRestaurantBinding;
import anthony.brenon.go4lunch.model.Restaurant;
import anthony.brenon.go4lunch.model.User;
import anthony.brenon.go4lunch.viewmodel.RestaurantViewModel;
import anthony.brenon.go4lunch.viewmodel.UserViewModel;

public class DetailsRestaurantActivity extends AppCompatActivity {
    private final String TAG = "my_logs";
    private final String LOG_INFO = "DetailsRestaurantActivity ";

    private RestaurantViewModel restaurantViewModel;
    private UserViewModel userViewModel;
    private ActivityDetailsRestaurantBinding binding;
    private static final int REQUEST_CALL = 123;
    private String placeId;
    private User currentUser;
    private Restaurant restaurantDto;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDetailsRestaurantBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        restaurantViewModel = new ViewModelProvider(this).get(RestaurantViewModel.class);
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        // set placeId
        placeId = getIntent().getStringExtra("place_id");
        // set current user
        userViewModel.getCurrentUserFirebase().addOnSuccessListener(user -> {
            this.currentUser = user;
            setLikeImage();
            setImageChoice();
        });
        // populate restaurant data
        if ( placeId != null) { populateDetailsRestaurant(placeId); }
        // get restaurant dto
        restaurantViewModel.getRestaurantDto(placeId).addOnSuccessListener(restaurant -> this.restaurantDto = restaurant);
        // set all buttons listeners
        onClickListenerBtnWebsite();
        onClickListenerBtnCall();
        onClickListenerBtnLike();
        onClickListenerFabChoice();
        setEnableButton();
    }


    private void populateDetailsRestaurant(String placeId) {
        restaurantViewModel.getRestaurantDetails(placeId).observe(this, restaurant -> {
            binding.tvDetailsName.setText(restaurant.getName());
            binding.tvDetailsAddress.setText(restaurant.getAddress());
            Glide.with(binding.ivDetailsRestaurant.getContext())
                    .load(restaurant.getPhoto(1000))
                    .placeholder(R.drawable.ic_image_not_supported)
                    .transform(new CenterCrop(), new RoundedCorners(8))
                    .into(binding.ivDetailsRestaurant);
        });
    }


    private void setLikeImage() {
        if (currentUser.getRestaurantsLiked().contains(placeId)) {
            //Log.d(TAG, LOG_INFO + "if is : " + currentUser.getRestaurantsLiked().contains(placeId));
            binding.imgLike.setVisibility(View.VISIBLE);
        } else {
            binding.imgLike.setVisibility(View.GONE);
            //Log.d(TAG, LOG_INFO + "if is : " + currentUser.getRestaurantsLiked().contains(placeId));
        }
    }


    private void setLikeList() {
        List<String> restaurantsIds = currentUser.getRestaurantsLiked();
        if (currentUser.getRestaurantsLiked().contains(placeId))
            restaurantsIds.remove(placeId);
        else restaurantsIds.add(placeId);
        currentUser.setRestaurantsLiked(restaurantsIds);
    }


    private void setImageChoice() {
        if (currentUser.getRestaurantChosenId().equals(placeId)) {
            binding.fabRestaurantChoice.setImageResource(R.drawable.ic_check_circle); }
        else { binding.fabRestaurantChoice.setImageResource(R.drawable.ic_restaurant_menu); }
    }


    private void setUserChoice() {
        if (currentUser.getRestaurantChosenId().equals(placeId) || currentUser.getRestaurantChosenId().equals("")) {
            // set new choice
            List<String> userChoice = restaurantDto.getUsersChoice();

            if (restaurantDto.getUsersChoice().contains(currentUser.getUid())) {
                userChoice.remove(currentUser.getUid());
                currentUser.setRestaurantChosenId("");
                currentUser.setRestaurantChosenName("");
            } else {
                userChoice.add(currentUser.getUid());
                currentUser.setRestaurantChosenId(restaurantDto.getId());
                currentUser.setRestaurantChosenName(restaurantDto.getName());
            }
            userViewModel.updateUser(currentUser);
            restaurantViewModel.updateRestaurantDto(restaurantDto);
            setImageChoice();
        } else // alert if user want to select 2 restaurants
            Toast.makeText(this, "Take can't chosen 2 restaurant", Toast.LENGTH_SHORT).show();
    }


    private void makePhoneCall() {
        restaurantViewModel.getRestaurantDetails(placeId).observe(this, restaurant -> {
             if (restaurant.getPhoneNumber().trim().length() > 0) {
               if (ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, REQUEST_CALL);
                } else {
                    String dial = "tel:" + restaurant.getPhoneNumber();
                    startActivity(new Intent(Intent.ACTION_CALL, Uri.parse(dial)));
                }
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


    private void setAlertDialog() {
        restaurantViewModel.getRestaurantDetails(placeId).observe(this, restaurant -> new AlertDialog.Builder(this)
                .setIcon(R.drawable.ic_phone_enabled)
                .setTitle(" call")
                .setMessage(restaurant.getName() + "  " + restaurant.getPhoneNumber() + " ?")
                .setPositiveButton(android.R.string.yes, (dialog, which) -> makePhoneCall())
                .setNegativeButton(android.R.string.no, null)
                .show());
    }


    // SET ON CLICK LISTENER METHODS
    private void onClickListenerFabChoice() {
        binding.fabRestaurantChoice.setOnClickListener(view -> setUserChoice());
    }


    private void onClickListenerBtnCall() {
        binding.btnCall.setOnClickListener(view -> restaurantViewModel.getRestaurantDetails(placeId).observe(this, restaurant -> setAlertDialog()));
    }


    private void onClickListenerBtnLike() {
        binding.btnLike.setOnClickListener(view -> {
            setLikeList();
            setLikeImage();
            userViewModel.updateUser(currentUser);
        });
    }


    private void onClickListenerBtnWebsite() {
        binding.btnWebsite.setOnClickListener(view -> restaurantViewModel.getRestaurantDetails(placeId).observe(this, restaurant -> {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(restaurant.getWebsite()));
            startActivity(browserIntent);
        }));
    }


    private void setEnableButton() {
        restaurantViewModel.getRestaurantDetails(placeId).observe(this, restaurant -> {
            if (restaurant.getWebsite() != null) {
                binding.btnWebsite.setEnabled(true);
            }
            if (restaurant.getPhoneNumber() != null)
                binding.btnCall.setEnabled(true);
        });
    }
}