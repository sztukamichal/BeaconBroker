package com.michalapps.blebroker;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by User on 2015-11-18.
 */
public class Device {

    private List<BeaconInRange> beaconsInRangeList;
    private String deviceId;
    public Device() {
        beaconsInRangeList = new ArrayList<>();
        deviceId = "unknown";
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public List<BeaconInRange> getBeaconsInRangeList() {
        return beaconsInRangeList;
    }

    public void setBeaconsInRangeList(List<BeaconInRange> beaconsInRangeList) {
        this.beaconsInRangeList = beaconsInRangeList;
    }

    @Override
    public String toString() {
        if (beaconsInRangeList.isEmpty()) {
            return "0";
        } else {
            String text = "" + beaconsInRangeList.size() + "\n";
            int i = 0;
            for (BeaconInRange dev : beaconsInRangeList) {
                i += 1;
                text += i + ". ";
                text += dev.toString();
                text += "\n";
            }
            return text;
        }
    }
}

