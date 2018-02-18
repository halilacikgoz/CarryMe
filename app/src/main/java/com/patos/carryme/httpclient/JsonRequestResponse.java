package com.patos.carryme.httpclient;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by fatih on 18/04/2017.
 */

public class JsonRequestResponse extends RequestResponse {

    public JsonRequestResponse(String url) {
        super(url);
    }

    public  JSONObject getJSONfromURL() {
        String result = "";
        JSONObject jArray = null;
        Response response = null;
        client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .build();

        try {
            response = client.newCall(request).execute();
            result= response.body().string();
            Log.e("log_tag",result.toString());
        } catch (Exception e) {
            Log.e("log_tag", "Error in http connection " + e.toString());
        }
        try {
            jArray = new JSONObject(result.substring(result.indexOf("{"), result.lastIndexOf("}") + 1));
        } catch (JSONException e) {
            Log.e("log_tag", "Error parsing data " + e.toString());
        }
        return jArray;
    }






    public  JSONObject postJSONfromURL(String url) {
        InputStream is = null;
        String result = "";
        JSONObject jArray = null;
        client = new OkHttpClient();
        Log.v("log_tag","burdayim");


        RequestBody formBody = new FormBody.Builder()
                .add("startDate", "2018-02-26")
                .add("endDate", "2019-05-26")
                .add("kg", String.valueOf(20))
                .build();
        Request request = new Request.Builder()
                .url(url)
                .post(formBody)
                .build();
        //request.headers('Content-Type') = "application/json";
        //request.headers['Accept'] = "application/json; charset=UTF-8";
        Log.v("log_tag",request.toString());
        Log.v("log_tag",request.method());



        Log.v("log_tag", String.valueOf(request.headers()));

        Response response = null;
        try {
            response = client.newCall(request).execute();
            if(!response.isSuccessful()) throw new IOException("Unexpected code " + response);{
                Log.v("log_tag",response.body().string());
                result=response.body().string();
            }

        } catch (IOException e) {
            e.printStackTrace();
            Log.e("log_tag", "Error  ıoexception " + e.toString());
        }

        try {
            jArray = new JSONObject(result.substring(result.indexOf("{"), result.lastIndexOf("}") + 1));
        } catch (JSONException e) {
            Log.e("log_tag", "Error parsing data " + e.toString());
        }

        return jArray;
    }


}
