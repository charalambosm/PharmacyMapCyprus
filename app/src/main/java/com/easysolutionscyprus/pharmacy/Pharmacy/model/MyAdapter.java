package com.easysolutionscyprus.pharmacy.Pharmacy.model;

import android.telephony.PhoneNumberUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.easysolutionscyprus.pharmacy.Preferences.model.Favorites;
import com.easysolutionscyprus.pharmacy.R;

import java.util.List;
import java.util.Locale;

public abstract class MyAdapter extends RecyclerView.Adapter<MyHolder>{
    private List<Pharmacy> pharmacyList;
    private List<Pharmacy> fullPharmacyList;
    private Favorites favorites;

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
        if (!model.isNight()) {
            holder.textViewNight.setVisibility(View.GONE);
        } else {
            holder.textViewNight.setVisibility(View.VISIBLE);
        }
        if (model.getDistance() == 0) {
            holder.textViewDistance.setVisibility(View.GONE);
        } else {
            holder.textViewDistance.setText(String.format(Locale.getDefault(), "%3.1f km",model.getDistance()));
        }
        if (model.getHomePhone() == 0) {
            holder.textViewPhonePharmacy.setText(PhoneNumberUtils.formatNumber("+357"+model.getPhone(), "CY"));
            holder.textViewPhoneHome.setVisibility(View.GONE);
        } else {
            holder.textViewPhoneHome.setVisibility(View.VISIBLE);
            holder.textViewPhonePharmacy.setText(PhoneNumberUtils.formatNumber("+357"+model.getPhone(), "CY"));
            holder.textViewPhoneHome.setText(PhoneNumberUtils.formatNumber("+357"+model.getHomePhone(), "CY"));
        }
        holder.moreButton.setOnClickListener(view -> cardViewShowMoreCallback(model, position));
        holder.favoritesButton.setOnClickListener(view -> cardViewBookmarksCallback(model, position));
        holder.directionButton.setOnClickListener(view -> cardViewDirectionCallback(model));
        if (favorites.isFavorite(model.getId())) {
            holder.favoritesButton.setImageResource(R.drawable.ic_favorite);
        } else {
            holder.favoritesButton.setImageResource(R.drawable.ic_favorite_add);
        }
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

    public void setFullPharmacyList(List<Pharmacy> pharmacyList) {
        this.fullPharmacyList = pharmacyList;
        this.pharmacyList = pharmacyList;
    }

    public void setFavorites(Favorites favorites) {
        this.favorites = favorites;
    }

    public abstract void cardViewShowMoreCallback(Pharmacy pharmacy, int position);

    public abstract void cardViewBookmarksCallback(Pharmacy pharmacy, int position);

    public abstract void cardViewDirectionCallback(Pharmacy pharmacy);
}