package Model;

import java.time.LocalDateTime;

// Make sure there is a User class in the same package or import it if it's elsewhere
public class comment {

    private int ID;
    private String content;
    private user user; // Assuming 'User' is a class that represents the author of the comment
    private LocalDateTime dateTime;

    public comment() {
        // Default constructor
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public user getUser() {
        return user;
    }

    public void setUser(user user) {
        this.user = user;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }
}