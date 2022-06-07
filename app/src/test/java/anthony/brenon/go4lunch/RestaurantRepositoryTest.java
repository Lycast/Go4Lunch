package anthony.brenon.go4lunch;


import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import anthony.brenon.go4lunch.model.Location;
import anthony.brenon.go4lunch.model.Restaurant;
import anthony.brenon.go4lunch.model.googleplace_models.OpeningHours;
import anthony.brenon.go4lunch.repository.RestaurantRepository;

@RunWith(JUnit4.class)
public class RestaurantRepositoryTest {

    RestaurantRepository restaurantRepository = new RestaurantRepository();

    List<Restaurant> restaurants;


    @Before
    public void setup() {
        restaurants = new ArrayList<>();
        restaurants.add(new Restaurant("ID_1", "Le petit dejeuner", "rue des peupliers", 2, 650, new OpeningHours(true)));
        restaurants.add(new Restaurant("ID_2", "Le bon repas", "rue des belettes", 4, 1050, new OpeningHours(false)));
        restaurants.add(new Restaurant("ID_3", "Le grand festin", "rue du jardin", 5, 450, new OpeningHours(true)));
    }

    @Test
    public void setUserChoiceToRestaurants() {
        // Test values
        List<Restaurant> restaurantsWithUserChoice = new ArrayList<>();
        List<String> userChoiceR1 = new ArrayList<>();
        userChoiceR1.add("U1");
        List<String> userChoiceR2 = new ArrayList<>();
        userChoiceR1.add("U3");
        List<String> userChoiceR3 = new ArrayList<>();
        userChoiceR1.add("U1");
        userChoiceR1.add("U2");
        restaurantsWithUserChoice.add(new Restaurant("ID_1", "Le petit dejeuner", "rue des peupliers", 2, 650, new OpeningHours(true), userChoiceR1));
        restaurantsWithUserChoice.add(new Restaurant("ID_2", "Le bon repas", "rue des belettes", 4, 1050, new OpeningHours(false), userChoiceR2));
        restaurantsWithUserChoice.add(new Restaurant("ID_3", "Le grand festin", "rue du jardin", 5, 450, new OpeningHours(true), userChoiceR3));

        Location userLocation = new Location(47.101574, -1.121140);

        // Call the tested method
        restaurantRepository.setUserChoiceToRestaurants(restaurants, userLocation, restaurantsWithUserChoice);

        // Verify data
        for(Restaurant restaurant : restaurants){
            assertTrue(restaurant.getDistance() > 0);
            Restaurant userChoiceRestaurant;
            int indexRestaurant = Arrays.binarySearch(restaurantsWithUserChoice.toArray(new Restaurant[restaurantsWithUserChoice.size()]), restaurant,  (u1, u2) -> u1.getId().compareTo(u2.getId()));
            if(indexRestaurant >= 0) {
                userChoiceRestaurant = restaurantsWithUserChoice.get(indexRestaurant);
                assertArrayEquals(userChoiceRestaurant.getUsersChoice().toArray(), restaurant.getUsersChoice().toArray());
            } else {
                throw new NullPointerException("Restaurant not found");
            }
        }
    }
}