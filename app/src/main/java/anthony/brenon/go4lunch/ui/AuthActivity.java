package anthony.brenon.go4lunch.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.firebase.ui.auth.AuthMethodPickerLayout;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Arrays;

import anthony.brenon.go4lunch.R;
import anthony.brenon.go4lunch.databinding.ActivityAuthBinding;
import anthony.brenon.go4lunch.model.User;

public class AuthActivity extends AppCompatActivity {

    private static final String TAG = "Activity_Auth";
    private ActivityAuthBinding binding;

    private static final String COLLECTION_NAME = "users";
    private static final int RC_SIGN_IN = 123;

    FirebaseAuth auth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAuthBinding.inflate(getLayoutInflater());

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
                createUser();
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
        Log.d(TAG, "on activity result");
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


    // -- IMPLEMENT USER IN DATABASE --
    // Get the Collection Reference
    private CollectionReference getUsersCollection() {
        return FirebaseFirestore.getInstance().collection(COLLECTION_NAME);
    }

    // Get the Current user id
    @Nullable
    public String getCurrentUserUID() {
        FirebaseUser user = auth.getCurrentUser();
        return (user != null) ? user.getUid() : null;
    }

    // Get data of user
    public Task<DocumentSnapshot> getUserData() {
        String uid = this.getCurrentUserUID();
        if (uid != null) {
            return this.getUsersCollection().document(uid).get();
        } else {
            return null;
        }
    }

    // Create User in Firestore
    public void createUser() {
        FirebaseUser user = auth.getCurrentUser();
        if (user != null) {
            String urlPicture = (user.getPhotoUrl() != null) ? user.getPhotoUrl().toString() : null;
            String username = user.getDisplayName();
            String uid = user.getUid();

            User userToCreate = new User(uid, username, urlPicture);
            getUserData()
                    .addOnSuccessListener(data ->
                            // User exist in database -> update user
                            getUsersCollection()
                                    .document(uid)
                                    .set(userToCreate)
                                    .addOnSuccessListener(documentSnapshot -> startMainActivity())
                                    .addOnFailureListener(updateUserException -> {
                                        Log.e(TAG, updateUserException.getMessage());
                                    })
                    )
                    .addOnFailureListener(notExistException ->
                            // User doesn't exist in database -> create user
                            getUsersCollection()
                                    .add(userToCreate)
                                    .addOnSuccessListener(documentSnapshot -> startMainActivity())
                                    .addOnFailureListener(createException -> {
                                        Log.e(TAG, createException.getMessage());
                                    })
                    );
        }
    }

    private void initUserSession() {
        if (auth.getCurrentUser() != null) {
            createUser();
        } else {
            customLayoutAuth();
        }
    }
}