package anthony.brenon.go4lunch.model;

import com.google.firebase.firestore.ServerTimestamp;

import java.sql.Date;

/**
 * Created by Lycast on 10/02/2022.
 */
class Message {


    private String message;
    private Date dateCreated;
    private Workmate workmateSender;
    private String urlImage;


    public Message() {}


    public Message(String message, Workmate workmateSender) {
        this.message = message;
        this.workmateSender = workmateSender;
    }


    public Message(String message, Workmate workmateSender, String urlImage) {
        this.message = message;
        this.workmateSender = workmateSender;
        this.urlImage = urlImage;
    }


    // GETTERS
    public String getMessage() { return message; }
    @ServerTimestamp
    public Date getDateCreated() { return dateCreated; }
    public Workmate getUserSender() { return workmateSender; }
    public String getUrlImage() { return urlImage; }


    // SETTERS
    public void setMessage(String message) { this.message = message; }
    public void setDateCreated(Date dateCreated) { this.dateCreated = dateCreated; }
    public void setUserSender(Workmate workmateSender) { this.workmateSender = workmateSender; }
    public void setUrlImage(String urlImage) { this.urlImage = urlImage; }
}
