package com.geoshare.backend.service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.geoshare.backend.dto.LocationDTO;
import com.geoshare.backend.dto.LocationListDTO;
import com.geoshare.backend.entity.Location;
import com.geoshare.backend.entity.LocationList;

public class DTOMapper {

	public static LocationDTO mapLocationDTO(Location location) {
		
		return new LocationDTO(
				location.getUrl(),
				location.getDescription(),
				location.getCountry().getID(),
				location.getUser().getId()
		);
		
	}
	
	public static LocationListDTO mapListDTO(LocationList locationList) {
		
		Set<LocationDTO> locs = locationList.getLocations()
				.stream()
				.map(DTOMapper::mapLocationDTO)
				.collect(Collectors.toSet());
		
		return new LocationListDTO( 
				locationList.getName(),
				locationList.getDescription(),
				locationList.isPublic(),
				locs
		);
	}
	
}
