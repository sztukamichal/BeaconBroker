package com.michalapps.blebroker;

import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
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

import no.nordicsemi.android.support.v18.scanner.BluetoothLeScannerCompat;
import no.nordicsemi.android.support.v18.scanner.ScanCallback;
import no.nordicsemi.android.support.v18.scanner.ScanFilter;
import no.nordicsemi.android.support.v18.scanner.ScanResult;
import no.nordicsemi.android.support.v18.scanner.ScanSettings;

public class MainActivity extends AppCompatActivity {

    private BluetoothLeScannerCompat scannerCompat;
    private RecyclerView recyclerView;
    List<BeaconDevice> beacons = new ArrayList<>();
    DevicesInRange devicesInRange = new DevicesInRange();
    boolean sendToServer = false;
    private String android_id ;

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
                } else {
                    Snackbar.make(view, "Przesyłam dane na serwer...", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                    sendToServer = true;
                }
            }
        });

        android_id = Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID);
        devicesInRange.setDeviceModel(Build.MODEL + " - " + android_id);

        recyclerView = new RecyclerView(this);
        recyclerView = (RecyclerView)findViewById(R.id.recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        scannerCompat = BluetoothLeScannerCompat.getScanner();

        ScanSettings settings = new ScanSettings.Builder().setReportDelay(500).build();
        List<ScanFilter> filterList = new ArrayList<>();
        filterList.add(new ScanFilter.Builder().setDeviceName("nRF51822").build());
        scannerCompat.startScan(filterList, settings, scanCallback);


    }

    private ScanCallback scanCallback = new ScanCallback() {
        @Override
        public void onBatchScanResults(List<ScanResult> results) {
            if(results.size() > 0) {
//                logToDisplay("Found " + results.size() + " beacons in area.");
                update(results);
                RecyclerViewAdapter recyclerViewAdapter = new RecyclerViewAdapter(beacons);
                recyclerView.setAdapter(recyclerViewAdapter);
                Log.v("Bekony", devicesInRange.toString());
                Log.v("Bekonyy", "Model: " + devicesInRange.getDeviceModel());
                if(sendToServer == true){
                    new HttpRequestTask().execute();
                }

            }
        }

        @Override
        public void onScanResult(int callbackType, ScanResult result) {
            super.onScanResult(callbackType, result);
            Log.v("Bekony", "onScanResult");
        }

        @Override
        public void onScanFailed(int errorCode) {
            Log.v("Bekony", "onScanFailed");
            super.onScanFailed(errorCode);
        }
    };

    private void update(final List<ScanResult> results) {
        for (final ScanResult result : results) {
            BeaconDevice device = findDevice(result);
            if (device == null) {
                device = new BeaconDevice(result);
                beacons.add(device);
                devicesInRange.getDevicesInRangeList().add(new DeviceInRange(device.getRssi(), device.getAddress()));
            } else {
                device.setAddress(result.getDevice() != null ? result.getDevice().getAddress() : null);
                device.setRssi(result.getRssi());
                for (DeviceInRange dev : devicesInRange.getDevicesInRangeList()) {
                    if(dev.getAddress().equals(device.getAddress())) {
                        dev.setRssi(device.getRssi());
                    }
                }
            }
        }
    }

    private BeaconDevice findDevice(final ScanResult result) {
        BeaconDevice resultDevice = new BeaconDevice(result);
        int j = 0;
        for(int i = 0; i < beacons.size(); i++) {
            beacons.get(i).visible -= 1;
            if(beacons.get(i).visible == -1) {
//                for (j = 0; j < devicesInRange.getDevicesInRangeList().size(); j++) {
//
////                    if(devicesInRange.getDevicesInRangeList().get(i).getAddress().equals(beacons.get(i).getAddress())) {
////                        devicesInRange.getDevicesInRangeList().remove(devicesInRange.getDevicesInRangeList().get(i));
////                    }
//                }
                devicesInRange.getDevicesInRangeList().remove(i);
                beacons.remove(i);
            }
        }
        for (final BeaconDevice device : beacons) {
            if (device.equals(resultDevice)) {
                device.visible = 3;
                return device;
            }
        }
        return null;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        scannerCompat.stopScan(scanCallback);
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
//                return restTemplate.postForObject("http://78.88.254.200:8081/devices", devicesInRange, String.class);
                return restTemplate.postForObject("http://192.168.0.9:8080/devices", devicesInRange, String.class);
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

}


