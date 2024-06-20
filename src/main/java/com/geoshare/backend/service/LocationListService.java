package com.geoshare.backend.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.geoshare.backend.entity.LocationList;
import com.geoshare.backend.repository.LocationListRepository;

@Service
public class LocationListService {

	@Autowired
	private LocationListRepository locationListRepository;
	
	List<LocationList> findAllByUser(Long userID) {
		return locationListRepository.findAllByUser(userID);
	}
	
	List<LocationList> findAllByUser(String username) {
		return locationListRepository.findAllByUser(username);
	}
	
	List<LocationList> findTopLiked(Long minLikes) {
		return locationListRepository.findTopLiked(minLikes);
	}
	
	LocationList findByName(String name) {
		return locationListRepository.findByName(name);
	}
	
}
