package com.michalapps.blebroker;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by User on 2015-11-17.
 */
public class RecyclerViewAdapter extends RecyclerView.Adapter<RowViewHolder> {

    private List<BeaconDevice> beaconsDevice = new ArrayList<>();

    public RecyclerViewAdapter(List<BeaconDevice> beacons) {
        this.beaconsDevice = beacons;
    }

    @Override
    public RowViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        Log.v("DEBAGG", "jestem tu");
        View view = inflater.inflate(R.layout.single_row, null);
        RowViewHolder viewHolder = new RowViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RowViewHolder holder, int position) {
        BeaconDevice device = beaconsDevice.get(position);
        holder.getAddress().setText(device.getAddress());
        holder.getRssi().setText("RSSI : "+device.getRssi());
    }

    @Override
    public int getItemCount() {
        return beaconsDevice.size();
    }
}
