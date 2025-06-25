/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class Post {
    private int id;
    private User author;
    private String content; // text, image or video URL
    private LocalDateTime timestamp;

    private ArrayList<Comment> comments = new ArrayList<>();
    private ArrayList<User> likes = new ArrayList<>();

    public Post(int id, User author, String content, LocalDateTime timestamp) {
        this.id = id;
        this.author = author;
        this.content = content;
        this.timestamp = timestamp;
    }

    // Getters
    public int getId() { return id; }
    public User getAuthor() { return author; }
    public String getContent() { return content; }
    public LocalDateTime getTimestamp() { return timestamp; }

    public ArrayList<Comment> getComments() { return comments; }
    public ArrayList<User> getLikes() { return likes; }

    // Like and Comment Methods
    public void like(User user) {
        if (!likes.contains(user)) {
            likes.add(user);
        }
    }

    public void addComment(Comment comment) {
        comments.add(comment);
    }
}
