// Updated Post.java
package Model;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class Post {
    private int id;
    private User author;
    private String content;
    private LocalDateTime timestamp;

    private ArrayList<Comment> comments = new ArrayList<>();
    private ArrayList<User> likes = new ArrayList<>();

    public Post(int id, User author, String content, LocalDateTime timestamp) {
        this.id = id;
        this.author = author;
        this.content = content;
        this.timestamp = timestamp;
    }

    public int getId() { return id; }
    public User getAuthor() { return author; }
    public String getContent() { return content; }
    public LocalDateTime getTimestamp() { return timestamp; }

    public ArrayList<Comment> getComments() { return comments; }
    public ArrayList<User> getLikes() { return likes; }

    public void like(User user) {
        if (!likes.contains(user)) {
            likes.add(user);
        }
    }

    public void addComment(Comment comment) {
        comments.add(comment);
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public void setAuthor(User author) {
        this.author = author;
    }
}
