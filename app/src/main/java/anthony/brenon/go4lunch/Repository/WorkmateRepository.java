package anthony.brenon.go4lunch.Repository;

import android.util.Log;

import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

import anthony.brenon.go4lunch.model.Workmate;

/**
 * Created by Lycast on 24/02/2022.
 * comment :
 * if your database is empty you need to create one restaurant with a attribute "id" so that the code reads correctly
 */
public class WorkmateRepository {
    private final String TAG = "my_logs";
    private final String LOG_INFO = "WorkmateRepository ";
    private static final String COLLECTION_WORKMATES = "users";

    private final FirebaseAuth auth;
    private final MutableLiveData<List<Workmate>> listMutableLiveData = new MutableLiveData<>();

    public WorkmateRepository() {
        auth = FirebaseAuth.getInstance();
    }


    // Update User in Firestore
    public void updateCurrentUserDatabase(Workmate workmateUpdate) {
        // getRestaurantsCollection().document(restaurant.getId()).set(restaurant);
        getWorkmatesCollection()
                .document(workmateUpdate.getUid())
                .set(workmateUpdate)
                .addOnFailureListener(updateUserException -> Log.e(TAG, LOG_INFO + updateUserException.getMessage()));
    }

    // GETS to FireStore
    public void getWorkmatesFromList(List<String> workmateIds) {
        getListWorkmate().addOnSuccessListener(workmates -> {
            Comparator<Workmate> c = (u1, u2) -> u1.getUid().compareTo(u2.getUid());
            List<Workmate> workmateList = new ArrayList<>();
            for (String workmateId : workmateIds) {
                Workmate a = new Workmate();
                a.setUid(workmateId);
                int indexWorkmate = Arrays.binarySearch(workmates.toArray(new Workmate[workmates.size()]), a, c);
                if(indexWorkmate >= 0) {
                    workmateList.add(workmates.get(indexWorkmate));
                }
            }
            listMutableLiveData.postValue(workmateList);
        });
    }

    // workmates list for details recycler view
    public MutableLiveData<List<Workmate>> getWorkmatesLiveData(){
        return listMutableLiveData;
    }

    public Task<Workmate> getCurrentUserDatabase() {
        return getFirebaseUserData().continueWith(data ->
                data.getResult().toObject(Workmate.class));
    }

    public Task<List<Workmate>> getListWorkmate() {
        return getWorkmatesCollection().get().continueWith(data ->
                data.getResult().toObjects(Workmate.class));
    }

    public LiveData<List<Workmate>> getWorkmatesDatabase() {
        String uid = getCurrentFirebaseUser().getUid();
        MutableLiveData<List<Workmate>> workmates = new MutableLiveData<>();

        getWorkmatesCollection().get().addOnSuccessListener(queryDocumentSnapshots -> {

            List<DocumentSnapshot> documents = queryDocumentSnapshots.getDocuments();
            List<Workmate> workmateList = new ArrayList<>();

            for (DocumentSnapshot document : documents) {
                Workmate workmate = document.toObject(Workmate.class);
                if(!Objects.requireNonNull(workmate).getUid().equals(uid))
                    workmateList.add(workmate);
            }
            workmates.postValue(workmateList);
        });
        return workmates;
    }

    // GETS FireBaseUser
    public FirebaseUser getCurrentFirebaseUser() {
        return auth.getCurrentUser();
    }

    @Nullable
    public String getCurrentUserId() {
        FirebaseUser user = auth.getCurrentUser();
        return (user != null) ? user.getUid() : null;
    }

    public void removeObserver(Observer<List<Workmate>> observer){
        this.listMutableLiveData.removeObserver(observer);
    }

    private CollectionReference getWorkmatesCollection() {
        return FirebaseFirestore.getInstance().collection(COLLECTION_WORKMATES);
    }

    private Task<DocumentSnapshot> getFirebaseUserData() {
        String uid = getCurrentFirebaseUser().getUid();
        Log.d("my_logs", uid);
        Log.d("my_logs", getWorkmatesCollection().document(uid).get().toString());
        return getWorkmatesCollection().document(uid).get();
    }

    public void deleteAccountCurrentUser() {
        String uid = getCurrentFirebaseUser().getUid();
        getWorkmatesCollection().document(uid).delete();
        Objects.requireNonNull(auth.getCurrentUser()).delete();
    }
}