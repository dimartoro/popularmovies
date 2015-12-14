package com.udacitydiana.android.populardimoviesapp.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Dimartoro on 12/12/15.
 */
public class Movie implements Parcelable{

    public String posterPath;
    public String title;
    public String synopsis;
    public double rating;
    public String releaseDate;
    public int id;

    public Movie(String posterPath,String title,String synopsis,double rating, String releaseDate,int id){
        this.posterPath =posterPath;
        this.title=title;
        this.synopsis=synopsis;
        this.rating=rating;
        this.releaseDate=releaseDate;
        this.id=id;
    }

    public Movie()
    {

    }


    public Movie(Parcel in){
        posterPath=in.readString();
        title=in.readString();
        synopsis=in.readString();
        rating=in.readDouble();
        releaseDate=in.readString();
        id=in.readInt();
    }




    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeString(this.posterPath);
        parcel.writeString(this.title);
        parcel.writeString(this.synopsis);
        parcel.writeDouble(this.rating);
        parcel.writeString(this.releaseDate);
        parcel.writeInt(this.id);

    }
    @Override
    public int describeContents() {
        return 0;
    }

    public static final Parcelable.Creator<Movie> CREATOR = new Parcelable.Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel parcel) {
            return new Movie(parcel);
        }

        @Override
        public Movie[] newArray(int i) {
            return new Movie[i];
        }

    };


    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSynopsis() {
        return synopsis;
    }

    public void setSynopsis(String synopsis) {
        this.synopsis = synopsis;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


}