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
import com.geoshare.backend.repository.CountryRepository;
import com.geoshare.backend.repository.GeoshareUserRepository;
import com.geoshare.backend.repository.LocationRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class LocationService {
	
	private LocationRepository locationRepository;
	private CountryRepository countryRepository;
	private GeoshareUserRepository userRepository;
	
	public LocationService(LocationRepository locationRepository, CountryRepository countryRepository,
			GeoshareUserRepository userRepository) {
		this.locationRepository = locationRepository;
		this.countryRepository = countryRepository;
		this.userRepository = userRepository;
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
		Long countryID = locationDTO.countryID();
		Long userID = locationDTO.userID();
		
		BigDecimal[] coords = parseLatAndLong(url);
		lat = coords[0];
		lng = coords[1];
		
		Country country = countryRepository.findByIDOrThrow(countryID);
		GeoshareUser geoshareUser = userRepository.findByIDOrThrow(userID);
		
		Location location = new Location(
				url,
				lat,
				lng,
				description,
				country,
				geoshareUser);
		

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
		
		for (Long locationID : locations) {
			if (!userOwnsLocation(auth, locationID)) {
				throw new AccessDeniedException("User: " + auth.getName() + " does not own location: " + locationID);
			}
		}
		
		for (Long locationID : locations) {
			locationRepository.deleteById(locationID);
		}
	}
	
	
}
