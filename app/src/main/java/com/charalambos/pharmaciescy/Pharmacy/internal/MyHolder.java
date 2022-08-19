package com.charalambos.pharmaciescy.Pharmacy.internal;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.charalambos.pharmaciescy.R;

public class MyHolder extends RecyclerView.ViewHolder{
    protected TextView textViewName, textViewAddress, textViewNight, textViewDistance, textViewPhone;

    public MyHolder(@NonNull View itemView) {
        super(itemView);
        textViewName = itemView.findViewById(R.id.pharmacyRecyclerViewItemNameTextView);
        textViewAddress = itemView.findViewById(R.id.pharmacyRecyclerViewItemAddressTextView);
        textViewDistance = itemView.findViewById(R.id.pharmacyRecyclerViewItemDistanceTextView);
        textViewPhone = itemView.findViewById(R.id.pharmacyRecyclerViewItemPhoneTextView);
        textViewNight = itemView.findViewById(R.id.pharmacyRecyclerViewItemNightTextView);
    }
}

