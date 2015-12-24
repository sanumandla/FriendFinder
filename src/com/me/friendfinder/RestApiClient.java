package com.me.friendfinder;


import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import com.google.gson.Gson;

import android.annotation.SuppressLint;
import android.support.annotation.NonNull;
import android.util.Log;

@SuppressLint("NewApi") public class RestApiClient {
		
	/**
	 * Make a REST API GET request
	 * 
	 * @param url API resource
	 * @return json on success / null on failure
	 */
	public static String get(@NonNull String url) {
		if (url == null || url.isEmpty()) {
			Log.i(RestApiClient.class.getName(), "Invalid URL");
			return null;
		}
		
	    HttpClient httpClient = new DefaultHttpClient();

	    // Prepare a request object
	    HttpGet httpGet = new HttpGet(url); 

	    // Execute the request
	    HttpResponse response;
	    
	    try {
	        response = httpClient.execute(httpGet);

	        // Examine the response status
	        Log.i(RestApiClient.class.getName(), response.getStatusLine().toString());

	        // Get hold of the response entity
	        HttpEntity entity = response.getEntity();	        
	        if (entity != null) {
	            // A Simple JSON Response Read
	        	String json = EntityUtils.toString(entity);
	        	Log.i(RestApiClient.class.getName(), "Response: " + json);	        	
	        		        		        	
	        	return json;
	        }
	    } catch (Exception e) {	  
	    	e.printStackTrace();
	    	Log.i(RestApiClient.class.getName(), "Get request failed: " + e.getMessage());	    	
	    }
	    
	    return null;
	}
	
	
	/**
	 * Make a REST API POST request
	 * 
	 * @param url API resource
	 * @param json payload
	 * @return json on success / null on failure
	 */
	public static String post(@NonNull String url, String json) {
		if (url == null || url.isEmpty()) {
			Log.i(RestApiClient.class.getName(), "Invalid URL");
			return null;
		}
		
		// If string is null then make it empty
		json = json == null ? "" : json;				
		
		HttpClient httpClient = new DefaultHttpClient();		

	    // Prepare a request object
		HttpPost httpPost = new HttpPost(url);		
		
		// Execute the request
	    HttpResponse response;
	    
	    try {
	    	StringEntity stringEntity = new StringEntity(json);			
	    	httpPost.setEntity(stringEntity);
	    	httpPost.setHeader("Accept", "application/json");
	    	httpPost.setHeader("Content-Type", "application/json");
	    	
	        response = httpClient.execute(httpPost);

	        // Examine the response status
	        Log.i(RestApiClient.class.getName(), response.getStatusLine().toString());

	        // Get hold of the response entity
	        HttpEntity entity = response.getEntity();	        
	        if (entity != null) {
	            // A Simple JSON Response Read
	        	String responseJson = EntityUtils.toString(entity);
	        	Log.i(RestApiClient.class.getName(), "Response: " + responseJson);
	        	
	        	return responseJson;
	        }
	    } catch (Exception e) {	  
	    	e.printStackTrace();
	    	Log.i(RestApiClient.class.getName(), "Get request failed: " + e.getMessage());
	    }
	    
	    return null;
	}
	
	
	/**
	 * Make a REST API PUT request
	 * 
	 * @param url API resource
	 * @param json payload
	 * @return json on success / null on failure
	 */
	public static String put(@NonNull String url, String json) {
		if (url == null || url.isEmpty()) {
			Log.i(RestApiClient.class.getName(), "Invalid URL");
			return null;
		} 
		
		// If string is null then make it empty
		json = json == null ? "" : json;				
		
		HttpClient httpClient = new DefaultHttpClient();		

	    // Prepare a request object
		HttpPut httpPut = new HttpPut(url);		
		
		// Execute the request
	    HttpResponse response;
	    
	    try {
	    	StringEntity stringEntity = new StringEntity(json);			
	    	httpPut.setEntity(stringEntity);
	    	httpPut.setHeader("Accept", "application/json");
	    	httpPut.setHeader("Content-Type", "application/json");
	    	
	        response = httpClient.execute(httpPut);

	        // Examine the response status
	        Log.i(RestApiClient.class.getName(), response.getStatusLine().toString());

	        // Get hold of the response entity
	        HttpEntity entity = response.getEntity();	        
	        if (entity != null) {
	            // A Simple JSON Response Read
	        	String responseJson = EntityUtils.toString(entity);
	        	Log.i(RestApiClient.class.getName(), "Response: " + responseJson);
	        	
	        	return responseJson;
	        }
	    } catch (Exception e) {	  
	    	e.printStackTrace();
	    	Log.i(RestApiClient.class.getName(), "Get request failed: " + e.getMessage());
	    }
	    
	    return null;
	}
			
}
