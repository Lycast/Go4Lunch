package anthony.brenon.go4lunch.ui.adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

import anthony.brenon.go4lunch.R;
import anthony.brenon.go4lunch.databinding.ItemWorkmateBinding;
import anthony.brenon.go4lunch.model.User;

/**
 * Created by Lycast on 09/03/2022.
 */
public class WorkmatesAdapter extends RecyclerView.Adapter<WorkmatesAdapter.WorkmatesViewHolder> {
    private final String TAG = "my_logs";
    private final String LOG_INFO = "WorkmatesAdapter ";

    public List<User> users;
    ItemWorkmateBinding binding;
    private static ClickListener clickListener;


    public WorkmatesAdapter() {}


    @SuppressLint("NotifyDataSetChanged")
    public void updateDataWorkmates(List<User> users) {
        this.users = users;
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public WorkmatesAdapter.WorkmatesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = ItemWorkmateBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new WorkmatesViewHolder(binding);
    }


    @Override
    public void onBindViewHolder(@NonNull WorkmatesAdapter.WorkmatesViewHolder workmatesViewHolder, int position) {
        workmatesViewHolder.bind(users.get(position));
    }


    @Override
    public int getItemCount() {
        if (users != null)
        return users.size();
        return 0;
    }


    public void setOnItemClickListener(WorkmatesAdapter.ClickListener clickListener) {
        WorkmatesAdapter.clickListener = clickListener;
    }


    public interface ClickListener {
        void onItemClick(String placeID);
    }


    class WorkmatesViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ItemWorkmateBinding itemBinding;


        public WorkmatesViewHolder(ItemWorkmateBinding itemBinding) {
            super(itemBinding.getRoot());
            itemView.setOnClickListener(this);
            this.itemBinding = itemBinding;
        }


        void bind(User user) {
            itemBinding.tvWorkmate.setText(user.getUsername());
            if (user.getRestaurantChosenName().equals(""))
                itemBinding.tvRestaurant.setText("no chosen restaurants");
            else itemBinding.tvRestaurant.setText("(" + user.getRestaurantChosenName() + ")");
            Glide.with(itemBinding.ivWorkmate.getContext())
                    .load(user.getUrlPicture())
                    .placeholder(R.drawable.ic_image_not_supported)
                    .apply(RequestOptions.circleCropTransform())
                    .into(itemBinding.ivWorkmate);
        }


        @Override
        public void onClick(View view) {
            clickListener.onItemClick(users.get(getAdapterPosition()).getRestaurantChosenId());
        }
    }
}
