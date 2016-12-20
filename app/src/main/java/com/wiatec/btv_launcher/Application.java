package com.wiatec.btv_launcher;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.wiatec.btv_launcher.Utils.Logger;

/**
 * Created by PX on 2016-11-14.
 */

public class Application extends android.app.Application {

    private static RequestQueue requestQueue;
    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        Logger.init("----px----");
        context = getApplicationContext();
        requestQueue = Volley.newRequestQueue(getApplicationContext());
    }

    public static Context getContext (){
        return context;
    }

    public static RequestQueue getRequestQueue (){
        return requestQueue;
    }
}
