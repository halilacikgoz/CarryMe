package com.patos.carryme.view;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.patos.carryme.R;
import com.patos.carryme.datePicker.DatePickerr;
import com.patos.carryme.controller.Getter;
import com.patos.carryme.controller.Server;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class RequestPage extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    boolean isDeparture;
    double departureLatitude;
    double departureLongitude;
    double arrivalLatitude ;
    double arrivalLongitude;
    int PLACE_PICKER_REQUEST = 1;
    TextView departureLocation ;
    TextView arrivalLocation;
    String startDateString;
    String endDateString;
    Date tempDate=null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_page);
        Server.init(this);
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

        final Activity currentActivity= this;
        Button myPackets = (Button) findViewById(R.id.myPackets);
        myPackets.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Getter.getPackets(currentActivity,  Settings.Secure.getString(currentActivity.getContentResolver(),
                        Settings.Secure.ANDROID_ID));
            }
        });

        final DatePickerr datePickerr= new DatePickerr(this);
        Button startDate = (Button) findViewById(R.id.startDate);
        startDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                datePickerr.isStart=true;
                datePickerr.get("Start Date Picker");

            }
        });

        Button endDate= (Button) findViewById(R.id.endDate);
        endDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(tempDate==null){
                    AlertDialog.Builder builder = new AlertDialog.Builder(RequestPage.this);
                    builder.setMessage(getResources().getString(R.string.requestPage_Date1_required))
                            .setTitle(getResources().getString(R.string.missing_values))
                            .setPositiveButton(android.R.string.ok, null);
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }else{

                datePickerr.isStart=false;
                datePickerr.get("End Date Picker");
                }
            }
        });



    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {


        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH,month);
        calendar.set(Calendar.DAY_OF_MONTH,dayOfMonth);
        String date = DateFormat.getDateInstance(DateFormat.FULL).format(calendar.getTime());

        Log.v("FullDate", date);
        if(DatePickerr.isStart) {
            tempDate = calendar.getTime();
            TextView startDateText = (TextView) findViewById(R.id.startDateText);

            startDateString = year+"-"+(month+1)+"-"+dayOfMonth;
            startDateText.setText(date);

        }
        else {

            if(tempDate.before(calendar.getTime())){
                TextView endDateText = (TextView) findViewById(R.id.endDateText);

                endDateString = year+"-"+(month+1)+"-"+dayOfMonth;
                endDateText.setText(date);
            } else {
                AlertDialog.Builder builder = new AlertDialog.Builder(RequestPage.this);
                builder.setMessage(getResources().getString(R.string.endDate_compatible))
                        .setTitle(getResources().getString(R.string.missing_values))
                        .setPositiveButton(android.R.string.ok, null);
                AlertDialog dialog = builder.create();
                dialog.show();
            }

        }

    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {


        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                Place place = PlacePicker.getPlace(data, RequestPage.this);
                if(isDeparture){
                    String toastMsg = String.format(getResources().getString(R.string.requestPage_departure)+"%s", place.getAddress());

                    departureLocation.setText(place.getAddress().toString());
                    departureLatitude=place.getLatLng().latitude;
                    departureLongitude=place.getLatLng().longitude;

                    Log.v("Latitude",place.getLatLng().latitude+"");
                    Toast.makeText(this, toastMsg, Toast.LENGTH_LONG).show();

                } else {
                    String toastMsg = String.format(getResources().getString(R.string.requestPage_arrival)+"%s", place.getAddress());

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

                if (departureLocation.getText().toString().equals(getResources().getString(R.string.give_an_address)) ||
                        arrivalLocation.getText().toString().equals(getResources().getString(R.string.give_an_address)) ||
                        packetKgText.getText().toString().equals("") ||
                        startDateString.equals("") ||
                        endDateString.equals("")) {

                    AlertDialog.Builder builder = new AlertDialog.Builder(RequestPage.this);
                    builder.setMessage(getResources().getString(R.string.enter_the_values))
                            .setTitle(getResources().getString(R.string.missing_values))
                            .setPositiveButton(getResources().getString(android.R.string.ok), null);
                    AlertDialog dialog = builder.create();
                    dialog.show();
                } else {
                double packetKg = Double.parseDouble(packetKgText.getText().toString());

                    try {
                        Getter.getAvailableCars(
                                new SimpleDateFormat("yyyy-MM-dd").parse(startDateString),
                                new SimpleDateFormat("yyyy-MM-dd").parse(endDateString),
                                packetKg,
                                departureLongitude,
                                departureLatitude,
                                arrivalLongitude,
                                arrivalLatitude);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

//                List<Car> availableCars = Getter.getAvailableCars(departureLatitude, departureLongitude, arrivalLatitude, arrivalLongitude, packetKg);
//
//                AvailableCars.availableCars = availableCars;
//                Log.v("PATOS", availableCars.size() + "");
//                Intent availableCarPage = new Intent(RequestPage.this, AvailableCars.class);
//                availableCarPage.putExtra("kg", packetKg);
//                startActivity(availableCarPage);
            }
            }
        });

    }
}
