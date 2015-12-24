package com.me.friendfinder;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import android.support.v7.app.ActionBarActivity;
import android.telephony.TelephonyManager;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.annotation.SuppressLint;
import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

@SuppressLint("NewApi") public class MainActivity extends ActionBarActivity implements ConnectionCallbacks, 
	OnConnectionFailedListener, LocationListener {

	/**
	 * Logger Key
	 */
	private static final String TAG = "GET-CURRENT-LOCATION";
	
	private static final String DATE_FORMAT = "yyyy-MM-dd hh:mm:ss";
	
	/**
     * The desired interval for location updates. Inexact. Updates may be more or less frequent.
     */
	private static final long UPDATE_INTERVAL_IN_MILLISECONDS = 300000;

    /**
     * The fastest rate for active location updates. Exact. Updates will never be more frequent
     * than this value.
     */
    private static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS = UPDATE_INTERVAL_IN_MILLISECONDS / 2;
    
    // Keys for storing activity state in the Bundle.
    private static final String REQUESTING_LOCATION_UPDATES_KEY = "requesting-location-updates-key";
    private static final String LOCATION_KEY = "location-key";
    private static final String LAST_UPDATED_TIME_STRING_KEY = "last-updated-time-string-key";
	
	/**
     * Provides the entry point to Google Play services.
     */
    private GoogleApiClient mGoogleApiClient;
    
    /**
     * Stores parameters for requests to the FusedLocationProviderApi.
     */
    private LocationRequest mLocationRequest;
    
    /**
     * Represents a geographical location.
     */
    private Location mCurrentLocation;
    
    /**
     * Tracks the status of the location updates request.
     */
    private Boolean mRequestingLocationUpdates;
    
    /**
     * Time when the location was updated represented as a String.
     */
    private String mLastUpdateTime;
                
    private GoogleMap map;
    private SimpleDateFormat simpleDateFormat;
    private String username;
    private String phoneNumber;
	
	@SuppressLint("NewApi") @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
						
		simpleDateFormat = new SimpleDateFormat(DATE_FORMAT);		
		mRequestingLocationUpdates = true;		
		
		username = getUsername();
		Log.i(TAG, "Username of the device owner is " + username);
		
		phoneNumber = getPhoneNumber();
		Log.i(TAG, "Phone number of the device owner is " + phoneNumber);		
		
		// Update values using data stored in the Bundle.
//        updateValuesFromBundle(savedInstanceState);     
		
		new RestApiTask().execute();
		
		// Connect to google play services// Kick off the process of building a GoogleApiClient and requesting the LocationServices
        // API.
		buildGoogleApiClient();
		
		map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();			
	}
		
	/**
	 * Get phone number of the device owner 
	 * 
	 * @return phone number on success / null on failure
	 */
	public String getPhoneNumber() {
		TelephonyManager tMgr = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
		String mPhoneNumber = tMgr.getLine1Number();
		
		return mPhoneNumber;
	}
	
	/**
	 * Get username of the device owner
	 * 
	 * @return username on success / null on failure
	 */
	public String getUsername() {
	    AccountManager manager = AccountManager.get(this); 
	    Account[] accounts = manager.getAccountsByType("com.google"); 
	    List<String> possibleEmails = new LinkedList<String>();

	    for (Account account : accounts) {
	      // TODO: Check possibleEmail against an email regex or treat
	      // account.name as an email address only for certain account.type values.
	      possibleEmails.add(account.name);
	    }

	    if (!possibleEmails.isEmpty() && possibleEmails.get(0) != null) {
	        String email = possibleEmails.get(0);
	        String[] parts = email.split("@");

	        if (parts.length > 1) {
	            return parts[0];
	        }
	    }
	    
	    return null;
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		stopLocationUpdates();
	}	
	
	/**
     * Updates fields based on data stored in the bundle.
     *
     * @param savedInstanceState The activity state saved in the Bundle.
     */
    private void updateValuesFromBundle(Bundle savedInstanceState) {
        Log.i(TAG, "Updating values from bundle");
        if (savedInstanceState != null) {
            // Update the value of mRequestingLocationUpdates from the Bundle
            if (savedInstanceState.keySet().contains(REQUESTING_LOCATION_UPDATES_KEY)) {
                mRequestingLocationUpdates = savedInstanceState.getBoolean(REQUESTING_LOCATION_UPDATES_KEY);
                Log.i(TAG, "Location update status is " + mRequestingLocationUpdates);
            }

            // Update the value of mCurrentLocation from the Bundle
            if (savedInstanceState.keySet().contains(LOCATION_KEY)) {
                // Since LOCATION_KEY was found in the Bundle, we can be sure that mCurrentLocation
                // is not null.
                mCurrentLocation = savedInstanceState.getParcelable(LOCATION_KEY);
                Log.i(TAG, "Current location with latitude = " + mCurrentLocation.getLatitude() + " and" +
                		" longitude = " + mCurrentLocation.getLongitude());
            }

            // Update the value of mLastUpdateTime from the Bundle
            if (savedInstanceState.keySet().contains(LAST_UPDATED_TIME_STRING_KEY)) {
                mLastUpdateTime = savedInstanceState.getString(LAST_UPDATED_TIME_STRING_KEY);
                Log.i(TAG, "Last updated time is " + mLastUpdateTime);
            }            
        }
    }
	
	/**
     * Builds a GoogleApiClient. Uses the addApi() method to request the LocationServices API.
     */
    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        
        // Create location request
        createLocationRequest();
    }
    
    /**
     * Sets up the location request. Android has two location request settings:
     * {@code ACCESS_COARSE_LOCATION} and {@code ACCESS_FINE_LOCATION}. These settings control
     * the accuracy of the current location. This sample uses ACCESS_FINE_LOCATION, as defined in
     * the AndroidManifest.xml.
     * <p/>
     * When the ACCESS_FINE_LOCATION setting is specified, combined with a fast update
     * interval (5 seconds), the Fused Location Provider API returns location updates that are
     * accurate to within a few feet.
     * <p/>
     * These settings are appropriate for mapping applications that show real-time location
     * updates.
     */
    private void createLocationRequest() {
        mLocationRequest = new LocationRequest();

        // Sets the desired interval for active location updates. This interval is
        // inexact. You may not receive updates at all if no location sources are available, or
        // you may receive them slower than requested. You may also receive updates faster than
        // requested if other applications are requesting location at a faster interval.
        mLocationRequest.setInterval(UPDATE_INTERVAL_IN_MILLISECONDS);

        // Sets the fastest rate for active location updates. This interval is exact, and your
        // application will never receive updates faster than this value.
        mLocationRequest.setFastestInterval(FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS);

        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);       
    }       
       
    /**
     * Make a request to update current location
     */
    private void startLocationUpdates() {
    	Log.i(TAG, "startLocationUpdates | Send location update request");
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
    }
    
    /**
     * Stop sending location update requests
     */
    private void stopLocationUpdates() {
    	Log.i(TAG, "stopLocationUpdates | Stop location updates");
        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
    }
    
	@SuppressLint("NewApi") public void onMapReady(GoogleMap map, LatLng location) {		
		Log.i(TAG, "onMapReady | Current location with latitude = " + location.latitude + " and longitude = " +
				location.longitude);
		
		// Check if username is null and if so get phoneNumber. If even phoneNumber is null then
		// assign a default value
		String title;
		if (username == null || username.isEmpty()) {
			if (phoneNumber == null || phoneNumber.isEmpty()) {
				title = "User";
			} else {
				title = phoneNumber;
			}
		} else {
			title = username;
		}
		
		MarkerOptions mOptions = new MarkerOptions();
		mOptions.title(title);
        mOptions.snippet("My Location !!!");
        mOptions.position(location);
    
        // Clear map and add new markers
        map.clear();
		map.addMarker(mOptions);			
	}		
	
    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }
    
    @Override
    public void onResume() {
        super.onResume();
        // Within {@code onPause()}, we pause location updates, but leave the
        // connection to GoogleApiClient intact.  Here, we resume receiving
        // location updates if the user has requested them.

        if (mGoogleApiClient.isConnected() && mRequestingLocationUpdates) {
            startLocationUpdates();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Stop location updates to save battery, but don't disconnect the GoogleApiClient object.
        stopLocationUpdates();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }            

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}		    

	@Override
	public void onLocationChanged(Location location) {
        mCurrentLocation = location;
        LatLng currentLatLng = new LatLng(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude());
        
        // Render current user location on the map
        onMapReady(map, currentLatLng);
       
        Date currentDate = Calendar.getInstance().getTime();		
        mLastUpdateTime = simpleDateFormat.format(currentDate);
        
        Log.i(TAG, "onLocationChanged | Location updated on " + mLastUpdateTime);
    }
	
	@Override
	public void onConnected(Bundle arg0) {
		// If the initial location was never previously requested, we use
        // FusedLocationApi.getLastLocation() to get it. If it was previously requested, we store
        // its value in the Bundle and check for it in onCreate(). 
        //
        // Because we cache the value of the initial location in the Bundle, it means that if the
        // user launches the activity,
        // moves to a new location, and then changes the device orientation, the original location
        // is displayed as the activity is re-created.
        if (mCurrentLocation == null) {
            mCurrentLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            Log.i(TAG, "onConnected | Get current location = " + mCurrentLocation);
                        
            Date currentDate = Calendar.getInstance().getTime();		            
            mLastUpdateTime = simpleDateFormat.format(currentDate); 
            Log.i(TAG, "onConnected | Get current time = " + mLastUpdateTime);                           
        }
        
		//Convert Location to LatLng
		LatLng currentLatLng = new LatLng(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude());
        
        // Render current user location on the map
        onMapReady(map, currentLatLng);
        
        // Move the camera instantly to the GPS location with a zoom level of 15
	    map.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 15));

	    // Zoom in, animating the camera
	    map.animateCamera(CameraUpdateFactory.zoomTo(10), 2000, null);

        // If the user presses the Start Updates button before GoogleApiClient connects, we set
        // mRequestingLocationUpdates to true (see startUpdatesButtonHandler()). Here, we check
        // the value of mRequestingLocationUpdates and if it is true, we start location updates.
        if (mRequestingLocationUpdates) {
            startLocationUpdates();
        }
	}		
	
	@Override
	public void onConnectionFailed(ConnectionResult result) {
		// Refer to the javadoc for ConnectionResult to see what error codes might be returned in
        // onConnectionFailed.
		Log.i(TAG, "onConnectionFailed | Connection failed: ConnectionResult.getErrorCode() = " + result.getErrorCode());		
	}

	@Override
	public void onConnectionSuspended(int arg0) {
		// The connection to Google Play services was lost for some reason. We call connect() to
        // attempt to re-establish the connection.
        Log.i(TAG, "onConnectionSuspended | Connection suspended");
        mGoogleApiClient.connect();
	}
	
	/**
     * Stores activity data in the Bundle.
     */
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putBoolean(REQUESTING_LOCATION_UPDATES_KEY, mRequestingLocationUpdates);
        savedInstanceState.putParcelable(LOCATION_KEY, mCurrentLocation);
        savedInstanceState.putString(LAST_UPDATED_TIME_STRING_KEY, mLastUpdateTime);
        super.onSaveInstanceState(savedInstanceState);
    }
    
}
