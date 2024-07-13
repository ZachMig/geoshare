package com.geoshare.backend.service;

import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.geoshare.backend.dto.LocationCommentDTO;
import com.geoshare.backend.entity.GeoshareUser;
import com.geoshare.backend.entity.Location;
import com.geoshare.backend.entity.LocationComment;
import com.geoshare.backend.repository.GeoshareUserRepository;
import com.geoshare.backend.repository.LocationCommentRepository;
import com.geoshare.backend.repository.LocationRepository;

@Service
public class LocationCommentService {

	private LocationCommentRepository locationCommentRepository;
	private GeoshareUserRepository userRepository;
	private LocationRepository locationRepository;
	
	public LocationCommentService(
			LocationCommentRepository locationCommentRepository,
			GeoshareUserRepository userRepository,
			LocationRepository locationRepository) {
		
		this.locationCommentRepository = locationCommentRepository;
		this.userRepository = userRepository;
		this.locationRepository = locationRepository;
	}
	
	public LocationComment findById(Long id) {
		return locationCommentRepository.findByIDOrThrow(id);
	}
	
	public List<LocationComment> findAllBy(Long userID, String username, Long locationID) {
		
		if (userID != null) {
			return locationCommentRepository.findAllByUser(userID);
		}
		
		if (username != null) {
			return locationCommentRepository.findAllByUser(username);
		}
		
		if (locationID != null) {
			return locationCommentRepository.findAllByLocation(locationID);
		}
		
		throw new IllegalArgumentException("This GET request requires a search parameter.");
	}
	
	
	public void createComment(LocationCommentDTO commentDTO, Authentication auth) {
		
		//Lookup the Location that this comment is commenting on
		Location owningLocation = locationRepository.findByIDOrThrow(commentDTO.locationID());
		
		//Lookup the user based on the authentication object name i.e. username of current logged in user
		GeoshareUser geoUser = userRepository.findByUsernameOrThrow(auth.getName());
		
		//If there is a parent comment, look it up in the DB
		LocationComment parentComment = null;
		if (commentDTO.parentCommentID() != null) {
			parentComment = locationCommentRepository.findByIDOrThrow(commentDTO.parentCommentID());
		}
		
		//Create the new comment to be written from what we have looked up, and what has been passed in
		LocationComment newComment = new LocationComment(
			commentDTO.content(),
			owningLocation,
			geoUser,
			parentComment
		);
		
		//Write the new comment to the database
		locationCommentRepository.save(newComment);
	}
	
}
