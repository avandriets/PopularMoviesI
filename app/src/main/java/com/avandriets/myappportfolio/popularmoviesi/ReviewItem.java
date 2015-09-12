package com.avandriets.myappportfolio.popularmoviesi;

import android.os.Parcel;
import android.os.Parcelable;

public class ReviewItem implements Parcelable {

    public String author;
    public String content;

    public ReviewItem(String author, String content) {
        this.author = author;
        this.content = content;
    }

    protected ReviewItem(Parcel in) {
        author = in.readString();
        content = in.readString();
    }

    public static final Creator<ReviewItem> CREATOR = new Creator<ReviewItem>() {
        @Override
        public ReviewItem createFromParcel(Parcel in) {
            return new ReviewItem(in);
        }

        @Override
        public ReviewItem[] newArray(int size) {
            return new ReviewItem[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(author);
        dest.writeString(content);
    }
}
