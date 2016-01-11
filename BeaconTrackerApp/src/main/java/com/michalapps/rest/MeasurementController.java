package com.michalapps.rest;

import java.util.ArrayList;
import java.util.Date;
import java.sql.Timestamp;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.michalapps.model.SelectData;
import com.michalapps.model.Measurement;
import com.michalapps.model.MeasurmentDao;

@RestController
public class MeasurementController {

	@Autowired
	MeasurmentDao measurementDao;

    @RequestMapping(value = "/getMeasurements", method = RequestMethod.GET)
    public List<Measurement> getAllMeasurements() {
    	List<Measurement> result = (List<Measurement>) measurementDao.findAll();
    	return result;
    }
    
    @RequestMapping(value = "/getMeasurementsSize", method = RequestMethod.GET)
    public long getMeasurementsSize() {
    	return measurementDao.count();
    }
    
    @RequestMapping(value = "/getDistances", method = RequestMethod.GET)
    public List<SelectData> getDistances() {
    	List<Float> dane = measurementDao.getDistances();
    	List<SelectData> result = new ArrayList<>();
    	for(Float f : dane) {
    		result.add(new SelectData(f, f));
    	}
    	return result;
    }
    
    @RequestMapping(value = "/getTxPowers", method = RequestMethod.GET)
    public List<SelectData> getTxPowers() {
    	List<Integer> dane = measurementDao.getTxPowers();
    	List<SelectData> result = new ArrayList<>();
    	for(Integer f : dane) {
    		result.add(new SelectData(f, f));
    	}
    	return result;
    }
	
    @RequestMapping(value = "/getMeasurementsByDate", params = {"from", "to"}, method = RequestMethod.GET)
    public List<Measurement> getMeasurementsByDate(
        	@RequestParam(value = "from") Timestamp from,
        	@RequestParam(value = "to") Timestamp to) {
    	return measurementDao.getMeasurementsByDateRange(from,to);
    }
    
    @RequestMapping(value = "/getSpecificMeasurements", params = {"from", "to","distance"}, method = RequestMethod.GET)
    public List<Measurement> getSpecificMeasurements(
        	@RequestParam(value = "from") Timestamp from,
        	@RequestParam(value = "to") Timestamp to,
        	@RequestParam(value = "distance") float distance) {
    	return measurementDao.getMeasurementsByDateRange(from,to);
    }
    
}
