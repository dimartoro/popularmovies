package com.udacitydiana.android.populardimoviesapp.data;
import android.annotation.TargetApi;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Created by Dimartoro on 1/16/16.
 */



public class MovieProvider extends ContentProvider {


    private static final int MOVIES = 100;
    private static final int FAVORITES = 200;
    static final int MOVIES_BY_MOVIE_ID = 102;
    static final int FAVORITES_BY_MOVIE_ID = 202;
    private static final long ILLEGAL_ROW_ID = -1;

    private static UriMatcher sUriMatcher = buildUriMatcher();

    private static UriMatcher buildUriMatcher() {
        UriMatcher result = new UriMatcher(UriMatcher.NO_MATCH);
        result.addURI(MovieContract.CONTENT_AUTHORITY, MovieContract.PATH_MOVIES, MOVIES);
        result.addURI(MovieContract.CONTENT_AUTHORITY, MovieContract.PATH_MOVIES + "/*", MOVIES_BY_MOVIE_ID);
        result.addURI(MovieContract.CONTENT_AUTHORITY, MovieContract.PATH_FAVORITES, FAVORITES);
        result.addURI(MovieContract.CONTENT_AUTHORITY, MovieContract.PATH_FAVORITES + "/*", FAVORITES_BY_MOVIE_ID);

        return result;
    }



    private SQLiteOpenHelper mSQLiteOpenHelper;

