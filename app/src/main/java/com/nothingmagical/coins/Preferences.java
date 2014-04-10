package com.nothingmagical.coins;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by soffes on 4/9/14.
 */
public class Preferences {

    public static final String PREFERENCES_NAME = "Preferences";
    public static final String CONVERSION_PREFERENCES_NAME = "ConversionPreferences";
    public static final String KEY_BTC = "BTC";
    public static final String KEY_CURRENCY = "Currency";
    public static final String KEY_UPDATED_AT = "UpdatedAt";

    public static String getCurrencyCode(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
        return preferences.getString(KEY_CURRENCY, "USD");
    }

    public static void setCurrencyCode(Context context, String code) {
        SharedPreferences preferences = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(KEY_CURRENCY, code);
        editor.commit();
    }

    public static double getBtc(Context context) {
        return Double.parseDouble(getBtcString(context));
    }

    public static String getBtcString(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
        return preferences.getString(KEY_BTC, "0");
    }

    public static void setBtc(Context context, String number) {
        SharedPreferences preferences = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(KEY_BTC, number);
        editor.commit();
    }

    public static double getRate(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(CONVERSION_PREFERENCES_NAME, Context.MODE_PRIVATE);
        return Double.parseDouble(preferences.getString(getCurrencyCode(context), "0"));
    }

    public static long getUpdatedAtTimestamp(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(CONVERSION_PREFERENCES_NAME, Context.MODE_PRIVATE);
        return preferences.getLong(KEY_UPDATED_AT, 0);
    }
}
