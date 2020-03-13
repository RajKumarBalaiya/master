package com.springboot.cognito.app.controller;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.amazonaws.DefaultRequest;
import com.amazonaws.auth.AWS4Signer;
import com.amazonaws.auth.AWSSessionCredentials;
import com.amazonaws.auth.AnonymousAWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.auth.BasicSessionCredentials;
import com.amazonaws.http.HttpMethodName;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.cognitoidentity.AmazonCognitoIdentity;
import com.amazonaws.services.cognitoidentity.AmazonCognitoIdentityClient;
import com.amazonaws.services.cognitoidentity.AmazonCognitoIdentityClientBuilder;
import com.amazonaws.services.cognitoidentity.model.Credentials;
import com.amazonaws.services.cognitoidentity.model.GetCredentialsForIdentityRequest;
import com.amazonaws.services.cognitoidentity.model.GetCredentialsForIdentityResult;
import com.amazonaws.services.cognitoidentity.model.GetIdRequest;
import com.amazonaws.services.cognitoidentity.model.GetIdResult;
import com.amazonaws.services.cognitoidentity.model.GetOpenIdTokenRequest;
import com.amazonaws.services.cognitoidentity.model.GetOpenIdTokenResult;
import com.amazonaws.services.securitytoken.AWSSecurityTokenService;
import com.amazonaws.services.securitytoken.AWSSecurityTokenServiceClient;
import com.amazonaws.services.securitytoken.model.AssumeRoleWithWebIdentityRequest;
import com.amazonaws.services.securitytoken.model.AssumeRoleWithWebIdentityResult;
import com.springboot.cognito.app.config.AppConfig;

/**
 * @author Raj
 *
 */
@RestController
@RequestMapping("/aws")
@CrossOrigin("*")
public class CognitoAuthenticationController {

	private static final String ACCESS_KEY = null;
	private static final String AWS_DATASHOP_SECRET_KEY = null;
	@Autowired
	private AppConfig appConfig;

	@GetMapping("/identityId")
	public String getCognitoIdentityId(@RequestParam String openidToken) {
		AmazonCognitoIdentity provider = AmazonCognitoIdentityClientBuilder.standard().withRegion(Regions.US_WEST_2)
				.build();
		GetIdRequest idRequest = new GetIdRequest();
		idRequest.setIdentityPoolId(appConfig.getCognitoIdentityPoolId());
		// If you are authenticating your users through an identity provider
		// then you can set the Map of tokens in the request
		Map<String, String> providerTokens = new HashMap<String, String>();
		providerTokens.put(appConfig.getIdentityProviderId(), openidToken);
		idRequest.setLogins(providerTokens);
		GetIdResult idResp = provider.getId(idRequest);
		String identityId = idResp.getIdentityId();
		return identityId;
	}

	@PostMapping("/cogitoToken")
	public String getCognitoOpenIdToken(@RequestParam String identityId, @RequestParam String openIdToken) {
		AmazonCognitoIdentity provider = AmazonCognitoIdentityClientBuilder.standard().withRegion(Regions.US_WEST_2)
				.build();
		// Create the request object
		GetOpenIdTokenRequest tokenRequest = new GetOpenIdTokenRequest();
		tokenRequest.setIdentityId(identityId);
		// If you are authenticating your users through an identity provider
		// then you can set the Map of tokens in the request
		Map<String, String> providerTokens = new HashMap<String, String>();
		providerTokens.put(appConfig.getIdentityProviderId(), openIdToken);
		tokenRequest.setLogins(providerTokens);
		GetOpenIdTokenResult tokenResp = provider.getOpenIdToken(tokenRequest);
		// get the OpenID token from the response
		String cognitoToken = tokenResp.getToken();
		return cognitoToken;
	}

