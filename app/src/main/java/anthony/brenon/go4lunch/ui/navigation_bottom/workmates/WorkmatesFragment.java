package anthony.brenon.go4lunch.ui.navigation_bottom.workmates;

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

import anthony.brenon.go4lunch.databinding.FragmentWorkmatesBinding;
import anthony.brenon.go4lunch.ui.adapter.WorkmatesAdapter;
import anthony.brenon.go4lunch.viewmodel.UserViewModel;


public class WorkmatesFragment extends Fragment {
    private final String TAG = "my_logs";

    private FragmentWorkmatesBinding binding;
    UserViewModel userViewModel;
    private final WorkmatesAdapter adapter = new WorkmatesAdapter();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentWorkmatesBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RecyclerView recyclerView = binding.workmatesRecyclerView;
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));
        recyclerView.setAdapter(adapter);
        userViewModel = new ViewModelProvider(requireActivity()).get(UserViewModel.class);
        userViewModel.getUsersList().observe(getViewLifecycleOwner(), adapter::updateDataWorkmates);
        adapter.setOnItemClickListener(placeID -> {
            //TODO open restaurant details
        });
    }
}