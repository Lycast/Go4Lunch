package anthony.brenon.go4lunch.data;

import androidx.annotation.Nullable;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * Created by Lycast on 29/01/2022.
 */
public final class Repository {

    private static Repository instance;

    private Repository() { }

    public static Repository getInstance() {
        Repository result = instance;
        if (result != null) {
            return result;
        }
        synchronized (Repository.class) {
            if(instance == null) {
                instance = new Repository();
            }
            return instance;
        }
    }

    @Nullable
    public FirebaseUser getCurrentUser() {
        return FirebaseAuth.getInstance().getCurrentUser();
    }
}
