package com.michalapps.blebroker;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

/**
 * Created by User on 2015-11-17.
 */
public class RowViewHolder extends RecyclerView.ViewHolder{

    private View view;
    private TextView address;
    private TextView rssi;

    public RowViewHolder(View itemView) {
        super(itemView);
        view = itemView;
        address = (TextView) itemView.findViewById(R.id.address_field);
        rssi = (TextView) itemView.findViewById(R.id.rssi_field);
    }

    public View getView() {
        return view;
    }

    public void setView(View view) {
        this.view = view;
    }

    public TextView getAddress() {
        return address;
    }

    public void setAddress(TextView address) {
        this.address = address;
    }

    public TextView getRssi() {
        return rssi;
    }

    public void setRssi(TextView rssi) {
        this.rssi = rssi;
    }
}
