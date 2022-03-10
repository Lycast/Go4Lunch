package anthony.brenon.go4lunch.ui.bottom_navigation.list_view;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import anthony.brenon.go4lunch.databinding.FragmentListViewBinding;
import anthony.brenon.go4lunch.model.googleplace_models.LocationPlace;
import anthony.brenon.go4lunch.ui.adapter.RestaurantsAdapter;

public class ListViewFragment extends Fragment {

    private static final String TAG = "list_view_fragment";
    LocationPlace locationPlace = new LocationPlace(47.089505,-1.286154);
    private FragmentListViewBinding binding;

    private final RestaurantsAdapter adapter = new RestaurantsAdapter();


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentListViewBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RecyclerView listRestaurants = binding.restaurantsRecyclerView;
        listRestaurants.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));
        listRestaurants.setAdapter(adapter);

        ListViewViewModel listViewViewModel = new ViewModelProvider(this).get(ListViewViewModel.class);
        listViewViewModel.getAllRestaurants(locationPlace).observe(this, adapter::updateData);
    }
}