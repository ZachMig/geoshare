package com.geoshare.backend.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.geoshare.backend.entity.Country;
import com.geoshare.backend.repository.CountryRepository;

@Service
public class CountryService {
	
	@Autowired
	private CountryRepository countryRepository;
	
	public Country findByID(Long id) {
		return countryRepository.findByID(id);
	}
	
	public List<Country> findAll() {
		return countryRepository.findAll();
	}
	
}
