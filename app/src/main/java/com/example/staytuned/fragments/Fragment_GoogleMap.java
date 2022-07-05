package com.example.staytuned.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.staytuned.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;


public class Fragment_GoogleMap extends Fragment implements OnMapReadyCallback {

    private GoogleMap mMap;
    private AppCompatActivity appCompatActivity;

    public Fragment_GoogleMap(AppCompatActivity appCompatActivity) {
        this.appCompatActivity = appCompatActivity;
    }
    public Fragment_GoogleMap(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_google_map, container, false);
        findViews(view);
        SupportMapFragment supportMapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        supportMapFragment.getMapAsync(this);
        return view;
    }

    private void findViews(View view) {
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;


    }

    public void setMapLocation(double lat, double lng) {
        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(lat, lng);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Place"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }
}