package com.patos.carryme.test;

import com.patos.carryme.model.Car;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Generator {

	
	public static List<Car> generateCars(int count) {
		
		List<Car> cars = new ArrayList<>();

		for(int i=0;i<count;i++)
			cars.add(getCar("test"));

		return cars;
	}
	
	private static Car getCar(String id) {
		Car tmp=new Car(id);
		
		String[] names = { "Sakir","Kazim","Kubra", "Busra", "Helin", "Husniye", "Haydar", "Huseyin", "Ali" };
		double[] weights    =  {5,10,15,20,5,8,11,14,17,25,12,18};
		double[] latitudes  =  {41.00527,39.92077,38.41885};
		double[] longitudes =  {28.97696,32.89411,27.12872};
		int[] diffrences = {51,21,18};
		
		
		Random rand=new Random();
		
		
		int rn1 = rand.nextInt(latitudes.length);
		
		double _lati=latitudes[rn1];
		double _long=longitudes[rn1];
		
		int r_diff = rand.nextInt(diffrences[rn1]) * (rand .nextBoolean() ? -1 : 1);
	
		
		_lati= _lati + r_diff/100.0;
		 r_diff = rand.nextInt(diffrences[rn1]) * (rand .nextBoolean() ? -1 : 1);
		 _long= _long + r_diff/100.0;
				
		tmp.set_s_latitude(_lati);
		tmp.set_s_longitude(_long);
		

		
		int rn2 = rand.nextInt(latitudes.length);
		while (rn1==rn2) {
			rn2 = rand.nextInt(latitudes.length);
		}
		
		_lati=latitudes[rn2];
		_long=longitudes[rn2];
		
		 r_diff = rand.nextInt(diffrences[rn2]) * (rand .nextBoolean() ? -1 : 1);
		
		_lati= _lati + r_diff/100.0;
		 r_diff = rand.nextInt(diffrences[rn2]) * (rand .nextBoolean() ? -1 : 1);
		 _long= _long + r_diff/100.0;
				
		tmp.set_d_latitude(_lati);
		tmp.set_d_longitude(_long);

		tmp.set_drivername(names[rand.nextInt(names.length-1)]);
		tmp.set_weight_capacity(weights[rand.nextInt(weights.length-1)]);
		
		return tmp;
		
	}
}
