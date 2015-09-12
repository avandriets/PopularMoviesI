package com.avandriets.myappportfolio.popularmoviesi;

import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import com.avandriets.myappportfolio.popularmoviesi.data.PopularMoviesContract;

public class FetchFilmTaskFromDB extends AsyncTask<String, Void, FilmItem[]> {

    private OnTaskCompleted listener;

    private Context context;

    private final String LOG_TAG = FetchFilmTaskFromDB.class.getSimpleName();

    public FetchFilmTaskFromDB(OnTaskCompleted listener, Context context) {
        this.listener = listener;
        this.context = context;
    }

    @Override
    protected FilmItem[] doInBackground(String... params) {

        Cursor cursor = context.getContentResolver().query(PopularMoviesContract.FavoriteEntry.CONTENT_URI, MainActivity.FAVORITE_COLUMNS,null,null,null );

        int rowCount = cursor.getCount();

        FilmItem[] result = new FilmItem[rowCount];
        int i = 0;

        while (cursor.moveToNext()) {

            String id = cursor.getString(cursor.getColumnIndex(PopularMoviesContract.FavoriteEntry._ID));
            String original_title = cursor.getString(cursor.getColumnIndex(PopularMoviesContract.FavoriteEntry.COLUMN_ORIGINAL_TITLE));
            String overview = cursor.getString(cursor.getColumnIndex(PopularMoviesContract.FavoriteEntry.COLUMN_OVERVIEW));
            double popularity = cursor.getDouble(cursor.getColumnIndex(PopularMoviesContract.FavoriteEntry.COLUMN_POPULARITY));
            String posterurl = cursor.getString(cursor.getColumnIndex(PopularMoviesContract.FavoriteEntry.COLUMN_POSTERURL));
            String release_date = cursor.getString(cursor.getColumnIndex(PopularMoviesContract.FavoriteEntry.COLUMN_RELEASE_DATE));
            String title = cursor.getString(cursor.getColumnIndex(PopularMoviesContract.FavoriteEntry.COLUMN_TITLE));
            double vote_average = cursor.getDouble(cursor.getColumnIndex(PopularMoviesContract.FavoriteEntry.COLUMN_VOTE_AVERAGE));
            result[i] = new FilmItem(id,title,posterurl,popularity,vote_average,overview,original_title,release_date);
            i++;
        }

        return result;
    }

    @Override
    protected void onPostExecute(FilmItem[] result) {

        listener.onTaskCompleted(result);

    }
}
