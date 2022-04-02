package anthony.brenon.go4lunch.Repository;

import android.util.Log;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import anthony.brenon.go4lunch.model.User;

/**
 * Created by Lycast on 24/02/2022.
 */
public class UsersRepository {
    private final String TAG = "my_logs";
    private final String LOG_INFO = "user_repository ";

    private static final String COLLECTION_NAME = "users";
    private static final int RC_SIGN_IN = 123;

    private FirebaseAuth auth;

    public UsersRepository() {
        auth = FirebaseAuth.getInstance();
    }

    // Create User in Firestore
    public Task<Void> updateUser(User userUpdate) {
        return getUsersCollection()
                .document(userUpdate.getUid())
                .set(userUpdate)
                .addOnFailureListener(updateUserException -> Log.e(TAG, LOG_INFO + updateUserException.getMessage()));
    }


    // Create User in Firestore
    public Task<User> createUser(User userToCreate) {
        return getUsersCollection()
                .add(userToCreate)
                .addOnFailureListener(createException -> Log.e(TAG, LOG_INFO + createException.getMessage()))
                .continueWith(continuation -> // DocumentReference
                    continuation.getResult().get().getResult().toObject(User.class)
                    );
    }

    public FirebaseUser getFirebaseUser() {
        return getCurrentUser();
    }

    // Get User from Firebase
    public Task<User> getUserData() {
        return getFirebaseUserData().continueWith(continuationTask -> continuationTask.getResult().toObject(User.class));
    }

    // Get data of user
    private Task<DocumentSnapshot> getFirebaseUserData() {
        String uid = getCurrentUserUID();
        if (uid != null) {
            return getUsersCollection().document(uid).get();
        } else {
            return null;
        }
    }

    private FirebaseUser getCurrentUser(){
        return auth.getCurrentUser();
    }

    private String getCurrentUserUID() {
        FirebaseUser user = getCurrentUser();
        return (user != null) ? user.getUid() : null;
    }

    private CollectionReference getUsersCollection() {
        return FirebaseFirestore.getInstance().collection(COLLECTION_NAME);
    }
}
