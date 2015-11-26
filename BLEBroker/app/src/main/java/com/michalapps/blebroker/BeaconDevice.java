package com.michalapps.blebroker;

import android.bluetooth.BluetoothDevice;

import no.nordicsemi.android.support.v18.scanner.ScanResult;

/**
 * Created by User on 2015-11-17.
 */
public class BeaconDevice {

    private BluetoothDevice bluetoothDevice;
    private int rssi;
    public String address;
    public int visible;
    public ScanResult scanResult;

    public BeaconDevice(ScanResult scanResult) {
        visible = 2;
        bluetoothDevice = scanResult.getDevice();
        rssi = scanResult.getRssi();
        address = bluetoothDevice.getAddress();
        this.scanResult = scanResult;
    }

    @Override
    public boolean equals(Object o) {
        if(o instanceof BeaconDevice) {
            BeaconDevice second = (BeaconDevice) o;
            return address.equals(second.address);
        }
        return super.equals(o);
    }

    public BluetoothDevice getBluetoothDevice() {
        return bluetoothDevice;
    }

    public int getRssi() {
        return rssi;
    }

    public String getAddress() {
        return address;
    }

    public void setBluetoothDevice(BluetoothDevice bluetoothDevice) {
        this.bluetoothDevice = bluetoothDevice;
    }

    public void setRssi(int rssi) {
        this.rssi = rssi;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public ScanResult getScanResult() {
        return scanResult;
    }

    public void setScanResult(ScanResult scanResult) {
        this.scanResult = scanResult;
    }
}
