package com.avandriets.myappportfolio.popularmoviesi;

import android.os.Parcel;
import android.os.Parcelable;

public class FilmItem implements Parcelable {

    static final String EXTRA_FILMID = "com.popularmoviesI.EXTRA_FILMID";
    static final String EXTRA_TITLE = "com.popularmoviesI.EXTRA_TITLE";
    static final String EXTRA_POSTERURL = "com.popularmoviesI.EXTRA_POSTERURL";
    static final String EXTRA_POPULARITY = "com.popularmoviesI.EXTRA_POPULARITY";
    static final String EXTRA_VOTE_AVERAGE = "com.popularmoviesI.EXTRA_VOTE_AVERAGE";
    static final String EXTRA_OVERVIEW = "com.popularmoviesI.EXTRA_OVERVIEW";
    static final String EXTRA_ORIGINAL_TITLE = "com.popularmoviesI.EXTRA_ORIGINAL_TITLE";
    static final String EXTRA_RELEASE_DATE = "com.popularmoviesI.EXTRA_RELEASE_DATE";

    public String FilmId;
    public String Title;
    public String PosterURL;
    public double popularity;
    public double vote_average;
    public String overview;
    public String original_title;
    public String release_date;

    public FilmItem(String filmId, String title, String posterURL, double popularity, double vote_average, String overview, String original_title, String release_date) {
        FilmId = filmId;
        Title = title;
        PosterURL = posterURL;
        this.popularity = popularity;
        this.vote_average = vote_average;
        this.overview = overview;
        this.original_title = original_title;
        this.release_date = release_date;
    }

    public FilmItem(String FilmId, String Title, String PosterURL) {
        this.Title = Title;
        this.PosterURL = PosterURL;
        this.FilmId = FilmId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeString(FilmId);
        dest.writeString(Title);
        dest.writeString(PosterURL);
        dest.writeDouble(popularity);
        dest.writeDouble(vote_average);
        dest.writeString(overview);
        dest.writeString(original_title);
        dest.writeString(release_date);

    }
}
