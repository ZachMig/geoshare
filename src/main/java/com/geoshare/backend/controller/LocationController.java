package com.geoshare.backend.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.geoshare.backend.entity.Location;
import com.geoshare.backend.service.LocationService;

@RestController
@RequestMapping("/api/locations")
public class LocationController {
	
	@Autowired
	private LocationService locationService;
	
	@GetMapping("/findall")
	public List<Location> getLocations(
	    @RequestParam(value = "uid", required = false) Long userID,
	    @RequestParam(value = "uname", required = false) String username,
	    @RequestParam(value = "cid", required = false) Long countryID,
	    @RequestParam(value = "cname", required = false) String countryName)
	{
		
		if (userID != null) {
			return locationService.findAllByUser(userID);
		}
		
		if (username != null) {
			return locationService.findAllByUser(username);
		}
		
		if (countryID != null) {
			return locationService.findAllByCountry(countryID);
		}
		
		if (countryName != null) {
			return locationService.findAllByCountry(countryName);
		}
		
		throw new IllegalArgumentException("This request requires one search parameter.");
		
	}
	
	@GetMapping("/find")
	public Location getLocation(
			@RequestParam(value = "lid", required = true) Long locationID) {
		return locationService.findByID(locationID);
	}

}
