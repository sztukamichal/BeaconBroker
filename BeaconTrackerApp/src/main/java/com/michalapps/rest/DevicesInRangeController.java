package com.michalapps.rest;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;

import com.michalapps.model.BeaconInRange;
import com.michalapps.model.DeviceLog;
import com.michalapps.model.DeviceLogDao;
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
    
  /*  @RequestMapping(value = "/changeDistaceToTrackedDevice", method = RequestMethod.POST)
    public String changeDistanceToTrackedDevice(@RequestBody TrackedDevice newDistance) {
    	TrackedDevice logDevice= findTrackedDevice(newDistance.getDeviceId());
    	if(logDevice == null) {
    		return "{\"status\":\"device is not tracked\"}";
        } else {
        	logDevice.setDistance(newDistance.getDistance());
    		return "{\"status\":\"changed\"}";
        }
    }*/
    
    @RequestMapping(value = "/postDeviceInRange", method = RequestMethod.POST)
    public String postDeviceInRange(@RequestBody Device device) {
        Device found = findDeviceInRange(device.getDeviceId());
        if(found != null) {
            found.setBeaconsInRangeList(device.getBeaconsInRangeList());
        } else {
            devicesInRange.add(device);
        }
        TrackedDevice dev = findTrackedDevice(device.getDeviceId());
        if(dev != null) {
        	Measurement measurment;
        	List<BeaconInRange> beacons = device.getBeaconsInRangeList();
        	float distance;
        	for(BeaconInRange beacon : beacons) {
        		distance = dev.getDistanceToBeacon(beacon.getAddress());
        		measurment = new Measurement(device.getDeviceId(), beacon.getAddress(), beacon.getRssi(), beacon.getTxPower(), new Timestamp(new java.util.Date().getTime()),distance);
        		measurmentDao.save(measurment);
        	}
        }
        
		return device.toString();
    }

    @RequestMapping(value = "/getDevicesInRange", method = RequestMethod.GET)
    public List<Device> getDevicesInRange() {
        return devicesInRange;
    }
    
    /*@RequestMapping(value = "/log-devices", method = RequestMethod.GET)
    public String logDevices() {
    	if(devicesInRange.size() == 0) {
    		return "Failed";
    	}
    	int distance = 0;
    	DeviceLog log ;
    	for(BeaconsInRange device : devicesInRange.get(0).getDevicesInRangeList()) {
    		log = new DeviceLog(device.getAddress(),0,device.getRssi(), device.getTxPower());		
    		log.setId(11);
    		System.out.println("/n/n/n/" + log + "/n/n/n" +log.getId());
    		deviceLogDao.save(log);
    	 
    	}
    	return "success";
    }*/

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
