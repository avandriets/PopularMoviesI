package com.avandriets.myappportfolio.popularmoviesi.data;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;


public class PopularMoviesContract {

    public static final String CONTENT_AUTHORITY = "com.avandriets.myappportfolio.popularmoviesi.app";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final String PATH_FAVORITE          = "favorite";


    public static final class FavoriteEntry implements BaseColumns {

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_FAVORITE).build();

        public static final String CONTENT_TYPE         = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_FAVORITE;
        public static final String CONTENT_ITEM_TYPE    = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_FAVORITE;

        public static final String TABLE_NAME               = "favorite";

        public static final String COLUMN_TITLE             = "title";
        public static final String COLUMN_POSTERURL         = "posterurl";
        public static final String COLUMN_POPULARITY        = "popularity";
        public static final String COLUMN_VOTE_AVERAGE      = "vote_average";
        public static final String COLUMN_OVERVIEW          = "overview";
        public static final String COLUMN_ORIGINAL_TITLE    = "original_title";
        public static final String COLUMN_RELEASE_DATE      = "release_date";

        public static Uri buildFavoriteUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        public static String getIdFromUri(Uri uri) {
            return uri.getPathSegments().get(1);
        }

    }



}
