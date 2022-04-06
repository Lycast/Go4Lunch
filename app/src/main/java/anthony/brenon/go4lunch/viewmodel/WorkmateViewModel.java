package anthony.brenon.go4lunch.viewmodel;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

import anthony.brenon.go4lunch.Repository.WorkmateRepository;
import anthony.brenon.go4lunch.model.Workmate;

/**
 * Created by Lycast on 01/04/2022.
 */
public class WorkmateViewModel extends ViewModel {
    private static final String LOG_INFO = "UserViewModel: ";
    private final String TAG = "my_logs";

    private final WorkmateRepository workmateRepository;


    public WorkmateViewModel() {
        super();
        workmateRepository = new WorkmateRepository();
    }


    public Task<Workmate> createWorkmateIntoDb() {
        FirebaseUser fbUser = workmateRepository.getCurrentFirebaseUser();
        if (fbUser == null) {
            throw new NullPointerException("FirebaseUser is not defined");
        }
        String urlPicture = (fbUser.getPhotoUrl() != null) ? fbUser.getPhotoUrl().toString() : null;
        String username = fbUser.getDisplayName();
        String uid = fbUser.getUid();
        String email = fbUser.getEmail();
        return workmateRepository.getWorkmateData()
                .addOnSuccessListener(dbUser -> {
                    // User exist in database -> update user
                    dbUser.setUsername(username);
                    dbUser.setUid(uid);
                    dbUser.setUrlPicture(urlPicture);
                    dbUser.setEmail(email);
                    workmateRepository.updateWorkmateIntoDB(dbUser);
                })
                .addOnFailureListener(notExistException -> {
                    // User doesn't exist in database -> create user
                    Workmate workmateToCreate = new Workmate(uid, username, urlPicture);
                    workmateRepository.createWorkmateIntoDB(workmateToCreate);
                });
    }


    public void updateWorkmate(Workmate workmateUpdate) {
        workmateRepository.getWorkmateData()
                .addOnSuccessListener(data -> {
                            // User exist in database -> update user
                            workmateRepository.updateWorkmateIntoDB(workmateUpdate);
                        }
                )
                .addOnFailureListener(notExistException ->
                        // User doesn't exist in database -> create user
                        Log.e(TAG, LOG_INFO + notExistException.getMessage())
                );
    }


    public LiveData<Workmate> getCurrentWorkmateData(){
        return workmateRepository.getCurrentWorkmateData();
    }


    public LiveData<List<Workmate>> getWorkmatesList() {
        return workmateRepository.getWorkmatesListData();
    }

    public LiveData<List<Workmate>> getWorkmateListForDetails(String placeID) {
        MutableLiveData<List<Workmate>> workmateList = new MutableLiveData<>();
        workmateList.setValue(workmateRepository.getWorkmateListForDetails(placeID));
        return workmateList;
    }
}