    @Override
    public boolean onCreate() {
        mSQLiteOpenHelper = new MovieDatabaseHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        String result;
        int type = sUriMatcher.match(uri);
        switch (type){
            case MOVIES:
                result = MovieContract.MovieEntry.CONTENT_TYPE;
                break;
            case MOVIES_BY_MOVIE_ID:
                result = MovieContract.MovieEntry.CONTENT_TYPE;
                break;
            case FAVORITES:
                result = MovieContract.FavoriteEntry.CONTENT_TYPE;
                break;
            case FAVORITES_BY_MOVIE_ID:
                result = MovieContract.FavoriteEntry.CONTENT_TYPE;
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        return result;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, ContentValues values) {
        Uri result = null;
        long rowId;
        switch (sUriMatcher.match(uri)){
            case FAVORITES:
                rowId = insertFavoriteMovie(values);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri : " + uri);
        }

        Context context = getContext();
        if(context != null && rowId != ILLEGAL_ROW_ID){
            result = MovieContract.FavoriteEntry.buildFavoriteMovieUri(rowId);

            context.getContentResolver().notifyChange(result, null);
        }

        return result;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        Cursor result;
        switch (sUriMatcher.match(uri)){
            case MOVIES:
                result = getMovieList();
                break;

            case MOVIES_BY_MOVIE_ID: {
                result = getMovieByMovieId(uri, projection, sortOrder);
                break;
            }

            case FAVORITES:
                result = getFavoriteList();
                break;

            case FAVORITES_BY_MOVIE_ID: {
                result = getFavoriteByMovieId(uri, projection, sortOrder);
                break;
            }

            default:
                throw new UnsupportedOperationException("Unknown uri : " + uri);
        }

        Context context = getContext();
        if(context != null ){
            result.setNotificationUri(getContext().getContentResolver(), uri);
        }

        return result;
    }

    private Cursor getFavoriteByMovieId(Uri uri, String[] projection, String sortOrder) {

        // String movieId = uri.getPathSegments().get(1);
        // String selection = " id = ? ";


        String movieId = MovieContract.FavoriteEntry.getMovieIdFromUri(uri);//metodo para extraer el parametro del uri (movieID)
        String selection = MovieContract.FavoriteEntry.COLUMN_MOVIE_ID + " = ? ";
        String[] selectionArgs = new String[]{movieId};

        return  mSQLiteOpenHelper.getReadableDatabase().query(
                MovieContract.FavoriteEntry.TABLE_NAME,
                null,
                selection,
                selectionArgs,
                null,
                null,
                null
        );

    }


    private Cursor getMovieByMovieId(Uri uri, String[] projection, String sortOrder) {

        String movieId = MovieContract.MovieEntry.getMovieIdFromUri(uri);
        String selection = MovieContract.MovieEntry.COLUMN_MOVIE_ID + " = ? ";
        String[] selectionArgs = new String[]{movieId};

        return  mSQLiteOpenHelper.getReadableDatabase().query(
                MovieContract.MovieEntry.TABLE_NAME,
                null,
                selection,
                selectionArgs,
                null,
                null,
                null
        );

    }


    @Override
    public int delete(@NonNull Uri uri, String selection, String[] selectionArgs) {
        int resultRows;
        switch (sUriMatcher.match(uri)){
            case MOVIES:
                resultRows = deleteMoviesList();
                break;
            case FAVORITES:
                resultRows = deleteFavoriteMovie(selection, selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Delete is not supported for uri: "
                        + uri);
        }

        Context context = getContext();
        if (context != null && resultRows > 0){
            context.getContentResolver().notifyChange(uri, null);
        }

        return resultRows;
    }

    @Override
    public int update(@NonNull Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        throw new UnsupportedOperationException("Update is not supported by the provider");
    }

    @Override
    public int bulkInsert(@NonNull Uri uri, @NonNull ContentValues[] values) {
        int resultCount;
        switch (sUriMatcher.match(uri)){
            case MOVIES:
                resultCount = insertMovieList(values);
                break;
            default:
                throw new UnsupportedOperationException("BulkInsert is not supported for uri: "
                        + uri);
        }

        Context context = getContext();
        if (context != null){


            context.getContentResolver().notifyChange(uri, null);
        }

        return resultCount;
    }

    @Override
    @TargetApi(11)
    public void shutdown() {
        mSQLiteOpenHelper.close();
        super.shutdown();
    }



    private Cursor getMovieList() {
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        queryBuilder.setTables(MovieContract.MovieEntry.TABLE_NAME + " LEFT JOIN " +
                MovieContract.FavoriteEntry.TABLE_NAME + " ON " + MovieContract.MovieEntry.TABLE_NAME + "."
                + MovieContract.MovieEntry.COLUMN_MOVIE_ID +
                "=" + MovieContract.FavoriteEntry.TABLE_NAME + "." + MovieContract.FavoriteEntry.COLUMN_MOVIE_ID);

        String[] projection = new String[]{
                MovieContract.MovieEntry.TABLE_NAME + "." + MovieContract.MovieEntry._ID,
                MovieContract.MovieEntry.TABLE_NAME + "." + MovieContract.MovieEntry.COLUMN_MOVIE_ID,
                MovieContract.MovieEntry.TABLE_NAME + "." + MovieContract.MovieEntry.COLUMN_RELEASE_DATE,
                MovieContract.MovieEntry.TABLE_NAME + "." + MovieContract.MovieEntry.COLUMN_TITLE,
                MovieContract.MovieEntry.TABLE_NAME + "." + MovieContract.MovieEntry.COLUMN_POPULARITY,
                MovieContract.MovieEntry.TABLE_NAME + "." + MovieContract.MovieEntry.COLUMN_VOTE_AVERAGE,
                MovieContract.MovieEntry.TABLE_NAME + "." + MovieContract.MovieEntry.COLUMN_POSTER_PATH,
                "CASE WHEN " + MovieContract.FavoriteEntry.TABLE_NAME + "." + MovieContract.FavoriteEntry.COLUMN_MOVIE_ID +
                        " IS NOT NULL THEN 1 ELSE 0 END AS " + MovieContract.MovieEntry.COLUMN_IS_FAVORITE
        };


        return  queryBuilder.query(mSQLiteOpenHelper.getReadableDatabase(), projection, null, null,
                null, null, null);
    }

    private Cursor getFavoriteList() {
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        queryBuilder.setTables(MovieContract.FavoriteEntry.TABLE_NAME);

        String[] projection = new String[]{
                MovieContract.FavoriteEntry.TABLE_NAME + "." + MovieContract.FavoriteEntry._ID,
                MovieContract.FavoriteEntry.TABLE_NAME + "." + MovieContract.FavoriteEntry.COLUMN_MOVIE_ID,
                MovieContract.FavoriteEntry.TABLE_NAME + "." + MovieContract.FavoriteEntry.COLUMN_RELEASE_DATE,
                MovieContract.FavoriteEntry.TABLE_NAME + "." + MovieContract.FavoriteEntry.COLUMN_TITLE,
                MovieContract.FavoriteEntry.TABLE_NAME + "." + MovieContract.FavoriteEntry.COLUMN_POPULARITY,
                MovieContract.FavoriteEntry.TABLE_NAME + "." + MovieContract.FavoriteEntry.COLUMN_VOTE_AVERAGE,
                MovieContract.FavoriteEntry.TABLE_NAME + "." + MovieContract.FavoriteEntry.COLUMN_POSTER_PATH,
                "1 AS " + MovieContract.FavoriteEntry.COLUMN_IS_FAVORITE
        };


        return  queryBuilder.query(mSQLiteOpenHelper.getReadableDatabase(), projection, null, null,
                null, null, null);
    }

    private int insertMovieList(ContentValues[] values) {
        SQLiteDatabase db = mSQLiteOpenHelper.getWritableDatabase();
        int resultCount = 0;
        db.beginTransaction();
        try {
            for (ContentValues contentValues: values){
                long rowId = db.insert(MovieContract.MovieEntry.TABLE_NAME, null, contentValues);
                if (rowId != ILLEGAL_ROW_ID){
                    resultCount++;
                }
            }
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }

        return resultCount;
    }

    private int deleteMoviesList() {
        SQLiteDatabase db = mSQLiteOpenHelper.getWritableDatabase();
        return db.delete(MovieContract.MovieEntry.TABLE_NAME, null, null);
    }

    private int deleteFavoriteMovie(String selection, String[] selectionArgs) {
        SQLiteDatabase db = mSQLiteOpenHelper.getWritableDatabase();

        return db.delete(MovieContract.FavoriteEntry.TABLE_NAME, selection, selectionArgs);
    }

    private long insertFavoriteMovie(ContentValues contentValue) {
        SQLiteDatabase db = mSQLiteOpenHelper.getWritableDatabase();

        return db.insert(MovieContract.FavoriteEntry.TABLE_NAME, null, contentValue);
    }
}