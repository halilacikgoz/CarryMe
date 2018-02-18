package com.patos.carryme.controller;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.model.LatLng;
import com.patos.carryme.view.AvailableCars;
import com.patos.carryme.view.MyPackets;
import com.patos.carryme.view.PacketLocationPage;
import com.patos.carryme.model.Car;
import com.patos.carryme.model.Packet;
import com.patos.carryme.model.singletons.Calculator;
import com.patos.carryme.model.singletons.Data;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.TreeMap;

public class Server {

    private static Context _context = null;

    public static void init(Context context){
        _context = context;
    }

    static void getAvailableCars(Date startDate, Date endDate,
                                      double kg, double deperatureLon, double deperatureLat,
                                      double arrivalLon, double arrivalLat){
        getCars(startDate, endDate, kg, deperatureLat, deperatureLon,arrivalLat,arrivalLon);
    }

    static void getPackets(final Activity activity, final String userID){
        RequestQueue queue = Volley.newRequestQueue(_context);
        String url = "https://protected-dusk-58376.herokuapp.com/getorders?id=" + userID;
        StringRequest postRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        try {
                            parsePacketData(activity, response);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        error.printStackTrace();
                    }
                }
        ) {

            @Override
            public String getBodyContentType() {
                return "application/x-www-form-urlencoded";
            }
        };
        postRequest.setRetryPolicy(new RetryPolicy() {
            @Override
            public int getCurrentTimeout() {
                return 50000;
            }

            @Override
            public int getCurrentRetryCount() {
                return 50000;
            }

            @Override
            public void retry(VolleyError error) throws VolleyError {

            }

        });
        Log.v("PATOS_LOG", "Request Url Body: " + new String(postRequest.getBodyContentType()));

        queue.add(postRequest);
    }

    static void addPacketToCar(Activity activity, String carID, String userID, double packageKG,
                               double slatitude, double slongitude,
                               double dlatitude, double dlongitude){
        Server.updateCarPacket(activity, carID, userID, packageKG, slatitude, slongitude, dlatitude, dlongitude);
    }

    static void getCar(final Activity activity, final String carID, final String packetID){
        RequestQueue queue = Volley.newRequestQueue(_context);
        String url = "https://protected-dusk-58376.herokuapp.com/getcar?id=" + carID;
        StringRequest postRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        try {
                            parseCarData(activity, response, packetID);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                }
        ) {

            @Override
            public String getBodyContentType() {
                return "application/x-www-form-urlencoded";
            }
        };
        postRequest.setRetryPolicy(new RetryPolicy() {
            @Override
            public int getCurrentTimeout() {
                return 50000;
            }

            @Override
            public int getCurrentRetryCount() {
                return 50000;
            }

            @Override
            public void retry(VolleyError error) throws VolleyError {

            }

        });
        Log.v("PATOS_LOG", "/getcar request sent.");

        queue.add(postRequest);
    }
    private static void parseCarData(Activity activity, String JSON, String packetID) throws JSONException, ParseException {
        List<Packet> returnList = new ArrayList<>();
        Log.v("PATOS_LOG", "Returned JSON: " + JSON);

        JSONObject carData = new JSONObject(JSON).getJSONObject("data");
        Car car = new Car(carData.getString("_id"));
        car.set_s_longitude(carData.getDouble("departureLong"));
        car.set_s_latitude(carData.getDouble("departureLat"));
        car.set_d_longitude(carData.getDouble("arrivalLong"));
        car.set_d_latitude(carData.getDouble("arrivalLat"));

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
        car.set_departure(sdf.parse(carData.getString("departureDate")));

        car.set_weight_capacity(carData.getDouble("maxKg"));
        car.set_c_weight(carData.getDouble("currentKg"));
        car.set_drivername(carData.getString("driver"));

        JSONArray packets = carData.getJSONArray("packetlist");
        for(int i = 0; i < packets.length(); i++){
            JSONArray jpacket = packets.getJSONArray(i);
            car.addPacket(
                    jpacket.getString(0),
                    jpacket.getDouble(1),
                    jpacket.getDouble(2),
                    jpacket.getDouble(3),
                    jpacket.getDouble(4),
                    jpacket.getDouble(5)
            );
            Log.v("Patos_log",car.getPacketList().get(i).ID+"");
        }

        Data.carWillBeDisplayed=car;
        //Log.v("Patos_log",car.getPacketList().get(0).ID+"");
        Intent intent = new Intent(activity,PacketLocationPage.class);
        intent.putExtra("packetID", packetID);
        activity.startActivity(intent);

    }

    private static void parsePacketData(Activity activity, String JSON) throws JSONException {
        List<Packet> returnList = new ArrayList<>();
        Log.v("PATOS_LOG", "Returned JSON: " + JSON);

        JSONObject jsonData = new JSONObject(JSON);

        JSONArray records = jsonData.getJSONArray("data");

        for(int i = 0; i< records.length();i++){
            JSONObject jo = records.getJSONObject(i);
            Packet p = new Packet();
            p.car = new Car(jo.getString("carID"));
            p.ID = jo.getString("_id");
            p.weight = jo.getDouble("packet");
            returnList.add(p);
        }
        Data.allPackets.clear();
        Data.allPackets = returnList;
        Intent intent = new Intent(activity,MyPackets.class);
        activity.startActivity(intent);

    }

    private static void updateCarPacket(final Activity activity, final String carID,
                                        final String userID, final double packageKG,
                                        final double slatitude, final double slongitude,
                                        final double dlatitude, final double dlongitude){
        RequestQueue queue = Volley.newRequestQueue(_context);
        String url = "https://protected-dusk-58376.herokuapp.com/addPacket";
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        Log.v("PATOS_LOG", "Response: " + response);
                        try {
                            processPacketResult(activity, response);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        error.printStackTrace();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<>();
                params.put("carID", carID); //Add the data you'd like to send to the foter.com.httppost.server.
                params.put("userID", userID); //Add the data you'd like to send to the foter.com.httppost.server.
                params.put("packageKG", packageKG + "");
                params.put("slatitude", slatitude + "");
                params.put("slongitude", slongitude + "");
                params.put("dlatitude", dlatitude + "");
                params.put("dlongitude", dlongitude + "");
                Log.v("PATOS_LOG", "CAR: " + carID + ", USER: " + userID + ", PacketKG: " + packageKG);
                return params;
            }

            @Override
            public String getBodyContentType() {
                return "application/x-www-form-urlencoded";
            }
        };
        postRequest.setRetryPolicy(new RetryPolicy() {
            @Override
            public int getCurrentTimeout() {
                return 50000;
            }

            @Override
            public int getCurrentRetryCount() {
                return 50000;
            }

            @Override
            public void retry(VolleyError error) throws VolleyError {

            }

        });
        Log.v("PATOS_LOG", "Request Url Body: " + new String(postRequest.getBodyContentType()));

        queue.add(postRequest);
    }

    private static void processPacketResult(Activity activity, String response) throws JSONException {
        JSONObject JSONresponse = new JSONObject(response);
        int responseStatus = JSONresponse.getInt("code");
        if(responseStatus == 0){
            Toast.makeText(activity.getApplicationContext(), "Added", Toast.LENGTH_SHORT);
            activity.finish();
        }else{
            Toast.makeText(activity.getApplicationContext(), "Cannot be added!", Toast.LENGTH_SHORT);
            activity.finish();
        }
    }

    private static void processCarResult(String response, Double kg,
                                         double deperatureLat, double deperatureLon,
                                         double arrivalLat, double arrivalLon){
        List<Car> returnList = new ArrayList<>();
        List<Car> availableCars = null;
        try {
            availableCars = parseCars(response);
            Dictionary<String, Car> carsDictionary = new Hashtable<>();
            Map<Double, String> totalDistances = new TreeMap<>();

            for(Car car : availableCars){
                carsDictionary.put(car.get_id(), car);
                double deperatureDistance = Calculator.calculateDistance(car.get_s_latitude(), deperatureLat, car.get_s_longitude(), deperatureLon);
                double arrivalDistance = Calculator.calculateDistance(car.get_d_latitude(), arrivalLat, car.get_d_longitude(), arrivalLon);
                car.d_distance = arrivalDistance;
                car.s_distance = deperatureDistance;

                totalDistances.put(deperatureDistance + arrivalDistance, car.get_id());
            }
            Log.v("PATOS_LOG",totalDistances.size()+"");

            for(Map.Entry<Double, String> carDetails : totalDistances.entrySet()){
                Car car = carsDictionary.get(carDetails.getValue());
                if(car.get_weight_capacity() - car.get_c_weight() >= kg)
                    returnList.add(car);
            }
        } catch (JSONException | ParseException e) {
            e.printStackTrace();
        }

        Log.v("PATOS_LOG", "KG: " + kg + "kg, Retrieved: " + response);

        AvailableCars.availableCars = returnList;
        Data.allCars = returnList;
        Log.v("PATOS_LOG", availableCars.size() + "");
        Intent availableCarPage = new Intent(_context, AvailableCars.class);
        availableCarPage.putExtra("kg", kg);
        availableCarPage.putExtra("slatitude", deperatureLat);
        availableCarPage.putExtra("slongitude", deperatureLon);
        availableCarPage.putExtra("dlatitude", arrivalLat);
        availableCarPage.putExtra("dlongitude", arrivalLon);
        _context.startActivity(availableCarPage);
    }

    private static List<Car> parseCars(String JSON) throws JSONException, ParseException {
        List<Car> returnList = new ArrayList<>();

        JSONObject jsonData = new JSONObject(JSON);

        JSONArray records = jsonData.getJSONArray("records");

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        sdf.setTimeZone(TimeZone.getTimeZone("GMT"));

        Log.v("PATOS_LOG", records.toString());
        for(int i = 0; i < records.length(); i++){
            JSONObject JSONcar = records.getJSONObject(i);
            Log.v("PATOS_LOG", JSONcar.toString());
            Car car = new Car(JSONcar.getString("_id"));
            car.set_d_longitude(JSONcar.getDouble("departureLong"));
            car.set_d_latitude(JSONcar.getDouble("departureLat"));
            car.set_s_latitude(JSONcar.getDouble("arrivalLat"));
            car.set_s_longitude(JSONcar.getDouble("arrivalLong"));
            car.set_departure(sdf.parse(JSONcar.getString("departureDate")));
            car.set_weight_capacity(JSONcar.getDouble("maxKg"));
            car.set_c_weight(JSONcar.getDouble("currentKg"));
            car.set_drivername(JSONcar.getString("driver"));
            returnList.add(car);
        }

        return returnList;
    }

    private static void getCars(final Date startDate, final Date endDate, final double kg,
                                final double deperatureLat, final double deperatureLon,
                                final double arrivalLat, final double arrivalLon){
        RequestQueue queue = Volley.newRequestQueue(_context);
        String url = "https://protected-dusk-58376.herokuapp.com/getbydate";
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        processCarResult(response, kg, deperatureLat, deperatureLon,arrivalLat,arrivalLon);
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        error.printStackTrace();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams()
            {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                Map<String, String>  params = new HashMap<>();
                params.put("enddate", sdf.format(endDate)); //Add the data you'd like to send to the foter.com.httppost.server.
                params.put("startdate", sdf.format(startDate)); //Add the data you'd like to send to the foter.com.httppost.server.
                params.put("kg", kg + "");
                return params;
            }
            @Override
            public String getBodyContentType() {
                return "application/x-www-form-urlencoded";
            }
        };
        postRequest.setRetryPolicy(new RetryPolicy() {
            @Override
            public int getCurrentTimeout() {
                return 50000;
            }

            @Override
            public int getCurrentRetryCount() {
                return 50000;
            }

            @Override
            public void retry(VolleyError error) throws VolleyError {

            }

        });
        Log.v("PATOS_LOG", "Request Url Body: " + new String(postRequest.getBodyContentType()));

        queue.add(postRequest);
    }

}
