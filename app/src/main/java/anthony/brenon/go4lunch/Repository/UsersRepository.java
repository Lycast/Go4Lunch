package anthony.brenon.go4lunch.Repository;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

import anthony.brenon.go4lunch.model.User;

/**
 * Created by Lycast on 24/02/2022.
 */
public class UsersRepository {
    private final String TAG = "my_logs";
    private final String LOG_INFO = "UsersRepository ";


    private static final String COLLECTION_USERS = "users";
    private final FirebaseAuth auth;


    public UsersRepository() {
        auth = FirebaseAuth.getInstance();
    }


    // Update User in Firestore
    public void updateUser(User userUpdate) {
        getUsersCollection()
                .document(userUpdate.getUid())
                .set(userUpdate)
                .addOnFailureListener(updateUserException -> Log.e(TAG, LOG_INFO + updateUserException.getMessage()));
    }


    // Create User in Firestore
    public void createUser(User userToCreate) {
        getUsersCollection()
                .add(userToCreate)
                .addOnFailureListener(createException -> Log.e(TAG, LOG_INFO + createException.getMessage()))
                .continueWith(continuation -> // DocumentReference
                        continuation.getResult().get().getResult().toObject(User.class)
                );
    }


    // Get User from Firebase
    public Task<User> getUserData() {
        return getFirebaseUserData().continueWith(data ->
                data.getResult().toObject(User.class));
    }


    // Get data of user
    private Task<DocumentSnapshot> getFirebaseUserData() {
        String uid = getCurrentFirebaseUser().getUid();
        return getUsersCollection().document(uid).get();
    }


    public FirebaseUser getCurrentFirebaseUser() {
        return auth.getCurrentUser();
    }


    private CollectionReference getUsersCollection() {
        return FirebaseFirestore.getInstance().collection(COLLECTION_USERS);
    }


    public LiveData<List<User>> getUsersLiveData() {
        MutableLiveData<List<User>> users = new MutableLiveData<>();
        getUsersCollection().get().addOnCompleteListener(queryDocumentSnapshots -> {
            List<DocumentSnapshot> documents = queryDocumentSnapshots.getResult().getDocuments();
            List<User> usersList = new ArrayList<>();
            for (DocumentSnapshot document : documents) {
                User user = document.toObject(User.class);
                usersList.add(user);
            }
            users.setValue(usersList);
        });
        return users;
    }
}



