package anthony.brenon.go4lunch.viewmodel;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
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

    //TODO implements
    public void researchWorkmatePrediction(String name) {
        // launch the research in the list of workmate
    }

    //TODO implements
    public void getWorkmatePrediction() {
        // return workmates prediction repository
    }


    // UPDATES
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
                    workmateRepository.updateWorkmateIntoFS(dbUser);
                })
                .addOnFailureListener(notExistException -> {
                    // User doesn't exist in database -> create user
                    Workmate workmateToCreate = new Workmate(uid, username, urlPicture, email);
                    workmateRepository.createWorkmateIntoFS(workmateToCreate);
                });
    }

    public void updateWorkmate(Workmate workmateUpdate) {
        workmateRepository.getWorkmateData()
                .addOnSuccessListener(data -> {
                            // User exist in database -> update user
                            workmateRepository.updateWorkmateIntoFS(workmateUpdate);
                        }
                )
                .addOnFailureListener(notExistException ->
                        // User doesn't exist in database -> create user
                        Log.e(TAG, LOG_INFO + notExistException.getMessage())
                );
    }


    // GETS
    public Task<Workmate> getCurrentWorkmateData(){
        return workmateRepository.getWorkmateData();
    }

    public LiveData<List<Workmate>> getWorkmatesListLiveData(){
        return workmateRepository.getListMutableLiveData();
    }

    public LiveData<List<Workmate>> getWorkmatesList() {
        return workmateRepository.getWorkmatesListData();
    }

    public void getWorkmatesFromList(List<String> workmateIds) {
        workmateRepository.getWorkmatesFromList(workmateIds);
    }

    public void removeObserver(Observer<List<Workmate>> observer){
        workmateRepository.removeObserver(observer);
    }
}