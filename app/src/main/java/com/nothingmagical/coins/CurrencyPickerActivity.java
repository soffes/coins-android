package com.nothingmagical.coins;

import android.app.ListActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.json.JSONArray;
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

            mCurrencies = new JSONObject(sb.toString());
            JSONObject lookup = mCurrencies.getJSONObject("currencies");

            JSONArray order = mCurrencies.getJSONArray("order");
            String[] names = new String[order.length()];
            String selectedKey = Preferences.getCurrencyCode(this);
            int selectedIndex = 0;
            for (int i = 0; i < order.length(); i++) {
                String key = order.getString(i);
                if (key.equals(selectedKey)) {
                    selectedIndex = i;
                }
                names[i] = lookup.getString(key);
            }

            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_checked, names);
            setListAdapter(adapter);
            getListView().setItemChecked(selectedIndex, true);

        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        for (int i = 0; i < getListAdapter().getCount(); i++) {
            if (i != position) {
                l.setItemChecked(i, false);
            }
        }

        try {
            JSONArray order = mCurrencies.getJSONArray("order");
            String code = order.getString(position);
            Preferences.setCurrencyCode(this, code);
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }

        finish();
    }
}
