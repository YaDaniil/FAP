package com.yadaniil.fap;

import com.orm.SugarApp;
import com.orm.SugarContext;

/**
 * Created by daniil on 25.08.16.
 */
public class FAPApp extends SugarApp {

    @Override
    public void onCreate() {
        super.onCreate();
        SugarContext.init(getApplicationContext());
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        SugarContext.terminate();
    }
}
