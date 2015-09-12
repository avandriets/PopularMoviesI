/*
 * Copyright (C) 2014 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.avandriets.myappportfolio.popularmoviesi.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.avandriets.myappportfolio.popularmoviesi.data.PopularMoviesContract.FavoriteEntry;


public class PopularMoviesDbHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;

    static final String DATABASE_NAME = "favorite.db";

    public PopularMoviesDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {


        final String SQL_CREATE_FAVORITE_TABLE = "CREATE TABLE " + FavoriteEntry.TABLE_NAME + " (" +
                FavoriteEntry._ID                       + " INTEGER PRIMARY KEY," +
                FavoriteEntry.COLUMN_TITLE              + " TEXT, " +
                FavoriteEntry.COLUMN_POSTERURL          + " TEXT, " +
                FavoriteEntry.COLUMN_POPULARITY         + " REAL, " +
                FavoriteEntry.COLUMN_VOTE_AVERAGE       + " REAL, " +
                FavoriteEntry.COLUMN_OVERVIEW           + " TEXT, " +
                FavoriteEntry.COLUMN_ORIGINAL_TITLE     + " TEXT, " +
                FavoriteEntry.COLUMN_RELEASE_DATE       + " TEXT " +
                " );";

        sqLiteDatabase.execSQL(SQL_CREATE_FAVORITE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {

        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + FavoriteEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);

    }
}
