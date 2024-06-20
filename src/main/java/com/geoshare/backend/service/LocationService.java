package com.geoshare.backend.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.geoshare.backend.entity.Location;
import com.geoshare.backend.repository.LocationRepository;

@Service
public class LocationService {
	
	@Autowired
	private LocationRepository locationRepository;
	
	List<Location> findAllByUser(Long userID) {
		return locationRepository.findAllByUser(userID);
	}
	
	List<Location> findAllByUser(String username) {
		return locationRepository.findAllByUser(username);
	}
	
	List<Location> findAllByCountry(Long countryID) {
		return locationRepository.findAllByCountry(countryID);
	}
	
	List<Location> findAllByCountry(String countryName) {
		return locationRepository.findAllByCountry(countryName);
	}
		
	Location findByID(Long id) {
		return locationRepository.findByLocationID(id);
	}
	
	
}
