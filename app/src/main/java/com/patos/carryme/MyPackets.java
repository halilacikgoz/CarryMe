package com.patos.carryme;

/**
 * Created by Halil on 17.02.2018.
 */

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.patos.carryme.objects.Car;
import com.patos.carryme.objects.Packet;
import com.patos.carryme.remote.Getter;
import com.patos.carryme.test.Data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class MyPackets extends AppCompatActivity {

    public static List<String> myPacketsString=new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_packets);

        myPacketsString.clear();
        //myPacketsString.clear();
        for(Packet p: Data.allPackets){
            myPacketsString.add("Packet Weight: "+p.weight+
                    "\nPacket ID: "+ p.ID+
                    "\nCar ID: "+p.car.get_id());
        }

        final Activity currentActivity = this;
        ListView listView = (ListView) findViewById(R.id.myPackets);
        final ArrayAdapter<String> arrayAdapter=new ArrayAdapter<String>
                (this, android.R.layout.simple_list_item_1, android.R.id.text1,myPacketsString );
        listView.setAdapter(arrayAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, final int i, long l) {

                Getter.getCar(currentActivity, Data.allPackets.get(i).car.get_id());
            }
        });
    }
}
