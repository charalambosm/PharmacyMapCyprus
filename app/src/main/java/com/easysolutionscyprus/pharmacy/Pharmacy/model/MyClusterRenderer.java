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
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;

public class MyClusterRenderer extends DefaultClusterRenderer<Pharmacy>{
    Context context;
    Marker selectedMarker;
    Pharmacy selectedPharmacy;

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
        if (selectedPharmacy != null && clusterItem.getId() == selectedPharmacy.getId()) {
            this.selectedMarker = marker;
            marker.setIcon(bitmapDescriptorFromVector(R.drawable.ic_pharmacy_marker_highlighted));
        }
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

    public void setSelectedPharmacy(Pharmacy selectedPharmacy) {
        this.selectedPharmacy = selectedPharmacy;
    }

    public void setSelectedMarker(Pharmacy item) {
        unHighlightPreviouslySelectedMarker();
        this.selectedPharmacy = item;
        this.selectedMarker =  getMarker(item);
        highlightSelectedMarker();
    }

    private void unHighlightPreviouslySelectedMarker() {
        if(selectedMarker!=null) {
            selectedMarker.setIcon(bitmapDescriptorFromVector(R.drawable.ic_pharmacy_marker));
        }
    }

    private void highlightSelectedMarker() {
        if(selectedMarker!=null) {
            selectedMarker.setIcon(bitmapDescriptorFromVector(R.drawable.ic_pharmacy_marker_highlighted));
        }
    }
}
