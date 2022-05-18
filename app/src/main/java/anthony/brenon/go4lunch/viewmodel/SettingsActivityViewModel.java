package anthony.brenon.go4lunch.viewmodel;


import android.util.Log;

import androidx.lifecycle.ViewModel;

import com.google.android.gms.tasks.Task;

import anthony.brenon.go4lunch.Repository.WorkmateRepository;
import anthony.brenon.go4lunch.model.Workmate;

/**
 * Created by Lycast on 09/05/2022.
 */
public class SettingsActivityViewModel extends ViewModel {

    private final WorkmateRepository workmateRepository;

    public SettingsActivityViewModel() {
        super();
        workmateRepository = new WorkmateRepository();
    }

    public void deleteAccount() {
        workmateRepository.deleteAccountCurrentUser();
    }

    public void updateWorkmate(Workmate workmateUpdate) {
        workmateRepository.getCurrentUserDatabase()
                .addOnSuccessListener(data -> {
                            // User exist in database -> update user
                            workmateRepository.updateCurrentUserDatabase(workmateUpdate);
                        }
                )
                .addOnFailureListener(notExistException ->
                        // User doesn't exist in database -> create user
                        Log.d("DEBUG_LOG",  notExistException.getMessage())
                );
    }

    public Task<Workmate> getCurrentWorkmate() {
        return workmateRepository.getCurrentUserDatabase();
    }
}
