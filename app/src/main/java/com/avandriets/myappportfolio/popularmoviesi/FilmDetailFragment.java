package com.avandriets.myappportfolio.popularmoviesi;

import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.avandriets.myappportfolio.popularmoviesi.data.PopularMoviesContract;
import com.squareup.picasso.Picasso;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


/**
 * A placeholder fragment containing a simple view.
 */
public class FilmDetailFragment extends Fragment {

    FilmItem filmItem = null;
    private ArrayList<ReviewItem> mReviewsArrayList;
    private ReviewAdapter mReviewsAdapter;

    private VideoTrailersAdapter mTrailerAdapter;
    private ArrayList<VideoItem> mTrailersArrayList;
    private ListView mListView;

    private FetchVideosTask videosTask;
    private FetchReviewsTask reviewsTask;

    public FilmDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putParcelableArrayList(FilmUtils.REVIEWS_KEY, mReviewsArrayList);
        outState.putParcelableArrayList(FilmUtils.VIDEOS_KEY, mTrailersArrayList);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        setRetainInstance(true);

        Intent intent = getActivity().getIntent();
        View rootView = inflater.inflate(R.layout.fragment_film_detail, container, false);

        String filmId = "-1";

        Bundle arguments = getArguments();
        if (arguments != null) {
            filmItem = arguments.getParcelable(FilmUtils.FRAGMENT_DATA);
        }
        else {

            if (intent != null && intent.hasExtra(FilmUtils.FRAGMENT_DATA)) {
                filmItem = intent.getParcelableExtra(FilmUtils.FRAGMENT_DATA);
            }
        }

        if(filmItem != null)
            filmId = filmItem.FilmId;

        PopulateForm(rootView);

        mReviewsArrayList = new ArrayList<ReviewItem>();
        mTrailersArrayList = new ArrayList<VideoItem>();

        mReviewsAdapter = new ReviewAdapter( getActivity(), R.layout.reviews_list_row, mReviewsArrayList);
        mTrailerAdapter = new VideoTrailersAdapter(getActivity(), R.layout.video_list_row, mTrailersArrayList);

        if(savedInstanceState == null )
        {
            String filmsApiKeys = getString(R.string.api_key);
            reviewsTask = new FetchReviewsTask();
            reviewsTask.execute(filmsApiKeys, filmId);

            videosTask = new FetchVideosTask();
            videosTask.execute(filmsApiKeys, filmId);

        }else
        {
            mReviewsArrayList  = savedInstanceState.getParcelableArrayList(FilmUtils.REVIEWS_KEY);
            mTrailersArrayList  = savedInstanceState.getParcelableArrayList(FilmUtils.VIDEOS_KEY);

            mReviewsAdapter.clear();
            mReviewsAdapter.addAll(mReviewsArrayList);

            mTrailerAdapter.clear();
            mTrailerAdapter.addAll(mTrailersArrayList);

            SetReviewsToForm(rootView);
        }

        mListView = (ListView) rootView.findViewById(R.id.TrailersListView);
        mListView.setAdapter(mTrailerAdapter);

