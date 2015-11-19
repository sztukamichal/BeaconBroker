/*
package com.michalapps.blebroker;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

import no.nordicsemi.android.support.v18.scanner.ScanResult;

*/
/**
 * Created by User on 2015-11-17.
 *//*

public class BeaconsListAdapter extends BaseAdapter{


    private final List<BeaconDevice> mDevices = new ArrayList<>();

    public void update(final List<ScanResult> results) {
        for (final ScanResult result : results) {
            final BeaconDevice device = findDevice(result);
            if (device == null) {
                mDevices.add(new BeaconDevice(result));
            } else {
                device.setAddress(result.getDevice() != null ? result.getDevice().getAddress() : null);
                device.setRssi(result.getRssi());
            }
        }
        notifyDataSetChanged();
    }

    private BeaconDevice findDevice(final ScanResult result) {
        BeaconDevice resultDevice = new BeaconDevice(result);
        for (final BeaconDevice device : mDevices)
            if (device.equals(resultDevice))
                return device;
        return null;
    }

    public void clearDevices() {
        if (mDevices != null) {
            mDevices.clear();
            notifyDataSetChanged();
        }
    }

    @Override
    public int getCount() {
        return mDevices.size();
    }

    @Override
    public Object getItem(int position) {
        if (position == 0)
            return "Not bonded";
        else
            return mDevices.get(position - 1);
    }

    @Override
    public int getViewTypeCount() {
        return 3;
    }

    @Override
    public boolean areAllItemsEnabled() {
        return false;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View oldView, ViewGroup parent) {
    }
}
*/
