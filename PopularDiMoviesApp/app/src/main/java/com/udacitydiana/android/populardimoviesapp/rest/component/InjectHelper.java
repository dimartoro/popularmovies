package com.udacitydiana.android.populardimoviesapp.rest.component;

/**
 * Created by Dimartoro on 1/6/16.
 */
public class InjectHelper {


    private static RootComponent sRootComponent;

    static {
        initModules();
    }

    private static void initModules() {
        sRootComponent = getRootComponentBuilder().build();
    }

    public static DaggerRootComponent.Builder getRootComponentBuilder() {
        return DaggerRootComponent.builder();
    }

    public static RootComponent getRootComponent() {
        if (sRootComponent == null) {
            initModules();
        }
        return sRootComponent;
    }

}
