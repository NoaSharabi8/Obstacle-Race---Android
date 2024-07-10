package com.example.mygame.Fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.mygame.R;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;

public class MapFragment extends Fragment implements OnMapReadyCallback{
    SupportMapFragment mapFragment;
    private GoogleMap googleMap;

    public MapFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_map, container, false);
        findViews();
        initViews();
        return view;
    }

    private void initViews() {
        try {
            mapFragment.getMapAsync(this);
        } catch (Exception e) {
            Log.d("Exception: ", e.toString());
        }
    }

    private void findViews() {
        mapFragment = ((SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.fragment_map));
    }
    public void zoomOnUser(double x, double y) {
          if (googleMap != null) {
            LatLng location = new LatLng(x, y);
            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(location, 18f);
            googleMap.animateCamera(cameraUpdate);
        } else {
            Log.d("MapFragment:", "googleMap is null");
        }
    }
    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        this.googleMap = googleMap;
        this.googleMap.clear();
    }
}