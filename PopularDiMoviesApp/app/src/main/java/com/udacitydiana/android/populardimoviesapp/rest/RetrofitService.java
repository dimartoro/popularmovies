package com.udacitydiana.android.populardimoviesapp.rest;

import retrofit.GsonConverterFactory;
import retrofit.Retrofit;

/**
 * Created by Dimartoro on 1/6/16.
 */
public class RetrofitService {

    public Retrofit buildRedditRetrofit() {


        return  new Retrofit.Builder()
                .baseUrl("http://api.themoviedb.org")
                .addConverterFactory(GsonConverterFactory.create())
                .build();



    }

}
