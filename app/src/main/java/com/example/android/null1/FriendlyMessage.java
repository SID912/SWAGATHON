package com.example.android.null1;

/**
 * Created by Siddharth  Singh on 09/09/2017.
 */

public class FriendlyMessage {

    private String text;
    private String name;
    private String interest;
    private String photoUrl;

    public FriendlyMessage() {
    }

    public FriendlyMessage(String text, String name, String photoUrl,String interest) {
        this.text = text;
        this.name = name;
        this.interest = interest;
        this.photoUrl = photoUrl;
    }

    public String getText() {
        return text;
    }
    public String getInterest(){
        return interest;
    }

    public void setText(String text) {
        this.text = text;
    }
    public String getName() {
            return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }
}
