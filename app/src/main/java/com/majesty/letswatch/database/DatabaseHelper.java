package com.majesty.letswatch.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import static android.content.ContentValues.TAG;
import static com.majesty.letswatch.database.DatabaseContract.FavoriteColumns.ID;
import static com.majesty.letswatch.database.DatabaseContract.TABLE_NAME;
import static com.majesty.letswatch.database.DatabaseContract.TABLE_NAME1;
import static com.majesty.letswatch.database.DatabaseContract.FavoriteColumns.ID_MOVIE;

public class DatabaseHelper extends SQLiteOpenHelper {
    public static String DATABASE_NAME = "favoriteMovie";
    private static final int DATABASE_VERSION = 1;
    private static final String SQL_CREATE_TABLE_FAVORITE = String.format("CREATE TABLE %s"
                    + " (%s TEXT PRIMARY KEY," +
                    " %s TEXT NOT NULL," +
                    " %s TEXT NOT NULL," +
                    " %s TEXT NOT NULL," +
                    " %s TEXT NOT NULL," +
                    " %s TEXT NOT NULL)",
            TABLE_NAME,
            DatabaseContract.FavoriteColumns.ID_MOVIE,
            DatabaseContract.FavoriteColumns.NAME_MOVIE,
            DatabaseContract.FavoriteColumns.DESCRIPTION_MOVIE,
            DatabaseContract.FavoriteColumns.PHOTO_MOVIE,
            DatabaseContract.FavoriteColumns.RELEASE_MOVIE,
            DatabaseContract.FavoriteColumns.RATING_MOVIE
    );
    private static final String SQL_CREATE_TABLE_FAVORITE_TV = String.format("CREATE TABLE %s"
                    + " (%s TEXT PRIMARY KEY," +
                    " %s TEXT NOT NULL," +
                    " %s TEXT NOT NULL," +
                    " %s TEXT NOT NULL," +
                    " %s TEXT NOT NULL," +
                    " %s TEXT NOT NULL)",
            TABLE_NAME1,
            ID,
            DatabaseContract.FavoriteColumns.NAME,
            DatabaseContract.FavoriteColumns.DESCRIPTION,
            DatabaseContract.FavoriteColumns.PHOTO,
            DatabaseContract.FavoriteColumns.RELEASE,
            DatabaseContract.FavoriteColumns.RATING
    );

    public DatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_TABLE_FAVORITE);
        db.execSQL(SQL_CREATE_TABLE_FAVORITE_TV);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME1);
        onCreate(db);
    }

    public long insertFavMovie(String id_movie, String name_movie, String description_movie, String photo_movie, String release_movie, String rating_movie) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("id_movie", id_movie);
        contentValues.put("name_movie", name_movie);
        contentValues.put("description_movie", description_movie);
        contentValues.put("photo_movie", photo_movie);
        contentValues.put("release_movie", release_movie);
        contentValues.put("rating_movie", rating_movie);
        long result = db.insert(TABLE_NAME, null, contentValues);
        return result;
    }

    public long insertFavTvShow(String id, String name, String description, String photo, String release, String rating) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("id", id);
        contentValues.put("name", name);
        contentValues.put("description", description);
        contentValues.put("photo", photo);
        contentValues.put("release", release);
        contentValues.put("rating", rating);
        long result = db.insert(TABLE_NAME1, null, contentValues);
        return result;
    }

    public boolean deleteDataFavMovie(String id_movie){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, "id_movie = ? ",new String[]{id_movie});
        db.close();
        return true;
    }

    public boolean deleteDataFavTvShow(String id){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME1, "id = ? ",new String[]{id});
        db.close();
        return true;
    }

    public Cursor getDataFavoriteMovie(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery("select * from "+TABLE_NAME,  null);
        return res;
    }

    public Cursor getDataFavoriteTvShow(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery("select * from "+TABLE_NAME1, null);
        return res;
    }

    public boolean DataFavoriteMovie(String id_movie){
        try {
            SQLiteDatabase db = this.getReadableDatabase();
            String query = "select "+ID_MOVIE+" from "+TABLE_NAME+" where "+ID_MOVIE+" = ? ";
            Cursor cursor = db.rawQuery(query, new String[]{id_movie});
            if (cursor.moveToFirst()){
                db.close();
                Log.d(TAG, "DataFavoriteMovie: Already exists");
                return true;
            }
        }catch (Exception errorException){
            Log.d(TAG, "DataFavoriteMovie: Exception Occured" + errorException);
        }return false;
    }

    public boolean DataFavoriteTvShow(String id){
        try {
            SQLiteDatabase db = this.getReadableDatabase();
            String query = "select "+ID+" from "+TABLE_NAME1+" where "+ID+" = ? ";
            Cursor cursor = db.rawQuery(query, new String[]{id});
            if (cursor.moveToFirst()){
                db.close();
                Log.d(TAG, "DataFavoriteMovie: Already exists");
                return true;
            }
        }catch (Exception errorException){
            Log.d(TAG, "DataFavoriteMovie: Exception Occured" + errorException);
        }return false;
    }

}
