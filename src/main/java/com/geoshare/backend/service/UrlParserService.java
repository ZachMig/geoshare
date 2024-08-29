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
	public class ParsePatterns {
		public ParsePatterns(Pattern coordsPattern, Pattern pitchPattern, Pattern yawPattern) {
			this.coordsPattern = coordsPattern;
			this.pitchPattern = pitchPattern;
			this.yawPattern = yawPattern;
		}
		private Pattern coordsPattern;
		private Pattern pitchPattern;
		private Pattern yawPattern;
	}
	
	//Container Class
	@Getter
	public class ParsedUrlData {
	    public ParsedUrlData(BigDecimal lat, BigDecimal lng, Integer pitch, Integer yaw) {
	        this.lat = lat;
	        this.lng = lng;
	        this.pitch = pitch;
	        this.yaw = yaw;
	    }
	    
	    private BigDecimal lat;
	    private BigDecimal lng;
	    private Integer pitch;
	    private Integer yaw;
	}
	
	public ParsedUrlData parseData(String url) {
		
        Pattern coordsPattern = Pattern.compile("@([-\\d.]+),([-\\d.]+)", Pattern.CASE_INSENSITIVE);
        Pattern pitchPattern = Pattern.compile("pitch%3D([-\\d.]+)", Pattern.CASE_INSENSITIVE);
        Pattern yawPattern = Pattern.compile("(\\d+(?:\\.\\d+)?)h", Pattern.CASE_INSENSITIVE);

		BigDecimal lat, lng;
		Integer pitch, yaw;
		
		Matcher coordsMatcher = coordsPattern.matcher(url);
		Matcher pitchMatcher = pitchPattern.matcher(url);
	    Matcher yawMatcher = yawPattern.matcher(url);
	    
	    if (coordsMatcher.find()) {
	    	lat = BigDecimal.valueOf(Double.parseDouble(coordsMatcher.group(1)));
	    	lng = BigDecimal.valueOf(Double.parseDouble(coordsMatcher.group(2)));
	    } else {
	    	throw new IllegalArgumentException("Unable to parse coordinates from provided Google Maps URL. Please ensure you are following the format described in the User Guide, and contact the site owner if you suspect an error.");
	    }
	    
	    if (pitchMatcher.find()) {
	    	pitch = (int) Double.parseDouble(pitchMatcher.group(1));
	    } else {
	    	throw new IllegalArgumentException("Unable to parse pitch from provided Google Maps URL. Please ensure you are following the format described in the User Guide, and contact the site owner if you suspect an error.");
	    }
	    
	    if (yawMatcher.find()) {
	    	yaw = (int) Double.parseDouble(yawMatcher.group(1));
	    } else {
	    	throw new IllegalArgumentException("Unable to parse yaw from provide Google Maps URL. Please ensure you are following the format described in the User Guide, and contact the site owner if you suspect an error.");
	    }
		
		return new ParsedUrlData(lat, lng, pitch, yaw);
	}

	
}
