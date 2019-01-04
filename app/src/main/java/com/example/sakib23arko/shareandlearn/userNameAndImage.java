package com.example.sakib23arko.shareandlearn;

public class userNameAndImage {
     String userName;
     String userImage;

     userNameAndImage(){}

     userNameAndImage(String userName, String userImage) {
        this.userName = userName;
        this.userImage = userImage;
    }

    public String getUserImage() {
        return userImage;
    }

    public String getUserName() {
        return userName;
    }
}
