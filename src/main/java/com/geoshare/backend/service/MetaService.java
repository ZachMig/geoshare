package com.geoshare.backend.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.geoshare.backend.entity.Meta;
import com.geoshare.backend.repository.MetaRepository;

@Service
public class MetaService {

	@Autowired
	private MetaRepository metaRepository;
	
	public List<Meta> findAll() {
		return metaRepository.findAll();
	}
}
