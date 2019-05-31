package com.amazonaws.samples;

import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.services.rekognition.AmazonRekognition;
import com.amazonaws.services.rekognition.AmazonRekognitionClientBuilder;
import com.amazonaws.services.rekognition.model.FaceMatch;
import com.amazonaws.services.rekognition.model.Image;
import com.amazonaws.services.rekognition.model.S3Object;
import com.amazonaws.services.rekognition.model.SearchFacesByImageRequest;
import com.amazonaws.services.rekognition.model.SearchFacesByImageResult;
import java.util.List;
import java.util.Scanner;
import java.util.HashMap;
import org.json.simple.JSONObject;
import net.nurigo.java_sdk.api.Message;
import net.nurigo.java_sdk.exceptions.CoolsmsException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.*;

public class SearchFaceMatchingImageCollection {
	public static final String collectionId = "collectionUser";
	public static final String bucket = "violence-image";
	// public static final String photo = "test3.jpg";
	static String file_name;
	public static String[] photos = new String[51];

	public static void main(String[] args) throws Exception {

		AmazonRekognition rekognitionClient = AmazonRekognitionClientBuilder.standard()
				.withCredentials(new ProfileCredentialsProvider()).withRegion("ap-northeast-2").build();

		ObjectMapper objectMapper = new ObjectMapper();
		String api_key = "api_key";
		String api_secret = "api_secret";
		Message coolsms = new Message(api_key, api_secret);
		while (true) {
			System.out.println("폴더 이름 입력 : ");
			Scanner scan = new Scanner(System.in);
			file_name = scan.nextLine();
			for (int i = 0; i < 40; i++) {

				photos[i] = file_name + "_" + i + ".png";

			}
			int i = 0;
			// Get an image object from S3 bucket.
			while (i <= 50) {
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
							System.out.println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(face));
							String target = "externalImageId";
							String data = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(face);
							int target_num = data.indexOf(target) + 20;
							String phone_number = data.substring(target_num,
									data.substring(target_num).indexOf(".jpg") + target_num);
							System.out.println("폰번호 : " + phone_number);
							HashMap<String, String> params = new HashMap<String, String>();
							params.put("to", phone_number.toString());
							params.put("from", "01036924340");
							params.put("type", "SMS");
							params.put("text", "자녀가 폭력의 피해자일 수 있습니다. 앱을 확인하세요!" + i + "번째  사진 확인");
							params.put("app_version", "test app 1.2"); // application name and version

							try {
								JSONObject obj = (JSONObject) coolsms.send(params);
								System.out.println(obj.toString());
							} catch (CoolsmsException e) {
								System.out.println(e.getMessage());
								System.out.println(e.getCode());
							}
							
							//System.out.println(face);
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