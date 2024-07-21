package com.geoshare.backend.service;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.geoshare.backend.dto.LocationDTO;
import com.geoshare.backend.entity.Country;
import com.geoshare.backend.entity.GeoshareUser;
import com.geoshare.backend.entity.Location;
import com.geoshare.backend.entity.LocationComment;
import com.geoshare.backend.entity.LocationList;
import com.geoshare.backend.entity.Meta;
import com.geoshare.backend.repository.CountryRepository;
import com.geoshare.backend.repository.GeoshareUserRepository;
import com.geoshare.backend.repository.LocationCommentRepository;
import com.geoshare.backend.repository.LocationListRepository;
import com.geoshare.backend.repository.LocationRepository;
import com.geoshare.backend.repository.MetaRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class LocationService {
	
	private LocationRepository locationRepository;
	private CountryRepository countryRepository;
	private GeoshareUserRepository userRepository;
	private LocationListRepository locationListRepository;
	private LocationCommentRepository locationCommentRepository;
	private MetaRepository metaRepository;
	
	public LocationService(
			LocationRepository locationRepository, 
			CountryRepository countryRepository,
			GeoshareUserRepository userRepository,
			LocationListRepository locationListRepository,
			LocationCommentRepository locationCommentRepository,
			MetaRepository metaRepository) {
		
		this.locationRepository = locationRepository;
		this.countryRepository = countryRepository;
		this.userRepository = userRepository;
		this.locationListRepository = locationListRepository;
		this.locationCommentRepository = locationCommentRepository;
		this.metaRepository = metaRepository;
	}

	public List<Location> findAllByUser(Long userID) {
		return locationRepository.findAllByUser(userID);
	}
	
	public List<Location> findAllByUser(String username) {
		return locationRepository.findAllByUser(username);
	}
	
	public List<Location> findAllByCountry(Long countryID) {
		return locationRepository.findAllByCountry(countryID);
	}
	
	public List<Location> findAllByCountry(String countryName) {
		return locationRepository.findAllByCountry(countryName);
	}
		
	public Location findByID(Long id) {
		return locationRepository.findByIDOrThrow(id);
	}
	
	public void createLocation(LocationDTO locationDTO) {
		BigDecimal lat, lng;
		String url = locationDTO.url();
		String description = locationDTO.description();
		String countryName = locationDTO.countryName();
		Long userID = locationDTO.userID();
		String metaName = locationDTO.meta();
		
		BigDecimal[] coords = parseLatAndLong(url);
		lat = coords[0];
		lng = coords[1];
		
		Country country = countryRepository.findByNameOrThrow(countryName);
		GeoshareUser geoshareUser = userRepository.findByIDOrThrow(userID);
		Meta meta = metaRepository.findByNameOrThrow(metaName);
		
		Location location = new Location(
				url,
				lat,
				lng,
				description,
				country,
				geoshareUser,
				meta);
		

		//Maybe check lat and lng to see if this user has saved this loc already?
		locationRepository.save(location);
		
	}
	
	private BigDecimal[] parseLatAndLong(String url) {
		String prefix = "google.com/maps/@";
		if (!url.startsWith(prefix)) {
			throw new IllegalArgumentException("Ensure URL starts with google.com/maps/@ and nothing else");
		}
		
		String trimmed = url.substring(prefix.length());
		String[] tokens = trimmed.split(",");
		BigDecimal[] coords = new BigDecimal[2];
		
		try {
			coords[0] = new BigDecimal(tokens[0]);
			coords[1] = new BigDecimal(tokens[1]);
			
			//Not sure if it's correct in Spring to catch stuff like this or just throw?
		} catch (IndexOutOfBoundsException e) {
			log.info("Unable to find both lat and long in passed URL. " + url);
		} catch (NumberFormatException e) {
			log.info("Unable to parse coordinates to BigDecimal. " + coords[0] + " " + coords[1]);
		}
		
		return coords;
	}
	
	
	public boolean userOwnsLocation(Authentication auth, Long locationID) {
		
		String usernameInDB = locationRepository.findByIDOrThrow(locationID).getUser().getUsername();
		
		
		if (usernameInDB.equalsIgnoreCase(auth.getName())) {
			return true;
		} else {
			return false;
		}
	}
	
	
	public void deleteLocation(Collection<Long> locations, Authentication auth) {
		
		//Check that the current user owns each of these locations
		//TODO
		//Look for a better way to do this instead of a bunch of individual SQL queries
		for (Long locationID : locations) {
			if (!userOwnsLocation(auth, locationID)) {
				throw new AccessDeniedException("User: " + auth.getName() + " does not own location: " + locationID);
			}
		}
		
		for (Long locationID : locations) {
			
			Location location = locationRepository.findByIDOrThrow(locationID);
			
			//Handle removing this location from all associated LocationLists
			Collection<LocationList> listsThisLocationIsPartOf = locationListRepository.findAllByLocationID(locationID);
			for(LocationList locationList : listsThisLocationIsPartOf) {
				locationList.removeFromLocations(location);
			}
			
			//Delete all comments on this Location
			//TODO
			//Maybe archive them?
			Collection<LocationComment> locationComments = locationCommentRepository.findAllByLocation(locationID);
			for (LocationComment comment : locationComments) {
				comment.setParentComment(null); //Remove all dependencies between these comments
			}
			locationCommentRepository.saveAll(locationComments); //Update with removed dependencies
			locationCommentRepository.deleteAll(locationComments); //Delete all associated comments
			
			
			
			//Finally delete the Location
			locationRepository.delete(location);
		}
	}
	
	
}
