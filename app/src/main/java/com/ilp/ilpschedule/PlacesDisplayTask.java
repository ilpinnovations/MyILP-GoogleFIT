package com.ilp.ilpschedule;

import java.util.HashMap;
import java.util.List;

import org.json.JSONObject;

import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class PlacesDisplayTask extends
		AsyncTask<Object, Integer, List<HashMap<String, String>>> {

	private static final String TAG = "PlaceDisplayTask";
	JSONObject googlePlacesJson;
	GoogleMap googleMap;
	int resId;
	int PROXIMITY_RADIUS;

	@Override
	protected List<HashMap<String, String>> doInBackground(Object... inputObj) {

		List<HashMap<String, String>> googlePlacesList = null;
		Places placeJsonParser = new Places();

		try {
			googleMap = (GoogleMap) inputObj[0];
			googlePlacesJson = new JSONObject((String) inputObj[1]);
			resId = (Integer) inputObj[2];
			PROXIMITY_RADIUS = (Integer) inputObj[3];
			googlePlacesList = placeJsonParser.parse(googlePlacesJson);
		} catch (Exception e) {
			Log.d("Exception", e.toString());
		}
		return googlePlacesList;
	}

	@Override
	protected void onPostExecute(List<HashMap<String, String>> list) {
		Log.d(TAG, "data parsed");
		googleMap.clear();
		for (int i = 0; i < list.size(); i++) {
			MarkerOptions markerOptions = new MarkerOptions();
			HashMap<String, String> googlePlace = list.get(i);
			double lat = Double.parseDouble(googlePlace.get("lat"));
			double lng = Double.parseDouble(googlePlace.get("lng"));
			String placeName = googlePlace.get("place_name");
			String vicinity = googlePlace.get("vicinity");
			LatLng latLng = new LatLng(lat, lng);
			markerOptions.position(latLng);
			markerOptions.title(placeName + " : " + vicinity);
			if (resId > 0)
				markerOptions.icon(BitmapDescriptorFactory.fromResource(resId));
			googleMap.addMarker(markerOptions);
			Log.d(TAG, i + " marker set to");
		}
	}
}