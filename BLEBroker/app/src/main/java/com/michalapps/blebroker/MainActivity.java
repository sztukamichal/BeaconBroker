package com.michalapps.blebroker;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanResult;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    Device thisDevice = new Device();
    boolean sendToServer = false;
    private OwnScanner ownScanner = new OwnScanner();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(sendToServer == true) {
                    Snackbar.make(view, "Zatrzymanie sledzenia.", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                    sendToServer = false;
                    new OnDestroyTask().execute();
                } else {
                    Snackbar.make(view, "Przesyłam dane na serwer...", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                    sendToServer = true;
                }
            }
        });

        String android_id = Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID);
        thisDevice.setDeviceId(Build.MODEL + " - " + android_id);

        recyclerView = new RecyclerView(this);
        recyclerView = (RecyclerView)findViewById(R.id.recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        ownScanner.beginScanning();
    }

    private void updateBeaconsInRange(ScanResult result) {
        BeaconInRange resultBeacon = new BeaconInRange(result.getScanRecord().getTxPowerLevel(),result.getRssi(), result.getDevice().getAddress());
        if(thisDevice.getBeaconsInRangeList().size() > 0) {
            BeaconInRange updateBeacon = findBeaconInRange(resultBeacon);
            if(updateBeacon == null) {
                thisDevice.getBeaconsInRangeList().add(resultBeacon);
            } else {
                updateBeacon.setTxPower(resultBeacon.getTxPower());
                updateBeacon.setRssi(resultBeacon.getRssi());
            }
        } else {
            thisDevice.getBeaconsInRangeList().add(resultBeacon);
        }
    }

    private BeaconInRange findBeaconInRange(BeaconInRange beacon) {
        for(BeaconInRange beaconInRange : thisDevice.getBeaconsInRangeList()) {
            if(beaconInRange.getAddress().equals(beacon.getAddress())) {
                return beaconInRange;
            }
        }
        return null;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        new OnDestroyTask().execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private class HttpRequestTask extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... params) {
            try {
                RestTemplate restTemplate = new RestTemplate();
                restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
//                return restTemplate.postForObject("http://78.88.254.200:8081/devices", thisDevice, String.class);
                return restTemplate.postForObject("http://192.168.1.105:8080/postDeviceInRange", thisDevice, String.class);
            } catch (Exception e) {
                Log.e("HTTPRequestTask", e.getMessage(), e);
            }

            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            Log.v("Odpowiedz", result);
        }

    }

    private class OnDestroyTask extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... params) {
            try {
                RestTemplate restTemplate = new RestTemplate();
                restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
//                return restTemplate.postForObject("http://78.88.254.200:8081/device-destroy", thisDevice, String.class);
                return restTemplate.postForObject("http://192.168.1.105:8080/deviceOutOfRange", thisDevice, String.class);
            } catch (Exception e) {
                Log.e("HTTPRequestTask", e.getMessage(), e);
            }

            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            Log.v("Odpowiedz", result);
        }

    }

    private class OwnScanner {

        private static final int SCAN_INTERVAL_MS = 250;

        private Handler scanHandler = new Handler();
        private List<android.bluetooth.le.ScanFilter> scanFilters = new ArrayList<android.bluetooth.le.ScanFilter>();
        private android.bluetooth.le.ScanSettings scanSettings;
        private boolean isScanning = false;

        public void beginScanning() {
            android.bluetooth.le.ScanSettings.Builder scanSettingsBuilder = new android.bluetooth.le.ScanSettings.Builder();
            scanSettingsBuilder.setScanMode(android.bluetooth.le.ScanSettings.SCAN_MODE_LOW_LATENCY);//.setReportDelay(50);
            scanSettings = scanSettingsBuilder.build();

            scanHandler.post(scanRunnable);
        }

        private Runnable scanRunnable = new Runnable() {
            @Override
            public void run() {
                BluetoothLeScanner scanner = BluetoothAdapter.getDefaultAdapter().getBluetoothLeScanner();

                if (isScanning) {
                    scanner.stopScan(scanCallback);
                } else {
                    scanner.startScan(scanFilters, scanSettings, scanCallback);
                }

                isScanning = !isScanning;

                scanHandler.postDelayed(this, SCAN_INTERVAL_MS);
            }
        };

        private android.bluetooth.le.ScanCallback scanCallback = new android.bluetooth.le.ScanCallback() {

            @Override
            public void onScanResult(int callbackType, android.bluetooth.le.ScanResult result) {
                updateBeaconsInRange(result);
                RecyclerViewAdapter recyclerViewAdapter = new RecyclerViewAdapter(thisDevice.getBeaconsInRangeList());
                recyclerView.setAdapter(recyclerViewAdapter);
                Log.i("Skaner", "Adres : " + result.getDevice().getAddress() + " RSSI : " + result.getRssi());
                if(sendToServer == true){
                    new HttpRequestTask().execute();
                }
            }

            @Override
            public void onScanFailed(int errorCode) {
                super.onScanFailed(errorCode);

                // a scan error occurred
            }
        };

    }



}

