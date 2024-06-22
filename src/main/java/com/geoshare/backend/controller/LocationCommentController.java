package com.geoshare.backend.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.geoshare.backend.entity.LocationComment;
import com.geoshare.backend.service.LocationCommentService;

@RestController
@RequestMapping("/api/locationcomments")
public class LocationCommentController {

	@Autowired
	private LocationCommentService locationCommentService;
	
	@GetMapping("/findall") 
	public List<LocationComment> getLocationComments(
			@RequestParam(value = "uid", required = false) Long userID,
			@RequestParam(value = "uname", required = false) String username,
			@RequestParam(value = "locationid", required = false) Long locationID)
	{
		
		if (userID != null) {
			return locationCommentService.findAllByUser(userID);
		}
		
		if (username != null) {
			return locationCommentService.findAllByUser(username);
		}
		
		if (locationID != null) {
			return locationCommentService.findAllByLocation(locationID);
		}
		
		throw new IllegalArgumentException("This request requires one search parameter.");

	}
	
	@GetMapping ("/find")
	public LocationComment getLocationComment(
			@RequestParam(value = "cid", required = true) Long locationCommentID) {
		return locationCommentService.findById(locationCommentID);
	}
	
}
