package com.deadscience.lambda.demo;

import com.amazonaws.services.s3.AmazonS3URI;

public class S3Credentials {

	private AmazonS3URI URI;
	private String AccessKey;
	private String SecretKey;
	
	
	public S3Credentials(String uRI, String accessKey, String secretKey) {
		super();
		URI = new AmazonS3URI(uRI);
		AccessKey = accessKey;
		SecretKey = secretKey;
	}


	public AmazonS3URI getURI() {
		return URI;
	}


	public void setURI(String uRI) {
		URI= new AmazonS3URI(uRI);
	}


	public String getAccessKey() {
		return AccessKey;
	}


	public void setAccessKey(String accessKey) {
		AccessKey = accessKey;
	}


	public String getSecretKey() {
		return SecretKey;
	}


	public void setSecretKey(String secretKey) {
		SecretKey = secretKey;
	}
	
	
	
	
}
