package com.udacitydiana.android.populardimoviesapp.rest.model;


import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;



@Parcel
public class Review {

    @SerializedName("id")
    private String reviewID;


    @SerializedName("author")
    private String reviewAuthor;

    @SerializedName("content")
    private String reviewContent;


    @SerializedName("url")
    private String reviewUrl;

    public Review(){}


    public String getReviewID() {
        return reviewID;
    }

    public void setReviewID(String reviewID) {
        this.reviewID = reviewID;
    }

    public String getReviewAuthor() {
        return reviewAuthor;
    }

    public void setReviewAuthor(String reviewAuthor) {
        this.reviewAuthor = reviewAuthor;
    }

    public String getReviewContent() {
        return reviewContent;
    }

    public void setReviewContent(String reviewContent) {
        this.reviewContent = reviewContent;
    }

    public String getReviewUrl() {
        return reviewUrl;
    }

    public void setReviewUrl(String reviewUrl) {
        this.reviewUrl = reviewUrl;
    }

}
