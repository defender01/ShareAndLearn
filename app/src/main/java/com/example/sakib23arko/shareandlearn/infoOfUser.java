package com.example.sakib23arko.shareandlearn;

import android.net.Uri;

public class infoOfUser {

    String title,description,tag,dateTime,chosenFileUrl,ext,userUid,userName,postID;

    public infoOfUser() {
        title = description = tag = dateTime = chosenFileUrl = ext = userUid = userName = postID ="";

    }

    public infoOfUser(String title, String description, String tag, String dateTime,String chosenFileUrl,String ext,String userUid,String userName,String postID) {

        this.title = title;
        this.description = description;
        this.tag = tag;
        this.dateTime = dateTime;
        this.chosenFileUrl= chosenFileUrl;
        this.ext=ext;
        this.userUid=userUid;
        this.userName=userName;
        this.postID = postID;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getTag() {
        return tag;
    }

    public String getDateTime() {
        return dateTime;
    }

    public String getchosenFileUrl() {  return chosenFileUrl;    }

    public String getExt() {
        return ext;
    }

    public String getUserUid() { return userUid; }

    public String getUserName() {return userName;}

    public String getPostID() {return postID; }
}
