package com.charalambos.pharmaciescy.Pharmacy.internal;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.charalambos.pharmaciescy.R;

import java.util.List;
import java.util.Locale;

public abstract class MyAdapter extends RecyclerView.Adapter<MyHolder>{
    private List<Pharmacy> pharmacyList;
    private List<Pharmacy> fullPharmacyList;

    public MyAdapter() {
        super();
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.pharmacy_recycler_view_item, parent, false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
        Pharmacy model = pharmacyList.get(position);
        holder.textViewName.setText(String.format("%s %s", model.getFirstName(), model.getLastName()));
        holder.textViewAddress.setText(model.getAddress());
        holder.textViewPhone.setText(String.format(Locale.getDefault(), "%d",model.getPhone()));
        if (!model.isNight()) {
            holder.textViewNight.setVisibility(View.GONE);
        } else {
            holder.textViewNight.setVisibility(View.VISIBLE);
        }
        holder.textViewDistance.setText(String.format(Locale.getDefault(), "%3.2f km",model.getDistance()));
        holder.itemView.setOnClickListener(view -> cardViewOnClickListener(model));
    }

    @Override
    public int getItemCount() {
        if (pharmacyList == null) {
            return 0;
        } else {
            return pharmacyList.size();
        }
    }

    public void searchPharmacyList(List<Pharmacy> matchedPharmacyList) {
        pharmacyList = matchedPharmacyList;
    }

    public List<Pharmacy> getFullPharmacyList() {
        return fullPharmacyList;
    }

    public void setFullPharmacyList(List<Pharmacy> pharmacyList) {
        this.fullPharmacyList = pharmacyList;
        this.pharmacyList = pharmacyList;
    }

    public abstract void cardViewOnClickListener(Pharmacy pharmacy);
}