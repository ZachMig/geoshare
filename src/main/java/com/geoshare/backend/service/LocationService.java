package com.geoshare.backend.service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.geoshare.backend.dto.LocationDTO;
import com.geoshare.backend.entity.Location;
import com.geoshare.backend.repository.LocationRepository;

@Service
public class LocationService {
	
	@Autowired
	private LocationRepository locationRepository;
	
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
		return locationRepository.findByLocationID(id);
	}
	
	public void createLocation(LocationDTO locationDTO) {
		BigDecimal lat, lng;
		String url = locationDTO.getUrl();
		String description = locationDTO.getDescription();
		Long countryID = locationDTO.getCountryID();
		
	}
	
	
}
