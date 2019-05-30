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
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.nio.file.Files;
import java.util.*;
import java.io.*;
public class SearchFaceMatchingImageCollection {
    public static final String collectionId = "CollectionFaceImage";
    public static final String bucket = "violence-image";
    public static String[] photos = new String[51];
    public static final String IP = "155.230.29.148";
    public static final int PORT = 9090;
    
    public static AmazonRekognition rekognitionClient = AmazonRekognitionClientBuilder.standard().withCredentials(new ProfileCredentialsProvider()).withRegion("ap-northeast-2").build();
    public static ObjectMapper objectMapper = new ObjectMapper();
    
    public static void main(String[] args) throws Exception {
    	
    	@SuppressWarnings("resource")
    	ServerSocket server = new ServerSocket();
    	server.bind(new InetSocketAddress(IP, PORT));
    	Socket clientSocket = server.accept();
    	
    	String result = "";
    	OutputStream output = clientSocket.getOutputStream();
    	InputStream input = clientSocket.getInputStream();
    	byte[] input_data = new byte[50];
    	String file_name;
    	
    	System.out.println("OPEN");
      //JsonParser jsonParser = new Jsonparser();
      //Scanner scan = new Scanner(System.in);
     
      while(true) {
	      // 클라이언트에게 파일이름 얻기
	      //System.out.println("폴더 이름 입력 : ");
	      //folder = scan.nextLine();
	     
    	  input.read(input_data);
    	  
    	  while(true) {
	    	  try {
		    	  file_name = input_data.toString();
		    	  output.write("ACK".getBytes());
		    	  break;
	    	  } catch(SocketException e) {
	    		  output.write("NAK".getBytes());
	    	  }
    	  }
	      
    	  // 폭력 이미지 파일 모으기
	      for(int i = 0; i < 40; i++) {
	    	  
	    	  photos[i] = file_name+"_"+i+".png";
	    	 
	      }
	      
	      //System.out.println("");
	      //int flag= 0;
	      
	      int i=1;
	      while(i <= 50) {
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
	      
	      
      }
       // Get an image object from S3 bucket.
      
   }
}