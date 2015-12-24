package com.me.friendfinder;

import java.util.Date;

import com.google.gson.Gson;

import android.os.AsyncTask;
import android.util.Log;

public class RestApiTask extends AsyncTask<String, Void, RestApiClient> {

	@Override
	protected RestApiClient doInBackground(String... params) {
		// GET
//		RestApiClient.get("http://ec2-54-148-188-84.us-west-2.compute.amazonaws.com:8080/user?phone=916-850-5925");
		
		UserJson userJson = new UserJson();
		userJson.setPhoneNumber("408-414-2244");
		userJson.setEmail("test.me@yahoo.com");
		userJson.setImei("1234");
		userJson.setDateUpdated(new Date().toString());
		userJson.setFirstName("Martin");
		userJson.setLastName("Luther");
		userJson.setPetName("Mar");
		
		Gson gson = new Gson();
		String userObject = gson.toJson(userJson);
		
		// POST
//		RestApiClient.post("http://ec2-54-148-188-84.us-west-2.compute.amazonaws.com:8080/user", userObject);
		
		// PUT
		RestApiClient.put("http://ec2-54-148-188-84.us-west-2.compute.amazonaws.com:8080/user", userObject);		
		
		return null;
	}
	
}
