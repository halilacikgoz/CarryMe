package com.patos.carryme.view;

import android.app.Activity;
import android.content.Intent;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.patos.carryme.R;
import com.patos.carryme.model.Car;
import com.patos.carryme.model.singletons.Calculator;
import com.patos.carryme.controller.Setter;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class AvailableCars extends AppCompatActivity {

    public static List<Car> availableCars=null;
    public static List<String> availableCarsString=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_available_cars);
        DecimalFormat df = new DecimalFormat(".###");

        Intent newData = getIntent();
        final double kg = newData.getDoubleExtra("kg",0);
        final double slatitude = newData.getDoubleExtra("slatitude",0);
        final double slongitude = newData.getDoubleExtra("slongitude",0);
        final double dlatitude = newData.getDoubleExtra("dlatitude",0);
        final double dlongitude = newData.getDoubleExtra("dlongitude",0);

        availableCarsString.clear();
        for(Car car: availableCars){

            availableCarsString.add(getResources().getString(R.string.driver)+car.get_drivername()+"\n"+
                    getResources().getString(R.string.departure_distance) +df.format(car.d_distance)+"\n"+
                    getResources().getString(R.string.arrival_distance)+ df.format(car.s_distance)+"\n"+
                    getResources().getString(R.string.price)+ new DecimalFormat(".##").format(calculatePrice(car))+"₺\n"+
                    getResources().getString(R.string.available_weight) +(car.get_weight_capacity()-car.get_c_weight())
            );
        }

        ListView listView = (ListView) findViewById(R.id.availableCars);
        final ArrayAdapter<String> arrayAdapter=new ArrayAdapter<String>
                (this, android.R.layout.simple_list_item_1, android.R.id.text1,availableCarsString );
        listView.setAdapter(arrayAdapter);

        final Activity currentActivity = this;
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
         @Override
         public void onItemClick(AdapterView<?> adapterView, View view, final int i, long l) {

             AlertDialog.Builder mBuilder = new AlertDialog.Builder(AvailableCars.this);
             View mView = getLayoutInflater().inflate(R.layout.yesno, null);
             Button yes = (Button) mView.findViewById(R.id.yes);
             Button no = (Button) mView.findViewById(R.id.no);

             mBuilder.setView(mView);
             final AlertDialog dialog = mBuilder.create();



             yes.setOnClickListener(new View.OnClickListener() {
                 @Override
                 public void onClick(View v) {
                     finish();
                     Setter.addPacketToCar(currentActivity, availableCars.get(i).get_id(),
                             Settings.Secure.getString(currentActivity.getContentResolver(),
                                     Settings.Secure.ANDROID_ID),
                                        kg, slatitude, slongitude, dlatitude, dlongitude);

                 }
             });
             no.setOnClickListener(new View.OnClickListener() {
                 @Override
                 public void onClick(View v) {
                     dialog.dismiss();
                 }
             });
             dialog.setMessage(getResources().getString(R.string.are_you_sure));
             dialog.setCancelable(false);
             dialog.show();

         }
     });


    }

    private double calculatePrice(Car c){
        return ((c.s_distance + c.d_distance)) / 3 ;
    }

}
