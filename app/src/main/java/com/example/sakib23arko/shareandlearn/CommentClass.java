package com.example.sakib23arko.shareandlearn;

import java.util.HashSet;

public class CommentClass {

    String commentDetails, whoCommented, dateTime;

    public CommentClass(String commentDetails, String whoCommented, String dateTime) {
        this.commentDetails = commentDetails;
        this.whoCommented = whoCommented;
        this.dateTime = dateTime;
    }

    public String getcommentDetails() {
        return commentDetails;
    }

    public void setcommentDetails(String commentDetails) {
        this.commentDetails = commentDetails;
    }

    public String getWhoCommented() {
        return whoCommented;
    }

    public void setWhoCommented(String whoCommented) {
        this.whoCommented = whoCommented;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }
}
