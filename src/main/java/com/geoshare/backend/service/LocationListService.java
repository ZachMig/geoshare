package com.geoshare.backend.service;

import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.geoshare.backend.dto.LocationListDTO;
import com.geoshare.backend.entity.GeoshareUser;
import com.geoshare.backend.entity.LocationList;
import com.geoshare.backend.repository.GeoshareUserRepository;
import com.geoshare.backend.repository.LocationListRepository;

@Service
public class LocationListService {

	private LocationListRepository locationListRepository;
	private GeoshareUserRepository userRepository;
	
	public LocationListService(LocationListRepository locationListRepository, GeoshareUserRepository userRepository) {
		this.locationListRepository = locationListRepository;
		this.userRepository = userRepository;
	}

	public List<LocationList> findAllByUser(Long userID) {
		return locationListRepository.findAllByUser(userID);
	}
	
	public List<LocationList> findAllByUser(String username) {
		return locationListRepository.findAllByUser(username);
	}
	
//	public List<LocationList> findTopLiked(Long minLikes) {
//		return locationListRepository.findTopLiked(minLikes);
//	}
	
//	public LocationList findByName(String name) {
//		return locationListRepository.findByName(name);
//	}

	public LocationList findByID(Long listID) {
		return locationListRepository.findByID(listID);
	}
	
	public void createList(LocationListDTO listDTO, Authentication auth) {
		
		GeoshareUser listOwner = userRepository.findByUsername(auth.getName());
		
		LocationList locationList = new LocationList(
				listDTO.name(),
				listDTO.description(),
				listDTO.isPublic(),
				Long.valueOf(0),
				listOwner);
		
		locationListRepository.save(locationList);
	}
	
	public void deleteList(Long listID, Authentication auth) {
		//TODO
	}
	
	public void updateList(Long listID, LocationListDTO listDTO, Authentication auth) {
		//TODO
	}
	
}
