package com.geoshare.backend.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.geoshare.backend.entity.Meta;
import com.geoshare.backend.service.MetaService;

@RestController
@RequestMapping("/api/metas")
public class MetaController {

	@Autowired
	private MetaService metaService;
	
	@GetMapping("/find/{id}")
	public Meta getMeta(@PathVariable Long id) {
		return metaService.findByID(id);
	}
	
	@GetMapping("/find/all")
	public List<Meta> getAllMetas() {
		return metaService.findAll();
	}
	
	@GetMapping("/healthy")
	public String getHealth() {
		return "healthy";
	}	
}


