package com.medi360.utils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import org.json.JSONException;


/**
 * Created by jegan on 2/28/2016.
 */
public class ApiSender extends AsyncTask<String,String,String> {
    Context context;
    Activity activity;
    Boolean showLoader = true;
    ProgressDialog pDialog;
    ApiCallback apiCallback;
    String response;

    public ApiSender(Context context,ApiCallback apiCallback) {
        this.context = context;
        this.apiCallback = apiCallback;
    }

    public ApiSender(Context context,ApiCallback apiCallback,Boolean showLoader) {
        this.context = context;
        this.apiCallback = apiCallback;
        this.showLoader = showLoader;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        WebService.allowAllSSL();
        if(this.showLoader){
            pDialog = new ProgressDialog(this.context);
            pDialog.setMessage("Loading");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }
    }

    @Override
    protected void onPostExecute(String response) {
        super.onPostExecute(response);
        if(pDialog != null && this.showLoader){
            pDialog.hide();
        }
        System.out.println("Response Data :::" + response);
        try {
            if(response != null){
//                response = response.replace("\\\"", "'");
//                response = response.substring(1, response.length() - 1);
                apiCallback.responseCallback(context,activity,response);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onProgressUpdate(String... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected String doInBackground(String... params) {
        String requestUrl = params[0];
        String requestType = params[1];
        MyHttpClient myHttpClient = new MyHttpClient();
        String response = null;
        System.out.println("Request Url:::" + requestUrl);
        System.out.println("Request Params:::" + params[2]);
        WebService.allowAllSSL();
        if(requestType.toUpperCase().equals("POST")) {
            response = WebService.invokeWS(params[2],requestUrl);
//            response = myHttpClient.sendPostRequest(requestUrl, params[2]);
        }
        else{
            response = myHttpClient.sendGetRequest(requestUrl);
        }
        return response;
    }
}

