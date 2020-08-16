package com.example.wifidemo;

import android.content.Context;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    Button updateBtn;
    TextView is5Gtv, isEnabledtv, getWifiTv;
    String TAG = "WifiDemo";
    private WifiManager wifiManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        updateBtn = (Button) findViewById(R.id.updateBtn);
        is5Gtv = (TextView) findViewById(R.id.is5GTV);
        isEnabledtv = (TextView) findViewById(R.id.isenabledTV);
        getWifiTv = (TextView) findViewById(R.id.getWifiTV);

        //create wifi manager object
        wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);

        //register listeners
        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                is5Gsupported();
                isWifiEnabled();
                getWifiState();
            }
        });
    }

    public void is5Gsupported() {
        if (wifiManager.is5GHzBandSupported()) {
            is5Gtv.setText("is5GHzBandSupported returns true");
        } else {
            is5Gtv.setText("is5GHzBandSupported returns false");
        }
        Log.d(TAG, "is5G Complete");
    }

    public void isWifiEnabled() {
        if (wifiManager.isWifiEnabled()) {
            isEnabledtv.setText("isWifiEnabled() returns true");
        } else {
            isEnabledtv.setText("isWifiEnabled() returns false");
        }
        Log.d(TAG, "isWifi Complete");
    }

    public void getWifiState() {
        int state = wifiManager.getWifiState();
        switch (state) {
            case 0:
                getWifiTv.setText("getWifiState() returns WIFI_STATE_DISABLING");
                break;
            case 1:
                getWifiTv.setText("getWifiState() returns WIFI_STATE_DISABLED");
                break;
            case 2:
                getWifiTv.setText("getWifiState() returns WIFI_STATE_ENABLING");
                break;
            case 3:
                getWifiTv.setText("getWifiState() returns WIFI_STATE_ENABLED");
                break;
            case 4:
                getWifiTv.setText("getWifiState() returns WIFI_STATE_UNKNOWN");
                break;
        }
        Log.d(TAG, "getWifi Complete");
    }
}