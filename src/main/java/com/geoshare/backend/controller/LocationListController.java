package com.geoshare.backend.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.geoshare.backend.entity.LocationList;
import com.geoshare.backend.service.LocationListService;

@RestController
@RequestMapping("/api/lists")
public class LocationListController {

	@Autowired
	private LocationListService locationListService;
	
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
	
	@GetMapping("/find")
	public LocationList getList(
			@RequestParam(value = "listid", required = true) Long listID) {
		return locationListService.findByID(listID);
	}
}
