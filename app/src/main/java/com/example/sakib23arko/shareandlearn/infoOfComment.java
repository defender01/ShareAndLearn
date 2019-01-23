package com.example.sakib23arko.shareandlearn;

public class infoOfComment {

    String userID, mainComment;
    String userImage;
    String timestamp;

    public infoOfComment() {
        userID = mainComment = userImage = "";
    }


    public infoOfComment(String userID, String mainComment, String userImage, String timestamp) {
        this.userID = userID;
        this.mainComment = mainComment;
        this.userImage = userImage;
        this.timestamp = timestamp;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getUserImage() {
        return userImage;
    }

    public void setUserImage(String userImage) {
        this.userImage = userImage;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getMainComment() {
        return mainComment;
    }

    public void setMainComment(String mainComment) {
        this.mainComment = mainComment;
    }
}
