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
import com.geoshare.backend.entity.ListComment;
import com.geoshare.backend.entity.Location;
import com.geoshare.backend.entity.LocationList;
import com.geoshare.backend.repository.GeoshareUserRepository;
import com.geoshare.backend.repository.ListCommentRepository;
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
	private ListCommentRepository listCommentRepository;
	
	public LocationListService(
			LocationListRepository locationListRepository,
			GeoshareUserRepository userRepository,
			LocationRepository locationRepository,
			ListCommentRepository listCommentRepository) {
		
		this.locationListRepository = locationListRepository;
		this.userRepository = userRepository;
		this.locationRepository = locationRepository;
		this.listCommentRepository = listCommentRepository;
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
	
	public Collection<LocationListDTO> findFormattedLists(String username, Authentication auth) {
		
		//TODO
		//If requested lists are private and not owned by logged in user, do not return them?
		//or remove private/public thing it's probably not needed
		
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
				true,
				locs);
				
		
		locationListRepository.findListsWithLocationsByUser(username).forEach(list -> System.out.println(list.getName()));
		
		//Pull all the user lists with attached locations
		List<LocationListDTO> allLists = locationListRepository.findListsWithLocationsByUser(username)
				.stream()
				.map(DTOMapper::mapListDTO)
				.sorted((a, b) -> a.name().compareTo(b.name()))
				.collect(Collectors.toList());
		
		//Prefix the list of lists with the "unlisted list"
		allLists.add(0, unlisted);
		
		allLists.forEach(list -> System.out.println(list.name()));
		
		return allLists;
		
		
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
				listDTO.isPublic(),
				Long.valueOf(0),
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
		
		//Remove all locations from this list, not sure if this step is actually required depending on how JPA works under the hood
		list.setLocations(new HashSet<Location>());
		
		//Delete all comments associated with this LocationList
		Collection<ListComment> listComments = listCommentRepository.findAllByList(listID);
		for (ListComment comment : listComments) {
			comment.setParentComment(null); //Remove all dependencies between these comments
		}
		listCommentRepository.saveAll(listComments); //Update with removed dependencies
		listCommentRepository.deleteAll(listComments); //Delete all associated comments
		 
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

		LocationList list = locationListRepository.findByIDOrThrow(listID);
		
		//Check is requesting user owns this list
		if (!HelperService.userOwns(auth, List.of(list))) {
			throw new AccessDeniedException("User does not own this list.");
		}
		
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
	public void addLocationsToLists(Collection<Long> listIDs, Collection<Long> locationIDs, Authentication auth) {
		
		Collection<LocationList> lists = locationListRepository.findAllById(listIDs);
		Collection<Location> locations = locationRepository.findAllById(locationIDs);
		
		if (!HelperService.userOwns(auth, lists)) {
			throw new AccessDeniedException("User " + auth.getName() + " attempted to modify un-owned list.");
		}
		
		if (!HelperService.userOwns(auth, locations)) {
			throw new AccessDeniedException("User " + auth.getName() + " attempted to modify un-owned location.");
		}
		
		for (LocationList list : lists) {
			for (Location location: locations) {
				list.addToLocations(location);
				location.setListed((long) 1);
			}
		}
		
		locationListRepository.saveAll(lists);
		locationRepository.saveAll(locations);
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
		
		LocationList list = locationListRepository.findByIDOrThrow(listID);

		//Check is requesting user owns this list
		if (!HelperService.userOwns(auth, List.of(list))) {
			throw new AccessDeniedException("User does not own this list.");
		}
		
		
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
