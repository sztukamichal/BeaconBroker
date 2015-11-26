package com.michalapps.rest;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by User on 2015-11-18.
 */
public class DevicesInRange {

    private List<DeviceInRange> devicesInRangeList ;
    private String deviceModel;
    public DevicesInRange() {
        devicesInRangeList = new ArrayList<>();
        deviceModel = "unknown";
    }

    public DevicesInRange(List<DeviceInRange> devicesInRangeList, String model) {
        deviceModel = model;
        this.devicesInRangeList = devicesInRangeList;
    }

    public String getDeviceModel() {
        return deviceModel;
    }

    public void setDeviceModel(String deviceModel) {
        this.deviceModel = deviceModel;
    }

    public List<DeviceInRange> getDevicesInRangeList() {
        return devicesInRangeList;
    }

    public void setDevicesInRangeList(List<DeviceInRange> devicesInRangeList) {
        this.devicesInRangeList = devicesInRangeList;
    }

    @Override
    public String toString() {
        if (devicesInRangeList.isEmpty()) {
            return "0";
        } else {
            String text = "" + devicesInRangeList.size() + "\n";
            int i = 0;
            for (DeviceInRange dev : devicesInRangeList) {
                i += 1;
                text += i + ". ";
                text += dev.toString();
                text += "\n";
            }
            return text;
        }
    }
}



