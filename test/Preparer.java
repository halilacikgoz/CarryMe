package com.patos.carryme.test;

public class Preparer {

    /**
     * Creates some cars randomly to use as test
     */
    public static void prepare(){
        Data.allCars = Generator.generateCars(Properties.carCount);
    }

}
