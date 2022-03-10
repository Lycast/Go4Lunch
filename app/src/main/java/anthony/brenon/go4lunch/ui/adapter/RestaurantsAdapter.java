package anthony.brenon.go4lunch.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import anthony.brenon.go4lunch.R;
import anthony.brenon.go4lunch.model.Restaurant;

/**
 * Created by Lycast on 09/03/2022.
 */
public class RestaurantsAdapter extends RecyclerView.Adapter<RestaurantsAdapter.RestaurantsViewHolder> {

    private List<Restaurant> restaurants;

    public RestaurantsAdapter() {
    }

    public void updateData(final List<Restaurant> restaurants) {
        this.restaurants = restaurants;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RestaurantsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_restaurant, parent, false);
        return new RestaurantsViewHolder(view);
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

        AppCompatImageView restaurantImage;
        TextView restaurantName, restaurantAddress, restaurantOpening, restaurantDistance, restaurantWorkmates;
        //implement rating.

        public RestaurantsViewHolder(@NonNull View itemView) {
            super(itemView);

            restaurantImage = itemView.findViewById(R.id.restaurant_image);
            restaurantName = itemView.findViewById(R.id.restaurant_name);
            restaurantAddress = itemView.findViewById(R.id.restaurant_address);
            restaurantOpening = itemView.findViewById(R.id.restaurant_opening);
            restaurantDistance = itemView.findViewById(R.id.restaurant_distance);
            restaurantWorkmates = itemView.findViewById(R.id.restaurant_workmates);
            //implement rating
        }

        void bind(Restaurant restaurant) {
            restaurantName.setText(restaurant.getName());
            restaurantAddress.setText(restaurant.getAddress());
        }
    }
}