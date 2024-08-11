package com.geoshare.backend.controller;

import java.util.Collection;
import java.util.List;

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
import com.geoshare.backend.service.LocationListService;
import com.geoshare.backend.service.LocationService;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/locations")
public class LocationController {
	
	private LocationService locationService;
	private LocationListService locationListService;
	
	public LocationController(LocationService locationService, LocationListService locationListService) {
		this.locationService = locationService;
		this.locationListService = locationListService;
	}
	
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
	public ResponseEntity<String> createLocation(
			@Valid
			@RequestBody
			LocationDTO locationDTO,
			Authentication auth) {
		
		Location createdLocation = locationService.createLocation(locationDTO);
		locationListService.addLocationsToLists(locationDTO.listIDs(), List.of(createdLocation.getId()), auth);
		
		return new ResponseEntity<>("Create request ran with no errors.", HttpStatus.OK);
		
	}
	
	@DeleteMapping("/delete")
	public ResponseEntity<String> deleteLocation(
			@Valid @RequestBody(required = true) Collection<Long> locations,
			Authentication auth) {
	
		locationService.deleteLocation(locations, auth);
		return new ResponseEntity<>("Delete request ran with no errors.", HttpStatus.OK);
	
		
	}
	
}
