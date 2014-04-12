/*
 * Copyright (C) 2012 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package hust.hgbk.vtio.vinafood.main;

import hust.hgbk.vtio.vinafood.R;
import hust.hgbk.vtio.vinafood.config.log;
import hust.hgbk.vtio.vinafood.constant.DrawableGetter;
import hust.hgbk.vtio.vinafood.constant.VLocation;
import hust.hgbk.vtio.vinafood.vtioservice.FullDataInstance;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.directions.route.Routing;
import com.directions.route.RoutingListener;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient.ConnectionCallbacks;
import com.google.android.gms.common.GooglePlayServicesClient.OnConnectionFailedListener;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

public class ShowPlaceOnMap extends FragmentActivity implements
		ConnectionCallbacks, OnConnectionFailedListener, LocationListener,
		OnInfoWindowClickListener, OnMarkerClickListener, RoutingListener {

	private GoogleMap mMap;
	private LocationClient mLocationClient;
	FullDataInstance fullDataInstance;
	private LatLng currentLatlng;
	LatLng cuisineLocation;
	// private String type;

	private static final LocationRequest REQUEST = LocationRequest.create()
			.setInterval(5000) // 5 seconds
			.setFastestInterval(16) // 16ms = 60fps
			.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.map_show_place);
		Bundle bundle = getIntent().getExtras();
		fullDataInstance = (FullDataInstance) bundle
				.getSerializable("fullinstance");
		cuisineLocation = new LatLng(fullDataInstance.getLatitude(),
				fullDataInstance.getLongitude());
		/* Set up google map */
		SupportMapFragment fm = (SupportMapFragment) getSupportFragmentManager()
				.findFragmentById(R.id.map);
		mMap = fm.getMap();
		mMap.setMyLocationEnabled(true);
		mMap.setBuildingsEnabled(true);
		mMap.setTrafficEnabled(true);
		mMap.getUiSettings().setCompassEnabled(true);
		mMap.getUiSettings().setAllGesturesEnabled(true);
		/* Get current location */
		double currentGeoLat = VLocation.GEO_LAT_DEFAULT;
		try {
			currentGeoLat = VLocation.getInstance().getLatitude();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		double currentGeoLong = VLocation.GEO_LON_DEFAULT;
		try {
			currentGeoLong = VLocation.getInstance().getLongtitude();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		currentLatlng = new LatLng(currentGeoLat, currentGeoLong);
		CameraUpdate center = CameraUpdateFactory.newLatLng(currentLatlng);
		CameraUpdate zoom = CameraUpdateFactory.zoomTo(15);
		/* Move camera to current location */
		mMap.moveCamera(center);
		mMap.animateCamera(zoom);
		routing(Routing.TravelMode.WALKING);
	}

	/**
	 * Switch travel mode
	 * 
	 * @param mode
	 */
	public void routing(Routing.TravelMode mode) {
		Routing routing = new Routing(mode);
		routing.registerListener(this);
		routing.execute(currentLatlng, cuisineLocation);
	}

	/**
	 * Implementation of {@link LocationListener}.
	 */
	@Override
	public void onLocationChanged(Location location) {
		LatLng latLng = new LatLng(location.getLatitude(),
				location.getLongitude());
		CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLng(latLng);
		CameraUpdate cameraZoom = CameraUpdateFactory.zoomTo(17);
		mMap.moveCamera(cameraUpdate);
		mMap.moveCamera(cameraZoom);
	}

	@Override
	public void onConnected(Bundle connectionHint) {
		mLocationClient.requestLocationUpdates(REQUEST, this); //
	}

	@Override
	public void onDisconnected() {
	}

	@Override
	public void onConnectionFailed(ConnectionResult result) {
	}

	@Override
	public boolean onMarkerClick(final Marker marker) {
		// System.out.println("On marker click");
		// Dialog dialog = new Dialog(this,
		// android.R.style.Theme_Translucent_NoTitleBar);
		// dialog.setContentView(R.layout.marker_view_detail);
		// dialog.show();
		return false;
	}

	@Override
	public void onRoutingFailure() {

	}

	@Override
	public void onRoutingStart() {

	}

	@Override
	public void onRoutingSuccess(PolylineOptions mPolyOptions) {
		PolylineOptions polyoptions = new PolylineOptions();
		polyoptions.color(Color.BLUE);
		polyoptions.width(10);
		polyoptions.addAll(mPolyOptions.getPoints());
		mMap.addPolyline(polyoptions);

		// Start marker
		MarkerOptions options = new MarkerOptions();
		options.position(currentLatlng);
		options.icon(BitmapDescriptorFactory.fromResource(R.drawable.start_pin));
		mMap.addMarker(options);
		log.m("Type of location = " + fullDataInstance.getType());
		Marker myMarker = mMap.addMarker(new MarkerOptions()
				.position(cuisineLocation)
				.title(fullDataInstance.getLabel())
				.snippet(fullDataInstance.getAddress())
				.icon(BitmapDescriptorFactory.fromResource(DrawableGetter
						.getDrawable(fullDataInstance.getType()))));
	}

	@Override
	public void onInfoWindowClick(Marker arg0) {

	}

}
