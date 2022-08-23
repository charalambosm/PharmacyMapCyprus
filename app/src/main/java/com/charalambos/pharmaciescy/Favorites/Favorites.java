package com.charalambos.pharmaciescy.Favorites;

import android.content.Context;
import android.content.SharedPreferences;

public class Favorites {
    protected final String favoritesSharedPreferencesFile = "favorites";
    protected final SharedPreferences favoritesSharedPreferences;
    SharedPreferences.Editor editor;

    public Favorites(Context context) {
        favoritesSharedPreferences = context.getSharedPreferences(favoritesSharedPreferencesFile, Context.MODE_PRIVATE);
    }

    public boolean isFavorite(int id) {
        return favoritesSharedPreferences.getBoolean(String.valueOf(id),false);
    }

    public void addFavorite(int id) {
        editor = favoritesSharedPreferences.edit();
        editor.putBoolean(String.valueOf(id), true);
        editor.apply();
    }

    public void deleteFavorite(int id) {
        editor = favoritesSharedPreferences.edit();
        editor.putBoolean(String.valueOf(id), false);
        editor.apply();
    }
}

