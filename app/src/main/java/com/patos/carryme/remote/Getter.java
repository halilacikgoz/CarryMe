package com.patos.carryme.remote;

import android.app.Activity;
import android.util.Log;

import com.patos.carryme.objects.Car;
import com.patos.carryme.objects.singletons.Calculator;
import com.patos.carryme.test.Data;
import com.patos.carryme.test.Preparer;
import com.patos.carryme.test.Properties;

import java.util.*;

public class Getter {


    /**
     * @param kg Weight of the packet
     * @return available cars which matches packet properties
     */
    public static List<Car> getAvailableCars(Date startDate, Date endDate, double kg,
                                             double deperatureLong, double deperatureLat,
                                             double arrivalLong, double arrivalLat){
        Server.getAvailableCars(startDate, endDate, kg,deperatureLong, deperatureLat,arrivalLong,arrivalLat);
        return new ArrayList<>();
    }

    public static void getPackets(Activity activity, String userID){
        Server.getPackets(activity, userID);
    }

    public static void getCar(Activity activity, String carID){
        Server.getCar(activity, carID);
    }



}
