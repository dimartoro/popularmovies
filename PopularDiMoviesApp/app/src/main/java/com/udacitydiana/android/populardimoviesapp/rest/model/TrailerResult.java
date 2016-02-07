package com.udacitydiana.android.populardimoviesapp.rest.model;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

import java.util.ArrayList;
import java.util.List;


@Parcel
public class TrailerResult {

    @SerializedName("id")
    private  int id;

    @SerializedName("results")
    private List<Trailer> results = new ArrayList<Trailer>();

    public TrailerResult(){

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<Trailer> getResults() {
        return results;
    }

    public void setResults(List<Trailer> results) {
        this.results = results;
    }
}
