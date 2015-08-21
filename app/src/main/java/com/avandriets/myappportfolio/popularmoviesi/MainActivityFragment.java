package com.avandriets.myappportfolio.popularmoviesi;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Parcelable;
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
    GridView grid;

    public MainActivityFragment() {
        // Construct the data source
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putParcelableArrayList(FilmUtils.MOVIE_KEY, arrayOfFilms);

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        arrayOfFilms = new ArrayList<FilmItem>();

        if (savedInstanceState != null)
        {
            arrayOfFilms = savedInstanceState.getParcelableArrayList(FilmUtils.MOVIE_KEY);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        mAdapter = new FilmAdapter(rootView.getContext(),arrayOfFilms);

        grid=(GridView)rootView.findViewById(R.id.gridView);
        grid.setAdapter(mAdapter);

        grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                Intent detailFragment = new Intent(getActivity(), FilmDetail.class);

                detailFragment.putExtra(FilmItem.EXTRA_FILMID, arrayOfFilms.get(position).FilmId);
                detailFragment.putExtra(FilmItem.EXTRA_ORIGINAL_TITLE, arrayOfFilms.get(position).original_title);
                detailFragment.putExtra(FilmItem.EXTRA_RELEASE_DATE, arrayOfFilms.get(position).release_date);
                detailFragment.putExtra(FilmItem.EXTRA_OVERVIEW, arrayOfFilms.get(position).overview);
                detailFragment.putExtra(FilmItem.EXTRA_VOTE_AVERAGE, arrayOfFilms.get(position).vote_average);
                detailFragment.putExtra(FilmItem.EXTRA_POSTERURL, arrayOfFilms.get(position).PosterURL);

                startActivity(detailFragment);
            }
        });

        return rootView;
    }

    public void loadFilms()
    {
        String filmsApiKeys = getString(R.string.api_key);

        SharedPreferences mPreference = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String defaultValue = getActivity().getResources().getString(R.string.pref_sort_default);

        String keyOfPreference = getActivity().getString(R.string.sort_key);
        String sortOrder = mPreference.getString(keyOfPreference, defaultValue);

        FetchFilmTask downloadFilmsTask = new FetchFilmTask();
        downloadFilmsTask.execute(filmsApiKeys, sortOrder);

    }

    public class FetchFilmTask extends AsyncTask<String, Void, FilmItem[]> {

        public FetchFilmTask() {
        }

        private final String LOG_TAG = FetchFilmTask.class.getSimpleName();

        private FilmItem[] getFilmDataFromJson(String filmJsonStr) throws JSONException {

            final String F_LIST = "results";

            final String F_ID = "id";
            final String F_TITLE = "title";
            final String F_POSTER_URL = "poster_path";
            final String F_POPULARITY = "popularity";
            final String F_VOTE_AVERAGE = "vote_average";
            final String F_OVERVIEW = "overview";
            final String F_ORIGINAL_TITLE = "original_title";
            final String F_RELEASE_DATE = "release_date";



            JSONObject filmJson = new JSONObject(filmJsonStr);
            JSONArray filmArray = filmJson.getJSONArray(F_LIST);

            FilmItem[] resultStrs = new FilmItem[filmArray.length()];

            for(int i = 0; i < filmArray.length(); i++) {

                JSONObject oneFilm = filmArray.getJSONObject(i);

                String id           = oneFilm.getString(F_ID);
                String Title        = oneFilm.getString(F_TITLE);
                String PosterUrl    = Uri.parse(FilmUtils.Posters_URL+oneFilm.getString(F_POSTER_URL)).buildUpon().toString();
                double popularity   = oneFilm.getDouble(F_POPULARITY);
                double vote_average = oneFilm.getDouble(F_VOTE_AVERAGE);
                String overview     = oneFilm.getString(F_OVERVIEW);
                String original_title = oneFilm.getString(F_ORIGINAL_TITLE);
                String release_date = oneFilm.getString(F_RELEASE_DATE);

                FilmItem NewFilmItem = new FilmItem(id, Title, PosterUrl,popularity, vote_average,overview,original_title,release_date);

                resultStrs[i] = NewFilmItem;
            }
            return resultStrs;

        }
        @Override
        protected FilmItem[] doInBackground(String... params) {

            if (params.length != 2) {
                return null;
            }

            String api_key   = params[0];
            String sort      = params[1];

            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            String forecastJsonStr = null;


            try {
                final String FORECAST_BASE_URL = FilmUtils.Movies_URL;
                final String  SORT_PARAM   = "sort_by";
                final String  APIKEY_PARAM = "api_key";

                Uri builtUri = Uri.parse(FORECAST_BASE_URL).buildUpon()
                        .appendQueryParameter(SORT_PARAM, sort)
                        .appendQueryParameter(APIKEY_PARAM, api_key)
                        .build();

                URL url = new URL(builtUri.toString());

                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                // Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    return null;
                }

                forecastJsonStr = buffer.toString();
            } catch (IOException e) {
                Log.e(LOG_TAG, "Error ", e);
                return null;
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e(LOG_TAG, "Error closing stream", e);
                    }
                }
            }

            try {
                return getFilmDataFromJson(forecastJsonStr);
            } catch (JSONException e) {
                Log.e(LOG_TAG, e.getMessage(), e);
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(FilmItem[] result) {
            if (result != null) {
                arrayOfFilms.clear();
                for(FilmItem loadFilmItem : result) {
                    arrayOfFilms.add(loadFilmItem);
                }

                mAdapter.notifyDataSetChanged();
            }
        }
    }
}
