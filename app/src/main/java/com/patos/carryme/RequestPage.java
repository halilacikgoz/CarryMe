package com.patos.carryme;

import android.content.Intent;
import android.os.Parcelable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.patos.carryme.objects.Car;
import com.patos.carryme.remote.Getter;
import com.patos.carryme.test.Preparer;

import java.io.Serializable;
import java.util.List;

public class RequestPage extends AppCompatActivity {

    boolean isDeparture;
    double departureLatitude;
    double departureLongitude;
    double arrivalLatitude ;
    double arrivalLongitude;
    int PLACE_PICKER_REQUEST = 1;
    TextView departureLocation ;
    TextView arrivalLocation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_page);
        Preparer.prepare();
        departureLocation = (TextView) findViewById(R.id.departureLocation);
        arrivalLocation = (TextView) findViewById(R.id.arrivalLocation);

        setListeners();

        Button departure = (Button) findViewById(R.id.departure);
        departure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
                Intent intent;
                isDeparture=true;
                try{
                    intent = builder.build(RequestPage.this);
                    startActivityForResult(intent, PLACE_PICKER_REQUEST);
                } catch (GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                } catch (GooglePlayServicesRepairableException e) {
                    e.printStackTrace();
                }



            }
        });

        Button arrival = (Button) findViewById(R.id.arrival);
        arrival.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
                Intent intent;
                isDeparture=false;
                try{
                    intent = builder.build(RequestPage.this);
                    startActivityForResult(intent, PLACE_PICKER_REQUEST);
                } catch (GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                } catch (GooglePlayServicesRepairableException e) {
                    e.printStackTrace();
                }



            }
        });




    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {


        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                Place place = PlacePicker.getPlace(data, RequestPage.this);
                if(isDeparture){
                    String toastMsg = String.format("Departure: %s", place.getAddress());

                    departureLocation.setText(place.getAddress().toString());
                    departureLatitude=place.getLatLng().latitude;
                    departureLongitude=place.getLatLng().longitude;

                    Log.v("Latitude",place.getLatLng().latitude+"");
                    Toast.makeText(this, toastMsg, Toast.LENGTH_LONG).show();

                } else {
                    String toastMsg = String.format("Arrival: %s", place.getAddress());

                    arrivalLocation.setText(place.getAddress().toString());
                    arrivalLatitude = place.getLatLng().latitude;
                    arrivalLongitude = place.getLatLng().longitude;

                    Log.v("Latitude", place.getLatLng().latitude + "");
                    Toast.makeText(this, toastMsg, Toast.LENGTH_LONG).show();
                }
            }
        }
    }


    private void setListeners(){
        Button showOptions      = (Button)   findViewById(R.id.showOptions);
        showOptions.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                EditText packetKgText = (EditText) findViewById(R.id.packetKg);

                if (departureLocation.toString().equals("") || arrivalLocation.toString().equals("")) {

                    AlertDialog.Builder builder = new AlertDialog.Builder(RequestPage.this);
                    builder.setMessage("Enter the values !")
                            .setTitle("Missing Values")
                            .setPositiveButton(android.R.string.ok, null);
                    AlertDialog dialog = builder.create();
                    dialog.show();
                } else {
                double packetKg = Double.parseDouble(packetKgText.getText().toString());

                List<Car> availableCars = Getter.getAvailableCars(departureLatitude, departureLongitude, arrivalLatitude, arrivalLongitude, packetKg);

                AvailableCars.availableCars = availableCars;
                Log.v("PATOS", availableCars.size() + "");
                Intent availableCarPage = new Intent(RequestPage.this, AvailableCars.class);
                availableCarPage.putExtra("kg", packetKg);
                startActivity(availableCarPage);
            }
            }
        });

    }
}
