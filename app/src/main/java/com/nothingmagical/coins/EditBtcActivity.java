package com.nothingmagical.coins;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.EditText;
import android.widget.TextView;


public class EditBtcActivity extends Activity {

    public static final String PREFERENCES_NAME = "Preferences";
    public static final String KEY_BTC = "BTC";

    protected EditText mTextField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_btc);

        mTextField = (EditText) findViewById(R.id.textField);
        mTextField.setText(String.valueOf(getBtc()));
        mTextField.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                String string = textView.getText().toString();
                setBtc(Double.parseDouble(string));
                finish();
                return false;
            }
        });
    }

    protected double getBtc() {
        SharedPreferences preferences = getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
        return Double.longBitsToDouble(preferences.getLong(KEY_BTC, Double.doubleToLongBits(2.0)));
    }

    protected void setBtc(double number) {
        SharedPreferences preferences = getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putLong(KEY_BTC, Double.doubleToRawLongBits(number));
        editor.commit();
    }
}