        if(savedInstanceState != null )
            setListViewHeightBasedOnChildren(mListView);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent youtubeVideoViewer = new Intent(Intent.ACTION_VIEW, Uri.parse(mTrailersArrayList.get(position).URL.toString()));
                startActivity(youtubeVideoViewer);
            }
        });

        ((Button)rootView.findViewById(R.id.AddToFavorite)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ContentValues FavoriteValues = new ContentValues();

                FavoriteValues.put(PopularMoviesContract.FavoriteEntry._ID, filmItem.FilmId);
                FavoriteValues.put(PopularMoviesContract.FavoriteEntry.COLUMN_TITLE, filmItem.Title);
                FavoriteValues.put(PopularMoviesContract.FavoriteEntry.COLUMN_ORIGINAL_TITLE, filmItem.original_title);
                FavoriteValues.put(PopularMoviesContract.FavoriteEntry.COLUMN_OVERVIEW, filmItem.overview);
                FavoriteValues.put(PopularMoviesContract.FavoriteEntry.COLUMN_POPULARITY, filmItem.popularity);
                FavoriteValues.put(PopularMoviesContract.FavoriteEntry.COLUMN_POSTERURL, filmItem.PosterURL);
                FavoriteValues.put(PopularMoviesContract.FavoriteEntry.COLUMN_RELEASE_DATE, filmItem.release_date);
                FavoriteValues.put(PopularMoviesContract.FavoriteEntry.COLUMN_VOTE_AVERAGE, filmItem.vote_average);

                try {
                    getActivity().getContentResolver().insert(PopularMoviesContract.FavoriteEntry.CONTENT_URI, FavoriteValues);
                    Toast.makeText(getActivity(), "Film was added to Favorites", Toast.LENGTH_SHORT).show();
                } catch (android.database.SQLException ex) {
                    Toast.makeText(getActivity(), "Cannot add film to favorite", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return rootView;
    }

    private void PopulateForm(View rootView) {

        if(filmItem == null)
            return;

        //Populate form elements
        ImageView imageView = (ImageView) rootView.findViewById(R.id.imageView);
        Picasso.with(rootView.getContext()).load(filmItem.PosterURL).into(imageView);

        TextView textViewOriginalTitle = (TextView) rootView.findViewById(R.id.original_title);
        textViewOriginalTitle.setText(filmItem.original_title);

        try {

            SimpleDateFormat originalFormat = new SimpleDateFormat("yyyy");
            Date date = originalFormat.parse(filmItem.release_date);

            TextView textViewRELEASE_DATE = (TextView) rootView.findViewById(R.id.releaseDate);
            textViewRELEASE_DATE.setText(originalFormat.format(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        TextView textViewVOTE_AVERAGE = (TextView) rootView.findViewById(R.id.userRating);
        textViewVOTE_AVERAGE.setText(String.valueOf(filmItem.vote_average) + "/10");

        TextView textViewOVERVIEW = (TextView) rootView.findViewById(R.id.plotSynopsis);
        textViewOVERVIEW.setText(String.valueOf(filmItem.overview));

    }


    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = listView.getPaddingTop() + listView.getPaddingBottom();
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            if (listItem instanceof ViewGroup) {
                listItem.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            }
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }

    public void SetReviewsToForm(View rootView){

        LinearLayout linearLayout;
        if(rootView == null)
        {
            linearLayout = (LinearLayout)getActivity().findViewById(R.id.ReviewsLinearLayout);
        }
        else
        {
            linearLayout = (LinearLayout)rootView.findViewById(R.id.ReviewsLinearLayout);
        }

        final int adapterCount = mReviewsAdapter.getCount();
        for (int i = 0; i < adapterCount; i++) {
            View item = mReviewsAdapter.getView(i, null, null);
            linearLayout.addView(item);
        }

    }

    public class FetchReviewsTask extends AsyncTask<String, Void, ReviewItem[]> {

        public FetchReviewsTask() {
        }

        private final String LOG_TAG = FetchReviewsTask.class.getSimpleName();

        private ReviewItem[] getFilmReviewsDataFromJson(String filmJsonStr) throws JSONException {

            final String F_LIST = "results";

            final String F_ID = "id";
            final String F_AUTHOR = "author";
            final String F_CONTENT = "content";
            final String F_URL = "url";

            JSONObject filmJson = new JSONObject(filmJsonStr);
            JSONArray filmArray = filmJson.getJSONArray(F_LIST);

            ReviewItem[] resultStrs = new ReviewItem[filmArray.length()];

            for(int i = 0; i < filmArray.length(); i++) {

                JSONObject oneFilm = filmArray.getJSONObject(i);

                String id           = oneFilm.getString(F_ID);
                String author       = oneFilm.getString(F_AUTHOR);
                String content      = oneFilm.getString(F_CONTENT);
                String url          = oneFilm.getString(F_URL);

                resultStrs[i] = new ReviewItem(author,content);
            }
            return resultStrs;

        }
        @Override
        protected ReviewItem[] doInBackground(String... params) {

            if (params.length != 2) {
                return null;
            }

            String api_key   = params[0];
            String id      = params[1];

            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            String forecastJsonStr = null;


            try {
                final String BASE_URL = FilmUtils.MOVIES_BASE_URL;
                final String APIKEY_PARAM = "api_key";


                Uri builtUri = Uri.parse(BASE_URL).buildUpon()
                        .appendPath(id)
                        .appendPath(FilmUtils.MOVIE_REVIEW_PATH)
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
                return getFilmReviewsDataFromJson(forecastJsonStr);
            } catch (JSONException e) {
                Log.e(LOG_TAG, e.getMessage(), e);
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(ReviewItem[] result) {
            if (result != null) {
                mReviewsArrayList.clear();
                for(ReviewItem str : result) {
                    mReviewsArrayList.add(str);
                }

                mReviewsAdapter.notifyDataSetChanged();
                SetReviewsToForm(null);
            }
        }
    }

    public class FetchVideosTask extends AsyncTask<String, Void, VideoItem[]> {

        public FetchVideosTask() {
        }

        private final String LOG_TAG = FetchVideosTask.class.getSimpleName();

        private VideoItem[] getFilmReviewsDataFromJson(String filmJsonStr) throws JSONException {

            final String F_LIST = "results";

            final String F_ID   = "id";
            final String F_NAME = "name";
            final String F_KEY  = "key";
            final String F_SITE = "site";

            JSONObject filmJson = new JSONObject(filmJsonStr);
            JSONArray filmArray = filmJson.getJSONArray(F_LIST);

            VideoItem[] resultStrs = new VideoItem[filmArray.length()];

            for(int i = 0; i < filmArray.length(); i++) {

                JSONObject oneTrailer = filmArray.getJSONObject(i);

                String id           = oneTrailer.getString(F_ID);
                String name         = oneTrailer.getString(F_NAME);
                String key          = oneTrailer.getString(F_KEY);
                String site         = oneTrailer.getString(F_SITE);

                Uri builtUri = Uri.parse(FilmUtils.YOUTUBE_VIDEO_URL).buildUpon()
                        .appendQueryParameter("v", key)
                        .build();

                resultStrs[i] =  new VideoItem(id, name, key, site, builtUri);
            }
            return resultStrs;

        }
        @Override
        protected VideoItem[] doInBackground(String... params) {

            if (params.length != 2) {
                return null;
            }

            String api_key   = params[0];
            String id      = params[1];

            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            String forecastJsonStr = null;


            try {
                final String BASE_URL = FilmUtils.MOVIES_BASE_URL;
                final String APIKEY_PARAM = "api_key";


                Uri builtUri = Uri.parse(BASE_URL).buildUpon()
                        .appendPath(id)
                        .appendPath(FilmUtils.MOVIE_TRAILERS_PATH)
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
                return getFilmReviewsDataFromJson(forecastJsonStr);
            } catch (JSONException e) {
                Log.e(LOG_TAG, e.getMessage(), e);
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(VideoItem[] result) {

            if (result != null) {
                mTrailersArrayList.clear();
                for(VideoItem str : result) {
                    mTrailersArrayList.add(str);
                }
                mTrailerAdapter.notifyDataSetChanged();
                setListViewHeightBasedOnChildren(mListView);
            }

        }
    }
}
