package com.udacitydiana.android.populardimoviesapp.rest.model;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

import java.util.ArrayList;
import java.util.List;


@Parcel
public class ReviewResult {

    @SerializedName("id")
    private  long id;

    @SerializedName("page")
    private  int page;

    @SerializedName("total_pages")
    private  int totalPages;

    @SerializedName("total_results")
    private  int totalResults;



    @SerializedName("results")
    private List<Review> results = new ArrayList<Review>();


    public ReviewResult(){}


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public int getTotalResults() {
        return totalResults;
    }

    public void setTotalResults(int totalResults) {
        this.totalResults = totalResults;
    }

    public List<Review> getResults() {
        return results;
    }

    public void setResults(List<Review> results) {
        this.results = results;
    }
}