	@SuppressWarnings("deprecation")
	@PostMapping("/stsCredential")
	public Credentials getCognitoCredential(@RequestParam String openIdToken, @RequestParam String cognitoIdentityId) {
		GetIdRequest tokenRequest = new GetIdRequest();
		Map<String, String> providerTokens = new HashMap<String, String>();
		providerTokens.put(appConfig.getIdentityProviderId(), openIdToken);
		tokenRequest.setLogins(providerTokens);
		AmazonCognitoIdentityClient identityClient = new AmazonCognitoIdentityClient();
		identityClient.setRegion(Region.getRegion(Regions.US_WEST_2));

		GetCredentialsForIdentityRequest request = new GetCredentialsForIdentityRequest();
		request.withLogins(providerTokens);
		request.setIdentityId(cognitoIdentityId);
		// request.setCustomRoleArn(appConfig.getRoleArn());
		GetCredentialsForIdentityResult tokenResp = identityClient.getCredentialsForIdentity(request);
		return tokenResp.getCredentials();
	}

	@PostMapping("assumeRoleCrdential")
	public com.amazonaws.services.securitytoken.model.Credentials getAssumeRole(@RequestParam String cognitoOpenIdToken,
			@RequestParam String identityId) {
		// you can now create a set of temporary, limited-privilege credentials to
		// access
		// your AWS resources through the Security Token Service utilizing the OpenID
		// token returned by the previous API call. The IAM Role ARN passed to this call
		// will be applied to the temporary credentials returned
		@SuppressWarnings("deprecation")
		AWSSecurityTokenService stsClient = new AWSSecurityTokenServiceClient(new AnonymousAWSCredentials());
		AssumeRoleWithWebIdentityRequest stsReq = new AssumeRoleWithWebIdentityRequest();
		stsReq.setRoleArn(appConfig.getIamRoleArn());
		stsReq.setWebIdentityToken(cognitoOpenIdToken);
		stsReq.setRoleSessionName("AppTestSession");
		AssumeRoleWithWebIdentityResult stsResp = stsClient.assumeRoleWithWebIdentity(stsReq);
		com.amazonaws.services.securitytoken.model.Credentials stsCredentials = stsResp.getCredentials();
		// Create the session credentials object
		@SuppressWarnings("unused")
		AWSSessionCredentials sessionCredentials = new BasicSessionCredentials(stsCredentials.getAccessKeyId(),
				stsCredentials.getSecretAccessKey(), stsCredentials.getSessionToken());
		// save the timeout for these credentials
		/*
		 * Date sessionCredentialsExpiration = stsCredentials.getExpiration(); // these
		 * credentials can then be used to initialize other AWS clients, // for example
		 * the Amazon Cognito Sync client
		 * 
		 * AmazonCognitoSync syncClient = new
		 * AmazonCognitoSyncClient(sessionCredentials); ListDatasetsRequest syncRequest
		 * = new ListDatasetsRequest(); syncRequest.setIdentityId(identityId);
		 * syncRequest.setIdentityPoolId(appConfig.getIdentityPoolId());
		 * ListDatasetsResult syncResp = syncClient.listDatasets(syncRequest);
		 */
		return stsResp.getCredentials();
	}

