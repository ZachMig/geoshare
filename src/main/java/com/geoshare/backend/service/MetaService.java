package com.geoshare.backend.service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.geoshare.backend.entity.Meta;
import com.geoshare.backend.repository.MetaRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
public class MetaService {

	private MetaRepository metaRepository;
	
	public MetaService(MetaRepository metaRepository) {
		this.metaRepository = metaRepository;
	}

	//Return a meta by ID
	public Meta findMeta(Long id) {
		Meta meta = mapMetasByID().get(id);
		
		if (meta == null) {
			throw new EntityNotFoundException("Unable to find meta by ID: " + id);
		}
		
		return meta;
	}
	
	//Return a meta by Name
	public Meta findMeta(String name) {
		Meta meta = mapMetasByName().get(name);
		
		if (meta == null) {
			throw new EntityNotFoundException("Unable to find meta by name: " + name);
		}
		
		return meta;
	}
	
	@Cacheable("metas_list")
	public List<Meta> findAll() {
		return metaRepository.findAll();
	}
	
	@Cacheable("metas_map_id")
    public Map<Long, Meta> mapMetasByID() {
        return metaRepository.findAll()
        		.stream()
        		.collect(Collectors.toMap(m -> m.getID(), m -> m));
    }
	
	@Cacheable("metas_map_name")
    public Map<String, Meta> mapMetasByName() {
        return metaRepository.findAll()
        		.stream()
        		.collect(Collectors.toMap(m -> m.getName(), m -> m));
    }
}
