package com.michalapps.rest;
import org.springframework.web.bind.annotation.*;

import com.michalapps.model.DeviceInRange;
import com.michalapps.model.DeviceLog;
import com.michalapps.model.DeviceLogDao;
import com.michalapps.model.DevicesInRange;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by User on 2015-11-18.
 */
@RestController
public class DevicesInRangeController {

    private List<DevicesInRange> trackedDevices = new ArrayList<>();
    private DeviceLogDao deviceLogDao;
    
    @RequestMapping(value = "/devices", method = RequestMethod.POST)
    public String postDevicesInRange(@RequestBody DevicesInRange device) {
        System.out.println(device.toString());
        DevicesInRange found = findTrackedDevice(device);
        if(found != null) {
            found.setDevicesInRangeList(device.getDevicesInRangeList());
        } else {
            trackedDevices.add(new DevicesInRange(device.getDevicesInRangeList(), device.getDeviceModel()));
        }
        return device.toString();
    }

    @RequestMapping(value = "/devices", method = RequestMethod.GET)
    public List<DevicesInRange> getTrackedDevices() {
        return trackedDevices;
    }
    
    @RequestMapping(value = "/log-devices", method = RequestMethod.GET)
    public String logDevices() {
    	if(trackedDevices.size() == 0) {
    		return "Failed";
    	}
    	int distance = 0;
    	DeviceLog log ;
    	for(DeviceInRange device : trackedDevices.get(0).getDevicesInRangeList()) {
    		log = new DeviceLog(device.getAddress(),0,device.getRssi(), device.getTxPower());		
    		log.setId(11);
    		System.out.println("/n/n/n/" + log + "/n/n/n" +log.getId());
    		deviceLogDao.save(log);
    	    
    	}
    	return "success";
    }

    @RequestMapping(value = "/device-destroy", method = RequestMethod.POST)
    public String destroyDevice(@RequestBody DevicesInRange device) {
        System.out.println("Destroy " + device.toString());
        DevicesInRange found = findTrackedDevice(device);
        if(found != null) {
            trackedDevices.remove(found);
        } else {
            System.out.println("Nie znaleziono takiego urządzenia");
        }
        return device.toString();
    }


    private DevicesInRange findTrackedDevice(DevicesInRange trackedDevice) {
        for(DevicesInRange device : trackedDevices) {
            if(device.getDeviceModel().equals(trackedDevice.getDeviceModel())) {
                return device;
            }
        }
        return null;
    }

}
