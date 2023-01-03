package com.application.mychat.model;

public class Rateresponse {
    private String userid, rating;

    public Rateresponse(String userid, String rating) {

        this.userid = userid;
        this.rating = rating;

    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }
}
