package com.geoshare.backend.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.geoshare.backend.entity.LocationComment;
import com.geoshare.backend.repository.LocationCommentRepository;

@Service
public class LocationCommentService {

	@Autowired
	private LocationCommentRepository locationCommentRepository;
	
	List<LocationComment> findAllByLocation(Long locationID) {
		return locationCommentRepository.findAllByLocation(locationID);
	}
	
	List<LocationComment> findAllByUser(Long userID) {
		return locationCommentRepository.findAllByUser(userID);
	}
	
	List<LocationComment> findAllByUser(String username) {
		return locationCommentRepository.findAllByUser(username);
	}
	
	LocationComment findById(Long id) {
		return locationCommentRepository.findByID(id);
	}
	
}
