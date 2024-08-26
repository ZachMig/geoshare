package com.geoshare.backend.service;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.geoshare.backend.dto.LocationDTO;
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
	
	public LocationList findByName(String name) {
		return locationListRepository.findByNameOrThrow(name);
	}

	public LocationList findByID(Long listID) {
		return locationListRepository.findByIDOrThrow(listID);
	}
	
	public Collection<LocationListDTO> findFormattedLists(String username) {
		
		//Pull all the user locations that belong to no lists
		Set<LocationDTO> locs = locationRepository.findUnlistedByUser(username)
				.stream()
				.map(DTOMapper::mapLocationDTO)
				.collect(Collectors.toSet());
		
		//Collect the unlisted locations into a "list" of unlisted
		LocationListDTO unlisted = new LocationListDTO(
				(long) -1,
				"Unlisted",
				"Unlisted",
				locs);
				
				
		//Pull all the user lists with attached locations
		List<LocationListDTO> userLists = locationListRepository.findListsWithLocationsByUser(username)
				.stream()
				.map(DTOMapper::mapListDTO)
				.sorted((a, b) -> a.name().compareTo(b.name()))
				.collect(Collectors.toList());
		
		//Prefix the list of lists with the "unlisted list"
		userLists.add(0, unlisted);
				
		return userLists;
	}
	
	
	public Collection<LocationListDTO> findAllByListNameSearchParam(String listNameSearchParam) {
		Collection<LocationList> foundLists = locationListRepository.findByNameContains(listNameSearchParam);
		
		System.out.println("Found " + foundLists.size() + " lists for input " + listNameSearchParam);
		foundLists.forEach(l -> System.out.println(l.getName()));
		
		Collection<LocationListDTO> foundListsDTO = foundLists
				.stream()
				.map(DTOMapper::mapListDTO)
				.sorted((a, b) -> a.name().compareTo(b.name()))
				.toList();
		
		
		return foundListsDTO;
		
	}
	
	
    /**
     * Creates a new Location List based on the provided DTO.
     *
     * @param listDTO The DTO containing data for creating the LocationList.
     * @param auth    The authentication object representing the logged in user.
     * @throws EntityNotFoundException  if the logged in user does not exist in the database.
     */
	public void createList(LocationListDTO listDTO, Authentication auth) {
		
		GeoshareUser listOwner = userRepository.findByUsernameOrThrow(auth.getName());
		
		LocationList locationList = new LocationList(
				listDTO.name(),
				listDTO.description(),
				listOwner);
		
		locationListRepository.save(locationList);
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
		
		LocationList list = locationListRepository.findByIDOrThrow(listID);
		
		//Check is requesting user owns this list
		if (!HelperService.userOwns(auth, List.of(list))) {
			throw new AccessDeniedException("User does not own this list.");
		}
		
		//If the list we are deleting is the last list each of it's locations belonged to, set the location to unlisted
		Collection<Location> locations = list.getLocations();
		locations.stream()
			.filter(location -> location.getLists().size() == 1)
			.forEach(location -> location.setListed((long) 0));
		
		//Remove all locations from this list, not sure if this step is actually required depending on how JPA works under the hood
		list.setLocations(new HashSet<Location>());
		
		//Delete the actual list
		locationListRepository.deleteById(listID);
	
	}
	
	public void unlinkFromList(Long listID, Collection<Long> locationIDs, Authentication auth) {
		
		LocationList list = locationListRepository.findByIDOrThrow(listID);
		
		if(!HelperService.userOwns(auth, List.of(list))) {
			throw new AccessDeniedException("User attempted to unlink from un-owned list: " + listID);
		}
		
		Collection<Location> locations = locationRepository.findAllById(locationIDs);
		
		if (!HelperService.userOwns(auth, locations)) {
			throw new AccessDeniedException("User attempted to unlink one or more un-owned locations: " + locationIDs);
		}
		
		//Now we know the list and all locations are owned by current user.
		
		//Remove each location from this list and don't forget to check and set unlisted if needed
		locations.forEach(location -> {
			//Remove this location
			list.removeFromLocations(location);
			//Now check if that was the last list for this location, and mark unlisted if so
			if (location.getLists().size() == 1) {
				location.setListed((long)0);				
			}
		});
		
		//Don't forget to save your changes
		locationListRepository.save(list);
		
		//This would not be necessary since LocationList owns the many to many relationship, so 
		//	the above save will handle the unlinking. This save is for marking the locations unlisted.
		locationRepository.saveAll(locations);
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
	public void updateList(LocationListDTO listDTO, Authentication auth) {

		LocationList list = locationListRepository.findByIDOrThrow(listDTO.id());
		
		//Check is requesting user owns this list
		if (!HelperService.userOwns(auth, List.of(list))) {
			throw new AccessDeniedException("User does not own this list.");
		}
		
		list.setName(listDTO.name());
		list.setDescription(listDTO.description());
		
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
	public void addLocationsToList(Long listID, Collection<Long> locationIDs, Authentication auth) {
		
		LocationList list = locationListRepository.findByIDOrThrow(listID);
		Collection<Location> locations = locationRepository.findAllById(locationIDs);
		
		if (!HelperService.userOwns(auth, List.of(list))) {
			throw new AccessDeniedException("User " + auth.getName() + " attempted to modify un-owned list.");
		}
		
		if (!HelperService.userOwns(auth, locations)) {
			throw new AccessDeniedException("User " + auth.getName() + " attempted to modify un-owned location.");
		}

		for (Location location: locations) {
			list.addToLocations(location);
			location.setListed((long) 1);
		}
		
		locationListRepository.save(list);
		locationRepository.saveAll(locations);
	}
	
}
