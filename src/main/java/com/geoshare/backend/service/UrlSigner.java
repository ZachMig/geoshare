package com.geoshare.backend.service;

import java.net.MalformedURLException;
import java.net.URL;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.geoshare.backend.config.ApiProps;


/**
 * https://developers.google.com/maps/documentation/streetview/digital-signature#java
 * @author Google
 */
@Service
public class UrlSigner {
	
	public UrlSigner(ApiProps apiProps) {
		this.apiSecret=apiProps.getSecret();
		this.key = Base64.getDecoder().decode(apiSecret.replace('-', '+').replace('_', '/'));
	}
	
	private String apiSecret;
	
	//Replace signature with value from environment variable on ec2
	private byte[] key;// = Base64.getDecoder().decode(apiSecret.replace('-', '+').replace('_', '/'));
	
	
	//Helper
	public String signRequest(String path, String query) throws NoSuchAlgorithmException, InvalidKeyException {

		// Retrieve the proper URL components to sign
		String resource = path + "?" + query;
		
	    // Get an HMAC-SHA1 signing key from the raw key bytes
		SecretKeySpec sha1Key = new SecretKeySpec(key, "HmacSHA1");
		
	    // Get an HMAC-SHA1 Mac instance and initialize it with the HMAC-SHA1 key
		Mac mac = Mac.getInstance("HmacSHA1");
		mac.init(sha1Key);
		
	    // compute the binary signature for the request
		byte[] sigBytes = mac.doFinal(resource.getBytes());

	    // base 64 encode the binary signature
		String signature = Base64.getEncoder().encodeToString(sigBytes);
		
	    // convert the signature to 'web safe' base 64
	    signature = signature.replace('+', '-');
	    signature = signature.replace('/', '_');
	    
	    return resource + "&signature=" + signature;
	}
	
	//Driver Function
	public String signUrl(String urlToSign) throws MalformedURLException, InvalidKeyException, NoSuchAlgorithmException {
		URL url = new URL(urlToSign);
		String request = signRequest(url.getPath(),url.getQuery());
		return url.getProtocol() + "://" + url.getHost() + request;
	}
	
	
	
	
	
	
	
}
