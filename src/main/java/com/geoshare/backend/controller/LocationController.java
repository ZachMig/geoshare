package com.geoshare.backend.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
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
	
	@DeleteMapping("/delete")
	public ResponseEntity<String> deleteLocation(
			@RequestParam(value = "lid", required = true) Long locationID,
			Authentication auth) {
		
		String username = auth.getName();
		
		if (locationService.userOwnsLocation(username, locationID)) {
			
			try {
				locationService.deleteLocation(locationID);
				return new ResponseEntity<>("Delete request ran with no errors.", HttpStatus.OK);
			} catch (Exception e) {
				return new ResponseEntity<>("Erorr attempting to delete location: " + e.getMessage(), HttpStatus.BAD_REQUEST);
			}
			
		} else { //This user does not own this location they are trying to delete
			return new ResponseEntity<>("Logged in user does not own this location.", HttpStatus.FORBIDDEN);
		}
		
	}
	
}
