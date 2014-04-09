package com.nothingmagical.coins;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;


public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView valueLabel = (TextView) findViewById(R.id.valueLabel);
        valueLabel.setText("$887.74");

        TextView btcLabel = (TextView) findViewById(R.id.btcLabel);
        btcLabel.setText("2 BTC");

        TextView updatedAtLabel = (TextView) findViewById(R.id.updatedAtLabel);
        updatedAtLabel.setText("Updated 2 minutes ago");
    }
}
