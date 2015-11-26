package com.michalapps.model;

import org.springframework.data.repository.CrudRepository;

import com.michalapps.model.DeviceLog;

public interface DeviceLogDao extends CrudRepository<DeviceLog, Long>{
}