package com.michalapps.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.michalapps.model.Phone;
import com.michalapps.model.PhoneDao;

@RestController
public class PhoneREST {
	
	@Autowired
    private PhoneDao phoneDao;
    
    @RequestMapping(value = "/phone/register", method = RequestMethod.POST)
    public String postDevicesInRange(@RequestBody Phone phone) {
        System.out.println("\nRegister new phone : " + phone.toString());
        try {
        	phoneDao.save(phone);
        } catch(Exception e) {
        	return "Failed";
        }
        return "Success";
    }

}
