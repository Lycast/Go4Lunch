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
import java.util.Objects;

import anthony.brenon.go4lunch.model.Workmate;

/**
 * Created by Lycast on 24/02/2022.
 */
public class WorkmateRepository {
    private final String TAG = "my_logs";
    private final String LOG_INFO = "WorkmateRepository ";


    private static final String COLLECTION_WORKMATES = "users";
    private final FirebaseAuth auth;


    public WorkmateRepository() {
        auth = FirebaseAuth.getInstance();
    }


    // Update User in Firestore
    public void updateWorkmateIntoDB(Workmate workmateUpdate) {
        getWorkmatesCollection()
                .document(workmateUpdate.getUid())
                .set(workmateUpdate)
                .addOnFailureListener(updateUserException -> Log.e(TAG, LOG_INFO + updateUserException.getMessage()));
    }


    // Create User in Firestore
    public void createWorkmateIntoDB(Workmate workmateToCreate) {
        getWorkmatesCollection()
                .add(workmateToCreate)
                .addOnFailureListener(createException -> Log.e(TAG, LOG_INFO + createException.getMessage()))
                .continueWith(continuation -> // DocumentReference
                        continuation.getResult().get().getResult().toObject(Workmate.class)
                );
    }


    // Get User from Firebase
    public Task<Workmate> getWorkmateData() {
        return getFirebaseUserData().continueWith(data ->
                data.getResult().toObject(Workmate.class));
    }


    public LiveData<Workmate> getCurrentWorkmateData() {
        MutableLiveData<Workmate> workmateMutableLiveData = new MutableLiveData<>();
        getFirebaseUserData().addOnSuccessListener(workmate1 ->
                workmateMutableLiveData.setValue(workmate1.toObject(Workmate.class)));
        return workmateMutableLiveData;
    }


    public LiveData<List<Workmate>> getWorkmatesListData() {
        String uid = getCurrentFirebaseUser().getUid();
        MutableLiveData<List<Workmate>> workmates = new MutableLiveData<>();
        getWorkmatesCollection().get().addOnCompleteListener(queryDocumentSnapshots -> {
            List<DocumentSnapshot> documents = queryDocumentSnapshots.getResult().getDocuments();
            List<Workmate> workmateList = new ArrayList<>();
            for (DocumentSnapshot document : documents) {
                Workmate workmate = document.toObject(Workmate.class);
                if(!workmate.getUid().equals(uid))
                    workmateList.add(workmate);
            }
         workmates.setValue(workmateList);
        });
        return workmates;
    }

    public LiveData<List<Workmate>> getWorkmateListForDetails(String placeID) {
        String uid = getCurrentFirebaseUser().getUid();
        MutableLiveData<List<Workmate>> workmates = new MutableLiveData<>();
        getWorkmatesCollection().get().addOnCompleteListener(queryDocumentSnapshots -> {
            List<DocumentSnapshot> documents = queryDocumentSnapshots.getResult().getDocuments();
            List<Workmate> workmateList = new ArrayList<>();
            for (DocumentSnapshot document : documents) {
                Workmate workmate = document.toObject(Workmate.class);
                if (Objects.requireNonNull(workmate).getRestaurantChosenId().equals(placeID) && !workmate.getUid().equals(uid))
                    workmateList.add(workmate);
            }
            workmates.setValue(workmateList);
        });
        return workmates;
    }


    private CollectionReference getWorkmatesCollection() {
        return FirebaseFirestore.getInstance().collection(COLLECTION_WORKMATES);
    }


    public FirebaseUser getCurrentFirebaseUser() {
        return auth.getCurrentUser();
    }


    // Get data of user
    private Task<DocumentSnapshot> getFirebaseUserData() {
        String uid = getCurrentFirebaseUser().getUid();
        return getWorkmatesCollection().document(uid).get();
    }
}



