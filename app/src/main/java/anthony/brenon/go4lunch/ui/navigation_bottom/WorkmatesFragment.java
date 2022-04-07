package anthony.brenon.go4lunch.ui.navigation_bottom;

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
import anthony.brenon.go4lunch.viewmodel.WorkmateViewModel;


public class WorkmatesFragment extends Fragment {
    private final String TAG = "my_logs";

    private FragmentWorkmatesBinding binding;
    WorkmateViewModel workmateViewModel;
    private final WorkmatesAdapter adapter = new WorkmatesAdapter(true);


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
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
        workmateViewModel = new ViewModelProvider(requireActivity()).get(WorkmateViewModel.class);
        workmateViewModel.getWorkmatesList().observe(this, adapter::updateDataWorkmates);
    }
}