package com.amazonaws.samples;

import java.util.HashMap;

import org.json.simple.JSONObject;

import net.nurigo.java_sdk.api.Message;
import net.nurigo.java_sdk.exceptions.CoolsmsException;
public class SMS {
	String api_key = "api_key";
	String api_secret = "api_secret";
	Message coolsms = new Message(api_key, api_secret);
	
	public void sendSMS(String phone_number,int index) {
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("to", phone_number.toString());
		params.put("from", "01036924340");
		params.put("type", "SMS");
		params.put("text", "자녀가 폭력의 피해자일 수 있습니다. 앱을 확인하세요!" + index + "번째  사진 확인");
		params.put("app_version", "test app 1.2"); // application name and version
		try {
			JSONObject obj = (JSONObject) coolsms.send(params);
			System.out.println(obj.toString());
		} catch (CoolsmsException e) {
			System.out.println(e.getMessage());
			System.out.println(e.getCode());
		}
	}
}
