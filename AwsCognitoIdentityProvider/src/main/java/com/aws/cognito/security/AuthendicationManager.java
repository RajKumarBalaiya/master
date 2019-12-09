package com.aws.cognito.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.aws.cognito.config.AppConfig;

@RestController
@RequestMapping("/auth")
public class AuthendicationManager {
	@Autowired
	private AppConfig appConfig;

	@PostMapping("/oidc")
	public ResponseEntity<Object> getOpenIdToken(@RequestParam String code) {

		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "Basic " + appConfig.getCliendSecret());
		headers.setAccessControlAllowOrigin("*");
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		MultiValueMap<String, String> requestPayload = new LinkedMultiValueMap<String, String>();
		requestPayload.add("grant_type", "authorization_code");
		requestPayload.add("code", code);
		requestPayload.add("redirect_uri", "redirectUrl");
		requestPayload.add("scope", "openid");
		requestPayload.add("Content-Type", "application/x-www-form-urlencoded");
		HttpEntity<?> httpEntity = new HttpEntity<>(requestPayload, headers);
		String tokenUrl = appConfig.getTokenUrl();
		RestTemplate restTemplate = new RestTemplate();
		restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory());
		ResponseEntity<Object> response = null;
		try {
			response = restTemplate.postForEntity(tokenUrl, httpEntity, Object.class);
		} catch (RestClientException e) {
			System.out.println("**********************");
			System.out.println(e.getMessage());
		}
		return response;

	}

	@PostMapping("user")
	public ResponseEntity<?> getUserDetails(@RequestParam String token) {
		HttpHeaders header = new HttpHeaders();
		header.add("Authorization", "Basic " + appConfig.getCliendSecret());
		header.setAccessControlAllowOrigin("*");
		header.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		MultiValueMap<String, String> payload = new LinkedMultiValueMap<>();
		payload.add("grant_type", "grant_type");
		payload.add("token", token);
		payload.add("Content-Type", "application/x-www-form-urlencoded");
		String url = appConfig.getTokenUrl();
		HttpEntity<?> request = new HttpEntity<>(payload, header);
		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<?> result = null;
		try {
			result = restTemplate.exchange(url, HttpMethod.POST, request, Object.class);
		} catch (RestClientException e) {
			System.out.println(e.getMessage());
		}
		return result;
	}
}
