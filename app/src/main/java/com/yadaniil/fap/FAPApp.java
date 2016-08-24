package com.yadaniil.fap;

import android.content.Context;

import com.orm.SugarApp;
import com.orm.SugarContext;

/**
 * Created by daniil on 25.08.16.
 */
public class FAPApp extends SugarApp {

    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        SugarContext.init(getApplicationContext());
        context = getApplicationContext();
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        SugarContext.terminate();
    }

    public static Context getContext() {
        return context;
    }
}
