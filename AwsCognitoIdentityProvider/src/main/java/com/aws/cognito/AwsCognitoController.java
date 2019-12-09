package com.aws.cognito;

import java.util.HashMap;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.amazonaws.auth.AnonymousAWSCredentials;
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

@RestController
@RequestMapping("/login")
public class AwsCognitoController {

	@SuppressWarnings("deprecation")
	@GetMapping("/aws")
	public String login(@RequestParam String token) {
		// https://dev.primesurvey.inbcu.com/#access_token=A5LBOyD4BJwLbHPLxWaXui2zcThY&token_type=Bearer&expires_in=7199
		// initialize the Cognito identity client with a set
		// of anonymous AWS credentials
		// AmazonCognitoIdentity provider = new AmazonCognitoIdentityClient(new
		// AnonymousAWSCredentials());
		AmazonCognitoIdentity provider = AmazonCognitoIdentityClientBuilder.standard().withRegion(Regions.US_WEST_2)
				.build();

		// provider1.setRegion(Region.getRegion(Regions.US_WEST_2));
		// AWSCognitoIdentityProvider provider =
		// AWSCognitoIdentityProviderClientBuilder.standard().defaultClient();
		// send a get id request. This only needs to be executed the first time
		// and the result should be cached.
		GetIdRequest idRequest = new GetIdRequest();
		// idRequest.setAccountId("accountid");
		idRequest.setIdentityPoolId("poolid");

		// If you are authenticating your users through an identity provider
		// then you can set the Map of tokens in the request
		Map providerTokens = new HashMap();
		providerTokens.put("providerUrl", token);
		idRequest.setLogins(providerTokens);

		GetIdResult idResp = provider.getId(idRequest);

		String identityId = idResp.getIdentityId();

		// TODO: At this point you should save this identifier so you won’t
		// have to make this call the next time a user connects
		return identityId;
	}

	@PostMapping("/credential")
	public Credentials getCredential(@RequestParam String openIdToken, @RequestParam String cognitoIdentityId) {
		GetIdRequest tokenRequest = new GetIdRequest();
		Map providerTokens = new HashMap();
		providerTokens.put("cognito-identity.amazonaws.com", openIdToken);
		tokenRequest.setLogins(providerTokens);

		AmazonCognitoIdentityClient identityClient = new AmazonCognitoIdentityClient();
		identityClient.setRegion(Region.getRegion(Regions.US_WEST_2));
		GetCredentialsForIdentityRequest request = new GetCredentialsForIdentityRequest();
		request.withLogins(providerTokens);
		request.setIdentityId(cognitoIdentityId);

		// request.setCustomRoleArn(
		// "arn:aws:cognito-identity:us-west-2:153279871056:identitypool/us-west-2:b3dfda6b-efa3-4677-9135-fc3f04db2f7a");
		GetCredentialsForIdentityResult tokenResp = identityClient.getCredentialsForIdentity(request);

		return tokenResp.getCredentials();
	}

	@PostMapping("/openid")
	public String getOpenIdToken(@RequestParam String identityId, @RequestParam String oidctoken) {
		AmazonCognitoIdentity provider = AmazonCognitoIdentityClientBuilder.standard().withRegion(Regions.US_WEST_2)
				.build();
		// Create the request object
		GetOpenIdTokenRequest tokenRequest = new GetOpenIdTokenRequest();
		tokenRequest.setIdentityId(identityId);
		// If you are authenticating your users through an identity provider
		// then you can set the Map of tokens in the request
		Map providerTokens = new HashMap();
		providerTokens.put("providerUrl", oidctoken);
		tokenRequest.setLogins(providerTokens);

		GetOpenIdTokenResult tokenResp = provider.getOpenIdToken(tokenRequest);
		// get the OpenID token from the response
		String openIdToken = tokenResp.getToken();
		return openIdToken;
	}

	@PostMapping("token")
	public com.amazonaws.services.securitytoken.model.Credentials getStsToken(@RequestParam String awsAccessToken,
			@RequestParam String roleARN) {
		// you can now create a set of temporary, limited-privilege credentials to
		// access
		// your AWS resources through the Security Token Service utilizing the OpenID
		// token returned by the previous API call. The IAM Role ARN passed to this call
		// will be applied to the temporary credentials returned
		AWSSecurityTokenService stsClient = new AWSSecurityTokenServiceClient(new AnonymousAWSCredentials());
		AssumeRoleWithWebIdentityRequest stsReq = new AssumeRoleWithWebIdentityRequest();
		stsReq.setRoleArn(roleARN);
		stsReq.setWebIdentityToken(awsAccessToken);
		stsReq.setRoleSessionName("test");

		AssumeRoleWithWebIdentityResult stsResp = stsClient.assumeRoleWithWebIdentity(stsReq);
		com.amazonaws.services.securitytoken.model.Credentials stsCredentials = stsResp.getCredentials();

		// Create the session credentials object
		// AWSSessionCredentials sessionCredentials = new
		// BasicSessionCredentials(stsCredentials.getAccessKeyId(),
		// stsCredentials.getSecretAccessKey(), stsCredentials.getSessionToken());
		return stsCredentials;

	}

}
