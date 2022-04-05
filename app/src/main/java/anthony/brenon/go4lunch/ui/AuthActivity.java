package anthony.brenon.go4lunch.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.firebase.ui.auth.AuthMethodPickerLayout;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Arrays;

import anthony.brenon.go4lunch.R;
import anthony.brenon.go4lunch.databinding.ActivityAuthBinding;
import anthony.brenon.go4lunch.viewmodel.WorkmateViewModel;

public class AuthActivity extends AppCompatActivity {
    private final String TAG = "my_logs";
    private final String LOG_INFO = "AuthActivity ";

    private static final int RC_SIGN_IN = 123;
    private ActivityAuthBinding binding;
    private WorkmateViewModel workmateViewModel;
    FirebaseAuth auth = FirebaseAuth.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAuthBinding.inflate(getLayoutInflater());
        workmateViewModel = new ViewModelProvider(this).get(WorkmateViewModel.class);
        initUserSession();
    }


    // Show Snack Bar with a message
    private void showSnackBar(String message) {
        Snackbar.make(binding.loginLayout, message, Snackbar.LENGTH_SHORT).show();
    }


    // Method that handles response after SignIn Activity close
    private void handleResponseAfterSignIn(int requestCode, int resultCode, Intent data) {
        IdpResponse response = IdpResponse.fromResultIntent(data);
        if (requestCode == RC_SIGN_IN) {
            // SUCCESS
            if (resultCode == RESULT_OK) {
                showSnackBar(getString(R.string.connection_succeed));
                workmateViewModel.createWorkmateIntoDb().addOnSuccessListener(user -> startMainActivity());
            } else {
                // ERRORS
                if (response == null) {
                    showSnackBar(getString(R.string.error_authentication_canceled));
                } else if (response.getError() != null) {
                    if (response.getError().getErrorCode() == ErrorCodes.NO_NETWORK) {
                        showSnackBar(getString(R.string.error_no_internet));
                    } else if (response.getError().getErrorCode() == ErrorCodes.UNKNOWN_ERROR) {
                        showSnackBar(getString(R.string.error_unknown_error));
                    }
                }
            }
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        Log.d(TAG, LOG_INFO + "on activity result");
        super.onActivityResult(requestCode, resultCode, data);
        this.handleResponseAfterSignIn(requestCode, resultCode, data);
    }


    // Create a custom layout for SignIn Activity
    private void customLayoutAuth() {
        setContentView(binding.getRoot());
        AuthMethodPickerLayout customLayout = new AuthMethodPickerLayout
                .Builder(R.layout.activity_auth)
                .setGoogleButtonId(R.id.sign_in_google)
                .setFacebookButtonId(R.id.sign_in_facebook)
                .build();
        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAuthMethodPickerLayout(customLayout)
                        .setTheme(R.style.Theme_Go4Lunch)
                        .setAvailableProviders(Arrays.asList(
                                new AuthUI.IdpConfig.GoogleBuilder().build(),
                                new AuthUI.IdpConfig.FacebookBuilder().build()
                        ))
                        .build(),
                RC_SIGN_IN);
    }


    // Start main activity and close this activity
    private void startMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }


    private void initUserSession() {
        if (auth.getCurrentUser() != null) {
            workmateViewModel.createWorkmateIntoDb().addOnSuccessListener(user -> startMainActivity());
        } else {
            customLayoutAuth();
        }
    }
}