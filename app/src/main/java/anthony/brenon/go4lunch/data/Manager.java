package anthony.brenon.go4lunch.data;

import com.google.firebase.auth.FirebaseUser;

/**
 * Created by Lycast on 29/01/2022.
 */
public class Manager {

    private static volatile Manager instance;
    private final Repository repository;

    private Manager() {
        repository = Repository.getInstance();
    }

    public static Manager getInstance() {
        Manager result = instance;
        if (result != null) {
            return result;
        }
        synchronized (Repository.class) {
            if (instance == null) {
                instance = new Manager();
            }
            return instance;
        }
    }

    public Boolean isCurrentUserLogged() {
        return (repository.getCurrentUser() != null);
    }

    public FirebaseUser getCurrentUser() {
        return repository.getCurrentUser();
    }
}
