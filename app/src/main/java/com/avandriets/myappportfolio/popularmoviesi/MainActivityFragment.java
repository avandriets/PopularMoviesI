package com.avandriets.myappportfolio.popularmoviesi;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;


/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    private ArrayList<FilmItem> arrayOfFilms;
    FilmAdapter mAdapter;
    GridView mGrid;

    public interface Callback {
        public void onItemSelected(FilmItem filmData);
    }

    public MainActivityFragment() {
        arrayOfFilms = new ArrayList<>();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(FilmUtils.MOVIE_KEY, arrayOfFilms);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        Bundle arguments = getArguments();
        if (arguments != null) {
            arrayOfFilms = arguments.getParcelableArrayList(FilmUtils.FRAGMENT_LIST_DATA);
        }

        if(savedInstanceState != null)
        {
            arrayOfFilms = savedInstanceState.getParcelableArrayList(FilmUtils.MOVIE_KEY);
        }

        mAdapter = new FilmAdapter(getActivity(), arrayOfFilms);

        mGrid =(GridView)rootView.findViewById(R.id.gridView);
        mGrid.setAdapter(mAdapter);

        mGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {

                ((Callback) getActivity()).onItemSelected(arrayOfFilms.get(position));
            }
        });

        return rootView;
    }
}