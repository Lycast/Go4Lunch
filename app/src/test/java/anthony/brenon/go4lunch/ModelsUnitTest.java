package anthony.brenon.go4lunch;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import anthony.brenon.go4lunch.model.Location;
import anthony.brenon.go4lunch.model.Restaurant;
import anthony.brenon.go4lunch.model.Workmate;
import anthony.brenon.go4lunch.model.googleplace_models.GeometryPlace;
import anthony.brenon.go4lunch.model.googleplace_models.OpeningHours;
import anthony.brenon.go4lunch.model.googleplace_models.Photo;

/**
 * Created by Lycast on 07/06/2022.
 */
@RunWith(JUnit4.class)
public class ModelsUnitTest {

    private Restaurant restaurant;
    private Workmate workmate;

    List<String> usersChoice;
    List<Photo> photos;
    GeometryPlace geometryPlace;

    @Before
    public void setup() {
        usersChoice = Arrays.asList("U1", "U2", "U3");
        photos = new ArrayList<>();
        Photo photo = new Photo();
        photo.setPhotoReference("Aap_uEBwfJhSTjg1iokkdnD4fyASTYLOm9KoLnWPXkYRFCO7_7j5hMw_uUr3VcvbYxnfRdUocsBZQFqcJIVlixXkHOawJvzzA9onCIMJBhfVupT3GfOC4Ns0iOfpwxFZyklH0wwy3Vlc0HYOen2CAYODoHV8JX1FDAIa-RCl0fFBem6-CB83");
        photos.add(photo);
        Location locationPlace = new Location(47.0969443, -1.1245876);
        geometryPlace = new GeometryPlace(locationPlace);

        // Restaurant data
        restaurant = new Restaurant("ChIJNyTA129tBkgR1OuAN8_Vs6M", "Carrefour Contact", "Le Pont De Moine Montfaucon/Moine, Sèvremoine",
                3, 574, new OpeningHours(true), usersChoice, geometryPlace, photos,
                "https://www.carrefour.fr/magasin/contact-montigne-sur-moine", "+33 2 41 64 60 25");
        // Workmate data
        workmate = new Workmate("ChIJt6s5in9tBkgRxZAle3tiCz0", "Vanessa Howel", "https://lh3.googleusercontent.com/a/AATXAJxG1WjRehmxPADfTb8TNyN3xCFgHwQTNMgo3eAC=s96-c",
                "howelvanessa@gmail.com", true, "2500");
    }

    @Test
    public void getRestaurantDataRight() {
        assertEquals("ChIJNyTA129tBkgR1OuAN8_Vs6M", restaurant.getId());
        assertEquals("Carrefour Contact", restaurant.getName());
        assertEquals("Le Pont De Moine Montfaucon/Moine, Sèvremoine", restaurant.getAddress());
        assertEquals(3, restaurant.getRating(), 0);
        assertEquals(574, restaurant.getDistance(), 0);
        assertTrue(restaurant.getOpeningHours().isOpen_now());
        assertArrayEquals(usersChoice.toArray(), restaurant.getUsersChoice().toArray());
        assertEquals(geometryPlace, restaurant.getGeometryPlace());
        assertEquals(photos.toString(), restaurant.getPhotosUrl().toString());
        assertEquals("https://www.carrefour.fr/magasin/contact-montigne-sur-moine", restaurant.getWebsite());
        assertEquals("+33 2 41 64 60 25", restaurant.getPhoneNumber());
    }

    @Test
    public void setRestaurantDataRight() {
        Restaurant restaurantb = new Restaurant();
        restaurantb.setId("ChIJNyTA129tBkgR1OuAN8_Vs6M");
        restaurantb.setName("Carrefour Contact");
        restaurantb.setAddress("Le Pont De Moine Montfaucon/Moine, Sèvremoine");
        restaurantb.setRating(3);
        restaurantb.setDistance(574);
        restaurantb.setOpeningHours(new OpeningHours(true));
        restaurantb.setUsersChoice(usersChoice);
        restaurantb.setGeometryPlace(geometryPlace);
        restaurantb.setPhotosUrl(photos);
        restaurantb.setWebsite("https://www.carrefour.fr/magasin/contact-montigne-sur-moine");
        restaurantb.setPhoneNumber("+33 2 41 64 60 25");

        assertEquals("ChIJNyTA129tBkgR1OuAN8_Vs6M", restaurantb.getId());
        assertEquals("Carrefour Contact", restaurantb.getName());
        assertEquals("Le Pont De Moine Montfaucon/Moine, Sèvremoine", restaurantb.getAddress());
        assertEquals(3, restaurantb.getRating(), 0);
        assertEquals(574, restaurantb.getDistance(), 0);
        assertTrue(restaurantb.getOpeningHours().isOpen_now());
        assertArrayEquals(usersChoice.toArray(), restaurantb.getUsersChoice().toArray());
        assertEquals(geometryPlace, restaurantb.getGeometryPlace());
        assertEquals(photos.toString(), restaurantb.getPhotosUrl().toString());
        assertEquals("https://www.carrefour.fr/magasin/contact-montigne-sur-moine", restaurantb.getWebsite());
        assertEquals("+33 2 41 64 60 25", restaurantb.getPhoneNumber());
    }

