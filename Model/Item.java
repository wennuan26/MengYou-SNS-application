/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;

public class Item {
    private int id;
    private String name;
    private String description;
    private double price;
    private User seller;
    private String imagePath;

    // Constructor with imagePath
    public Item(int id, String name, String description, double price, User seller, String imagePath) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.seller = seller;
        this.imagePath = imagePath;
    }

    // Constructor without imagePath (optional use)
    public Item(int id, String name, String description, double price, User seller) {
        this(id, name, description, price, seller, ""); // default empty path
    }

    // Getters
    public int getId() { return id; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public double getPrice() { return price; }
    public User getSeller() { return seller; }
    public String getImagePath() { return imagePath; }

    // Setters
    public void setId(int id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setDescription(String description) { this.description = description; }
    public void setPrice(double price) { this.price = price; }
    public void setSeller(User seller) { this.seller = seller; }
    public void setImagePath(String imagePath) { this.imagePath = imagePath; }
}
