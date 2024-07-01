package com.geoshare.backend.service;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

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

/**
 * Service layer class for managing LocationList Entities.
 * To be used by Controller layer classes for any logic involving LocationList entities such as CRUD or other.
 *
 * @author Zach Migliorini
 */
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
		return locationListRepository.findByIDOrThrow(listID);
	}
	
    /**
     * Creates a new Location List based on the provided DTO.
     *
     * @param listDTO The DTO containing data for creating the LocationList.
     * @param auth    The authentication object representing the logged in user.
     * @throws EntityNotFoundException  if the logged in user does not exist in the database.
     */
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
	
    /**
     * Checks if the logged in user owns the LocationList identified by listID.
     *
     * @param auth       The authentication object representing the logged in user.
     * @param list The ID of the Location to check ownership of.
     * @return true if the logged in user owns the LocationList, otherwise false.
     * @throws AccessDeniedException if the logged in user does not own the specified LocationList.
     * @throws EntityNotFoundException if the LocationList with listID was not found in the database.
     */
	public boolean userOwnsList(Authentication auth, Long listID) {
		
		LocationList list = locationListRepository.findByIDOrThrow(listID);
		
		String usernameInDB = locationListRepository.findByIDOrThrow(listID).getUser().getUsername();
		
		if (auth.getName().equals(usernameInDB)) {
			return true;
		} else {
			return false;
		}
	}
	
    /**
     * Checks if the logged in user owns the Location identified by locationID.
     *
     * @param auth       The authentication object representing the logged in user.
     * @param locationID The ID of the Location to check ownership of.
     * @return true if the logged in user owns the Location, otherwise false.
     * @throws AccessDeniedException if the logged in user does not own the specified Location.
     * @throws EntityNotFoundException if the Location with locationID was not found in the database.
     */
	public boolean userOwnsLocation(Authentication auth, Long locationID) {
		
		String usernameInDB = locationRepository.findByIDOrThrow(locationID).getUser().getUsername();
		
		if (usernameInDB.equals(auth.getName())) {
			return true;
		} else {
			return false;
		}
	}
	
    /**
     * Deletes a LocationList identified by listID. Associated Locations are not deleted but 
     * 	the relationship between the Location and LocationList is.
     *
     * @param listID The ID of the LocationList to delete.
     * @param auth   The authentication object representing the current logged in user.
     * @throws AccessDeniedException if the logged in user does not own the specified LocationList.
     * @throws EntityNotFoundException if the LocationList with listID was not found in the database.
     */
	public void deleteList(Long listID, Authentication auth) {
		
		//Check is requesting user owns this list
		if (!userOwnsList(auth, listID)) {
			throw new AccessDeniedException("User does not own this list.");
		}
		
		LocationList list = locationListRepository.findByIDOrThrow(listID);
		
		//Remove all locations from this list, not sure if this step is actually required depending on how JPA works under the hood
		list.setLocations(new HashSet<Location>());;
		
		//Delete the actual list
		locationListRepository.deleteById(listID);
	
	}
	
	/**
     * Updates a LocationList identified by listID with data from listDTO.
     *
     * @param listID  The ID of the LocationList to update.
     * @param listDTO The DTO containing updated data for the LocationList.
     * @param auth    The authentication object representing the current logged in user.
     * @throws AccessDeniedException if the logged in user does not own the specified LocationList.
     * @throws EntityNotFoundException if the LocationList with listID was not found in the database.
     */
	public void updateList(Long listID, LocationListDTO listDTO, Authentication auth) {
		
		//Check is requesting user owns this list
		if (!userOwnsList(auth, listID)) {
			throw new AccessDeniedException("User does not own this list.");
		}
		
		LocationList list = locationListRepository.findByIDOrThrow(listID);
		
		list.setName(listDTO.name());
		list.setDescription(listDTO.description());
		list.setPublic(listDTO.isPublic());
		
		locationListRepository.save(list);
	}
	
	
    /**
     * Adds Locations to a LocationList identified by listID.
     *
     * @param listID    The ID of the LocationList to add Locations to.
     * @param locations The collection of Location IDs to add to the LocationList.
     * @param auth      The authentication object representing the current user.
     * @throws AccessDeniedException if the logged in user does not own the specified list or location.
     * @throws EntityNotFoundException if the Location List with listID was not found in the database,
     *                                 or if any of the Locations were not found in the database.
     */
	public void addToList(Long listID, Collection<Long> locations, Authentication auth) {
		
		//Check is requesting user owns this list
		if (!userOwnsList(auth, listID)) {
			throw new AccessDeniedException("User does not own this list.");
		}
		
		LocationList list = locationListRepository.findByIDOrThrow(listID);
		
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
				Optional<Location> location = locationRepository.findById(locationID);
				if (location.isEmpty()) {
					throw new EntityNotFoundException("Location identified by id: " + locationID + " not found in database.");
				}
				list.addToLocations(location.get());
			}
		}
		
		locationListRepository.save(list);
	}
	
	
    /**
     * Removes locations from a LocationList identified by listID.
     *
     * @param listID    The ID of the LocationList to remove Locations from.
     * @param locations The collection of Location IDs to remove from the LocationList.
     * @param auth      The authentication object representing the current user.
     * @throws AccessDeniedException if the logged in user does not own this LocationList.
     * @throws EntityNotFoundException if the LocationList identified by listID was not found in the database,
     * 			or if one of the Locations identified by locations was not found in the database.
     */
	public void removeFromList(Long listID, Collection<Long> locations, Authentication auth) {
		
		//Check is requesting user owns this list
		if (!userOwnsList(auth, listID)) {
			throw new AccessDeniedException("User does not own this list.");
		}
		
		LocationList list = locationListRepository.findByIDOrThrow(listID);
		
		for (Long locationID: locations) {
			//Should never be the case that the location is not owned by the user
			Location location = locationRepository.findById(locationID).get();
			if (location == null) {
				throw new EntityNotFoundException("Location indicated by id: " + locationID + " was not found in database.");
			}
			list.removeFromLocations(location);
		}
		
		locationListRepository.save(list);
		
	}
	
}
