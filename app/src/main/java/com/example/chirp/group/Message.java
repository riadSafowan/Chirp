package com.example.chirp.group;

public class Message {
    private String chatDate;
    private String chatMessage;
    private String chatName;
    private String chatNameID;
    private String chatTime;

    public Message(String chatDate, String chatMessage, String chatName, String chatNameID, String chatTime) {
        this.chatDate = chatDate;
        this.chatMessage = chatMessage;
        this.chatName = chatName;
        this.chatNameID = chatNameID;
        this.chatTime = chatTime;
    }

    public String getChatDate() {
        return chatDate;
    }

    public String getChatMessage() {
        return chatMessage;
    }

    public String getChatName() {
        return chatName;
    }

    public String getChatNameID() {
        return chatNameID;
    }

    public String getChatTime() {
        return chatTime;
    }
}
