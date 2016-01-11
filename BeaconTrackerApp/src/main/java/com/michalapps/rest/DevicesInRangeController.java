package com.michalapps.rest;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;

import com.michalapps.model.BeaconInRange;
import com.michalapps.model.Device;
import com.michalapps.model.MeasurmentDao;
import com.michalapps.model.TrackedDevice;
import com.michalapps.model.Measurement;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by User on 2015-11-18.
 */
@RestController
public class DevicesInRangeController {

    private List<Device> devicesInRange = new ArrayList<>();
    private List<TrackedDevice> trackedDevices = new ArrayList<>();
    @Autowired
    private MeasurmentDao measurmentDao;
    
    @RequestMapping(value = "/trackDevice", method = RequestMethod.POST)
    public String trackDevice(@RequestBody TrackedDevice trackDevice) {
    	TrackedDevice foundDevice = findTrackedDevice(trackDevice.getDeviceId());
    	if(foundDevice != null) {
    		trackedDevices.remove(foundDevice);
    	}
    	trackedDevices.add(trackDevice);
		return "{\"status\":\"success\"}";
    }
    
    @RequestMapping(value = "stopTrackDevice", method = RequestMethod.POST)
    public String stopTrackDevice(@RequestBody TrackedDevice trackedDevice) {
    	TrackedDevice foundDevice = findTrackedDevice(trackedDevice.getDeviceId());
    	if(foundDevice != null) {
    		trackedDevices.remove(foundDevice);
    		return "{\"status\":\"success\"}";
    	}
		return "{\"status\":\"device is not tracked\"}";
    }
    
    @RequestMapping(value = "/getTrackedDevices", method = RequestMethod.GET)
    public List<TrackedDevice> getTrackedDevices() {
    	return trackedDevices;
    }
    
    @RequestMapping(value = "/postDeviceInRange", method = RequestMethod.POST)
    public String postDeviceInRange(@RequestBody Device device) {
        Device found = findDeviceInRange(device.getDeviceId());
        if(found != null) {
            found.setBeaconsInRangeList(device.getBeaconsInRangeList());
            found.setLastUpdated(device.getLastUpdated());
        } else {
            devicesInRange.add(device);
        }
        TrackedDevice dev = findTrackedDevice(device.getDeviceId());
        if(dev != null) {
        	Measurement measurment;
        	BeaconInRange lastUpdated = device.getLastUpdated();
        	float distance = dev.getDistanceToBeacon(lastUpdated.getAddress());
    		measurment = new Measurement(device.getDeviceId(), lastUpdated.getAddress(), lastUpdated.getRssi(), lastUpdated.getTxPower(), new Timestamp(new java.util.Date().getTime()),distance, device.getBatteryLevel());
    		measurmentDao.save(measurment);	
        }
        
		return device.toString();
    }

    @RequestMapping(value = "/getDevicesInRange", method = RequestMethod.GET)
    public List<Device> getDevicesInRange() {
        return devicesInRange;
    }

    @RequestMapping(value = "deviceOutOfRange", method = RequestMethod.POST)
    public String deviceOutOfRange(@RequestBody Device device) {
        Device found = findDeviceInRange(device.getDeviceId());
        if(found != null) {
            devicesInRange.remove(found);
    		return device.toString();
        } else {
    		return device.toString();
        }
    }
    
    private Device findDeviceInRange(String deviceId) {
        for(Device device : devicesInRange) {
            if(device.getDeviceId().equals(deviceId)) {
                return device;
            }
        }
        return null;
    }

	private TrackedDevice findTrackedDevice(String deviceId) {
	    for(TrackedDevice device : trackedDevices) {
            if(device.getDeviceId().equals(deviceId)) {
                return device;
            }
        }
        return null;
    }
}
