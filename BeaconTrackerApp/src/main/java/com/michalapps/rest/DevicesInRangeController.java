package com.michalapps.rest;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by User on 2015-11-18.
 */
@RestController
public class DevicesInRangeController {

    private List<DevicesInRange> trackedDevices = new ArrayList<>();

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