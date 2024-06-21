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
	
	@GetMapping("/find")
	public List<Location> getLocation(
	    @RequestParam(value = "uid", required = false) Long userID,
	    @RequestParam(value = "uname", required = false) String username,
	    @RequestParam(value = "cid", required = false) Long countryID,
	    @RequestParam(value = "cname", required = false) String countryName,
	    @RequestParam(value = "lid", required = false) Long locationID) 
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
		
		if (locationID != null) {
			return List.of(locationService.findByID(locationID));
		}
		
		throw new IllegalArgumentException("This request requires one search parameter.");
		
	}

	
	
	/**
	@GetMapping("/find")
	public List<Location> getLocationByUserID(@RequestParam(value="uid") Long userID) {
		return locationService.findAllByUser(userID);
	}
	
	@GetMapping("/find")
	public List<Location> getLocationByUsername(@RequestParam(value="uname") String username) {
		return locationService.findAllByUser(username);
	}
	
	@GetMapping("/find")
	public List<Location> getLocationByCountryID(@RequestParam(value="cid") Long countryID) {
		return locationService.findAllByCountry(countryID);
	}
	
	@GetMapping("/find")
	public List<Location> getLocationByCountryName(@RequestParam(value="cname") String countryName) {
		return locationService.findAllByCountry(countryName);
	}
	
	@GetMapping("/find")
	public Location getLocationByID(@RequestParam(value="lid") Long locationID) {
		return locationService.findByID(locationID);
	}
	*/
	
}
