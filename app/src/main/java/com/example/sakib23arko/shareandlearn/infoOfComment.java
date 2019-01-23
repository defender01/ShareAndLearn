package com.example.sakib23arko.shareandlearn;

public class infoOfComment {

    String userID, mainComment;
    String userImage;

    public infoOfComment() {
        userID = mainComment = userImage = "";
    }

    public infoOfComment(String userID, String mainComment, String userImage) {
        this.userID = userID;
        this.mainComment = mainComment;
        this.userImage = userImage;
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
