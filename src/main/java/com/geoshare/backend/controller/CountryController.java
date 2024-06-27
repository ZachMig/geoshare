package com.geoshare.backend.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.geoshare.backend.entity.Country;
import com.geoshare.backend.service.CountryService;

@RestController
@RequestMapping("/api/countries")
public class CountryController {

	private CountryService countryService;

	public CountryController(CountryService countryService) {
		this.countryService = countryService;
	}
	
	@GetMapping("/find")
	public Country getCountry(
			@RequestParam(value = "countryID", required = true)Long countryID) {
		return countryService.findByID(countryID);
	}
	
	@GetMapping("/findall")
	public List<Country> getAllCountries() {
		return countryService.findAll();
	}
	
}
