package com.patos.carryme.httpclient;

import android.os.AsyncTask;

import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

import android.util.Log;

import com.google.gson.Gson;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.ListIterator;

import okhttp3.HttpUrl;


/**
 * Created by fatih on 18/04/2017.
 */

public class Backgroudjob extends AsyncTask<ArrayList<String>, Integer, JSONObject> {

    private OnTaskCompleted listener;
    private Exception exception;

    public static final String W_URL = "https://protected-dusk-58376.herokuapp.com/getbydate";
    public HttpUrl.Builder urlB;



    public Backgroudjob(OnTaskCompleted listener) {
        this.listener = listener;
    }

    @Override
    protected JSONObject doInBackground(ArrayList<String>... params) {
        JSONObject jsonobject;




        HttpUrl.Builder urlbuild = new HttpUrl.Builder()
                .scheme("https")
                .host("maps.googleapis.com")
                .addPathSegment("maps")
                .addPathSegment("api")
                .addPathSegment("directions")
                .addPathSegment("json")
                .addQueryParameter("origin", "41.00527,28.97696")
                .addQueryParameter("destination","38.41885,27.12872")
                .addQueryParameter("mode","driving")
                .addQueryParameter("sensor","false");


       // HttpUrl.Builder urlBuilder2 = HttpUrl.parse(url).newBuilder();


        String url = urlbuild.build().toString();
        JsonRequestResponse jsonhttp=new JsonRequestResponse(url);
        JSONObject json = jsonhttp.getJSONfromURL();


        try {
            Log.d("log_tag",""+json.getJSONArray("rows"));
        } catch (JSONException e) {
            e.printStackTrace();
        }


        return null;
    }

    @Override
    protected void onPreExecute() {//like initalazinon method

        super.onPreExecute();

    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);

    }
    @Override
    protected void onPostExecute(JSONObject data) {//after finishing doing background

        super.onPostExecute(data);

        Log.d("log_tag",""+"\n==> on post Execute..");

        listener.onTaskCompleted(data);
    }


}

