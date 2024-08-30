package com.geoshare.backend.service;

import java.math.BigDecimal;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.stereotype.Service;

import lombok.Getter;

@Service
public class UrlParserService {

	//Container Class
	@Getter
	public class ParsedUrlData {
	    public ParsedUrlData(BigDecimal lat, BigDecimal lng, Integer fov, BigDecimal heading, BigDecimal pitch) {
	        this.lat = lat;
	        this.lng = lng;
	        this.fov = fov;
	        this.heading = heading;
	        this.pitch = pitch;
	    }
	    
	    private BigDecimal lat;
	    private BigDecimal lng;
	    private Integer fov;
	    private BigDecimal heading;
	    private BigDecimal pitch;
	}
	
	public ParsedUrlData parseData(String url) {
		char[] urlChars = url.toCharArray();
		int pointer = 0;
		StringBuilder buffer = new StringBuilder();
		int len = url.length();
		
		BigDecimal lat = null, lng = null;
		Integer fov = null;
		BigDecimal heading = null, pitch = null;
		
		//Parse Coordinates
		pointer = url.indexOf("@") + 1;
		while (pointer < len && lat == null && lng == null) {
			//Parse Latitude
			while (urlChars[pointer] != ',') {
				buffer.append(urlChars[pointer]);
				pointer++;
			}
			lat = BigDecimal.valueOf(Double.parseDouble(buffer.toString()));
			buffer = new StringBuilder();
			pointer++;
			
			//Parse Longitude
			while (urlChars[pointer] != ',') {
				buffer.append(urlChars[pointer]);
				pointer++;
			}
			lng = BigDecimal.valueOf(Double.parseDouble(buffer.toString()));
			buffer = new StringBuilder();
		}
		
		//Parse Directional Values
		pointer = url.indexOf("3a,") + 3;
		while (pointer < len && fov == null && heading == null && pitch == null) {
			
			//Parse FoV
			while (urlChars[pointer] != 'y') {
				buffer.append(urlChars[pointer]);
				pointer++;
			}
			fov = Integer.parseInt(buffer.toString());
			buffer = new StringBuilder();
			pointer+=2; //Move past the 'y' and also the ','
			
			//Parse Heading
			while (urlChars[pointer] != 'h') {
				buffer.append(urlChars[pointer]);
				pointer++;
			}
			heading = BigDecimal.valueOf(Double.parseDouble(buffer.toString()));
			buffer = new StringBuilder();
			pointer+=2; //Move past the 'h' and also the ','
			
			//Parse Pitch
			while (urlChars[pointer] != 't') {
				buffer.append(urlChars[pointer]);
				pointer++;
			}
			Double pitchD = Double.parseDouble(buffer.toString());
			pitchD -= 90.0; // Google Maps API requests and Street View URLs handle pitch differently 90->90 vs 0->180
			pitch = BigDecimal.valueOf(pitchD);
		}
		
		
		//TODO
		// Maybe change this to allow creating locations with URLs that don't pass? Just won't have a Preview available.
		if (lat == null || lng == null) {
			throw new IllegalArgumentException("Unable to parse coordinates from provided Google Maps URL. Please ensure you are following the format described in the User Guide, and contact the site owner if you suspect an error.");
		}
		
		if (fov == null) {
			throw new IllegalArgumentException("Unable to parse FoV from provided Google Maps URL. Please ensure you are following the format described in the User Guide, and contact the site owner if you suspect an error.");
		}
		
		if (heading == null) {
			throw new IllegalArgumentException("Unable to parse heading from provided Google Maps URL. Please ensure you are following the format described in the User Guide, and contact the site owner if you suspect an error.");
		}
		
		if (pitch == null) {
			throw new IllegalArgumentException("Unable to parse pitch from provided Google Maps URL. Please ensure you are following the format described in the User Guide, and contact the site owner if you suspect an error.");
		}
		
		return new ParsedUrlData(lat, lng, fov, heading, pitch);
	}

	
}
