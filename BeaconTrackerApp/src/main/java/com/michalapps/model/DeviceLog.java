package com.michalapps.model;

import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "devices_log")
public class DeviceLog {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
    private String address;
	
	private int distance;
	private int rssi;
    private int txPower;

    public DeviceLog() {
    	
    }
   
    public DeviceLog(String address, int distance, int rssi,
			int txPower) {
		this.address = address;
		this.distance = distance;
		this.rssi = rssi;
		this.txPower = txPower;
	}



	public String getAddress() {
		return address;
	}



	public int getId() {
		return id;
	}



	public void setId(int id) {
		this.id = id;
	}



	public void setAddress(String address) {
		this.address = address;
	}




	public int getDistance() {
		return distance;
	}



	public void setDistance(int distance) {
		this.distance = distance;
	}



	public int getRssi() {
		return rssi;
	}



	public void setRssi(int rssi) {
		this.rssi = rssi;
	}



	public int getTxPower() {
		return txPower;
	}



	public void setTxPower(int txPower) {
		this.txPower = txPower;
	}



	@Override
    public String toString() {
        return "DeviceInRange{" +
                "tx=" + txPower +
                ", rssi=" + rssi +
                ", distance=" + distance +
                ", address='" + address + '\'' +
                '}';
    }
	
	

}
