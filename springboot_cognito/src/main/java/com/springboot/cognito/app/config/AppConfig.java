package com.springboot.cognito.app.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author Raj
 *
 */
@Configuration
@ConfigurationProperties(prefix = "cognito")
public class AppConfig {

	private String identityProviderId;
	private String cognitoIdentityPoolId;
	private String iamRoleArn;

	public String getIdentityProviderId() {
		return identityProviderId;
	}

	public void setIdentityProviderId(String identityProviderId) {
		this.identityProviderId = identityProviderId;
	}

	public String getCognitoIdentityPoolId() {
		return cognitoIdentityPoolId;
	}

	public void setCognitoIdentityPoolId(String cognitoIdentityPoolId) {
		this.cognitoIdentityPoolId = cognitoIdentityPoolId;
	}

	public String getIamRoleArn() {
		return iamRoleArn;
	}

	public void setIamRoleArn(String iamRoleArn) {
		this.iamRoleArn = iamRoleArn;
	}

}
