package com.geoshare.backend.dto;

import java.util.Set;
import com.geoshare.backend.entity.Location;

public record LocationListDTO(
		String name,
		String description,
		boolean isPublic,
		Set<LocationDTO> locations)
{}
