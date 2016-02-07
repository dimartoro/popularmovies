package com.udacitydiana.android.populardimoviesapp.rest.service;

import com.udacitydiana.android.populardimoviesapp.rest.model.MovieResult;
import com.udacitydiana.android.populardimoviesapp.rest.model.ReviewResult;
import com.udacitydiana.android.populardimoviesapp.rest.model.TrailerResult;

import retrofit.Call;
import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.Query;

/**
 * Created by Dimartoro on 1/6/16.
 */
public interface ApiMovie {

    @GET("/3/discover/movie")
    Call<MovieResult> getResultMovie( @Query("sort_by") String sortBy, @Query("api_key") String apiKey );

    @GET("3/movie/{id}/videos")
    Call<TrailerResult> getResultTrailer( @Path("id") String id, @Query("api_key") String apiKey );

    @GET("3/movie/{id}/reviews")
    Call<ReviewResult> getResultReviews( @Path("id") String id, @Query("api_key") String apiKey );

}
