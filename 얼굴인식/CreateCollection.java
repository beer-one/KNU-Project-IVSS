package com.amazonaws.samples;

import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.services.rekognition.AmazonRekognition;
import com.amazonaws.services.rekognition.AmazonRekognitionClientBuilder;
import com.amazonaws.services.rekognition.model.CreateCollectionRequest;
import com.amazonaws.services.rekognition.model.CreateCollectionResult;


public class CreateCollection {

	   public static void main(String[] args) throws Exception {


	      AmazonRekognition rekognitionClient = AmazonRekognitionClientBuilder.standard().withCredentials(new ProfileCredentialsProvider()).withRegion("ap-northeast-2").build();

	      
	      String collectionId = "CollectionFaceImage";//collectionId : CollectionFaceImage
	            System.out.println("Creating collection: " +
	         collectionId );
	            
	        CreateCollectionRequest request = new CreateCollectionRequest()
	                    .withCollectionId(collectionId);
	           
	      CreateCollectionResult createCollectionResult = rekognitionClient.createCollection(request); 
	      System.out.println("CollectionArn : " +
	         createCollectionResult.getCollectionArn());
	      System.out.println("Status code : " +
	         createCollectionResult.getStatusCode().toString());

	   } 

	}