    @Test
    public void sortRestaurantMethodIsRight() {
        List<String> userChoiceR1 = new ArrayList<>();
        List<String> userChoiceR2 = new ArrayList<>();
        userChoiceR2.add("U3");
        List<String> userChoiceR3 = new ArrayList<>();
        userChoiceR3.add("U1");
        userChoiceR3.add("U2");
        List<Restaurant> restaurants = new ArrayList<>();
        Restaurant restaurant1 = new Restaurant("ID_1", "Le petit dejeuner", "rue des peupliers", 2, 650, new OpeningHours(), userChoiceR1);
        restaurants.add(restaurant1);
        Restaurant restaurant2 = new Restaurant("ID_2", "Le bon repas", "rue des belettes", 4, 1050, new OpeningHours(false), userChoiceR2);
        restaurants.add(restaurant2);
        Restaurant restaurant3 = new Restaurant("ID_3", "Le grand festin", "rue du jardin", 5, 450, new OpeningHours(true), userChoiceR3);
        restaurants.add(restaurant3);

        Collections.sort(restaurants, Restaurant.compareDistance);
        assertEquals(restaurant3, restaurants.get(0));
        assertEquals(restaurant1, restaurants.get(1));
        assertEquals(restaurant2, restaurants.get(2));

        Collections.sort(restaurants, Restaurant.compareRating);
        assertEquals(restaurant3, restaurants.get(0));
        assertEquals(restaurant2, restaurants.get(1));
        assertEquals(restaurant1, restaurants.get(2));

        Collections.sort(restaurants, Restaurant.compareOpening);
        assertEquals(restaurant3, restaurants.get(0));
        assertEquals(restaurant2, restaurants.get(1));
        assertEquals(restaurant1, restaurants.get(2));

        Collections.sort(restaurants, Restaurant.compareWorkmate);
        assertEquals(restaurant3, restaurants.get(0));
        assertEquals(restaurant2, restaurants.get(1));
        assertEquals(restaurant1, restaurants.get(2));
    }

    @Test
    public void getWorkmateDataRight() {
        assertEquals("ChIJt6s5in9tBkgRxZAle3tiCz0", workmate.getUid());
        assertEquals("Vanessa Howel", workmate.getUsername());
        assertEquals("https://lh3.googleusercontent.com/a/AATXAJxG1WjRehmxPADfTb8TNyN3xCFgHwQTNMgo3eAC=s96-c", workmate.getUrlPicture());
        assertEquals("howelvanessa@gmail.com", workmate.getEmail());
        assertTrue(workmate.isEnableNotification());
        assertEquals("2500", workmate.getResearchRadius());
    }

    @Test
    public void setWorkmateDataRight() {
        Workmate workmateb = new Workmate();
        workmateb.setUid("ChIJt6s5in9tBkgRxZAle3tiCz0");
        workmateb.setUsername("Vanessa Howel");
        workmateb.setUrlPicture("https://lh3.googleusercontent.com/a/AATXAJxG1WjRehmxPADfTb8TNyN3xCFgHwQTNMgo3eAC=s96-c");
        workmateb.setEmail("howelvanessa@gmail.com");
        workmateb.setEnableNotification(true);
        workmateb.setResearchRadius("2500");

        assertEquals("ChIJt6s5in9tBkgRxZAle3tiCz0", workmateb.getUid());
        assertEquals("Vanessa Howel", workmateb.getUsername());
        assertEquals("https://lh3.googleusercontent.com/a/AATXAJxG1WjRehmxPADfTb8TNyN3xCFgHwQTNMgo3eAC=s96-c", workmateb.getUrlPicture());
        assertEquals("howelvanessa@gmail.com", workmateb.getEmail());
        assertTrue(workmateb.isEnableNotification());
        assertEquals("2500", workmateb.getResearchRadius());
    }
}
