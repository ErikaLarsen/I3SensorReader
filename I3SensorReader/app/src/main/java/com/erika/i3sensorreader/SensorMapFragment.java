package com.erika.i3sensorreader;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * Created by erikalarsen on 1/12/18.
 */

public class SensorMapFragment extends Fragment implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener{
    private static final String TAG = "SensorMapFragment";

    private GoogleMap mMap;
    private MapView mMapView;
    private View mView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.activity_maps,container,false);


        return mView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mMapView = (MapView) mView.findViewById(R.id.map);
        if(mMapView!=null){
            mMapView.onCreate(null);
            mMapView.onResume();
            mMapView.getMapAsync(this);
        }


    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        Log.d("MapsActivity", "clicked on marker");
        ((MainActivity)getActivity()).startNextActivity(marker.getTitle());
//        if(marker.getTag().equals("Sensor 1")){
//            startNextActivity(marker.getTitle());
//        }
        return true;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        MapsInitializer.initialize(getContext());

        MapStyleOptions style =  new MapStyleOptions("[" +
                "  {" +
                "    \"featureType\":\"poi\"," +
                "    \"elementType\":\"all\"," +
                "    \"stylers\":[" +
                "      {" +
                "        \"visibility\":\"off\"" +
                "      }" +
                "    ]" +
                "  }," +
                "  {" +
                "    \"featureType\":\"transit\"," +
                "    \"elementType\":\"all\"," +
                "    \"stylers\":[" +
                "      {" +
                "        \"visibility\":\"off\"" +
                "      }" +
                "    ]" +
                "  }" +
                "]");



        mMap = googleMap;

        mMap.setMapStyle(style);

        String title = "Sensor ";

        mMap.addMarker(new MarkerOptions().position(new LatLng(34.02061334338637,-118.28923400241484)).title(title+1)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.sensor1))).hideInfoWindow();
        mMap.addMarker(new MarkerOptions().position(new LatLng(34.02022429784283,-118.28885178763022)).title(title+2)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.sensor2))).hideInfoWindow();
        mMap.addMarker(new MarkerOptions().position(new LatLng(34.01994863020688,-118.28862514096846)).title(title+3)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.sensor3))).hideInfoWindow();
        mMap.addMarker(new MarkerOptions().position(new LatLng(34.01949733367669,-118.28982677060714)).title(title+4)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.sensor4))).hideInfoWindow();

        mMap.setOnMarkerClickListener(this);

    }
}
