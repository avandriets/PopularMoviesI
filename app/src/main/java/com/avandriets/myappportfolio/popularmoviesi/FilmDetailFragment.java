package com.avandriets.myappportfolio.popularmoviesi;

import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


/**
 * A placeholder fragment containing a simple view.
 */
public class FilmDetailFragment extends Fragment {

    public FilmDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Intent intent = getActivity().getIntent();
        View rootView = inflater.inflate(R.layout.fragment_film_detail, container, false);

        if(intent != null && intent.hasExtra(FilmItem.EXTRA_FILMID) )
        {
            String filmId = intent.getStringExtra(FilmItem.EXTRA_FILMID);
            //Toast.makeText(getActivity(), filmId, Toast.LENGTH_SHORT).show();
        }

        if(intent != null && intent.hasExtra(FilmItem.EXTRA_POSTERURL) )
        {
            String filmPosterURL = intent.getStringExtra(FilmItem.EXTRA_POSTERURL);
            ImageView imageView = (ImageView) rootView.findViewById(R.id.imageView);
            Picasso.with(rootView.getContext()).load(filmPosterURL).into(imageView);
        }

        if(intent != null && intent.hasExtra(FilmItem.EXTRA_ORIGINAL_TITLE) )
        {
            String filmORIGINAL_TITLE = intent.getStringExtra(FilmItem.EXTRA_ORIGINAL_TITLE);
            TextView textViewOriginalTitle = (TextView) rootView.findViewById(R.id.original_title);
            textViewOriginalTitle.setText(filmORIGINAL_TITLE);
        }

        //TODO преобразовать в дату и вывест год
        if(intent != null && intent.hasExtra(FilmItem.EXTRA_RELEASE_DATE) )
        {
            SimpleDateFormat originalFormat = new SimpleDateFormat("yyyy");
            String filmRELEASE_DATE = intent.getStringExtra(FilmItem.EXTRA_RELEASE_DATE);

            try {
                Date date = originalFormat.parse(filmRELEASE_DATE);

                TextView textViewRELEASE_DATE = (TextView) rootView.findViewById(R.id.releaseDate);
                textViewRELEASE_DATE.setText(originalFormat.format(date));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        if(intent != null && intent.hasExtra(FilmItem.EXTRA_VOTE_AVERAGE) )
        {
            double filmVOTE_AVERAGE = intent.getDoubleExtra(FilmItem.EXTRA_VOTE_AVERAGE, 0);
            TextView textViewVOTE_AVERAGE = (TextView) rootView.findViewById(R.id.userRating);
            textViewVOTE_AVERAGE.setText( String.valueOf(filmVOTE_AVERAGE)+"/10");
        }

        if(intent != null && intent.hasExtra(FilmItem.EXTRA_OVERVIEW) )
        {
            String filmOVERVIEW = intent.getStringExtra(FilmItem.EXTRA_OVERVIEW);
            TextView textViewOVERVIEW = (TextView) rootView.findViewById(R.id.plotSynopsis);
            textViewOVERVIEW.setText( String.valueOf(filmOVERVIEW));
        }

        return rootView;
    }
}
