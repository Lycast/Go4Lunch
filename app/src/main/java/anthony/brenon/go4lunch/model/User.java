package anthony.brenon.go4lunch.model;

/**
 * Created by Lycast on 29/01/2022.
 */
class User {

    private String urlImage;
    private String firstName;
    private String mail;

    public User(String urlImage, String firstName, String mail) {
        this.urlImage = urlImage;
        this.firstName = firstName;
        this.mail = mail;
    }

    public String getUrlImage() { return urlImage; }
    public void setUrlImage(String urlImage) { this.urlImage = urlImage; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getMail() { return mail; }
    public void setMail(String mail) { this.mail = mail; }
}
