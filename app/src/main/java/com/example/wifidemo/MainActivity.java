package com.example.wifidemo;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    Button updateBtn, scanBtn;
    TextView is5Gtv, isEnabledtv, getWifiTv;
    String TAG = "WifiDemo";
    private WifiManager wifiManager;
    Spinner spinner;
    BroadcastReceiver wifiScanReceiver;

    List<String> wifiNetworks = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        updateBtn = (Button) findViewById(R.id.updateBtn);
        scanBtn = (Button) findViewById(R.id.scanBtn);
        is5Gtv = (TextView) findViewById(R.id.is5GTV);
        isEnabledtv = (TextView) findViewById(R.id.isenabledTV);
        getWifiTv = (TextView) findViewById(R.id.getWifiTV);
        spinner = (Spinner) findViewById(R.id.spinner);

        //create wifi manager object
        wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);

        //intent filter & register broadcast receiver
        wifiScanReceiver = new MyWifiReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);
        this.registerReceiver(wifiScanReceiver, intentFilter);

        //check permissions
        checkPermissions();

        //register listeners
        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                is5Gsupported();
                isWifiEnabled();
                getWifiState();
            }
        });

        scanBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startScan();
            }
        });
    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(wifiScanReceiver);
        super.onDestroy();
    }

    private void checkPermissions()
    {

    }

    private void scanSuccess() {
        List<ScanResult> results = wifiManager.getScanResults();
        wifiNetworks.clear();


        for (ScanResult result : results) {
            wifiNetworks.add(result.SSID); // Add the SSID of the network to the list
        }

        Log.d(TAG, results.toString());
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, wifiNetworks);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

    }

    private void scanFailure() {
        // handle failure: new scan did NOT succeed
        // consider using old scan results: these are the OLD results!
        Log.d(TAG, "Scan failed");
    }

    public void startScan()
    {
        boolean success = wifiManager.startScan();
        Log.d(TAG, "Scan started");
        if (!success) {
            // scan failure handling
            scanFailure();
            Log.d(TAG, "Scan fail in start");
        }
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

    public class MyWifiReceiver extends BroadcastReceiver {
        private static final String TAG = "MyWifiReceiver";
        @Override
        public void onReceive(Context c, Intent intent) {
            boolean success = intent.getBooleanExtra(
                    WifiManager.EXTRA_RESULTS_UPDATED, false);
            if (success) {
                Log.d(TAG, "Scan success in rx");
                scanSuccess();
            } else {
                // scan failure handling
                scanFailure();
                Log.d(TAG, "Scan fail in rx");
            }
        }
    };
}