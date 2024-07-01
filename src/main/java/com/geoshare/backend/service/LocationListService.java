package com.geoshare.backend.service;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.geoshare.backend.dto.LocationListDTO;
import com.geoshare.backend.entity.GeoshareUser;
import com.geoshare.backend.entity.Location;
import com.geoshare.backend.entity.LocationList;
import com.geoshare.backend.repository.GeoshareUserRepository;
import com.geoshare.backend.repository.LocationListRepository;
import com.geoshare.backend.repository.LocationRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
public class LocationListService {

	private LocationListRepository locationListRepository;
	private GeoshareUserRepository userRepository;
	private LocationRepository locationRepository;
	
	public LocationListService(
			LocationListRepository locationListRepository,
			GeoshareUserRepository userRepository,
			LocationRepository locationRepository) {
		
		this.locationListRepository = locationListRepository;
		this.userRepository = userRepository;
		this.locationRepository = locationRepository;
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
		if (listOwner == null)  {
			throw new EntityNotFoundException("User not found in database.");
		}
		
		LocationList locationList = new LocationList(
				listDTO.name(),
				listDTO.description(),
				listDTO.isPublic(),
				Long.valueOf(0),
				listOwner);
		
		locationListRepository.save(locationList);
	}
	
	public boolean userOwnsList(Authentication auth, Long listID) {
		
		String nameInDB = locationListRepository.findByID(listID).getUser().getUsername();
		
		if (auth.getName().equals(nameInDB)) {
			return true;
		} else {
			return false;
		}
	}
	
	public boolean userOwnsLocation(Authentication auth, Long locationID) {
		
		String usernameInDB = locationRepository.findByLocationID(locationID).getUser().getUsername();
		
		if (usernameInDB.equals(auth.getName())) {
			return true;
		} else {
			return false;
		}
	}
	
	public void deleteList(Long listID, Authentication auth) {
		
		//Check is requesting user owns this list
		if (!userOwnsList(auth, listID)) {
			throw new AccessDeniedException("User does not own this list.");
		}
		
		LocationList list = locationListRepository.findByID(listID);
		if (list == null) {
			throw new EntityNotFoundException("List not found in database.");
		}
		
		//Remove all locations from this list 
		list.setLocations(new HashSet<Location>());;
		
		//Delete the actual list
		locationListRepository.deleteById(listID);
	
	}
	
	
	public void updateList(Long listID, LocationListDTO listDTO, Authentication auth) {
		
		//Check is requesting user owns this list
		if (!userOwnsList(auth, listID)) {
			throw new AccessDeniedException("User does not own this list.");
		}
		
		LocationList list = locationListRepository.findByID(listID);
		if (list == null) {
			throw new EntityNotFoundException("List not found in database.");
		}
		
		list.setName(listDTO.name());
		list.setDescription(listDTO.description());
		list.setPublic(listDTO.isPublic());
		
		locationListRepository.save(list);
	}
	
	public void addToList(Long listID, Collection<Long> locations, Authentication auth) {
		
		//Check is requesting user owns this list
		if (!userOwnsList(auth, listID)) {
			throw new AccessDeniedException("User does not own this list.");
		}
		
		LocationList list = locationListRepository.findByID(listID);
		if (list == null) {
			throw new EntityNotFoundException("List not found in database.");
		}
		
		
		/**
		 * If you are a potential employer reading this, please know that I am someone
		 *  who prioritizes understandable code over number of lines saved.
		 *  While there are cases for things like this lambda expression approach,
		 *  I will always try to write the most readable code possible. Thank you.
		 * 
		locations.forEach((locationID) -> {
			if(userOwnsLocation(auth, locationID)) {
				list.addToLocations(locationRepository.findById(locationID).get());
			}
		});
		 */
		
		for (Long locationID: locations) {
			if (userOwnsLocation(auth, locationID)) {
				Location location = locationRepository.findById(locationID).get();
				list.addToLocations(location);
			}
		}
		
		locationListRepository.save(list);
	}
	
}
