package com.majesty.letswatch.database;

import android.provider.BaseColumns;

public class DatabaseContract {
    static String TABLE_NAME = "favorite";
    static String TABLE_NAME1 = "favoriteTvShow";

    public static final class FavoriteColumns implements BaseColumns {
        public static String ID_MOVIE = "id_movie";
        public static String NAME_MOVIE = "name_movie";
        public static String DESCRIPTION_MOVIE = "description_movie";
        public static String PHOTO_MOVIE = "photo_movie";
        public static String RELEASE_MOVIE = "release_movie";
        public static String RATING_MOVIE = "rating_movie";
        public static String ID = "id";
        public static String NAME = "name";
        public static String DESCRIPTION = "description";
        public static String PHOTO = "photo";
        public static String RELEASE = "release";
        public static String RATING = "rating";
    }
}
