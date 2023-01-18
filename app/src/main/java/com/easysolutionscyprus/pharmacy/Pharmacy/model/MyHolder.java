package com.easysolutionscyprus.pharmacy.Pharmacy.model;

import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.easysolutionscyprus.pharmacy.R;

public class MyHolder extends RecyclerView.ViewHolder{
    protected final InfoLayoutAdapter infoLayoutAdapter;
    protected final TextView textViewDistance;
    protected final ImageButton moreButton;
    protected final ImageButton favoritesButton;
    protected final ImageButton directionButton;

    public MyHolder(@NonNull View itemView) {
        super(itemView);
        infoLayoutAdapter = new InfoLayoutAdapter(itemView);
        textViewDistance = itemView.findViewById(R.id.pharmacyRecyclerViewItemDistanceTextView);
        moreButton = itemView.findViewById(R.id.pharmacyRecyclerViewMoreButton);
        favoritesButton = itemView.findViewById(R.id.cardViewBookmarksButton);
        directionButton = itemView.findViewById(R.id.cardViewDirectionButton);
    }
}

