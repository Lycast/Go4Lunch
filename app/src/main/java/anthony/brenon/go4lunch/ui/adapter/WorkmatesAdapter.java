package anthony.brenon.go4lunch.ui.adapter;

import android.annotation.SuppressLint;
import android.content.res.Resources;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

import anthony.brenon.go4lunch.R;
import anthony.brenon.go4lunch.databinding.ItemWorkmateBinding;
import anthony.brenon.go4lunch.model.Workmate;

/**
 * Created by Lycast on 09/03/2022.
 */
public class WorkmatesAdapter extends RecyclerView.Adapter<WorkmatesAdapter.WorkmatesViewHolder> {

    public List<Workmate> workmates;
    ItemWorkmateBinding binding;
    private final boolean isListView;


    public WorkmatesAdapter(boolean isListView) {
        this.isListView = isListView;
    }


    @SuppressLint("NotifyDataSetChanged")
    public void updateDataWorkmates(List<Workmate> workmates) {
        this.workmates = workmates;
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
        workmatesViewHolder.bind(workmates.get(position));
    }


    @Override
    public int getItemCount() {
        if (workmates != null)
        return workmates.size();
        return 0;
    }


    class WorkmatesViewHolder extends RecyclerView.ViewHolder {

        ItemWorkmateBinding itemBinding;


        public WorkmatesViewHolder(ItemWorkmateBinding itemBinding) {
            super(itemBinding.getRoot());
            this.itemBinding = itemBinding;
        }


        void bind(Workmate workmate) {
            Resources res = itemView.getContext().getResources();
            itemBinding.tvWorkmate.setText(workmate.getUsername());

            if (isListView) {
                if (workmate.getRestaurantChosenName().equals("")) {
                    itemBinding.tvWorkmate.setTextColor(Color.GRAY);
                    itemBinding.tvRestaurant.setText(R.string.has_not_decided_yet);
                    itemBinding.tvRestaurant.setTextColor(Color.GRAY);
                } else {
                    itemBinding.tvRestaurant.setText(String.format(res.getString(R.string.populate_tv_user), res.getString(R.string.have_chosen), workmate.getRestaurantChosenName()));
                }
            } else {
                itemBinding.tvRestaurant.setText(R.string.is_joining);
            }

                Glide.with(itemBinding.ivWorkmate.getContext())
                        .load(workmate.getUrlPicture())
                        .placeholder(R.drawable.ic_image_not_supported)
                        .apply(RequestOptions.circleCropTransform())
                        .into(itemBinding.ivWorkmate);
            }
    }
}
