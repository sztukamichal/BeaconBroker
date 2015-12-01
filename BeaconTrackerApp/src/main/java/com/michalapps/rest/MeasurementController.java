package com.michalapps.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

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
	
}
