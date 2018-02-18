package com.patos.carryme.controller;

import android.util.Log;

import com.patos.carryme.model.Car;
import com.patos.carryme.model.singletons.Calculator;
import com.patos.carryme.test.Data;
import com.patos.carryme.test.Preparer;
import com.patos.carryme.test.Properties;

import java.util.*;

public class Getter {


    /**
     * @param deperatureLatitude X location of the packet
     * @param deperatureLongitude Y locaton of the packet
     * @param arrivalLatitude X location of the destination
     * @param arrivalLongitude Y location of the destination
     * @param kg Weight of the packet
     * @return available cars which matches packet properties
     */
    static public List<Car> getAvailableCars(
            double deperatureLatitude, double deperatureLongitude,
            double arrivalLatitude, double arrivalLongitude,
            double kg){
        List<Car> availableCars = new ArrayList<>();

        Dictionary<String, Car> carsDictionary = new Hashtable<>();
        Map<Double, String> totalDistances = new TreeMap<>();

        for(Car car : Data.allCars){
            carsDictionary.put(car.get_id(), car);
            double deperatureDistance = Calculator.calculateDistance(car.get_s_latitude(), deperatureLatitude, car.get_s_longitude(), deperatureLongitude);
            double arrivalDistance = Calculator.calculateDistance(car.get_d_latitude(), arrivalLatitude, car.get_d_longitude(), arrivalLongitude);
            car.d_distance = arrivalDistance;
            car.s_distance = deperatureDistance;

            totalDistances.put(deperatureDistance + arrivalDistance, car.get_id());
        }
        Log.v("PATOS2",totalDistances.size()+"");

        for(Map.Entry<Double, String> carDetails : totalDistances.entrySet()){
            if(carDetails.getKey() > Properties.maxDistance)
                break;
            Car car = carsDictionary.get(carDetails.getValue());
            if(car.get_weight_capacity() - car.get_c_weight() >= kg)
                availableCars.add(car);
        }

        return availableCars;
    }

    public static List<Car> getAvailableCars(Date startDate, Date endDate, double kg,
                                             double deperatureLong, double deperatureLat,
                                             double arrivalLong, double arrivalLat){
        Server.getAvailableCars(startDate, endDate, kg,deperatureLong, deperatureLat,arrivalLong,arrivalLat);
        return new ArrayList<>();
    }


}
