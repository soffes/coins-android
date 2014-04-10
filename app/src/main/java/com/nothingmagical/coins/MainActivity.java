package com.nothingmagical.coins;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.NumberFormat;
import java.util.Currency;
import java.util.Iterator;
import java.util.Timer;
import java.util.TimerTask;


public class MainActivity extends Activity {

    public static final String TAG = MainActivity.class.getSimpleName();

    protected TextView mValueLabel;
    protected TextView mBtcLabel;
    protected TextView mUpdatedAtLabel;
    protected boolean mUpdating;
    protected Timer mTimer;
    protected Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            updateUpdatedAtLabel();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mValueLabel = (TextView) findViewById(R.id.valueLabel);
        mValueLabel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, CurrencyPickerActivity.class);
                startActivity(intent);
            }
        });

        mBtcLabel = (TextView) findViewById(R.id.btcLabel);
        mBtcLabel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, EditBtcActivity.class);
                startActivity(intent);
            }
        });

        mUpdatedAtLabel = (TextView) findViewById(R.id.updatedAtLabel);
        mUpdatedAtLabel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                refresh();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        refresh();

        if (mTimer == null) {
            mTimer = new Timer();
            mTimer.schedule(new TimerTask() {
                @Override
                public void run() {
                    mHandler.obtainMessage(1).sendToTarget();
                }
            }, 1000, 1000);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mTimer != null) {
            mTimer.cancel();
            mTimer = null;
        }
    }

    protected void refresh() {
        if (mUpdating) {
            return;
        }

        if (isNetworkAvailable()) {
            mUpdating = true;
            updateInterface();

            GetConversionTask task = new GetConversionTask();
            task.execute();
        } else {
            updateInterface();
            Toast.makeText(this, R.string.no_network, Toast.LENGTH_LONG).show();
        }
    }

    protected void updateInterface() {
        double btc = Preferences.getBtc(this);
        double rate = Preferences.getRate(this);
        double value = btc * rate;

        // Value
        String code = Preferences.getCurrencyCode(this);
        NumberFormat format = NumberFormat.getCurrencyInstance();
        format.setCurrency(Currency.getInstance(code));
        mValueLabel.setText(format.format(value));

        // BTC
        format = NumberFormat.getNumberInstance();
        format.setMaximumFractionDigits(10);
        mBtcLabel.setText(format.format(btc) + " BTC");

        // Updated at
        updateUpdatedAtLabel();
    }

    protected void updateUpdatedAtLabel() {
        if (mUpdating) {
            mUpdatedAtLabel.setText(R.string.updating);
        } else {
            long timestamp = Preferences.getUpdatedAtTimestamp(this);
            if (timestamp == 0) {
                mUpdatedAtLabel.setText(R.string.updated_never);
            } else {
                long now = System.currentTimeMillis();
                if (now - timestamp < 11000) {
                    mUpdatedAtLabel.setText(R.string.updated_just_now);
                } else {
                    CharSequence timeAgoInWords = DateUtils.getRelativeTimeSpanString(timestamp, now, 0);
                    mUpdatedAtLabel.setText(String.format(getString(R.string.updated_format), timeAgoInWords));
                }
            }
        }
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager manager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();

        return networkInfo != null && networkInfo.isConnected();
    }

    public class GetConversionTask extends AsyncTask<Object, Void, Object> {
        @Override
        protected Void doInBackground(Object... arg0) {
            try {
                // Connect
                String json = getJSON("https://coinbase.com/api/v1/currencies/exchange_rates", 1500);
                if (json == null) {
                    return null;
                }
                JSONObject data = new JSONObject(json);

                // Put values into map
                Iterator<String> keys = data.keys();

                SharedPreferences preferences = getSharedPreferences(Preferences.CONVERSION_PREFERENCES_NAME, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();

                while (keys.hasNext()) {
                    String key = keys.next();
                    if (key.startsWith("btc_to_")) {
                        String value = data.getString(key);
                        key = key.replace("btc_to_", "").toUpperCase();
                        editor.putString(key, value);
                    }
                }

                editor.putLong(Preferences.KEY_UPDATED_AT, System.currentTimeMillis());
                editor.commit();
            }
            catch (Exception e) {
                e.printStackTrace();
                Log.e(TAG, e.getClass().getSimpleName() + " - " + e.getMessage());
            }

            return null;
        }

        @Override
        protected void onPostExecute(Object _) {
            mUpdating = false;
            updateInterface();
        }

        public String getJSON(String url, int timeout) {
            try {
                URL u = new URL(url);
                HttpURLConnection c = (HttpURLConnection) u.openConnection();
                c.setRequestMethod("GET");
                c.setRequestProperty("Content-length", "0");
                c.setUseCaches(false);
                c.setAllowUserInteraction(false);
                c.setConnectTimeout(timeout);
                c.setReadTimeout(timeout);
                c.connect();
                int status = c.getResponseCode();

                switch (status) {
                    case 200:
                    case 201:
                        BufferedReader br = new BufferedReader(new InputStreamReader(c.getInputStream()));
                        StringBuilder sb = new StringBuilder();
                        String line;
                        while ((line = br.readLine()) != null) {
                            sb.append(line+"\n");
                        }
                        br.close();
                        return sb.toString();
                }

            } catch (MalformedURLException e) {
                Log.e(TAG, e.getMessage());
            } catch (IOException e) {
                Log.e(TAG, e.getMessage());
            }
            return null;
        }
    }
}
