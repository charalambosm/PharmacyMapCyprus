package com.charalambos.pharmaciescy.Pharmacy.internal;

import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.charalambos.pharmaciescy.R;

public class MyHolder extends RecyclerView.ViewHolder{
    protected final TextView textViewName;
    protected final TextView textViewAddress;
    protected final TextView textViewNight;
    protected final TextView textViewDistance;
    protected final TextView textViewPhone;
    protected final ImageButton moreButton;
    protected final ImageButton favoritesButton;
    protected final ImageButton directionButton;

    public MyHolder(@NonNull View itemView) {
        super(itemView);
        textViewName = itemView.findViewById(R.id.pharmacyRecyclerViewItemNameTextView);
        textViewAddress = itemView.findViewById(R.id.pharmacyRecyclerViewItemAddressTextView);
        textViewDistance = itemView.findViewById(R.id.pharmacyRecyclerViewItemDistanceTextView);
        textViewPhone = itemView.findViewById(R.id.pharmacyRecyclerViewItemPhoneTextView);
        textViewNight = itemView.findViewById(R.id.pharmacyRecyclerViewItemNightTextView);
        moreButton = itemView.findViewById(R.id.pharmacyRecyclerViewMoreButton);
        favoritesButton = itemView.findViewById(R.id.cardViewBookmarksButton);
        directionButton = itemView.findViewById(R.id.cardViewDirectionButton);
    }
}

