package com.amazonaws.samples;

import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.services.rekognition.AmazonRekognition;
import com.amazonaws.services.rekognition.AmazonRekognitionClientBuilder;
import com.amazonaws.services.rekognition.model.FaceMatch;
import com.amazonaws.services.rekognition.model.Image;
import com.amazonaws.services.rekognition.model.S3Object;
import com.amazonaws.services.rekognition.model.SearchFacesByImageRequest;
import com.amazonaws.services.rekognition.model.SearchFacesByImageResult;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.corba.se.impl.ior.ByteBuffer;

import java.io.IOException;
import java.nio.file.Files;
import java.util.*;
import java.io.*;
public class SearchFaceMatchingImageCollection {
    public static final String collectionId = "CollectionFaceImage";
    public static final String bucket = "dongwookbucket";
    public static String[] photos = new String[51];
    
    
    public static void main(String[] args) throws Exception {

       AmazonRekognition rekognitionClient = AmazonRekognitionClientBuilder.standard().withCredentials(new ProfileCredentialsProvider()).withRegion("ap-northeast-2").build();
        
      ObjectMapper objectMapper = new ObjectMapper();
      
      String folder;
       //JsonParser jsonParser = new Jsonparser();
      Scanner scan = new Scanner(System.in);
     
      System.out.println("폴더 이름 입력 : ");
      folder = scan.nextLine();
      
      for(int i = 1; i <= 50; i++) {
    	  if(i<10) {
    		  photos[i] = folder+" 0"+i+".jpg";
    	  }else {
    		  photos[i] = folder+" "+i+".jpg";
    	  }
    	  
      }
      System.out.println("");
      //int flag= 0;
      int i=1;
      while(true) {
    	  try {
	    	  Image image=new Image()
	                  .withS3Object(new S3Object()
	                          .withBucket(bucket)
	                          .withName(photos[i]));
	          
	          // Search collection for faces similar to the largest face in the image.
	          SearchFacesByImageRequest searchFacesByImageRequest = new SearchFacesByImageRequest()
	                  .withCollectionId(collectionId)
	                  .withImage(image)
	                  .withFaceMatchThreshold(70F)
	                  .withMaxFaces(10);
	               
	           SearchFacesByImageResult searchFacesByImageResult = 
	                   rekognitionClient.searchFacesByImage(searchFacesByImageRequest);
	
	           //System.out.println("Faces matching largest face in image from" + photo);
	          List < FaceMatch > faceImageMatches = searchFacesByImageResult.getFaceMatches();
	          if(faceImageMatches.size()==0) {
	        	  System.out.println(i+"번째 사진에서 매칭된 id를 찾지 못했 습니다.");
	        	  i++;
	        	  continue;
	          }
	          else {
	        	  for (FaceMatch face: faceImageMatches) {
	            	  System.out.println("");
	                  //System.out.println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(face));
	            	  System.out.println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(face));
	            	  //System.out.println("face id : 46d4440f-8598-41ce-9c2c-9cd3d55b9ab5");
	            	  System.out.println("");
	            	  System.out.println();
	                 
	              }
	              System.out.println(i+"번째 frame에서 찾았습니다.");
	              break;
	          }
	          //flag=1;
    	  }
    	  catch (Exception e) {
    		  System.out.println(i+"번째 사진에서 매칭된 id를 찾지 못했 습니다.");
        	  i++;
    		  continue;
    	  }
      }
       // Get an image object from S3 bucket.
     
   }
}