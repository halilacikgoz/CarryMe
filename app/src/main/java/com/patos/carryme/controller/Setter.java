package com.patos.carryme.controller;

import android.app.Activity;

/**
 * Created by Ahmet on 17.02.2018.
 */

public class Setter {
    public static void addPacketToCar(Activity activity, String carID, String userID, double packageKG){
        Server.addPacketToCar(activity,carID,userID,packageKG);
    }
}
