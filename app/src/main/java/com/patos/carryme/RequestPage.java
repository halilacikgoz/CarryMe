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

import com.patos.carryme.objects.Car;
import com.patos.carryme.remote.Getter;
import com.patos.carryme.test.Preparer;

import java.io.Serializable;
import java.util.List;

public class RequestPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_page);
        Preparer.prepare();





        setListeners();


    }


    private void setListeners(){
        Button showOptions      = (Button)   findViewById(R.id.showOptions);
        showOptions.setOnClickListener(new View.OnClickListener() {



            @Override
            public void onClick(View view) {
                EditText departureXText = (EditText) findViewById(R.id.departure_x);
                EditText departureYText = (EditText) findViewById(R.id.departure_y);
                EditText arrivalXText = (EditText) findViewById(R.id.arrival_x);
                EditText arrivalYText = (EditText) findViewById(R.id.arrival_y);
                EditText packetKgText = (EditText) findViewById(R.id.packetKg);

                if (departureXText.getText().toString().equals("") ||
                        departureYText.getText().toString().equals("") ||
                        arrivalXText.getText().toString().equals("") ||
                        arrivalYText.getText().toString().equals("") ||
                        packetKgText.getText().toString().equals("")
                        ) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(RequestPage.this);
                    builder.setMessage("Enter the values !")
                            .setTitle("Missing Values")
                            .setPositiveButton(android.R.string.ok, null);
                    AlertDialog dialog = builder.create();
                    dialog.show();
                } else {

                double departureLatitude = Double.parseDouble(departureXText.getText().toString());
                double departureLongitude = Double.parseDouble(departureYText.getText().toString());
                double arrivalLatitude = Double.parseDouble(arrivalXText.getText().toString());
                double arrivalLongitude = Double.parseDouble(arrivalYText.getText().toString());
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
