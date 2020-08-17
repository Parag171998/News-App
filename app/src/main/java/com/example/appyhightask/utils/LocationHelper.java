package com.example.appyhightask.utils;

import android.app.Activity;
import android.content.Context;
import android.content.IntentSender;
import android.location.LocationManager;
import android.widget.Toast;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.SettingsClient;

public class LocationHelper {

	private static final int GPS_REQUEST = 100;
	private static final int REQUEST_INTERVAL = 1000;
	private static final int REQUEST_FASTEST_INTERVAL = 1500;
	private final Context context;
	private final SettingsClient mSettingsClient;
	private final LocationSettingsRequest mLocationSettingsRequest;
	private final LocationManager locationManager;
	private final LocationRequest locationRequest;

	public LocationHelper(Context context) {
		this.context = context;
		locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
		mSettingsClient = LocationServices.getSettingsClient(context);
		locationRequest = LocationRequest.create();
		locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
		locationRequest.setInterval(REQUEST_INTERVAL);
		locationRequest.setFastestInterval(REQUEST_FASTEST_INTERVAL);
		LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
				.addLocationRequest(locationRequest);
		builder.setAlwaysShow(true);
		mLocationSettingsRequest = builder.build();
	}

	public LocationRequest getLocationRequest() {
		return locationRequest;
	}

	// method for turn on GPS
	public void turnGPSOn(final OnGPSStateChangeListener OnGPSStateChangeListener) {
		if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
			if (OnGPSStateChangeListener != null) {
				OnGPSStateChangeListener.onGPSStateChanged(true);
			}
		} else {
			mSettingsClient
					.checkLocationSettings(mLocationSettingsRequest)
					.addOnSuccessListener((Activity) context, locationSettingsResponse -> {
						if (OnGPSStateChangeListener != null) {
							OnGPSStateChangeListener.onGPSStateChanged(true);
						}
					})
					.addOnFailureListener((Activity) context, e -> {
						int statusCode = ((ApiException) e).getStatusCode();
						switch (statusCode) {
							case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
								try {
									// Show the dialog by calling startResolutionForResult(), and check the
									// result in onActivityResult().
									ResolvableApiException rae = (ResolvableApiException) e;
									rae.startResolutionForResult((Activity) context, GPS_REQUEST);
								} catch (IntentSender.SendIntentException sie) {
								}
								break;
							case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
								String errorMessage = "Error in starting Location! Please fix in Location Setting.";
								Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show();
								break;
						}
					});
		}
	}

	public interface OnGPSStateChangeListener {
		void onGPSStateChanged(boolean enabled);
	}
}