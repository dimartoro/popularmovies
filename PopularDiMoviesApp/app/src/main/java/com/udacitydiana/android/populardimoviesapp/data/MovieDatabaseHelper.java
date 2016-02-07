package com.udacitydiana.android.populardimoviesapp.data;






        import android.content.Context;
        import android.database.sqlite.SQLiteDatabase;
        import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Dimartoro on 1/25/16.
 */

public class MovieDatabaseHelper extends SQLiteOpenHelper {

    public static String NAME = "movies.db";
    public static int DATABASE_VERSION = 2;

    public MovieDatabaseHelper(Context context) {
        super(context, NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String CREATE_MOVIE_TABLE = "CREATE TABLE " + MovieContract.MovieEntry.TABLE_NAME + " (" +
                MovieContract.MovieEntry._ID + " INTEGER PRIMARY KEY, " +
                MovieContract.MovieEntry.COLUMN_MOVIE_ID + " INTEGER NOT NULL, " +
                MovieContract.MovieEntry.COLUMN_TITLE + " TEXT, " +
                MovieContract.MovieEntry.COLUMN_POSTER_PATH + " TEXT, " +
                MovieContract.MovieEntry.COLUMN_OVERVIEW + " TEXT, " +
                MovieContract.MovieEntry.COLUMN_POPULARITY + " REAL, " +
                MovieContract.MovieEntry.COLUMN_VOTE_AVERAGE + " REAL," +
                MovieContract.MovieEntry.COLUMN_RELEASE_DATE + " INTEGER, " +
                "UNIQUE (" + MovieContract.MovieEntry.COLUMN_MOVIE_ID + ") ON CONFLICT REPLACE" +
                ");";

        final String CREATE_FAVORITES_TABLE = "CREATE TABLE " + MovieContract.FavoriteEntry.TABLE_NAME + " (" +
                MovieContract.FavoriteEntry._ID + " INTEGER PRIMARY KEY, " +
                MovieContract.FavoriteEntry.COLUMN_MOVIE_ID + " INTEGER NOT NULL, " +
                MovieContract.FavoriteEntry.COLUMN_TITLE + " TEXT, " +
                MovieContract.FavoriteEntry.COLUMN_POSTER_PATH + " TEXT, " +
                MovieContract.MovieEntry.COLUMN_OVERVIEW + " TEXT, " +
                MovieContract.FavoriteEntry.COLUMN_POPULARITY + " REAL, " +
                MovieContract.FavoriteEntry.COLUMN_VOTE_AVERAGE + " REAL," +
                MovieContract.FavoriteEntry.COLUMN_RELEASE_DATE + " INTEGER, " +
                "UNIQUE (" + MovieContract.FavoriteEntry.COLUMN_MOVIE_ID + ") ON CONFLICT REPLACE" +
                ");";

        db.execSQL(CREATE_MOVIE_TABLE);
        db.execSQL(CREATE_FAVORITES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists " + MovieContract.MovieEntry.TABLE_NAME + ";");
        db.execSQL("drop table if exists " + MovieContract.FavoriteEntry.TABLE_NAME + ";");

        onCreate(db);
    }
}
