package com.patos.carryme.view;


import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.patos.carryme.R;
import com.patos.carryme.model.Car;
import com.patos.carryme.model.Packet;
import com.patos.carryme.model.singletons.Data;

public class PacketLocationPage extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    Car currentCar;
    String packetId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_packet_location_page);


        Intent intent= getIntent();
        packetId= intent.getStringExtra("packetID");

        currentCar = Data.carWillBeDisplayed;
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }



    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;


        LatLng carDeparture = new LatLng(currentCar.get_s_latitude(),currentCar.get_s_longitude() );
        LatLng carDestination = new LatLng(currentCar.get_d_latitude(),currentCar.get_d_longitude());

        Packet currentPacket = null;
        Log.v("PATOS_LOG",""+RequestPage.userID);
        for(Packet p: currentCar.getPacketList()){

            Log.v("PATOS_LOG",""+p.ID);

            if(RequestPage.userID.equals(p.ID)){
                currentPacket=p;
                break;
            }

        }

        LatLng packetDeparture = currentPacket.deperature;
        LatLng packetArrival = currentPacket.arrival;

        MarkerOptions packetMarker = new MarkerOptions().position(packetDeparture).icon(BitmapDescriptorFactory.
                fromResource(R.drawable.box)).title(currentPacket.weight+"");

        MarkerOptions carMarker = new MarkerOptions().position(carDeparture).icon(BitmapDescriptorFactory.
                fromResource(R.drawable.ferrari)).title(currentCar.get_drivername());

        MarkerOptions carArrivalMarker = new MarkerOptions().position(carDestination).icon(BitmapDescriptorFactory.
                fromResource(R.drawable.flag));

        MarkerOptions packetArrivalMarker = new MarkerOptions().position(packetArrival).icon(BitmapDescriptorFactory.
                fromResource(R.drawable.down));

        mMap.addMarker(carMarker);
        mMap.addMarker(packetMarker);
        mMap.addMarker(carArrivalMarker);
        mMap.addMarker(packetArrivalMarker);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(packetDeparture,10));

        //        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(carDeparture,10));

        Polyline route = mMap.addPolyline(new PolylineOptions().add(new LatLng(currentCar.get_s_latitude(),currentCar.get_s_longitude()),
                carDestination).width(9).color(Color.GREEN));

        Polyline packetToCar = mMap.addPolyline(new PolylineOptions().add(packetDeparture,
                carDeparture).width(9).color(Color.RED));

        Polyline carToPacketArrival = mMap.addPolyline(new PolylineOptions().add(carDestination,
                packetArrival).width(9).color(Color.RED));





    }

}
