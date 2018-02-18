package com.patos.carryme.model;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.Date;

public class Car {
	private String _id;
	private String _drivername;
	private double _s_latitude,_s_longitude;
	private double _d_latitude,_d_longitude;
	private Date _departure;
	private double _weight_capacity;
	private double _c_weight;
	private ArrayList<Packet> packetList = new ArrayList();
	public double s_distance,d_distance;
	
	
	public Car(String _id, String _drivername, double _s_latitude, double _s_longitude, double _d_latitude,
			double _d_longitude, Date _departure, double _weight_capacity) {
		super();
		this._id = _id;
		this._drivername = _drivername;
		this._s_latitude = _s_latitude;
		this._s_longitude = _s_longitude;
		this._d_latitude = _d_latitude;
		this._d_longitude = _d_longitude;
		this._departure = _departure;
		this._weight_capacity = _weight_capacity;
		this._c_weight = 0;
	}
	public Car(String _id) {
		this._id = _id;
	}
	public void addPacket(String id,double weight, double slatitude, double slongitude, double dlatitude, double dlongitude) {
		Packet p = new Packet();
		p.ID = id;
		p.weight = weight;
		p.arrival = new LatLng(dlatitude, dlongitude);
		p.deperature = new LatLng(slatitude, slongitude);

		packetList.add(p);

	}
	public String get_id() {
		return _id;
	}
	public void set_id(String _id) {
		this._id = _id;
	}
	public String get_drivername() {
		return _drivername;
	}
	public void set_drivername(String _drivername) {
		this._drivername = _drivername;
	}
	public double get_s_latitude() {
		return _s_latitude;
	}
	public void set_s_latitude(double _s_latitude) {
		this._s_latitude = _s_latitude;
	}
	public double get_s_longitude() {
		return _s_longitude;
	}
	public void set_s_longitude(double _s_longitude) {
		this._s_longitude = _s_longitude;
	}
	public double get_d_latitude() {
		return _d_latitude;
	}
	public void set_d_latitude(double _d_latitude) {
		this._d_latitude = _d_latitude;
	}
	public double get_d_longitude() {
		return _d_longitude;
	}
	public void set_d_longitude(double _d_longitude) {
		this._d_longitude = _d_longitude;
	}
	public Date get_departure() {
		return _departure;
	}
	public void set_departure(Date _departure) {
		this._departure = _departure;
	}
	public double get_weight_capacity() {
		return _weight_capacity;
	}
	public void set_weight_capacity(double _weight_capacity) {
		this._weight_capacity = _weight_capacity;
	}
	public double get_c_weight() {
		return _c_weight;
	}
	public void set_c_weight(double _c_weight) {
		this._c_weight = _c_weight;
	}
	public ArrayList<Packet> getPacketList() {
		return packetList;
	}
	public String toString(){
		  return "id: "+this._id +" , name: "+ this._drivername+" ,weig: "+this.get_weight_capacity()
				  
		  +" ,s_lati: "+this.get_s_latitude()
		  +" ,s_long: "+this.get_s_longitude() 
		  +" ,d_lati: "+this.get_d_latitude()  
		  +" ,d_long: "+this.get_d_longitude();
		  
		  
	}  
	
	
	
	
	
	
	
	
	
	

}
