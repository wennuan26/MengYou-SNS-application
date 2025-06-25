/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;

import java.util.ArrayList;

public class User {
    private int id;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String role; // guest, registered, admin

    private ArrayList<Post> posts = new ArrayList<>();
    private ArrayList<User> friends = new ArrayList<>();
    private ArrayList<Comment> comments = new ArrayList<>();

    public User(int id, String firstName, String lastName, String email, String password, String role) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    // Getters and Setters
    public int getId() { return id; }
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public String getEmail() { return email; }
    public String getPassword() { return password; }
    public String getRole() { return role; }

    public void setRole(String role) { this.role = role; }

    public ArrayList<Post> getPosts() { return posts; }
    public ArrayList<User> getFriends() { return friends; }
    public ArrayList<Comment> getComments() { return comments; }
}
