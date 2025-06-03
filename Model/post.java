package Model;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class post {

    private int ID;
    private String content;
    private user user;
    // Assuming 'user' is a class that represents the author of the post
    // If 'user' is a class, it should be imported or defined in this package
    private LocalDateTime dateTime;
    private ArrayList<comment> comments;
    private ArrayList<user> likes;

    public post() {
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
    public ArrayList<comment> getComments() {
        return comments;
    }
    public void setComments(ArrayList<comment> comments) {
        this.comments = comments;

    }
    public ArrayList<user> getLikes() {
        return likes;
    }
    public void setLikes(ArrayList<user> likes) {
        this.likes = likes;
    }
    public void addComment(comment c) {
        if (comments == null) {
            comments = new ArrayList<>();
        }
        comments.add(c);
    }
    public void addLike(user user) {
        if (likes == null) {
            likes = new ArrayList<>();
        }
        likes.add(user);
    }
}
