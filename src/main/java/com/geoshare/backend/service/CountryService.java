package com.geoshare.backend.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.geoshare.backend.entity.Country;
import com.geoshare.backend.repository.CountryRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class CountryService {
	
	@Autowired
	private CountryRepository countryRepository;
	
	public Country findByID(Long id) {
		Country country = countryRepository.findByIDOrThrow(id);
		
		return country;
	}
	
	public List<Country> findAll() {
		return countryRepository.findAll();
	}
	
}
