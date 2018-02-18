package com.patos.carryme.view;


import android.content.Intent;
import android.graphics.Color;
import android.provider.Settings;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
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
import com.patos.carryme.httpclient.Backgroudjob;
import com.patos.carryme.httpclient.OnTaskCompleted;
import com.patos.carryme.model.Car;
import com.patos.carryme.model.Packet;
import com.patos.carryme.model.singletons.Data;

import org.json.JSONObject;

import java.util.ArrayList;

import okhttp3.HttpUrl;

public class PacketLocationPage extends FragmentActivity implements OnMapReadyCallback,OnTaskCompleted {

    private GoogleMap mMap;
    Car currentCar;
    String packetId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_packet_location_page);


        Intent intent= getIntent();
        packetId= intent.getStringExtra("packetID");
        loadDataFromServer();
        HttpUrl.Builder url = new HttpUrl.Builder()
                .scheme("https")
                .host("maps.googleapis.com")
                .addPathSegment("maps")
                .addPathSegment("api")
                .addPathSegment("directions")
                .addPathSegment("json")
                .addQueryParameter("origin", "41.00527,28.97696")
                .addQueryParameter("destination","38.41885,27.12872")
                .addQueryParameter("mode","driving")
                .addQueryParameter("sensor","false");
        //http://maps.googleapis.com/maps/api/directions/json?origin=41.00527,28.97696&destination=38.41885,27.12872&mode=driving&sensor=false;

        //url.addQueryParameter("key", "AIzaSyDUoH9wr838_lX_6w6KLt0s8RI2HUtIEa8");
        Log.i("log_tag", String.valueOf(url));


//https://maps.googleapis.com/maps/api/distancematrix/json?units=imperial&origins=41.00527,28.97696 | 39.92077,32.89411&destinations=38.41885,27.12872 | 37.91441,40.230629 &key=AIzaSyASw8IzAFpoeR-35Wr84LOj1cfYJHmgPW4

        currentCar = Data.carWillBeDisplayed;
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    void loadDataFromServer() {
        ArrayList<String> passing = new ArrayList<String>();
        passing.add("a");
        passing.add("b");
        passing.add("1");//these are url parameters

        Backgroudjob myjob = new Backgroudjob(this);
        myjob.execute(passing);//it will be url buildir object

    }

    @Override
    public void onTaskCompleted(JSONObject a) {

        Log.d("log_tag", "" + "\n==> After AstncTask");


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
