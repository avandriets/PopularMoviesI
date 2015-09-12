package com.avandriets.myappportfolio.popularmoviesi;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

public class VideoItem implements Parcelable {

    public String id;
    public String name;
    public String key;
    public String site;
    public Uri URL;

    public VideoItem(String id, String name, String key, String site, Uri URL) {
        this.id = id;
        this.name = name;
        this.key = key;
        this.site = site;
        this.URL = URL;
    }


    protected VideoItem(Parcel in) {
        id = in.readString();
        name = in.readString();
        key = in.readString();
        site = in.readString();
    }

    public static final Creator<VideoItem> CREATOR = new Creator<VideoItem>() {
        @Override
        public VideoItem createFromParcel(Parcel in) {
            return new VideoItem(in);
        }

        @Override
        public VideoItem[] newArray(int size) {
            return new VideoItem[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(name);
        dest.writeString(key);
        dest.writeString(site);
    }
}
