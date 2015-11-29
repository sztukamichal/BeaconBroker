package com.michalapps.model;

import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
@Table(name = "measurments")
public class Measurment {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
    
	private String deviceId;
	private String beaconId;
	
	private int rssi;
    private int txPower;
    private Timestamp time;
    // dystans który podany jest przez uzytkownika
    private float distance;
    
    
    public Measurment() {
    	
    }
    
    

	public Measurment(String deviceId, String beaconId, int rssi, int txPower,
			Timestamp time, float distance) {
		this.deviceId = deviceId;
		this.beaconId = beaconId;
		this.rssi = rssi;
		this.txPower = txPower;
		this.time = time;
		this.distance = distance;
	}



	public int getId() {
		return id;
	}


	public void setId(int id) {
		this.id = id;
	}


	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public String getBeaconId() {
		return beaconId;
	}

	public void setBeaconId(String beaconId) {
		this.beaconId = beaconId;
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


	public Timestamp getTime() {
		return time;
	}


	public void setTime(Timestamp time) {
		this.time = time;
	}


	public float getDistance() {
		return distance;
	}


	public void setDistance(float distance) {
		this.distance = distance;
	}

	

}
