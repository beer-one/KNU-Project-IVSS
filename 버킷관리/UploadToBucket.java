package com.amazonaws.samples;

import java.io.File;
import java.util.Scanner;

import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.PutObjectRequest;


public class UploadToBucket {
	final static AmazonS3 s3 = AmazonS3ClientBuilder.standard().withCredentials(new ProfileCredentialsProvider()).withRegion("ap-northeast-2").build();
	private static final String bucketName = "dongwookbucket";
	private static final String filePath = "C:\\Users\\dongwook\\Desktop\\data\\";
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
			System.out.println("파일은 항상 path폴더에 존재해야된다.\n파일 이름 입력 : ");
			Scanner scan = new Scanner(System.in);
	        String fileName = scan.nextLine();//파일이름 입력받음

	        PutObjectRequest request = new PutObjectRequest(bucketName,fileName,new File(filePath+fileName));
	        s3.putObject(request);
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		
	}

}
