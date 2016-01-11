package com.michalapps.model;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface MeasurmentDao extends CrudRepository<Measurement, Long>{
			
	
	@Query("FROM Measurement where time >= ?1 and time <= ?2")
	List<Measurement> getMeasurementsByDateRange(Timestamp from, Timestamp to);
	
	@Query("SELECT m.distance FROM Measurement m GROUP BY distance")
	List<Float> getDistances();
	
	@Query("SELECT m.txPower FROM Measurement m GROUP BY txPower")
	List<Integer> getTxPowers();
	
	@Query("SELECT m.beaconId FROM Measurement m GROUP BY beaconId")
	List<String> getBeacons();
	
}
