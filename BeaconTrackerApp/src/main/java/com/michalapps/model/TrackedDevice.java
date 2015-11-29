package com.michalapps.model;

import java.util.HashMap;

public class TrackedDevice {

	private String deviceId;
	private HashMap<String, Float> distancesToBeacons = new HashMap<String, Float>();

	
	public TrackedDevice() {
		super();/*
		deviceId = "teelfon";
		distancesToBeacons.put("pierwszy", (float) 1.2);
		distancesToBeacons.put("drugi", (float) 2.2);
		distancesToBeacons.put("trzeci", (float) 0.2);*/
	}
	
	public String getDeviceId() {
		return deviceId;
	}
	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public HashMap<String, Float> getDistancesToBeacons() {
		return distancesToBeacons;
	}

	public void setDistancesToBeacons(HashMap<String, Float> distancesToBeacons) {
		this.distancesToBeacons = distancesToBeacons;
	}
	
	public void changeDistanceToBeacon(String beaconId, float distance) {
		distancesToBeacons.replace(beaconId, distance);
	}
	
	public float getDistanceToBeacon(String beaconId) {
		return distancesToBeacons.get(beaconId);
	}

	
	
}
