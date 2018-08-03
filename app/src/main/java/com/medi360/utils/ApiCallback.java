package com.medi360.utils;

import android.app.Activity;
import android.content.Context;

import org.json.JSONException;

/**
 * Created by jegan on 2/28/2016.
 */
public interface ApiCallback {
    public void responseCallback(Context context, Activity activity, String response) throws JSONException;
}
