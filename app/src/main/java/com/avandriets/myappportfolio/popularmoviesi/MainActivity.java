package com.avandriets.myappportfolio.popularmoviesi;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;


public class MainActivity extends AppCompatActivity {

    private final String FILM_LIST_FRAGMENT_TAG = "FLTAG";

    private String mSortOrder;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Save current sort order
        SharedPreferences mPreference = PreferenceManager.getDefaultSharedPreferences(this);
        String defaultValue = this.getResources().getString(R.string.pref_sort_default);
        String keyOfPreference = this.getString(R.string.sort_key);

        mSortOrder = mPreference.getString(keyOfPreference, defaultValue);

        if (savedInstanceState == null){

            getSupportFragmentManager().beginTransaction().add(R.id.container, new MainActivityFragment(), FILM_LIST_FRAGMENT_TAG).commit();
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
            MainActivityFragment ff = (MainActivityFragment)getSupportFragmentManager().findFragmentByTag(FILM_LIST_FRAGMENT_TAG);
            ff.loadFilms();
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
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {

            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
