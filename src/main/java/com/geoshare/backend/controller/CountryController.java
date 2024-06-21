package com.geoshare.backend.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.geoshare.backend.entity.Country;
import com.geoshare.backend.service.CountryService;

@RestController
@RequestMapping("/api/countries")
public class CountryController {

	@Autowired
	private CountryService countryService;
	
	@GetMapping("/find/{id}")
	public Country getCountry(@PathVariable Long id) {
		return countryService.findByID(id);
	}
	
	@GetMapping("/find/all")
	public List<Country> getAllCountries() {
		return countryService.findAll();
	}
	
}
