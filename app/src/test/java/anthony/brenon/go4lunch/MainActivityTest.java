package anthony.brenon.go4lunch;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mock;

import anthony.brenon.go4lunch.model.Restaurant;
import anthony.brenon.go4lunch.model.Workmate;
import anthony.brenon.go4lunch.model.googleplace_models.OpeningHours;
import anthony.brenon.go4lunch.repository.WorkmateRepository;
import anthony.brenon.go4lunch.viewmodel.MainActivityViewModel;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(JUnit4.class)
public class MainActivityTest {

    private MainActivityViewModel viewModel;

    private static final Restaurant restaurant1 = new Restaurant("r1", "Le petit dejeuner", "rue des peupliers", 2, 650, new OpeningHours(true));
    private static final Restaurant restaurant2 = new Restaurant("r2", "Le bon repas", "rue des belettes", 4, 1050, new OpeningHours(false));
    private static final Restaurant restaurant3 = new Restaurant("r3", "Le grand festin", "rue du jardin", 5, 450, new OpeningHours(true));

    private static final Workmate workmate1 = new Workmate("w1", "Patrice Harrow", "pictureURL", "jeanjacquesgoldman@gmail.com");
    private static final Workmate workmate2 = new Workmate("w2", "Marie Bubble", "pictureURL", "sting@hotmail.com");
    private static final Workmate workmate3 = new Workmate("w3", "Jean Come", "pictureURL", "bono@u2.com");

    @Mock
    WorkmateRepository repository;

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    @Before
    public void setUp() {
        viewModel = new MainActivityViewModel();
    }

    @Test
    public void currentUser_isGetRight() {
    }

    @Test
    public void listOfWorkmates_isGetRight() {
    }

    @Test
    public void listOfRestaurants_isGetRight() {}

    @Test
    public void sortRestaurants_byDistance_isGetRight() {}

    @Test
    public void sortRestaurants_byRating_isGetRight() {}

    @Test
    public void sortRestaurants_byWorkmates_isGetRight() {}

    @Test
    public void sortRestaurants_byOpening_isGetRight() {}

    @Test
    public void callNearbyRestaurantsApi_isGetRight() {}
}