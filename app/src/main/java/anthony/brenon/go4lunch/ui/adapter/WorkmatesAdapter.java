package anthony.brenon.go4lunch.ui.adapter;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by Lycast on 09/03/2022.
 */
public class WorkmatesAdapter extends RecyclerView.Adapter<WorkmatesAdapter.WorkmatesViewHolder> {
    private final String TAG = "my_logs";

    @NonNull
    @Override
    public WorkmatesAdapter.WorkmatesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull WorkmatesAdapter.WorkmatesViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    static class WorkmatesViewHolder extends RecyclerView.ViewHolder {
        public WorkmatesViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