	@GetMapping("/sign")
	public ResponseEntity<String> getAllEmployee() {
		HttpHeaders headers = new HttpHeaders();
		headers.set("Authorization",
				"AWS4-HMAC-SHA256 Credential=ASIA3TP26BOB4YO5VZUM/20200114/us-west-2/execute-api/aws4_request, SignedHeaders=content-type;host;x-amz-content-sha256;x-amz-date;x-amz-security-token, Signature=60650b2c42d23b9b0a79a51fe201214916573be4fca1e74074751030a29b7710");
		headers.set("X-Amz-Security-Token",
				"IQoJb3JpZ2luX2VjEM///////////wEaCXVzLXdlc3QtMiJHMEUCIHbEZ54CDgTJknNqYtUfLMMcBFhfY7UoRaP2OOmm+dYcAiEA4G4VIk7UMGgaPNj/gvcx/owFMY9IMO2mNOb4A/7opaAqvgMIWBAAGgw3OTc3Nzk1NjEzNDciDAqGVdsH0n2lv7OWjiqbA4fGGYtABNNPlyVhsQl7AbUjCV3TONTZ1V/Dbaj0zeuNNZrai9BIJnNqPBZUmP8phVQhPgg3DtoXrZeXoE0Av873blVUO0JyYa3l5rJo+go0oVbw9yyf4nO1u9pHNvnc2Ju4TBHHRMqhsQDVhJX0ZoTKlFmcPqMc/Przp6ZzhKkO39myNKPNpyNGhGPFNcyn9UthW+eFsifot9i4IdlFnO8LtSdLbJfVTI3iZRlAbhkBRr+YVOkMK+O165Dr+J3CAULrV//27gINquF6Tur67FfVR1Pog7xCOGQEwfgmILdAYBqPZDJ3mMYXc53pNznJ+Uc74VNIVaeRxThDVJU9vcyFOZaPnUugjZkB9CvkFilEt0z+C4tXFLxYfaULnVrUgxLgibo1Nq9kIlMlGzPh+N6ij24OiCStNHT79vUpoanYHjip2VTIlghNjz4iI6IzjLRZm4kZaX5SmR9cuiicuqVdA67RiXPVuY0zkcr+fvaR6f6CZY84ERbejFt8vBvmsNemB/oEFahi+9NDKubsB5dJNKzH3T6IOz7qFTDMzPXwBTrKAknZwf346XKKtm52MpOFg48hVHDkhNbz+xng9p3iBhvFc2al/CDxnj2/73WYXzEG9vsp/s1OoNx2jLm9sKqzjNnYOkQtx9LhHX69tbm+HbFBACNMGCGut+lNXRAuWGGZImDAVvse/ClkWpHz7lJ9+GCeymuV5167+W5FpJqVp7ecES5/KJHaqblbZCUEJDA0yF1T1gPVHdIBXav2LhQqS5S0gKnwHrYfgoibsOjiF4JuG2YpgBboyZ77xQpXIs7xdUZFj7q6Qu5bhkvffIQvGjkI+T1lUkWBbaSVN0neDyGgMy6t18OVXA0DJCCzJedTdeepVKtLc11gshwG3eeWdeDFFyQ3K9QCvMlIIsMQSXGJXlExQ4vt/q9zT6ThoO42yFtHwj7bSGDMP91aTe3vHNaS3zsuk2a6EEXOiwDBmfPmpdSTJNix1RtTlQ==");
		headers.set("Host", "0v13tki5b3.execute-api.us-west-2.amazonaws.com");
		headers.set("X-Amz-Date", "20200114T065340Z");
		HttpEntity<Object> entity = new HttpEntity<Object>(headers);
		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<String> response = null;
		try {
			response = restTemplate.exchange(
					"https://0v13tki5b3.execute-api.us-west-2.amazonaws.com/dev/employee/getAll", HttpMethod.GET,
					entity, String.class);
		} catch (RestClientException e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response.getBody());

	}
	
	public void sign()
	{
		AWS4Signer signer = new AWS4Signer();
		BasicAWSCredentials awsCreds = new BasicAWSCredentials(ACCESS_KEY, AWS_DATASHOP_SECRET_KEY);
		DefaultRequest<?> signableRequest = new DefaultRequest<>("aws-service-name");
		signableRequest.setHttpMethod(HttpMethodName.GET);
		signableRequest.setResourcePath("fooo");
		String baar = null;
		signableRequest.setEndpoint(URI.create(baar));
		// signableRequest.addParameter("execution_id", executionId);
		signableRequest.addHeader("Content-Type", "application/json");

		signer.sign(signableRequest, awsCreds);
		Response<String> rsp = new AmazonHttpClient(new ClientConfiguration())
			    .requestExecutionBuilder()
			    .executionContext(new ExecutionContext(true))
			    .request(request)
			    .errorResponseHandler(new SimpleAwsErrorHandler())
			    .execute(new SimpleResponseHandler<String>());
	}
}
