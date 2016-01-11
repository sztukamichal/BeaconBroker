package com.michalapps.rest;

import java.util.ArrayList;
import java.util.Date;
import java.sql.Timestamp;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.michalapps.model.Beacon;
import com.michalapps.model.BeaconDao;

@RestController
public class BeaconController {

	@Autowired
	BeaconDao beaconDao;

    @RequestMapping(value = "/getBeacons", method = RequestMethod.GET)
    public List<Beacon> getBeacons() {
    	List<Beacon> result = (List<Beacon>) beaconDao.findAll();
    	return result;
    }
	
    @RequestMapping(value = "/saveBeacon", method = RequestMethod.POST)
    public String saveBeacon(@RequestBody Beacon beacon) {
    	List<Beacon> result = (List<Beacon>) beaconDao.findAll();
    	for(Beacon b : result) {
    		if(b.getAddress().equals(beacon.getAddress())) {
    			beaconDao.delete(b);
    		}
    	}
    	beaconDao.save(beacon);
		return "{\"status\":\"success\"}";
    }
    
    @RequestMapping(value = "/saveConfiguration", method = RequestMethod.POST)
    public String saveConfiguration(@RequestBody List<Beacon> beacons) {
    	List<Beacon> result = (List<Beacon>) beaconDao.findAll();
		for(Beacon b : result) {
    		beaconDao.delete(b);
		}
    	for(Beacon n : beacons){
        	beaconDao.save(n);
        	System.out.println("ZAPISANE");
    	}
		return "{\"status\":\"success\"}";
    }
    
}
