package com.michalapps.model;


import java.sql.Timestamp;

/**
 * Created by User on 2015-11-18.
 */
public class BeaconInRange {

    private int rssi;
    private String address;
    private int txPower;
    private Timestamp lastActivity;

    public BeaconInRange() {
    }

    public BeaconInRange(int txPower, int rssi, String address, Timestamp time) {
        this.rssi = rssi;
        this.address = address;
        this.txPower = txPower;
        this.lastActivity = time;
    }

    public Timestamp getLastActivity() {
        return lastActivity;
    }

    public void setLastActivity(Timestamp lastActivity) {
        this.lastActivity = lastActivity;
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
                ", lastActivity= " + lastActivity +
                '}';
    }
}





