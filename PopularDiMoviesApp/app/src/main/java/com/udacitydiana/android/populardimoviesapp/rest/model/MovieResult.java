package com.udacitydiana.android.populardimoviesapp.rest.model;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dimartoro on 1/6/16.
 */

@Parcel
public class MovieResult {

    @SerializedName("page")
    private  int page;

    @SerializedName("results")
    private List<Movie> results = new ArrayList<Movie>();

    @SerializedName("totalResults")
    private int totalResults;

    @SerializedName("totalPages")
    private int totalPages;


    public MovieResult (){

    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public List<Movie> getResults() {
        return results;
    }

    public void setResults(List<Movie> results) {
        this.results = results;
    }

    public int getTotalResults() {
        return totalResults;
    }

    public void setTotalResults(int totalResults) {
        this.totalResults = totalResults;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }
}
