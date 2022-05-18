package anthony.brenon.go4lunch.ui;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.firebase.ui.auth.AuthUI;

import anthony.brenon.go4lunch.R;
import anthony.brenon.go4lunch.databinding.ActivitySettingsBinding;
import anthony.brenon.go4lunch.model.Workmate;
import anthony.brenon.go4lunch.viewmodel.SettingsActivityViewModel;

public class SettingsActivity extends AppCompatActivity {

    private ActivitySettingsBinding binding;
    private SettingsActivityViewModel viewModel;
    private Workmate currentWorkmate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySettingsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        viewModel = new ViewModelProvider(this).get(SettingsActivityViewModel.class);

        getCurrentWorkmateData();

        onClickDeleteAccount();
        onChangeRadiusSlider();
        onSwitchNotification();
    }

    private void onChangeRadiusSlider() {
        binding.seekBar.addOnChangeListener((slider, value, fromUser) -> {
            float sliderValue = binding.seekBar.getValue();
            currentWorkmate.setResearchRadius(String.valueOf( (int) sliderValue));
            updateSettingsUser();
        });
    }

    private void onSwitchNotification() {
        binding.switchNotification.setOnCheckedChangeListener((buttonView, isChecked) -> {
            currentWorkmate.setEnableNotification(binding.switchNotification.isChecked());
            updateSettingsUser();
        });
    }

    private void onClickDeleteAccount() {
        binding.btnDeleteAccount.setOnClickListener(view -> showAlertDialog());
    }

    private void showAlertDialog() {
        new AlertDialog.Builder(this)
                .setTitle(R.string.dialog_delete_account)
                .setMessage(R.string.alert_message_delete_account)
                .setPositiveButton(android.R.string.yes, (dialog, which) -> {
                    viewModel.deleteAccount();
                    signOut();
                })
                .setNegativeButton(android.R.string.no, null)
                .show();
    }

    // Logout application
    private void signOut(){
        AuthUI.getInstance().signOut(this).addOnSuccessListener(unused -> {
            Intent intent = new Intent(this, AuthActivity.class);
            startActivity(intent);
            finish();
        });
    }

    private void updateSettingsUser() {
        viewModel.updateWorkmate(currentWorkmate);
    }

    private void getCurrentWorkmateData()  {
        viewModel.getCurrentWorkmate().addOnSuccessListener(workmate -> {
            currentWorkmate = workmate;
            if ( workmate.getResearchRadius() == null ) { binding.seekBar.setValue(500);
            } else {
                binding.seekBar.setValue(Integer.parseInt(currentWorkmate.getResearchRadius()));
            }
            binding.switchNotification.setChecked(currentWorkmate.isEnableNotification());
        });
    }
}