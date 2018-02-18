package com.patos.carryme.model.singletons;

import com.patos.carryme.model.Car;
import com.patos.carryme.model.Packet;

import java.util.ArrayList;
import java.util.List;

public class Data {
    public static List<Car> allCars = new ArrayList<>();
    public static List<Packet> allPackets = new ArrayList<>();

    public static Car carWillBeDisplayed;
}
