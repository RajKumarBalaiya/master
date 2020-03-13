package com.springboot.cognito.app.controller;
public class AwsCredentials {

    private final String accessKey;
    private final String secretKey;

    public AwsCredentials(String accessKey, String secretKey) {
        this.accessKey = accessKey;
        this.secretKey = secretKey;
    }

    public String getAccessKey() {
        return accessKey;
    }

    public String getSecretKey() {
        return secretKey;
    }
}