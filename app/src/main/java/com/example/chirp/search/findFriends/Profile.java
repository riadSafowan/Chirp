package com.example.chirp.search.findFriends;

public class Profile {
    private String userName ;
    private String userID;
    private String ProfileImageUrl;

    public Profile() {
    }

    public String getUserID() {
        return userID;
    }

    public Profile(String userName, String userID , String ProfileImageUrl) {
        this.userName = userName;
        this.userID = userID;
        this.ProfileImageUrl = ProfileImageUrl;
    }


    public String getUserName() {
        return userName;
    }
    public String getProfileImageUrl() {
        return ProfileImageUrl;
    }
}
