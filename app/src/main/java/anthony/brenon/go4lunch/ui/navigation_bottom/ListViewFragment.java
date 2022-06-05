package anthony.brenon.go4lunch.ui.navigation_bottom;


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
import anthony.brenon.go4lunch.ui.adapter.RestaurantsAdapter;
import anthony.brenon.go4lunch.viewmodel.MainActivityViewModel;

public class ListViewFragment extends Fragment {

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
        RecyclerView recyclerView = binding.restaurantsRecyclerView;
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));
        recyclerView.setAdapter(adapter);
        MainActivityViewModel viewModel = new ViewModelProvider(requireActivity()).get(MainActivityViewModel.class);

        viewModel.getLiveDataListRestaurants().observe(getViewLifecycleOwner(), adapter::updateDataRestaurants);

        adapter.setOnItemClickListener(placeId -> {
            Intent intent = new Intent(getActivity(), DetailsRestaurantActivity.class);
            intent.putExtra("place_id", placeId);
            startActivity(intent);
        });
    }
}