package com.patos.carryme;

import android.app.DatePickerDialog;
import android.content.Intent;
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
import com.patos.carryme.datePicker.DatePickerr;
import com.patos.carryme.objects.Car;
import com.patos.carryme.remote.Getter;
import com.patos.carryme.test.Preparer;

import java.io.Serializable;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

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
                    builder.setMessage("Please Enter Start Date of Range !")
                            .setTitle("Missing Values")
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
                builder.setMessage("Başlangıç tarihiyle uyumlu bir tarih gir !")
                        .setTitle("Missing Values")
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

                if (departureLocation.getText().toString().equals("Give an Address") ||
                        arrivalLocation.getText().toString().equals("Give an Address") ||
                        packetKgText.getText().toString().equals("") ||
                        startDateString.equals("") ||
                        endDateString.equals("")) {

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
