package com.udacitydiana.android.populardimoviesapp.rest.model;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;


@Parcel
public class Trailer {

    @SerializedName("id")
    private String trailerID;

    @SerializedName("iso")
    private String trailerIso;

    @SerializedName("key")
    private String trailerKey;

    @SerializedName("name")
    private String trailerName;

    @SerializedName("site")
    private String trailerSite;

    @SerializedName("size")
    private int trailerSize;

    @SerializedName("type")
    private String trailerType;

    public Trailer(){

    }

    public String getTrailerID() {
        return trailerID;
    }

    public void setTrailerID(String trailerID) {
        this.trailerID = trailerID;
    }

    public String getTrailerIso() {
        return trailerIso;
    }

    public void setTrailerIso(String trailerIso) {
        this.trailerIso = trailerIso;
    }

    public String getTrailerKey() {
        return trailerKey;
    }

    public void setTrailerKey(String trailerKey) {
        this.trailerKey = trailerKey;
    }

    public String getTrailerName() {
        return trailerName;
    }

    public void setTrailerName(String trailerName) {
        this.trailerName = trailerName;
    }

    public String getTrailerSite() {
        return trailerSite;
    }

    public void setTrailerSite(String trailerSite) {
        this.trailerSite = trailerSite;
    }

    public int getTrailerSize() {
        return trailerSize;
    }

    public void setTrailerSize(int trailerSize) {
        this.trailerSize = trailerSize;
    }

    public String getTrailerType() {
        return trailerType;
    }

    public void setTrailerType(String trailerType) {
        this.trailerType = trailerType;
    }

}
