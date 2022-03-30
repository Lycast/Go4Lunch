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

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;

import anthony.brenon.go4lunch.R;
import anthony.brenon.go4lunch.databinding.ActivityDetailsRestaurantBinding;

public class DetailsRestaurantActivity extends AppCompatActivity {
    private final String TAG = "my_logs";

    private DetailsRestaurantViewModel detailsRestaurantViewModel;
    private ActivityDetailsRestaurantBinding binding;

    private static final int REQUEST_CALL = 123;
    private String phoneNumber;
    private String restaurantName;

    //TODO go to make that into restaurant model !
    private Boolean like = false;
    private Boolean restaurantSelected = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDetailsRestaurantBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        detailsRestaurantViewModel = new ViewModelProvider(this).get(DetailsRestaurantViewModel.class);

        setLikeImage();


        onClickListenerBtnLike();
        setSelectedRestaurant();


        if(getIntent().hasExtra("place_id")) {
            String placeId = getIntent().getStringExtra("place_id");
            Log.d(TAG, "getExtra : " + placeId);
            populateDetailsRestaurant(placeId);
            onClickListenerBtnWebsite(placeId);
            onClickListenerBtnCall(placeId);
            setEnableButton(placeId);
        }
    }


    //TODO make enable button if  we get website and phoneNumber

    private void populateDetailsRestaurant(String placeId) {
        detailsRestaurantViewModel.getRestaurantDetails(placeId).observe(this, restaurant -> {
            phoneNumber = restaurant.getPhoneNumber();
            restaurantName = restaurant.getName();
            binding.tvDetailsName.setText(restaurant.getName());
            binding.tvDetailsAddress.setText(restaurant.getAddress());
            Glide.with(binding.ivDetailsRestaurant.getContext())
                    .load(restaurant.getPhoto(1000))
                    .placeholder(R.drawable.ic_image_not_supported)
                    .transform(new CenterCrop(), new RoundedCorners(8))
                    .into(binding.ivDetailsRestaurant);
            Log.d(TAG, restaurant.getName());
        });
    }

    private void setLikeImage() {
        if (like)
            binding.imgLike.setVisibility(View.VISIBLE);
        else binding.imgLike.setVisibility(View.GONE);
    }

    private void setSelectedRestaurantImage() {
        if (restaurantSelected)
            binding.fabRestaurantChoice.setImageResource(R.drawable.ic_check_circle);
        else binding.fabRestaurantChoice.setImageResource(R.drawable.ic_restaurant_menu);
    }

    private void makePhoneCall() {
        if (phoneNumber.trim().length() > 0) {
            if(ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.CALL_PHONE}, REQUEST_CALL);
            } else {
                String dial = "tel:" + phoneNumber;
                startActivity(new Intent(Intent.ACTION_CALL, Uri.parse(dial))); }
        } else {
            Toast.makeText(this, "No phone number", Toast.LENGTH_SHORT).show();
        }
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

        new AlertDialog.Builder(this)
                .setIcon(R.drawable.ic_phone_enabled)
                .setMessage("Are you sure you want to call this number?\n\n" + restaurantName + "  "  + phoneNumber)
                .setPositiveButton(android.R.string.yes, (dialog, which) -> makePhoneCall())
                .setNegativeButton(android.R.string.no, null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    // SET ON CLICK LISTENER METHODS
    private void setSelectedRestaurant() {
        binding.fabRestaurantChoice.setOnClickListener(view -> {
            restaurantSelected = !restaurantSelected;
            setSelectedRestaurantImage();
        });
    }
    private void onClickListenerBtnCall(String placeId) {
        binding.btnCall.setOnClickListener(view -> {
            detailsRestaurantViewModel.getRestaurantDetails(placeId).observe(this, restaurant -> {
                setAlertDialog();
            });
        });
    }
    private void onClickListenerBtnLike() {
        binding.btnLike.setOnClickListener(view -> {
            like = !like;
            setLikeImage();
        });
    }
    private void onClickListenerBtnWebsite(String placeId) {
        binding.btnWebsite.setOnClickListener(view -> {
            detailsRestaurantViewModel.getRestaurantDetails(placeId).observe(this, restaurant -> {
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(restaurant.getWebsite()));
                    startActivity(browserIntent);
            });
        });
    }

    private void setEnableButton(String placeId) {
        detailsRestaurantViewModel.getRestaurantDetails(placeId).observe(this, restaurant -> {
            if(restaurant.getWebsite() != null) {
                binding.btnWebsite.setEnabled(true);
                binding.btnWebsite.setActivated(true);
            }
            if(restaurant.getPhoneNumber() != null)
                binding.btnCall.setEnabled(true);
        });
    }
}