package anthony.brenon.go4lunch.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;

import java.util.List;

import anthony.brenon.go4lunch.R;
import anthony.brenon.go4lunch.databinding.ItemRestaurantBinding;
import anthony.brenon.go4lunch.model.Restaurant;

/**
 * Created by Lycast on 09/03/2022.
 */
public class RestaurantsAdapter extends RecyclerView.Adapter<RestaurantsAdapter.RestaurantsViewHolder> {

    private List<Restaurant> restaurants;
    Context context;
    ItemRestaurantBinding binding;

    public RestaurantsAdapter() {
    }

    public void updateData(List<Restaurant> restaurants) {
        this.restaurants = restaurants;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RestaurantsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = ItemRestaurantBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new RestaurantsViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull RestaurantsViewHolder restaurantViewHolder, int position) {
        restaurantViewHolder.bind(restaurants.get(position));
    }

    @Override
    public int getItemCount() {
        if (restaurants != null)
            return restaurants.size();
        return 0;
    }


    // --ViewHolder--
    static class RestaurantsViewHolder extends RecyclerView.ViewHolder {

            ItemRestaurantBinding itemBinding;

            public RestaurantsViewHolder(ItemRestaurantBinding itemBinding) {
                super(itemBinding.getRoot());
                this.itemBinding = itemBinding;
            }

            void bind(Restaurant restaurant) {
                itemBinding.restaurantName.setText(restaurant.getName());
                itemBinding.restaurantAddress.setText(restaurant.getAddress());
                Glide.with(itemBinding.restaurantImage.getContext())
                        .load(restaurant.getPhoto(600))
                        .placeholder(R.drawable.ic_image_not_supported_24)
                        .transform(new CenterCrop(), new RoundedCorners(8))
                        .into(itemBinding.restaurantImage);
                setRestaurantRating((int) restaurant.getRating());
                itemBinding.restaurantOpening.setText("" + (int) restaurant.getRating());
        }

        private void setRestaurantRating(int rating) {
            if (rating >= 1)
                itemBinding.restaurantRating1.setImageResource(R.drawable.ic_star_rate);
            if (rating >= 2)
                itemBinding.restaurantRating2.setImageResource(R.drawable.ic_star_rate);
            if (rating >= 3)
                itemBinding.restaurantRating3.setImageResource(R.drawable.ic_star_rate);
            if (rating >= 4)
                itemBinding.restaurantRating4.setImageResource(R.drawable.ic_star_rate);
            if (rating == 5)
                itemBinding.restaurantRating5.setImageResource(R.drawable.ic_star_rate);
        }
    }
}