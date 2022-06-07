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
    public void getWorkmateDataRight() {
        assertEquals("ChIJt6s5in9tBkgRxZAle3tiCz0", workmate.getUid());
        assertEquals("Vanessa Howel", workmate.getUsername());
        assertEquals("https://lh3.googleusercontent.com/a/AATXAJxG1WjRehmxPADfTb8TNyN3xCFgHwQTNMgo3eAC=s96-c", workmate.getUrlPicture());
        assertEquals("howelvanessa@gmail.com", workmate.getEmail());
        assertTrue(workmate.isEnableNotification());
        assertEquals("2500", workmate.getResearchRadius());
    }
}
