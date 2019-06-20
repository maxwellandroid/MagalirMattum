package com.maxwell.mahalirmattum;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.TextView;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class ContactUsFragment extends Fragment implements OnMapReadyCallback {

    View view;
    private GoogleMap mMap;
    MapView mapView;
    TextView text_email,text_phone_number;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.layout_contact_us_content, container, false);

        text_email=(TextView)view.findViewById(R.id.text_email);
        text_phone_number=(TextView)view.findViewById(R.id.text_phone_number);
       /* mapView = (MapView) view.findViewById(R.id.maps);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);*/

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
/*
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.maps);
        mapFragment.onCreate(savedInstanceState);
        mapFragment.getMapAsync(this);*/

        // Updates the location and zoom of the MapView
       /* CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(new LatLng(43.1, -87.9), 10);
        mMap.animateCamera(cameraUpdate);*/

        text_email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

             /*   Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
                emailIntent.setData(Uri.parse("mailto:info@innerconnection.in"));*/

                try{
                    Intent intent = new Intent (Intent.ACTION_VIEW , Uri.parse("mailto:vipratelevision@gmail.com"));
                    startActivity(intent);
                }catch(ActivityNotFoundException e){
                    //TODO smth
                }
            }
        });

        text_phone_number.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try{
                    Intent intent = new Intent (Intent.ACTION_DIAL , Uri.parse("tel:044 2435 0088"));
                    startActivity(intent);
                }catch(ActivityNotFoundException e){
                    //TODO smth
                }

            }
        });

        return view;
    }

    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        LatLng latLng = new LatLng(13.036210, 80.240770);
        mMap.addMarker(new MarkerOptions().position(latLng).title("13/4 Vaidhyaraman Street," +
                "T Nagar," +
                "Chennai 600017")).showInfoWindow();
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,15.0f));

    }
}
