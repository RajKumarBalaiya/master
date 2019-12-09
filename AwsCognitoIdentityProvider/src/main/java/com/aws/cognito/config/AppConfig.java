package com.aws.cognito.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "oauth")
public class AppConfig {

	private String tokenUrl;
	private String cliendSecret;

	public String getTokenUrl() {
		return tokenUrl;
	}

	public void setTokenUrl(String tokenUrl) {
		this.tokenUrl = tokenUrl;
	}

	public String getCliendSecret() {
		return cliendSecret;
	}

	public void setCliendSecret(String cliendSecret) {
		this.cliendSecret = cliendSecret;
	}

}
