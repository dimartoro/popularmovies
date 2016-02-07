package com.udacitydiana.android.populardimoviesapp.rest.component;

import com.udacitydiana.android.populardimoviesapp.MovieFragment;
import com.udacitydiana.android.populardimoviesapp.fragments.DetailMovieFragment;
import com.udacitydiana.android.populardimoviesapp.rest.ServiceModule;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by Dimartoro on 1/6/16.
 */
@Singleton
@Component(modules = {ServiceModule.class})

public interface RootComponent {
    void inject(MovieFragment mainActivity);
    void inject(DetailMovieFragment detailMovieFragment);

}