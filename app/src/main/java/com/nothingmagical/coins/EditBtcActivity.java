package com.nothingmagical.coins;

import android.app.Activity;
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
        mTextField.setText(Preferences.getBtcString(this));
        mTextField.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                Preferences.setBtc(EditBtcActivity.this, textView.getText().toString());
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
}
