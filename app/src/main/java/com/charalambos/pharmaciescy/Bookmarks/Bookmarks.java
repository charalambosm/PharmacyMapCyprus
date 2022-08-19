package com.charalambos.pharmaciescy.Bookmarks;

import android.content.Context;
import android.content.SharedPreferences;

public class Bookmarks {
    protected final String bookmarksSharedPreferencesFile = "bookmarks";
    protected SharedPreferences bookmarksSharedPreferences;
    SharedPreferences.Editor editor;

    public Bookmarks(Context context) {
        bookmarksSharedPreferences = context.getSharedPreferences(bookmarksSharedPreferencesFile, Context.MODE_PRIVATE);
    }

    public boolean isBookmark(int id) {
        return bookmarksSharedPreferences.getBoolean(String.valueOf(id),false);
    }

    public void addBookmark(int id) {
        editor = bookmarksSharedPreferences.edit();
        editor.putBoolean(String.valueOf(id), true);
        editor.apply();
    }

    public void deleteBookmark(int id) {
        editor = bookmarksSharedPreferences.edit();
        editor.putBoolean(String.valueOf(id), false);
        editor.apply();
    }
}

