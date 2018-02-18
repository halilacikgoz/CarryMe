package com.patos.carryme;


import android.support.v4.app.FragmentActivity;
import android.os.Bundle;


import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.patos.carryme.objects.Car;
import com.patos.carryme.test.Data;

public class PacketLocationPage extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    Car currentCar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_packet_location_page);


        currentCar = Data.carWillBeDisplayed;
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;



        // Add a marker in Sydney and move the camera
        LatLng packetLocation = new LatLng(currentCar.get_s_latitude(),currentCar.get_s_longitude() );
        MarkerOptions marker = new MarkerOptions().position(packetLocation).icon(BitmapDescriptorFactory.
                fromResource(R.drawable.ferrari)).title(currentCar.get_drivername());
        mMap.addMarker(marker);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(packetLocation,10));
    }

//    public class MapsMarkerActivity extends AppCompatActivity
//            implements OnMapReadyCallback {
//        // Include the OnCreate() method here too, as described above.
//        @Override
//        public void onMapReady(GoogleMap googleMap) {
//            // Add a marker in Sydney, Australia,
//            // and move the map's camera to the same location.
//            LatLng packetLocation = new LatLng(currentPacket.car.get_s_latitude(),currentPacket.car.get_s_longitude() );
//            googleMap.addMarker(new MarkerOptions().position(packetLocation)
//                    .title("Current Packet"));
//            googleMap.moveCamera(CameraUpdateFactory.newLatLng(packetLocation));
//        }
//    }
}
