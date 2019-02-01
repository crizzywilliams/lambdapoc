package com.deadscience.lambda.demo;

import java.io.File;
import java.io.FileOutputStream;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.ListObjectsRequest;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.drew.imaging.ImageMetadataReader;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.Tag;

public class LambdaFunctionHandler implements RequestHandler<S3Credentials, String> {

    @Override
    public String handleRequest(S3Credentials input, Context context) {
        
        AWSCredentials credentials = new BasicAWSCredentials(input.getAccessKey(), input.getSecretKey());
        
        try {
        	AmazonS3 s3Client = AmazonS3ClientBuilder.standard()
        			.withCredentials(new AWSStaticCredentialsProvider(credentials))
        			.withRegion(Regions.AP_NORTHEAST_1)
        			.build();
        	
        	ListObjectsRequest listRequest = new ListObjectsRequest().withBucketName(input.getURI().getBucket());
        	ObjectListing objects = s3Client.listObjects(listRequest);
        	
        	for(S3ObjectSummary objectSummary : objects.getObjectSummaries()) {
        		S3Object object = s3Client.getObject(objectSummary.getBucketName(), objectSummary.getKey());
        		String contentType = object.getObjectMetadata().getContentType();
        		
        		File file = new File(objectSummary.getKey());
        		FileOutputStream fos = new FileOutputStream(file);
        		
        		S3ObjectInputStream inputStream = object.getObjectContent();
        		
        		int data = inputStream.read();
        		
        		while(data != -1) {
        			fos.write(data);
        			data = inputStream.read();
        		}
        		
        		fos.close();
        		
        		if(contentType.startsWith("image") || contentType.startsWith("video") ) {
	        		Metadata metadata = ImageMetadataReader.readMetadata(file);
	        		
	        		
	        		for(Directory directory: metadata.getDirectories()) {
	        			for(Tag tag : directory.getTags()) {
	        				System.out.println(tag);
	        			}
	        		}
        		} else {
        			System.out.println(contentType);
        		}
        		
        		
	        		
        		
        	}
        	
        	
        } catch(Exception e) {
        	System.out.println(e);
        	
        }
        return output;
    }

}
