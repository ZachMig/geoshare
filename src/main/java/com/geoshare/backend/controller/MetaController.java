package com.geoshare.backend.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.geoshare.backend.entity.Meta;
import com.geoshare.backend.service.MetaService;

@RestController
@RequestMapping("/api/metas")
public class MetaController {

	@Autowired
	private MetaService metaService;
	
	@GetMapping("/find")
	public Meta getMeta(
			@RequestParam(value="metaid", required = true) Long metaID) {
		return metaService.findByID(metaID);
	}
	
	@GetMapping("/findall")
	public List<Meta> getAllMetas() {
		return metaService.findAll();
	}
	
}


