package com.geoshare.backend.service;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.geoshare.backend.dto.LocationDTO;
import com.geoshare.backend.entity.Country;
import com.geoshare.backend.entity.GeoshareUser;
import com.geoshare.backend.entity.Location;
import com.geoshare.backend.entity.LocationList;
import com.geoshare.backend.entity.Meta;
import com.geoshare.backend.repository.GeoshareUserRepository;
import com.geoshare.backend.repository.LocationListRepository;
import com.geoshare.backend.repository.LocationRepository;
import com.geoshare.backend.service.UrlParserService.ParsedUrlData;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@PropertySource("classpath:application.properties")
public class LocationService {
	
	private CountryService countryService;
	private MetaService metaService;
	private LocationRepository locationRepository;
	private GeoshareUserRepository userRepository;
	private LocationListRepository locationListRepository;
	private UrlParserService urlParserService;
	private UrlSigner urlSigner;
	private String apiKey;
	private AtomicInteger apiCount;

	
	public LocationService(
			CountryService countryService,
			MetaService metaService,
			LocationRepository locationRepository, 
			GeoshareUserRepository userRepository,
			LocationListRepository locationListRepository,
			UrlParserService urlParserService,
			UrlSigner urlSigner,
			@Value("${MAPS_API_KEY}") String apiKey,
			AtomicInteger apiCount) {
		
		this.countryService = countryService;
		this.metaService = metaService;
		this.locationRepository = locationRepository;
		this.userRepository = userRepository;
		this.locationListRepository = locationListRepository;
		this.urlParserService = urlParserService;
		this.urlSigner = urlSigner;
		this.apiKey = apiKey;
		this.apiCount = apiCount;
	}
	
//	@Value("${maps.api.key}")
//	private String apiKey;
	
	public List<Location> findAllByUser(Long userID) {
		return locationRepository.findAllByUser(userID);
	}
	
	public List<Location> findAllByUser(String username) {
		return locationRepository.findAllByUser(username);
	}
	
	public List<Location> findAllByCountry(Long countryID) {
		return locationRepository.findAllByCountry(countryID);
	}
	
	public List<Location> findAllByCountry(String countryName) {
		return locationRepository.findAllByCountry(countryName);
	}
		
	public Location findByID(Long id) {
		return locationRepository.findByIDOrThrow(id);
	}
	
	public Location createLocation(LocationDTO locationDTO) {
		
		ParsedUrlData urlData = urlParserService.parseData(locationDTO.url());
	    
		Long userID = locationDTO.userID();
		
		Country country = countryService.findCountry(locationDTO.countryName()); //Cached
		Meta meta = metaService.findMeta(locationDTO.meta()); //Cached
		GeoshareUser geoshareUser = userRepository.findByIDOrThrow(userID);
		
		String previewUrlNoKey = "https://maps.googleapis.com/maps/api/streetview?size=640x640&location=%s&fov=%s&heading=%s&pitch=%s&key="
				.formatted(
						urlData.getLat()+","+urlData.getLng(),
						urlData.getFov(),
						urlData.getHeading(),
						urlData.getPitch());
		
		Location location = new Location(
				locationDTO.url(),
				locationDTO.description(),
				country,
				geoshareUser,
				meta,
				urlData.getLat(),
				urlData.getLng(),
				urlData.getFov(),
				urlData.getHeading(),
				urlData.getPitch(),
				previewUrlNoKey);
		

		return locationRepository.save(location);
	}
	
	public void deleteLocation(Collection<Long> locationIDs, Authentication auth) {
		
		List<Location> locations = locationRepository.findAllById(locationIDs);
		
		if (!HelperService.userOwns(auth, locations)) {
			throw new AccessDeniedException("User: " + auth.getName() + " does not own at least one of the given locations.");
		}
		
		for (Location location : locations) {
			
			//Handle removing this location from all associated LocationLists
			Collection<LocationList> listsThisLocationIsPartOf = locationListRepository.findAllByLocationID(location.getId());
			for(LocationList locationList : listsThisLocationIsPartOf) {
				locationList.removeFromLocations(location);
			}
			
			//Finally delete the Location
			locationRepository.delete(location);
		}
	}
	
	
	public LocationDTO updateLocation(LocationDTO locationDTO, Authentication auth) {
		
		Location location = locationRepository.findByIDOrThrow(locationDTO.id());
		
		if (!HelperService.userOwns(auth, List.of(location))) {
			throw new AccessDeniedException("User " + auth.getName() + " does not own this location.");
		}
		
		ParsedUrlData urlData = urlParserService.parseData(locationDTO.url());
		String previewUrlNoKey = "https://maps.googleapis.com/maps/api/streetview?size=640x640&location=%s&fov=%s&heading=%s&pitch=%s&key="
				.formatted(
						urlData.getLat()+","+urlData.getLng(),
						urlData.getFov(),
						urlData.getHeading(),
						urlData.getPitch());
		
		
		Country newCountry = countryService.findCountry(locationDTO.countryName());
		Meta newMeta = metaService.findMeta(locationDTO.meta());
		
		location.setUrl(locationDTO.url());
		location.setCountry(newCountry);
		location.setMeta(newMeta);
		location.setDescription(locationDTO.description());
		location.setLat(urlData.getLat());
		location.setLng(urlData.getLng());
		location.setFov(urlData.getFov());
		location.setHeading(urlData.getHeading());
		location.setPitch(urlData.getPitch());
		location.setPreviewUrl(previewUrlNoKey);
		
		return DTOMapper.mapLocationDTO(locationRepository.save(location));
	}
	
	
	public Mono<byte[]> fetchPreview(Long locationID, Authentication auth) {
		
		if (apiCount.getAndDecrement() < 1) {
			return Mono.empty();
		}
		
		Location location = locationRepository.findByIDOrThrow(locationID);
		
		if (!HelperService.userOwns(auth, List.of(location))) {
			throw new AccessDeniedException("User " + auth.getName() + " does not own this location.");
		}
		
		String urlToSign = location.getPreviewUrl().concat(apiKey);
		
		//System.out.println("URL To Sign: " + urlToSign);
		
		String signedURL = "";
		try {
			signedURL = urlSigner.signUrl(urlToSign);
		} catch (Exception e) {
			//IDk man throw this up? its 4am
			e.printStackTrace();
		}

		//System.out.println("Signed URL: " + signedURL);
		
		WebClient webClient = WebClient.create();
		
		Mono<byte[]> apiResponse = webClient
		.get()
		.uri(signedURL)
		.retrieve()
		.bodyToMono(byte[].class)
		.doOnNext(bytes -> {
		    if (bytes.length == 0) {
		    	System.out.println("Empty response from Google API.");
		    } else {
		    	System.out.println("Received bytes from Google API: " + bytes.length);
		    }
		})
		.onErrorResume(e -> {
			e.printStackTrace();
			return Mono.empty();
		});
		
		
		return apiResponse;
		
	}
	
	
	public Integer checkApiCount() {
		return apiCount.get();
	}
	
}
