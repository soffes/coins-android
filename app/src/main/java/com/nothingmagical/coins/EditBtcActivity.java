package com.nothingmagical.coins;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
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
        mTextField.setText(getBtc());
        mTextField.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                setBtc(textView.getText().toString());
                finish();
                return false;
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        mTextField.requestFocus(View.FOCUS_RIGHT);
    }

    protected String getBtc() {
        SharedPreferences preferences = getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
        return preferences.getString(KEY_BTC, "0");
    }

    protected void setBtc(String number) {
        SharedPreferences preferences = getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(KEY_BTC, number);
        editor.commit();
    }
}
