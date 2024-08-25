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
import com.geoshare.backend.repository.GeoshareUserRepository;
import com.geoshare.backend.repository.LocationCommentRepository;
import com.geoshare.backend.repository.LocationListRepository;
import com.geoshare.backend.repository.LocationRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class LocationService {
	
	private CountryService countryService;
	private MetaService metaService;
	private LocationRepository locationRepository;
	private GeoshareUserRepository userRepository;
	private LocationListRepository locationListRepository;
	private LocationCommentRepository locationCommentRepository;

	
	public LocationService(
			CountryService countryService,
			MetaService metaService,
			LocationRepository locationRepository, 
			GeoshareUserRepository userRepository,
			LocationListRepository locationListRepository,
			LocationCommentRepository locationCommentRepository) {
		
		this.countryService = countryService;
		this.metaService = metaService;
		this.locationRepository = locationRepository;
		this.userRepository = userRepository;
		this.locationListRepository = locationListRepository;
		this.locationCommentRepository = locationCommentRepository;
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
	
	public Location createLocation(LocationDTO locationDTO) {
		BigDecimal lat, lng;
		String url = locationDTO.url();
		String description = locationDTO.description();
		Long userID = locationDTO.userID();
		
		BigDecimal[] coords = parseLatAndLong(url);
		lat = coords[0];
		lng = coords[1];
		
		Country country = countryService.findCountry(locationDTO.countryName());
		Meta meta = metaService.findMeta(locationDTO.meta());
		GeoshareUser geoshareUser = userRepository.findByIDOrThrow(userID);
		
		Location location = new Location(
				url,
				lat,
				lng,
				description,
				country,
				geoshareUser,
				meta);
		

		//Maybe check lat and lng to see if this user has saved this loc already?
		return locationRepository.save(location);
		
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
	
	
	public void deleteLocation(Collection<Long> locationIDs, Authentication auth) {
		
		List<Location> locations = locationRepository.findAllById(locationIDs);
		
		if (!HelperService.userOwns(auth, locations)) {
			throw new AccessDeniedException("User: " + auth.getName() + " does not own at least one of the given locations.");
		}
		
		for (Location location : locations) {
			
			//Handle removing this location from all associated LocationLists
			Collection<LocationList> listsThisLocationIsPartOf = locationListRepository.findAllByLocationID(location.getId());
			for(LocationList locationList : listsThisLocationIsPartOf) {
				locationList.removeFromLocations(location);
			}
			
			//Delete all comments on this Location
			//TODO
			//Maybe archive them?
			Collection<LocationComment> locationComments = locationCommentRepository.findAllByLocation(location.getId());
			for (LocationComment comment : locationComments) {
				comment.setParentComment(null); //Remove all dependencies between these comments
			}
			locationCommentRepository.saveAll(locationComments); //Update with removed dependencies
			locationCommentRepository.deleteAll(locationComments); //Delete all associated comments
			
			//Finally delete the Location
			locationRepository.delete(location);
		}
	}
	
	
	public LocationDTO updateLocation(LocationDTO locationDTO, Authentication auth) {
		
		Location location = locationRepository.findByIDOrThrow(locationDTO.id());
		
		if (!HelperService.userOwns(auth, List.of(location))) {
			throw new AccessDeniedException("User " + auth.getName() + " does not own this location.");
		}
		
		Country newCountry = countryService.findCountry(locationDTO.countryName());
		Meta newMeta = metaService.findMeta(locationDTO.meta());
		
		location.setUrl(locationDTO.url());
		location.setCountry(newCountry);
		location.setMeta(newMeta);
		location.setDescription(locationDTO.description());
		
		return DTOMapper.mapLocationDTO(locationRepository.save(location));
		
	}
	
}
