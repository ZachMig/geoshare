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

import com.geoshare.backend.dto.ListContainer;
import com.geoshare.backend.dto.LocationListDTO;
import com.geoshare.backend.entity.Location;
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
			@RequestParam(value = "uname", required = true) String username,
			Authentication auth) {
		
		Collection<LocationListDTO> allLists = locationListService.findFormattedLists(username, auth);
		
		return new ResponseEntity<>(allLists, HttpStatus.OK);
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
	
	@PutMapping("/update")
	public ResponseEntity<?> updateList(
			@RequestParam(value = "listid", required = true) Long listID,
			@Valid @RequestBody LocationListDTO listDTO,
			Authentication auth) {
		
		locationListService.updateList(listID, listDTO, auth);
		return new ResponseEntity<>("List update request handled successfully.", HttpStatus.OK);
	}
	
	@PutMapping("/add")
	public ResponseEntity<?> addLocationsToLists(
			@Valid @RequestBody 
			Collection<Long> lists,
			Collection<Long> locations,
			Authentication auth) {

		locationListService.addLocationsToLists(lists, locations, auth);
		return new ResponseEntity<>("List add request handled successfully.", HttpStatus.OK);
	}
	
	@PutMapping("/remove")
	public ResponseEntity<?> removeFromList(
			@RequestParam(value = "listid", required = true) Long listID,
			@Valid @RequestBody Collection<Long> locations,
			Authentication auth) {
		
		locationListService.removeFromList(listID, locations, auth);
		return new ResponseEntity<>("List remove request handled successfully.", HttpStatus.OK);
	}
	
}







