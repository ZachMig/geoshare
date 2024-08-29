package com.geoshare.backend.service;

import java.util.Collection;
import java.util.List;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.geoshare.backend.dto.LocationDTO;
import com.geoshare.backend.entity.Country;
import com.geoshare.backend.entity.GeoshareUser;
import com.geoshare.backend.entity.Location;
import com.geoshare.backend.entity.LocationList;
import com.geoshare.backend.entity.Meta;
import com.geoshare.backend.repository.GeoshareUserRepository;
import com.geoshare.backend.repository.LocationListRepository;
import com.geoshare.backend.repository.LocationRepository;
import com.geoshare.backend.service.UrlParserService.ParsedUrlData;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class LocationService {
	
	private CountryService countryService;
	private MetaService metaService;
	private LocationRepository locationRepository;
	private GeoshareUserRepository userRepository;
	private LocationListRepository locationListRepository;
	private UrlParserService urlParserService;

	
	public LocationService(
			CountryService countryService,
			MetaService metaService,
			LocationRepository locationRepository, 
			GeoshareUserRepository userRepository,
			LocationListRepository locationListRepository,
			UrlParserService urlParserService) {
		
		this.countryService = countryService;
		this.metaService = metaService;
		this.locationRepository = locationRepository;
		this.userRepository = userRepository;
		this.locationListRepository = locationListRepository;
		this.urlParserService = urlParserService;
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
		
		ParsedUrlData urlData = urlParserService.parseData(locationDTO.url());
	    
		Long userID = locationDTO.userID();
		
		Country country = countryService.findCountry(locationDTO.countryName()); //Cached
		Meta meta = metaService.findMeta(locationDTO.meta()); //Cached
		GeoshareUser geoshareUser = userRepository.findByIDOrThrow(userID);
		
		Location location = new Location(
				locationDTO.url(),
				locationDTO.description(),
				country,
				geoshareUser,
				meta,
				urlData.getLat(),
				urlData.getLng(),
				urlData.getPitch(),
				urlData.getYaw());
		

		return locationRepository.save(location);
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
			
			//Finally delete the Location
			locationRepository.delete(location);
		}
	}
	
	
	public LocationDTO updateLocation(LocationDTO locationDTO, Authentication auth) {
		
		Location location = locationRepository.findByIDOrThrow(locationDTO.id());
		
		if (!HelperService.userOwns(auth, List.of(location))) {
			throw new AccessDeniedException("User " + auth.getName() + " does not own this location.");
		}
		
//		//Make sure received URL is a valid Google Maps Pattern
//		if (!mapsUrlPattern.matcher(locationDTO.url()).matches()) {
//			throw new IllegalArgumentException("Invalid Google Maps URL given as part of Create Location request.");
//		}
		
		ParsedUrlData urlData = urlParserService.parseData(locationDTO.url());
		
		Country newCountry = countryService.findCountry(locationDTO.countryName());
		Meta newMeta = metaService.findMeta(locationDTO.meta());
		
		location.setUrl(locationDTO.url());
		location.setCountry(newCountry);
		location.setMeta(newMeta);
		location.setDescription(locationDTO.description());
		location.setLat(urlData.getLat());
		location.setLng(urlData.getLng());
		location.setPitch(urlData.getPitch());
		location.setYaw(urlData.getYaw());
		
		return DTOMapper.mapLocationDTO(locationRepository.save(location));
		
	}
	
	
}
