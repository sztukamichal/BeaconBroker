package com.michalapps.blebroker;


/**
 * Created by User on 2015-11-18.
 */
public class BeaconInRange {

    private int rssi;
    private String address;
    private int txPower;

    public BeaconInRange() {
    }

    public BeaconInRange(int txPower, int rssi, String address) {
        this.rssi = rssi;
        this.address = address;
        this.txPower = txPower;
    }

    public int getRssi() {
        return rssi;
    }

    public void setRssi(int rssi) {
        this.rssi = rssi;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getTxPower() {
        return txPower;
    }

    public void setTxPower(int txPower) {
        this.txPower = txPower;
    }

    @Override
    public String toString() {
        return "BeaconInRange{" +
                "tx=" + txPower +
                ", rssi=" + rssi +
                ", address='" + address + '\'' +
                '}';
    }
}


