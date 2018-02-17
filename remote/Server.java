package com.patos.carryme.remote;

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
import com.patos.carryme.AvailableCars;
import com.patos.carryme.objects.Car;
import com.patos.carryme.objects.singletons.Calculator;
import com.patos.carryme.test.Data;
import com.patos.carryme.test.Properties;

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
                                      double kg, double deperatureLat, double deperatureLon,
                                      double arrivalLat, double arrivalLon){
        getCars(startDate, endDate, kg, deperatureLat, deperatureLon,arrivalLat,arrivalLon);
    }

    static void addPacketToCar(Activity activity, String carID, String userID, double packageKG){
        Server.updateCarPacket(activity, carID, userID, packageKG);
    }

    private static void updateCarPacket(final Activity activity, final String carID, final String userID, final double packageKG){
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
        Log.v("PATOS_LOG", "Response: " + response);
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
            Log.v("PATOS2",totalDistances.size()+"");

            for(Map.Entry<Double, String> carDetails : totalDistances.entrySet()){
                if(carDetails.getKey() > Properties.maxDistance)
                    break;
                Car car = carsDictionary.get(carDetails.getValue());
                if(car.get_weight_capacity() - car.get_c_weight() >= kg)
                    availableCars.add(car);
            }
        } catch (JSONException | ParseException e) {
            e.printStackTrace();
        }

        Log.v("PATOS_LOG", "KG: " + kg + "kg, Retrieved: " + response);

        AvailableCars.availableCars = availableCars;
        Data.allCars = availableCars;
        Log.v("PATOS", availableCars.size() + "");
        Intent availableCarPage = new Intent(_context, AvailableCars.class);
        availableCarPage.putExtra("kg", kg);
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
                        Log.v("PATOS_LOG", "Response: " + response);
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
