package com.amazonaws.samples;

import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.services.rekognition.AmazonRekognition;
import com.amazonaws.services.rekognition.AmazonRekognitionClientBuilder;
import com.amazonaws.services.rekognition.model.FaceMatch;
import com.amazonaws.services.rekognition.model.Image;
import com.amazonaws.services.rekognition.model.S3Object;
import com.amazonaws.services.rekognition.model.SearchFacesByImageRequest;
import com.amazonaws.services.rekognition.model.SearchFacesByImageResult;

import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.List;
import java.util.Scanner;

import java.io.*;
import com.fasterxml.jackson.databind.ObjectMapper;

public class SearchFaceMatchingImageCollection {
	public static final String collectionId = "collectionUser";
	public static final String bucket = "violence-image";
	// public static final String photo = "test3.jpg";
	//static String file_name;
	public static String[] photos = new String[51];
	public static final String IP = "155.230.29.148";
	public static final int PORT = 9115;

	public static void main(String[] args) throws Exception {
		@SuppressWarnings("resource")
		AmazonRekognition rekognitionClient = AmazonRekognitionClientBuilder.standard()
				.withCredentials(new ProfileCredentialsProvider()).withRegion("ap-northeast-2").build();

		ObjectMapper objectMapper = new ObjectMapper();

		ServerSocket server = new ServerSocket();
		server.bind(new InetSocketAddress(IP, PORT));
		Socket clientSocket = server.accept();
		clientSocket.setSoTimeout(1);
		

		String result = "";
		BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
		BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
		String file_name ;
		

		System.out.println("OPEN");

		while (true) {
			while(true) {
				try {
					file_name = reader.readLine();
					System.out.println(file_name);
					break;
				} catch (SocketTimeoutException e) {
					//System.out.println("NO");
					continue;
				}
			}
			
			//writer.write("ACK");
			
			//System.out.println("폴더 이름 입력 : ");
			//Scanner scan = new Scanner(System.in);
			//file_name = scan.nextLine();
			for (int i = 0; i < 10; i++) {
				photos[i] = file_name + "_" + i + ".png";
			}
			int i = 0;
			// Get an image object from S3 bucket.
			while (i < 10) {
				try {
					Image image = new Image().withS3Object(new S3Object().withBucket(bucket).withName(photos[i]));

					// Search collection for faces similar to the largest face in the image.
					SearchFacesByImageRequest searchFacesByImageRequest = new SearchFacesByImageRequest()
							.withCollectionId(collectionId).withImage(image).withFaceMatchThreshold(70F)
							.withMaxFaces(10);

					SearchFacesByImageResult searchFacesByImageResult = rekognitionClient
							.searchFacesByImage(searchFacesByImageRequest);

					// System.out.println("Faces matching largest face in image from" + photo);
					List<FaceMatch> faceImageMatches = searchFacesByImageResult.getFaceMatches();
					if (faceImageMatches.size() == 0) {
						System.out.println(i + "번째 사진에서 매칭된 id를 찾지 못했 습니다.");
						i++;
						continue;
					} else {
						for (FaceMatch face : faceImageMatches) {

							System.out.println("");
							// System.out.println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(face));
							System.out.println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(face));
							// System.out.println("face id : 46d4440f-8598-41ce-9c2c-9cd3d55b9ab5");
							System.out.println("");
							System.out.println();
							//System.out.println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(face));
							String target = "externalImageId";
							String data = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(face);
							int target_num = data.indexOf(target) + 20;
							String phone_number = data.substring(target_num,
									data.substring(target_num).indexOf(".jpg") + target_num);
							System.out.println("폰번호 : " + phone_number);

							SMS sms = new SMS();
							sms.sendSMS(phone_number, i);

							RequestBucket bucket = new RequestBucket();
							bucket.BucketToBucket("violence-image", "detected-image", photos[i]);

							// System.out.println(face);
						}
						System.out.println(i + "번째 frame에서 찾았습니다.");
						break;

					}
					// flag=1;
				} catch (Exception e) {
					System.out.println(i + "번째 사진에서 매칭된 id를 찾지 못했 습니다.");
					i++;
					continue;
				}
			}

		}
	}
}