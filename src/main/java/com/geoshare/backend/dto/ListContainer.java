package com.geoshare.backend.dto;

import java.util.Collection;
import java.util.Map;

import com.geoshare.backend.entity.Location;
import com.geoshare.backend.entity.LocationList;

public record ListContainer (
		Collection<LocationDTO> unlisted,
		Collection<LocationListDTO> listed) {
}
