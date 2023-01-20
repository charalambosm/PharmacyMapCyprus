package com.easysolutionscyprus.pharmacy.Pharmacy.model;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.easysolutionscyprus.pharmacy.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;

public abstract class MyClusterRenderer extends DefaultClusterRenderer<Pharmacy>{
    Context context;
    Pharmacy currentPharmacy;

    public MyClusterRenderer(Context context, GoogleMap map, ClusterManager<Pharmacy> clusterManager) {
        super(context, map, clusterManager);
        this.context = context;
    }

    @Override
    protected void onBeforeClusterItemRendered(@NonNull Pharmacy item, @NonNull MarkerOptions markerOptions) {
        markerOptions.icon(bitmapDescriptorFromVector(R.drawable.ic_pharmacy_marker));
        markerOptions.title(item.getTitle());
        markerOptions.snippet(item.getSnippet());
    }

    @Override
    protected void onClusterItemRendered(@NonNull Pharmacy clusterItem, @NonNull Marker marker) {
        if (currentPharmacy != null && clusterItem.getId() == currentPharmacy.getId()) {
            selectNewMarker(clusterItem);
        } else if (clusterItem.isNight()) {
            marker.setIcon(bitmapDescriptorFromVector(R.drawable.ic_pharmacy_night_marker));
        }
    }

    public Pharmacy getCurrentPharmacy() {
        return currentPharmacy;
    }

    private BitmapDescriptor bitmapDescriptorFromVector(int vectorResId) {
        Drawable vectorDrawable = ContextCompat.getDrawable(context, vectorResId);
        assert vectorDrawable != null;
        vectorDrawable.setBounds(0, 0, vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight());
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }

    @Override
    protected boolean shouldRenderAsCluster(@NonNull Cluster<Pharmacy> cluster) {
        return cluster.getSize() > 6;
    }

    @Override
    protected void onClusterRendered(@NonNull Cluster<Pharmacy> cluster, @NonNull Marker marker) {
        super.onClusterRendered(cluster, marker);
        for (Pharmacy pharmacy : cluster.getItems()) {
            if (currentPharmacy != null && pharmacy.getId() == currentPharmacy.getId()) {
                unselectPharmacy();
                break;
            }
        }
    }

    protected void unselectPreviouslySelectedMarker() {
        if (getMarker(currentPharmacy) != null) {
            if (currentPharmacy.isNight()) {
                getMarker(currentPharmacy).setIcon(bitmapDescriptorFromVector(R.drawable.ic_pharmacy_night_marker));
            } else {
                getMarker(currentPharmacy).setIcon(bitmapDescriptorFromVector(R.drawable.ic_pharmacy_marker));
            }
            getMarker(currentPharmacy).hideInfoWindow();
        }
        currentPharmacy = null;
    }

    protected void selectNewMarker(Pharmacy pharmacy) {
        if (getMarker(pharmacy) != null) {
            if (pharmacy.isNight()) {
                getMarker(pharmacy).setIcon(bitmapDescriptorFromVector(R.drawable.ic_pharmacy_night_marker_highlighted));
            } else {
                getMarker(pharmacy).setIcon(bitmapDescriptorFromVector(R.drawable.ic_pharmacy_marker_highlighted));
            }
            getMarker(pharmacy).showInfoWindow();
        }
        currentPharmacy = pharmacy;
    }

    public abstract void selectPharmacy(Pharmacy pharmacy);

    public abstract void unselectPharmacy();
}
