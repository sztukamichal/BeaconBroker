package rest;

import org.springframework.core.env.SystemEnvironmentPropertySource;
import org.springframework.mobile.device.DevicePlatform;
import org.springframework.web.bind.annotation.*;
import org.springframework.mobile.device.Device;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by User on 2015-11-18.
 */
@RestController
public class DevicesInRangeController {

    private DevicesInRange devicesInRange = new DevicesInRange();

    @RequestMapping(value = "/devices", method = RequestMethod.POST)
    public String postDevicesInRange(@RequestBody DevicesInRange devices) {
        System.out.println(devices.toString());
        devicesInRange.setDevicesInRangeList(devices.getDevicesInRangeList());
        devicesInRange.setDeviceModel(devices.getDeviceModel());
        return devicesInRange.toString();
    }

    @RequestMapping(value = "/devices", method = RequestMethod.GET)
    public DevicesInRange getDevicesInRange() {
        return devicesInRange;
    }

    @RequestMapping(value = "device-type", method = RequestMethod.GET)
    public @ResponseBody String detectDevice(Device device) {
        String deviceType = "unknown";
        if(device.isNormal()) {
            deviceType = "normal";
        } else if(device.isMobile()) {
            deviceType = "mobile";
        } else if(device.isTablet()) {
            deviceType = "tablet";
        }
        return "Hello " + deviceType + " browser! " + device.toString();

    }


}
