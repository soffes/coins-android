package com.nothingmagical.coins;

import android.app.ListActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;


public class CurrencyPickerActivity extends ListActivity {

    public static final String TAG = CurrencyPickerActivity.class.getSimpleName();
    protected JSONObject mCurrencies;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_currency_picker);

        getListView().setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        // Load currencies
        try {
            InputStream is = getResources().openRawResource(R.raw.currencies);
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line + "\n");
            }
            br.close();
            String jsonString = sb.toString();
            mCurrencies = new JSONObject(jsonString);
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
    }
}
