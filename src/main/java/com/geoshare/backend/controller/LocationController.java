package com.geoshare.backend.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.geoshare.backend.dto.LocationDTO;
import com.geoshare.backend.entity.Location;
import com.geoshare.backend.service.LocationService;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@Slf4j
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
	
	
	/**
	 * Make sure the app passes a trimmed location to this API
	 *  without https://www etc. Should just start with 
	 *  google.com/maps/@
	 */
	@PostMapping("/create")
	public HttpStatus createLocation(
			@Valid
			@RequestBody
			LocationDTO locationDTO) {
		
		try {
			locationService.createLocation(locationDTO);
			return HttpStatus.OK;
		} catch (Exception e) {
			log.info(e.getMessage());
			return HttpStatus.BAD_REQUEST;
		}
		
	}
	
}
