package anthony.brenon.go4lunch.ui.bottom_navigation.list_view;


import android.content.Intent;
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
import anthony.brenon.go4lunch.ui.DetailsRestaurantActivity;
import anthony.brenon.go4lunch.ui.SharedViewModel;
import anthony.brenon.go4lunch.ui.adapter.RestaurantsAdapter;

public class ListViewFragment extends Fragment {

    private FragmentListViewBinding binding;
    SharedViewModel sharedViewModel;

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

        sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
        sharedViewModel.getRestaurantsLiveData().observe(getViewLifecycleOwner(), adapter::updateData);

        adapter.setOnItemClickListener(new RestaurantsAdapter.ClickListener() {
            @Override
            public void onItemClick(String placeId) {
                Intent intent = new Intent(getActivity(), DetailsRestaurantActivity.class);
                intent.putExtra("place_id", placeId);
                startActivity(intent);
            }
        });
    }
}