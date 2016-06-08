package com.ilp.ilpschedule;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.ilp.ilpschedule.model.Employee;
import com.ilp.ilpschedule.model.ILPLocation;
import com.ilp.ilpschedule.util.Constants;
import com.ilp.ilpschedule.util.Util;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LocationActivity extends ActionBarActivity implements
		ConnectionCallbacks, OnConnectionFailedListener {

	public static final String TAG = "com.ilp.ilpschedule.LocationActivity";

	private GoogleMap map;
	private LocationManager locationManager;
	private ImageButton imageButtonIlp, imageButtonMyLocation,
			imageButtonHostel, imageButtonSearch, imageButtonLocationNavigate;
	private ProgressBar progressBarSearchLocation;
	private EditText editTextSearch;
	private double latitude = 0;
	private double longitude = 0;
	private RequestQueue reqQue;
	private int PROXIMITY_RADIUS = 5000;
	private LocationRequest locationRequest;
	private GoogleApiClient googleApiClient;
	private LatLng touchedPointOnMap;
	private OnClickListener navigateButtonClickListner = new OnClickListener() {
		@Override
		public void onClick(View v) {

			if (touchedPointOnMap != null)
				fireNavigationIntent(touchedPointOnMap.longitude,
						touchedPointOnMap.latitude);
			else
				Util.toast(getApplicationContext(),
						"Touch a point on map to enable navigation");
		}
	};
	private Listener<String> searchPlacesSuccessListner = new Listener<String>() {
		public void onResponse(String result) {
			if (Util.checkString(result)) {
				Log.d(TAG, result);
				PlacesDisplayTask placesDisplayTask = new PlacesDisplayTask();
				Object[] toPass = new Object[4];
				toPass[0] = getMap();
				toPass[1] = result;
				toPass[2] = R.drawable.ic_marker;
				toPass[3] = PROXIMITY_RADIUS;
				placesDisplayTask.execute(toPass);
			}
			hideProgressLocationSearch();
		};
	};
	private ErrorListener searchPlacesErrorListner = new ErrorListener() {

		@Override
		public void onErrorResponse(VolleyError error) {
			Log.d(TAG, "error" + error);
			Util.toast(getApplicationContext(),
					getString(R.string.toast_loc_search_error));
			hideProgressLocationSearch();
		};
	};
	private OnClickListener imageButtonIlpClickListner = new OnClickListener() {
		@Override
		public void onClick(View v) {

			clear();
			Employee emp = Util.getEmployee(getApplicationContext());
			List<ILPLocation> ilp_locs = Util.getLocations(emp.getLocation(),
					Constants.LOCATIONS.TYPE.ILP);
			if (ilp_locs.size() > 0) {
				Util.toast(getApplicationContext(), "Tracking your ILP center");
				for (ILPLocation loc : ilp_locs) {
					LatLng latLon = new LatLng(loc.getLat(), loc.getLon());
					getMap().moveCamera(CameraUpdateFactory.newLatLng(latLon));
					getMap().animateCamera(CameraUpdateFactory.zoomTo(15));
					getMap().addMarker(
							new MarkerOptions()
									.position(latLon)
									.title(loc.getName())
									.icon(BitmapDescriptorFactory
											.fromResource(R.drawable.ic_ilp))
									.flat(true));

				}
			} else {
				Util.toast(getApplicationContext(), "No ILP found");
			}
		}
	};
	private OnClickListener imageButtonHostelClickListner = new OnClickListener() {

		@Override
		public void onClick(View v) {

			clear();
			Employee emp = Util.getEmployee(getApplicationContext());
			List<ILPLocation> hotel_locs = Util.getLocations(emp.getLocation(),
					Constants.LOCATIONS.TYPE.HOSTEL);
			if (hotel_locs.size() > 0) {
				Util.toast(getApplicationContext(), "Tracking Hostels Near your ILP");
				for (ILPLocation loc : hotel_locs) {
					LatLng latLon = new LatLng(loc.getLat(), loc.getLon());
					getMap().moveCamera(CameraUpdateFactory.newLatLng(latLon));
					getMap().animateCamera(CameraUpdateFactory.zoomTo(15));
					getMap().addMarker(
							new MarkerOptions()
									.position(latLon)
									.title(loc.getName())
									.icon(BitmapDescriptorFactory
											.fromResource(R.drawable.ic_hostel))
									.flat(true));

				}
			} else {
				Util.toast(getApplicationContext(), "No hostels found");
			}
		}
	};
	private OnClickListener imageButtonMyLocationClickListner = new OnClickListener() {

		@Override
		public void onClick(View v) {
			Util.toast(getApplicationContext(), "Tracking your location ...");
googleApiClient.disconnect();
            googleApiClient.connect();

			clear();
		}
	};
	private LocationListener locationListner = new LocationListener() {

		@Override
		public void onLocationChanged(Location location) {
			//Util.toast(getApplicationContext(), "got location");
			latitude = location.getLatitude();
			longitude = location.getLongitude();
		}
	};
	private MarkerOptions touchedMapMarkerOptions = null;

	private class GetAddressAsyncTask extends AsyncTask<Object, Void, Address> {

		@Override
		protected Address doInBackground(Object... params) {
			Geocoder geocoder = (Geocoder) params[0];
			LatLng latLng = (LatLng) params[1];
			List<Address> address = null;
			try {
				address = geocoder.getFromLocation(latLng.latitude,
						latLng.longitude, 1);
			} catch (IOException ex) {
				Log.d(TAG, "error fetching address" + ex.getLocalizedMessage());

			}
			if (address != null && address.size() > 0)
				return address.get(0);
			return null;
		}

		@Override
		protected void onPostExecute(Address address) {
			if (address != null && address.getMaxAddressLineIndex() > 0)
				touchedMapMarkerOptions.title(address.getAddressLine(0));
			Util.toast(getApplicationContext(), "Unknown location");
		}
	}

	private void fireNavigationIntent(double lon, double lat) {


        Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                Uri.parse("http://maps.google.com/maps?saddr="+latitude+","+longitude+"&daddr="+lat+","+lon+""));
        startActivity(intent);


		//Uri intentUri = Uri.parse("google.navigation:q=" + lon + "," + lat
			//	+ "&mode=d");
		//Intent intent = new Intent(Intent.ACTION_VIEW, intentUri);
		//startActivity(intent);
	}

	private OnMapClickListener mapClickListner = new OnMapClickListener() {

		@Override
		public void onMapClick(LatLng latLng) {
			clear();
            googleApiClient.disconnect();
            googleApiClient.connect();
			touchedMapMarkerOptions = new MarkerOptions().position(latLng)
					.draggable(true).title("Navigate to this place").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE));
			getMap().addMarker(touchedMapMarkerOptions);
			touchedPointOnMap = latLng;
			imageButtonLocationNavigate
					.setImageResource(R.drawable.ic_navigate);
			imageButtonLocationNavigate.setEnabled(true);

            Util.toast(getApplicationContext(),"Now Click on the navigator button to start navigation !");
		}
	};

	private void clear() {
		getMap().clear();
		imageButtonLocationNavigate
				.setImageResource(R.drawable.ic_navigate_inactive);
		touchedPointOnMap = null;
	}

	private void showProgressLocationSearch() {
		progressBarSearchLocation.setVisibility(View.VISIBLE);
		imageButtonSearch.setVisibility(View.GONE);
	}

	private void hideProgressLocationSearch() {
		progressBarSearchLocation.setVisibility(View.GONE);
		imageButtonSearch.setVisibility(View.VISIBLE);
	}

	private OnClickListener searchButtonClickListner = new OnClickListener() {

		@Override
		public void onClick(View v) {
			String type = editTextSearch.getText().toString().trim();
			if (type.length() > 0) {
				if (Util.hasInternetAccess(getApplicationContext())) {
					showProgressLocationSearch();
					Map<String, String> params = new HashMap<>();
					params.put(Constants.NETWORK_PARAMS.MAP.LOCATION, latitude
							+ "," + longitude);
					params.put(Constants.NETWORK_PARAMS.MAP.RADIUS,
							String.valueOf(PROXIMITY_RADIUS));
					params.put(Constants.NETWORK_PARAMS.MAP.TYPES, type);
					params.put(Constants.NETWORK_PARAMS.MAP.SENSOR,
							String.valueOf(true));
					params.put(Constants.NETWORK_PARAMS.MAP.KEY,
							Constants.GOOGLE_MAP_API_KEY);
					StringBuilder googlePlacesUrl = new StringBuilder(
							Constants.URL_GOOGLE_MAP_SEARCH).append(Util
							.getUrlEncodedString(params));
					Log.d(TAG, "ping at ->>" + googlePlacesUrl.toString());
					StringRequest request = new StringRequest(
							googlePlacesUrl.toString(),
							searchPlacesSuccessListner,
							searchPlacesErrorListner);
					getReqQueue().add(request);
				} else {
					Util.toast(getApplicationContext(),
							getString(R.string.toast_no_internet));
				}
			}
		}
	};

	protected synchronized void buildGoogleApiClient() {
		googleApiClient = new GoogleApiClient.Builder(getApplicationContext())
				.addConnectionCallbacks(this)
				.addOnConnectionFailedListener(this)
				.addApi(LocationServices.API).build();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_location);
		// create google api client
		buildGoogleApiClient();
		googleApiClient.connect();
		// check for GPS
		checkGps();
		// check play services
		if (!Util.isGooglePlayServicesAvailable(this)) {
			finish();
		}
		// set ui elements
		imageButtonIlp = (ImageButton) findViewById(R.id.imageButtonLocationIlp);
		imageButtonIlp.setOnClickListener(imageButtonIlpClickListner);
		imageButtonMyLocation = (ImageButton) findViewById(R.id.imageButtonLocationMyLocation);
		imageButtonMyLocation
				.setOnClickListener(imageButtonMyLocationClickListner);
		imageButtonHostel = (ImageButton) findViewById(R.id.imageButtonLocationHostel);
		imageButtonHostel.setOnClickListener(imageButtonHostelClickListner);
		imageButtonSearch = (ImageButton) findViewById(R.id.imageButtonLocationSearch);
		imageButtonSearch.setOnClickListener(searchButtonClickListner);

		imageButtonLocationNavigate = (ImageButton) findViewById(R.id.imageButtonLocationNavigate);
		imageButtonLocationNavigate
				.setOnClickListener(navigateButtonClickListner);

		editTextSearch = (EditText) findViewById(R.id.editTextLocationSearch);
		progressBarSearchLocation = (ProgressBar) findViewById(R.id.progressBarLocationSearch);

		// set to a default ILP location depending on persons ILP
		Employee emp = Util.getEmployee(getApplicationContext());
		List<ILPLocation> locs = Util.getLocations(emp.getLocation(),
				Constants.LOCATIONS.TYPE.ILP);
		if (locs.size() > 0) {
			ILPLocation loc = locs.get(0);
			longitude = loc.getLon();
			latitude = loc.getLat();
		}
	}

	private GoogleMap getMap() {
		if (map == null) {
			SupportMapFragment fragment = ((SupportMapFragment) getSupportFragmentManager()
					.findFragmentById(R.id.fragmentLocationMap));
			if (fragment != null)
				map = fragment.getMap();
		}
		return map;
	}

	@Override
	protected void onStart() {
		initMap();

		super.onStart();
	}

	private void initMap() {
		getMap().setMapType(GoogleMap.MAP_TYPE_NORMAL);
		getMap().setMyLocationEnabled(true);
		getMap().getUiSettings().setZoomControlsEnabled(false);
		getMap().getUiSettings().setCompassEnabled(true);
		getMap().getUiSettings().setRotateGesturesEnabled(true);
		getMap().getUiSettings().setZoomGesturesEnabled(true);
		getMap().setOnMapClickListener(mapClickListner);
	}

	private RequestQueue getReqQueue() {
		if (reqQue == null)
			reqQue = Volley.newRequestQueue(getApplicationContext());
		return reqQue;
	}

	public void RequestLocationUpdates() {
		if (locationRequest == null) {
			locationRequest = new LocationRequest();
			locationRequest.setInterval(5000);
			locationRequest.setFastestInterval(10000);
			locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
		}

		try{
			LocationServices.FusedLocationApi.requestLocationUpdates(
					googleApiClient, locationRequest, locationListner);
		}
		catch (Exception ex){
			Toast.makeText(getApplicationContext(),"Please enable location permissions from app setings",Toast.LENGTH_LONG).show();
		}

	}

	private void checkGps() {
		if (!getLocationManager().isProviderEnabled(
				LocationManager.GPS_PROVIDER)) {
			buildAlertMessageNoGps();
		}
	}

	private void buildAlertMessageNoGps() {
		final AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage(
				"Your GPS seems to be disabled, do you want to enable it?")
				.setCancelable(false)
				.setPositiveButton("Yes",
						new DialogInterface.OnClickListener() {
							public void onClick(final DialogInterface dialog,
									final int id) {
								startActivity(new Intent(
										android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
							}
						})
				.setNegativeButton("No", new DialogInterface.OnClickListener() {
					public void onClick(final DialogInterface dialog,
							final int id) {
						dialog.cancel();
					}
				});
		final AlertDialog alert = builder.create();
		alert.show();
	}

	private LocationManager getLocationManager() {
		if (locationManager == null) {
			locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		}
		return locationManager;
	}

	@Override
	public void onConnectionFailed(ConnectionResult arg0) {
		Util.toast(getApplicationContext(), "gps connection failed");
	}

	@Override
	public void onConnected(Bundle bundle) {
		Location location = LocationServices.FusedLocationApi
				.getLastLocation(googleApiClient);
		if (location != null) {
			try{
				latitude = location.getLatitude();
				longitude = location.getLongitude();
				LatLng latLng = new LatLng(latitude, longitude);

				getMap().addMarker(new MarkerOptions().position(latLng)).setVisible(true);
				getMap().moveCamera(CameraUpdateFactory.newLatLng(latLng));
				getMap().animateCamera(CameraUpdateFactory.zoomTo(15));
			}
			catch (Exception ex){
				Toast.makeText(getApplicationContext(),"Please enable location permissions from app setings",Toast.LENGTH_LONG).show();
			}


			//Util.toast(getApplicationContext(), "got last location");
		} else {
			//Util.toast(getApplicationContext(), "no last location");
            RequestLocationUpdates();
		}
		//Util.toast(getApplicationContext(), "requesting for location updates");

	}

	@Override
	public void onConnectionSuspended(int arg0) {
		//Util.toast(getApplicationContext(), "gapi connection suspended");
	}

	@Override
	protected void onResume() {
		googleApiClient.connect();
		super.onResume();
	}

	@Override
	protected void onPause() {
		super.onPause();
		stopLocationUpdates();
		googleApiClient.disconnect();
	}

	private void stopLocationUpdates() {
		LocationServices.FusedLocationApi.removeLocationUpdates(
				googleApiClient, locationListner);
	}


}