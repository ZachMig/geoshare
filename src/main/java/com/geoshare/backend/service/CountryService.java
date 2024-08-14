package com.geoshare.backend.service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.geoshare.backend.entity.Country;
import com.geoshare.backend.repository.CountryRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class CountryService {
	
	private CountryRepository countryRepository;
	
	public CountryService(CountryRepository countryRepository) {
		this.countryRepository = countryRepository;
	}
	
	//Return a country by ID
	public Country findCountry(Long id) {
		Country country = mapCountriesByID().get(id);
		
		if (country == null) {
			throw new EntityNotFoundException("Unable to find country by ID: " + id);
		}
		
		return country;
	}
	
	//Return a country by Name
	public Country findCountry(String name) {
		Country country = mapCountriesByName().get(name);
		
		if (country == null) {
			throw new EntityNotFoundException("Unable to find country by name: " + name);
		}
		
		return country;
	}
	
	@Cacheable("countries_list")
	public List<Country> findAll() {
		return countryRepository.findAll();
	}
	
	@Cacheable("countries_map_id")
	public Map<Long, Country> mapCountriesByID() {
		return countryRepository.findAll()
				.stream()
				.collect(Collectors.toMap(c -> c.getID(), c -> c));
	}
	
	@Cacheable("countries_map_name")
    public Map<String, Country> mapCountriesByName() {
        return countryRepository.findAll()
        		.stream()
        		.collect(Collectors.toMap(c -> c.getName(), c -> c));
    }
}
