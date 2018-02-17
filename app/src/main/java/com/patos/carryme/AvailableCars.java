package com.patos.carryme;

import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.patos.carryme.objects.Car;

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
        availableCarsString.clear();
        for(Car car: availableCars){

            availableCarsString.add("Driver: "+car.get_drivername()+"\nDeparture Distance: "
                    +df.format(car.d_distance)+"\nArrival Distance: "+df.format(car.s_distance)+"\nAvailable weight: "
                    +(car.get_weight_capacity()-car.get_c_weight())
            );
        }

        ListView listView = (ListView) findViewById(R.id.availableCars);
        final ArrayAdapter<String> arrayAdapter=new ArrayAdapter<String>
                (this, android.R.layout.simple_list_item_1, android.R.id.text1,availableCarsString );
        listView.setAdapter(arrayAdapter);

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
                     if(availableCars.get(i).addPacket(1,kg))
                         Toast.makeText(AvailableCars.this, "Added your packet!",
                                 Toast.LENGTH_SHORT).show();
                     else {
                         Toast.makeText(AvailableCars.this, "Your packet can not be added!",
                                 Toast.LENGTH_SHORT).show();

                     }

                 }
             });
             no.setOnClickListener(new View.OnClickListener() {
                 @Override
                 public void onClick(View v) {
                     dialog.dismiss();
                 }
             });
             dialog.setMessage("Do you want to send your packet with this driver?");
             dialog.setCancelable(false);
             dialog.show();

         }
     });


    }

}
