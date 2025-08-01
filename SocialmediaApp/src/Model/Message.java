/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;

import java.sql.Timestamp;

public class Message {
    private int senderId;
    private String content;
    private Timestamp timestamp;

    public Message(int senderId, String content, Timestamp timestamp) {
        this.senderId = senderId;
        this.content = content;
        this.timestamp = timestamp;
    }

    public int getSenderId() { return senderId; }
    public String getContent() { return content; }
    public Timestamp getTimestamp() { return timestamp; }
}
