package com.easysolutionscyprus.pharmacy.Pharmacy.model;

import android.view.View;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.easysolutionscyprus.pharmacy.R;

public class MyHolder extends RecyclerView.ViewHolder{
    protected final InfoLayoutAdapter infoLayoutAdapter;
    protected final ImageButton moreButton;
    protected final ImageButton favoritesButton;

    public MyHolder(@NonNull View itemView) {
        super(itemView);
        infoLayoutAdapter = new InfoLayoutAdapter(itemView);
        moreButton = itemView.findViewById(R.id.pharmacyRecyclerViewMoreButton);
        favoritesButton = itemView.findViewById(R.id.cardViewBookmarksButton);
    }
}

