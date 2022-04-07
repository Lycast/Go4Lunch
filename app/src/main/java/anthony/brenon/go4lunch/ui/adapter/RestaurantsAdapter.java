package anthony.brenon.go4lunch.ui.adapter;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
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


    public List<Restaurant> restaurants;
    ItemRestaurantBinding binding;
    private static ClickListener clickListener;


    public RestaurantsAdapter() {}


    @SuppressLint("NotifyDataSetChanged")
    public void updateDataRestaurants(List<Restaurant> restaurants) {
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


    public void setOnItemClickListener(ClickListener clickListener) {
        RestaurantsAdapter.clickListener = clickListener;
    }


    public interface ClickListener {
        void onItemClick(String placeId);
    }


    // --ViewHolder--
    class RestaurantsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ItemRestaurantBinding itemBinding;

        public RestaurantsViewHolder(ItemRestaurantBinding itemBinding) {
            super(itemBinding.getRoot());
            itemView.setOnClickListener(this);
            this.itemBinding = itemBinding;
        }

        void bind(Restaurant restaurant) {
            itemBinding.restaurantName.setText(restaurant.getName());
            itemBinding.restaurantAddress.setText(restaurant.getAddress());
            itemBinding.restaurantWorkmates.setText(String.format("(%s)", restaurant.getUsersChoice().size()));
            Glide.with(itemBinding.restaurantImage.getContext())
                    .load(restaurant.getPhoto(600))
                    .placeholder(R.drawable.ic_image_not_supported)
                    .transform(new CenterCrop(), new RoundedCorners(8))
                    .into(itemBinding.restaurantImage);
            itemBinding.restaurantRatingBar.setRating(restaurant.getRating());
            itemBinding.restaurantDistance.setText(String.format("%sm", (int) restaurant.getDistance()));
            if (restaurant.getOpeningHours() != null)
                if (restaurant.getOpeningHours().isOpen_now()) {
                    itemBinding.restaurantOpening.setText(R.string.isOpen);
                    itemBinding.restaurantOpening.setTextColor(Color.parseColor("#139e2f"));
                } else {
                    itemBinding.restaurantOpening.setText(R.string.isClose);
                    itemBinding.restaurantOpening.setTextColor(Color.parseColor("#a11912"));
                }
        }

        @Override
        public void onClick(View v) {
            clickListener.onItemClick(restaurants.get(getAdapterPosition()).getId());
        }
    }
}