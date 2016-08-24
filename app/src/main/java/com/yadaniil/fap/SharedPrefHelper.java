package com.yadaniil.fap;

import android.content.Context;
import android.content.SharedPreferences;

import java.math.BigDecimal;

/**
 * Created by daniil on 25.08.16.
 */
public class SharedPrefHelper {

    private static final String USER_CURRENT_BALANCE = "user_current_balance";
    private static SharedPrefHelper instance;
    private SharedPreferences sharedPreferences;


    public static SharedPrefHelper getInstance(){
        if(instance == null) {
            Context context = FAPApp.getContext();
            instance = new SharedPrefHelper(context);
        }
        return instance;
    }

    private SharedPrefHelper(Context context) {
        initSharedPreferences(context);
    }

    private void initSharedPreferences(Context context) {
        sharedPreferences = context.getSharedPreferences("shared_pref_helper", Context.MODE_PRIVATE);
    }

    public void setBalance(String balance) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(USER_CURRENT_BALANCE, balance);
        editor.apply();
    }

    public String getBalance() {
        return sharedPreferences.getString(USER_CURRENT_BALANCE, null);
    }


}
