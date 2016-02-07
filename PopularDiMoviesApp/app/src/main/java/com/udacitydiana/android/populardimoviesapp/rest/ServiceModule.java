package com.udacitydiana.android.populardimoviesapp.rest;

import com.udacitydiana.android.populardimoviesapp.rest.service.ApiMovie;

import javax.inject.Singleton;
import dagger.Module;
import dagger.Provides;
import retrofit.Retrofit;

/**
 * Created by Dimartoro on 1/6/16.
 */

@Module
public class ServiceModule {

    @Provides
    @Singleton
    public RetrofitService providesRetrofitService() {
        return new RetrofitService();
    }


    @Provides
    @Singleton
    public ApiMovie providesApiService(RetrofitService retrofitService) {
        Retrofit retrofit = retrofitService.buildRedditRetrofit();
        return retrofit.create(ApiMovie.class);
    }
}
