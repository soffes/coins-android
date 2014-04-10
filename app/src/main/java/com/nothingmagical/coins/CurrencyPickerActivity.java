package com.nothingmagical.coins;

import android.app.ListActivity;
import android.os.Bundle;


public class CurrencyPickerActivity extends ListActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_currency_picker);
    }
}
