package anthony.brenon.go4lunch.Repository;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

import anthony.brenon.go4lunch.model.User;

/**
 * Created by Lycast on 24/02/2022.
 */
public class UsersRepository {
    private final String TAG = "my logs";

    private List<User> userList;
    private FirebaseUser user;


    public List<User> getUserList() { return userList; }

    public FirebaseUser getUser() {
        return FirebaseAuth.getInstance().getCurrentUser(); }

    public void setUser() { }

    public void createUser() { }
}
