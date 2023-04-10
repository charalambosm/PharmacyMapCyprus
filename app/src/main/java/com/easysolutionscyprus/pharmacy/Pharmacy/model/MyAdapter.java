package com.easysolutionscyprus.pharmacy.Pharmacy.model;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.easysolutionscyprus.pharmacy.R;

import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyHolder>{
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
        holder.infoLayoutAdapter.setPharmacy(model);
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

    public void changeItemAtPosition(int position) {
        notifyItemChanged(position);
    }

    public void removeItemFromPosition(int position) {
        pharmacyList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, pharmacyList.size());
    }

    public List<Pharmacy> getFullPharmacyList() {
        return fullPharmacyList;
    }

    public void setPharmacyList(List<Pharmacy> pharmacyList) {
        this.fullPharmacyList = pharmacyList;
        this.pharmacyList = pharmacyList;
    }
}