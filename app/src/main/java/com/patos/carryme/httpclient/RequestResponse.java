package com.patos.carryme.httpclient;


import okhttp3.OkHttpClient;
import okhttp3.RequestBody;


/**
 * Created by fatih on 18/04/2017.
 */

public class RequestResponse {

    String url;
    OkHttpClient client;
    RequestBody body;

    public RequestResponse(String url) {
        this.url = url;
        client=new OkHttpClient();
    }


}
