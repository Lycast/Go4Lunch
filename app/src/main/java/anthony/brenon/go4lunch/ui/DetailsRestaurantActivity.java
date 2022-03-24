package anthony.brenon.go4lunch.ui;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;

import anthony.brenon.go4lunch.R;
import anthony.brenon.go4lunch.databinding.ActivityDetailsRestaurantBinding;

public class DetailsRestaurantActivity extends AppCompatActivity {

    private DetailsRestaurantViewModel detailsRestaurantViewModel;
    private ActivityDetailsRestaurantBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDetailsRestaurantBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        detailsRestaurantViewModel = new ViewModelProvider(this).get(DetailsRestaurantViewModel.class);

        if(getIntent().hasExtra("place_id")) {
            String placeId = getIntent().getStringExtra("place_id");
            Log.d("getExtra", "getExtra : " + placeId);
            populateDetailsRestaurant(placeId);
        }
    }

    private void populateDetailsRestaurant(String placeId) {
        detailsRestaurantViewModel.getRestaurantDetails(placeId).observe(this, restaurant -> {
            binding.tvDetailsName.setText(restaurant.getName());
            binding.tvDetailsAddress.setText(restaurant.getAddress());
            Glide.with(binding.ivDetailsRestaurant.getContext())
                    .load(restaurant.getPhoto(1000))
                    .placeholder(R.drawable.ic_image_not_supported)
                    .transform(new CenterCrop(), new RoundedCorners(8))
                    .into(binding.ivDetailsRestaurant);
            Log.d("tagR", restaurant.getName());
        });
    }
}