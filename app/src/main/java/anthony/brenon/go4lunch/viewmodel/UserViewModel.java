package anthony.brenon.go4lunch.viewmodel;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

import anthony.brenon.go4lunch.Repository.UsersRepository;
import anthony.brenon.go4lunch.model.User;

/**
 * Created by Lycast on 01/04/2022.
 */
public class UserViewModel extends ViewModel {
    private static final String LOG_INFO = "UserViewModel: ";
    private final String TAG = "my_logs";

    private final UsersRepository usersRepository;


    public UserViewModel() {
        super();
        usersRepository = new UsersRepository();
    }


    public Task<User> createUser() {
        FirebaseUser fbUser = usersRepository.getCurrentFirebaseUser();
        if (fbUser == null) {
            throw new NullPointerException("FirebaseUser is not defined");
        }
        String urlPicture = (fbUser.getPhotoUrl() != null) ? fbUser.getPhotoUrl().toString() : null;
        String username = fbUser.getDisplayName();
        String uid = fbUser.getUid();
        return usersRepository.getUserData()
                .addOnSuccessListener(dbUser -> {
                    // User exist in database -> update user
                    dbUser.setUsername(username);
                    dbUser.setUid(uid);
                    dbUser.setUrlPicture(urlPicture);
                    usersRepository.updateUser(dbUser);
                })
                .addOnFailureListener(notExistException -> {
                    // User doesn't exist in database -> create user
                    User userToCreate = new User(uid, username, urlPicture);
                    usersRepository.createUser(userToCreate);
                });
    }


    public void updateUser(User userUpdate) {
        usersRepository.getUserData()
                .addOnSuccessListener(data -> {
                            updateUser(userUpdate, data);
                            // User exist in database -> update user
                            usersRepository.updateUser(userUpdate);
                        }
                )
                .addOnFailureListener(notExistException ->
                        // User doesn't exist in database -> create user
                        Log.e(TAG, LOG_INFO + notExistException.getMessage())
                );
    }


    public Task<User> getCurrentUserFirebase(){
        return usersRepository.getUserData();
    }


    private void updateUser(User userUpdate, User dbUser) {
        userUpdate.setUsername(dbUser.getUsername());
        userUpdate.setUid(dbUser.getUid());
        userUpdate.setUrlPicture(dbUser.getUrlPicture());
    }


    public LiveData<List<User>> getUsersList() {
        return usersRepository.getUsersLiveData();
    }
}