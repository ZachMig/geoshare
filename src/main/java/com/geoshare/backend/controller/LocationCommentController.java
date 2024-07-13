package com.geoshare.backend.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.geoshare.backend.dto.LocationCommentDTO;
import com.geoshare.backend.entity.LocationComment;
import com.geoshare.backend.service.LocationCommentService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/locationcomments")
public class LocationCommentController {

	@Autowired
	private LocationCommentService locationCommentService;
	
	@GetMapping("/findall") 
	public ResponseEntity<List<LocationComment>> getLocationComments(
			@RequestParam(value = "uid", required = false) Long userID,
			@RequestParam(value = "uname", required = false) String username,
			@RequestParam(value = "locationid", required = false) Long locationID) {
		
		return new ResponseEntity<>(locationCommentService.findAllBy(userID, username, locationID), HttpStatus.OK);
	}
	
	@GetMapping ("/find")
	public ResponseEntity<LocationComment> getLocationComment(
			@RequestParam(value = "cid", required = true) Long locationCommentID) {
		
		return new ResponseEntity<>(locationCommentService.findById(locationCommentID), HttpStatus.OK);
	}
	
	
	@PostMapping("/create") 
	public ResponseEntity<String> createLocationComment(
			@Valid
			@RequestBody
			LocationCommentDTO commentDTO,
			Authentication auth) {
		
		locationCommentService.createComment(commentDTO, auth);
		return new ResponseEntity<>("Location Comment create request handled successfully.", HttpStatus.OK);
	}
			
	
}
