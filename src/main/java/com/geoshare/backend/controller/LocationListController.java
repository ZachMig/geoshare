package com.geoshare.backend.controller;

import java.util.Collection;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.geoshare.backend.dto.LocationListDTO;
import com.geoshare.backend.entity.LocationList;
import com.geoshare.backend.service.LocationListService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/lists")
public class LocationListController {

	private LocationListService locationListService;
	
	public LocationListController(LocationListService locationListService) {
		this.locationListService = locationListService;
	}
	
	@GetMapping("/findall")
	public List<LocationList> getLists(
			@RequestParam(value = "uid", required = false) Long userID,
			@RequestParam(value = "username", required = false) String username)
	{
		
		if (userID != null) {
			return locationListService.findAllByUser(userID);
		}
		
		if (username != null) {
			return locationListService.findAllByUser(username);
		}
		
		throw new IllegalArgumentException("This request requires one search parameter.");
	}
	
	@GetMapping("/findformatted")
	public ResponseEntity<?> getFormattedLists(
			@RequestParam(value = "uname", required = true) String username) {
		
		Collection<LocationListDTO> userLists = locationListService.findFormattedLists(username.toLowerCase().trim());
		
		return new ResponseEntity<>(userLists, HttpStatus.OK);
	}
	
	@GetMapping("/searchlists")
	public ResponseEntity<Collection<LocationListDTO>> searchLists(
			@RequestParam(value = "includes", required = true) String listNameSearchParam) {
		
		Collection<LocationListDTO> matchedSearchedLists = locationListService.findAllByListNameSearchParam(listNameSearchParam);
		return new ResponseEntity<>(matchedSearchedLists, HttpStatus.OK);
		
	}
	
	@GetMapping("/find")
	public ResponseEntity<LocationList> getList(
			@RequestParam(value = "listid", required = false) Long listID,
			@RequestParam(value = "name", required = false) String name) {
		
		if (listID == null) {
			return new ResponseEntity<>(locationListService.findByID(listID), HttpStatus.OK);
		}
		
		if (name == null) {
			return new ResponseEntity<>(locationListService.findByName(name), HttpStatus.OK);
		}
		
		throw new IllegalArgumentException("This request requires one search parameter.");
	}
	
	@PostMapping("/create")
	public ResponseEntity<?> createList(
			@Valid 
			@RequestBody 
			LocationListDTO listDTO,
			Authentication auth) {
		
		locationListService.createList(listDTO, auth);
		return new ResponseEntity<>("List create request handled successfully.", HttpStatus.CREATED);
	}
	
	@DeleteMapping("/delete")
	public ResponseEntity<?> deleteList(
			@RequestParam(value = "listid", required = true) Long listID,
			Authentication auth) {
		
		locationListService.deleteList(listID, auth);
		return new ResponseEntity<>("List delete request handled successfully.", HttpStatus.OK);
	}
	
	@PutMapping("/unlink")
	public ResponseEntity<?> unlinkFromList(
			@RequestParam(value = "listid", required = true) Long listID,
			@RequestBody(required = true) Collection<Long> locationIDs,
			Authentication auth) {
		
		locationListService.unlinkFromList(listID, locationIDs, auth);
		
		return new ResponseEntity<>("Unlink request handled successfully.", HttpStatus.OK);
		
	}
	
	@PutMapping("/update")
	public ResponseEntity<?> updateList(
			@Valid @RequestBody LocationListDTO listDTO,
			Authentication auth) {
		
		locationListService.updateList(listDTO, auth);
		return new ResponseEntity<>("List update request handled successfully.", HttpStatus.OK);
	}
	
	@PutMapping("/add")
	public ResponseEntity<?> addLocationsToList(
			@RequestParam(value = "listid", required = true) Long listID,
			@Valid @RequestBody(required = true) Collection<Long> locations,
			Authentication auth) {

		locationListService.addLocationsToList(listID, locations, auth);
		return new ResponseEntity<>("List add request handled successfully.", HttpStatus.OK);
	}	
}







