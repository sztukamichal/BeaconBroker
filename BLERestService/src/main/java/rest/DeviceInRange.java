package rest;

/**
 * Created by User on 2015-11-18.
 */
public class DeviceInRange {

    private int rssi;
    private String address;

    public DeviceInRange() {
    }

    public DeviceInRange(int rssi, String address) {
        this.rssi = rssi;
        this.address = address;
    }

    public int getRssi() {
        return rssi;
    }

    public void setRssi(int rssi) {
        this.rssi = rssi;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public String toString() {
        return "DeviceInRange{" +
                "rssi=" + rssi +
                ", address='" + address + '\'' +
                '}';
    }
}
