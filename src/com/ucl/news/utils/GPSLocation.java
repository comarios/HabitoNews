package com.ucl.news.utils;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

public class GPSLocation implements LocationListener {

	private Context context;

	public GPSLocation(Context _context) {
		context = _context;
	}

	@Override
	public void onLocationChanged(Location loc) {
		// TODO Auto-generated method stub

		String longitude = "Longitude: " + loc.getLongitude();
		String latitude = "Latitude: " + loc.getLatitude();
		
		System.out.println("Longitude: " + longitude + ", Latitude: " + latitude);
		Toast.makeText(context, "Longitude: " + longitude + ", Latitude: " + latitude, Toast.LENGTH_LONG).show();
		/*------- To get city name from coordinates -------- */
		String cityName = null;
		Geocoder gcd = new Geocoder(context, Locale.getDefault());
		List<Address> addresses;
		try {
			addresses = gcd.getFromLocation(loc.getLatitude(),
					loc.getLongitude(), 1);
			if (addresses.size() > 0)
				System.out.println(addresses.get(0).getLocality());
			cityName = addresses.get(0).getLocality();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("longitude " + "\n" + latitude + "\n\nMy Current City is: "+ cityName);
		
	}

	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub

	}

}
