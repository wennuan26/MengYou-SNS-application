package Model;

import java.util.ArrayList;

public class user {

    private int ID;
    private String Fname;
    private String Lname;
    private String email;
    private String password;
    private ArrayList<post> posts;
    private ArrayList<comment> comments;
    private ArrayList<post> likes;
    private ArrayList<user> friends;

    // Fixed constructor syntax and initialized lists to avoid NullPointerException
    public user() {
        posts = new ArrayList<>();
        comments = new ArrayList<>();
        likes = new ArrayList<>();
        friends = new ArrayList<>();
    }

    public int getID(){
        return ID;

    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getFname() {
        return Fname;
    }
    public void setFname(String fname) {
        this.Fname = fname;
    }
    public String getLname() {
        return Lname;
    }
    public void setLname(String lname) {
        this.Lname = lname;
    }

    public String getName(){
        return Fname + " " + Lname;

    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }

    public ArrayList<comment> getComments() {
        return comments;
    }
    public void setComments(ArrayList<comment> comments) {
        this.comments = comments;
    }
    public ArrayList<post> getLikes() {
        return likes;
    }
    public void setLikes(ArrayList<post> likes) {
        this.likes = likes;
    }
    public ArrayList<user> getFriends() {
        return friends;
    }
    public void setFriends(ArrayList<user> friends) {
        this.friends = friends;

    }

    public ArrayList<Integer> getFriendsIDs(){
        ArrayList<Integer> friendsIDs = new ArrayList<>();
        for (user friend : friends) {
            friendsIDs.add(friend.getID());
        }
        return friendsIDs;
    }
}
