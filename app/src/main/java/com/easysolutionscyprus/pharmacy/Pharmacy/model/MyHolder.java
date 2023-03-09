package com.easysolutionscyprus.pharmacy.Pharmacy.model;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MyHolder extends RecyclerView.ViewHolder{
    protected final InfoLayoutAdapter infoLayoutAdapter;

    public MyHolder(@NonNull View itemView) {
        super(itemView);
        infoLayoutAdapter = new InfoLayoutAdapter.InfoLayoutAdapterBuilder(itemView)
                .build();
    }
}

