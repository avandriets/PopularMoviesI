package com.avandriets.myappportfolio.popularmoviesi;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.avandriets.myappportfolio.popularmoviesi.data.PopularMoviesContract;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity implements MainActivityFragment.Callback, OnTaskCompleted {

    public static final String[] FAVORITE_COLUMNS = {
            PopularMoviesContract.FavoriteEntry.TABLE_NAME + "." + PopularMoviesContract.FavoriteEntry._ID,
            PopularMoviesContract.FavoriteEntry.TABLE_NAME + "." + PopularMoviesContract.FavoriteEntry.COLUMN_ORIGINAL_TITLE,
            PopularMoviesContract.FavoriteEntry.TABLE_NAME + "." + PopularMoviesContract.FavoriteEntry.COLUMN_OVERVIEW,
            PopularMoviesContract.FavoriteEntry.TABLE_NAME + "." + PopularMoviesContract.FavoriteEntry.COLUMN_POPULARITY,
            PopularMoviesContract.FavoriteEntry.TABLE_NAME + "." + PopularMoviesContract.FavoriteEntry.COLUMN_POSTERURL,
            PopularMoviesContract.FavoriteEntry.TABLE_NAME + "." + PopularMoviesContract.FavoriteEntry.COLUMN_RELEASE_DATE,
            PopularMoviesContract.FavoriteEntry.TABLE_NAME + "." + PopularMoviesContract.FavoriteEntry.COLUMN_TITLE,
            PopularMoviesContract.FavoriteEntry.TABLE_NAME + "." + PopularMoviesContract.FavoriteEntry.COLUMN_VOTE_AVERAGE
    };

    private final String FILM_LIST_FRAGMENT_TAG = "FLTAG";
    private final String FILM_DETAIL_FRAGMENT_TAG = "FDETAILTAG";

    private String mSortOrder;
    private boolean mTwoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Save current sort order
        SharedPreferences mPreference = PreferenceManager.getDefaultSharedPreferences(this);
        String defaultValue = this.getResources().getString(R.string.pref_sort_default);
        String keyOfPreference = this.getString(R.string.sort_key);

        mSortOrder = mPreference.getString(keyOfPreference, defaultValue);

        FetchData(mSortOrder);

        if (findViewById(R.id.film_detail_container_tablet) != null) {
            mTwoPane = true;
        } else {
            mTwoPane = false;
            getSupportActionBar().setElevation(0f);

            if (savedInstanceState == null)
                getSupportFragmentManager().beginTransaction().add(R.id.container, new MainActivityFragment(), FILM_LIST_FRAGMENT_TAG).commit();
        }

    }

    private void FetchData(String sortOrder)
    {
        String filmsApiKeys = getString(R.string.api_key);

        if(!sortOrder.equals("favorite")) {
            FetchFilmTask downloadFilmsTask = new FetchFilmTask(this);
            downloadFilmsTask.execute(filmsApiKeys, sortOrder);
        }
        else
        {
            FetchFilmTaskFromDB downloadFilmsTaskFromDB = new FetchFilmTaskFromDB(this, this);
            downloadFilmsTaskFromDB.execute();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        SharedPreferences mPreference = PreferenceManager.getDefaultSharedPreferences(this);
        String defaultValue = this.getResources().getString(R.string.pref_sort_default);
        String keyOfPreference = this.getString(R.string.sort_key);
        String sortOrder = mPreference.getString(keyOfPreference, defaultValue);

        if (!sortOrder.equals(mSortOrder))
        {
            FilmDetailFragment ff = (FilmDetailFragment)getSupportFragmentManager().findFragmentByTag(FILM_DETAIL_FRAGMENT_TAG);
            if(ff != null)
                getSupportFragmentManager().beginTransaction().remove(ff).commit();
            FetchData(sortOrder);
        }
        mSortOrder = sortOrder;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_settings) {

            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemSelected(FilmItem filmItem) {

        if (mTwoPane) {
            Bundle args = new Bundle();
            args.putParcelable("FRAGMENT_DATA", filmItem);

            FilmDetailFragment fragment = new FilmDetailFragment();
            fragment.setArguments(args);
            getSupportFragmentManager().beginTransaction().replace(R.id.film_detail_container_tablet, fragment, FILM_DETAIL_FRAGMENT_TAG).commit();

        } else {
            Intent detailFragment = new Intent(this, FilmDetail.class);
            detailFragment.putExtra("FRAGMENT_DATA", filmItem);
            startActivity(detailFragment);
        }

    }

    @Override
    public void onTaskCompleted(FilmItem[] result) {

        Bundle args = new Bundle();

        ArrayList<FilmItem> arrayList = new ArrayList<FilmItem>();

        for(FilmItem loadFilmItem : result) {
                arrayList.add(loadFilmItem);
        }

        args.putParcelableArrayList("FRAGMENT_list_DATA", arrayList);

        MainActivityFragment fragment = new MainActivityFragment();
        fragment.setArguments(args);

        if(mTwoPane) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_film_list, fragment, FILM_LIST_FRAGMENT_TAG).commit();
        }
        else
        {
            getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment, FILM_LIST_FRAGMENT_TAG).commit();
        }

    }
}
