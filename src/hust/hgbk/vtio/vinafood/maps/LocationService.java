package hust.hgbk.vtio.vinafood.maps;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient.ConnectionCallbacks;
import com.google.android.gms.common.GooglePlayServicesClient.OnConnectionFailedListener;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationRequest;

public class LocationService implements ConnectionCallbacks,
		OnConnectionFailedListener {
	private Context context;
	private static LocationService locationService;
	private CustomLocationListener locationListener;
	private Location location = null;

	// api for update with LocationManager
	private LocationManager locationManager;
	private Timer removeUpdateTimer;
	private LocationListener locationManagerListener;

	// api for update with GoogleServiceApi
	private LocationRequest locationRequest;
	private LocationClient locationClient;

	private Timer getLastLocationTimer;
	private Timer expirationTimer;

	public static final long EXPIRATION_TIME = 25000;
	public static final int LOCATION_SETTING_CODE = 10;
	private final int UPDATE_INTERVAL = 5000;
	private final long FASTEST_INTERVAL = 500;
	private final long TIMEOUT_FOR_UPDATE = 8000;

	private boolean isUpdated = false;
	private static boolean isUpdating = false;

	public static LocationService getInstance(Context context) {
		return new LocationService(context);
	}

	public LocationService(Context context) {
		this.context = context;
	}

	private int timeCountForGetLastLocation = 0;
	private int timeCountForExpiration = 0;

	public void updateLocation(Context context, long expirationTime,
			final CustomLocationListener locationListener) {
		this.context = context;
		this.locationListener = locationListener;
		isUpdated = false;

		locationManager = (LocationManager) context
				.getSystemService(Context.LOCATION_SERVICE);
		List<String> providers = checkProvider();
		if (providers.size() == 0) {
			((Activity) context).runOnUiThread(new Runnable() {

				@Override
				public void run() {
					locationListener.onProviderDisabled();
				}
			});

			return;
		}

		isUpdating = true;
		if (checkGooglePlayServiceAvailable(context)) {
			updateWithGoogleServiceAPI(context, expirationTime,
					locationListener);
		} else {
			for (int i = 0; i < providers.size(); i++) {
				updateWithLocationManager(context, providers.get(i),
						expirationTime, locationListener);
			}
		}

		timeCountForGetLastLocation = 0;
		getLastLocationTimer = new Timer();
		getLastLocationTimer.schedule(new TimerTask() {

			@Override
			public void run() {
				if (!isUpdated && isUpdating) {
					if (locationClient != null) {
						location = locationClient.getLastLocation();
						if (location != null) {
						}
					} else if (locationManager != null) {
						location = locationManager
								.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
						if (location == null) {
							location = locationManager
									.getLastKnownLocation(LocationManager.GPS_PROVIDER);
						}
					}

					if (location != null) {
						((Activity) LocationService.this.context)
								.runOnUiThread(new Runnable() {

									@Override
									public void run() {
										LocationService.this.locationListener
												.onGetLastLocation(location);
									}
								});
					}
				}

				this.cancel();
				getLastLocationTimer.purge();
			}
		}, TIMEOUT_FOR_UPDATE, TIMEOUT_FOR_UPDATE);

		expirationTimer = new Timer();
		expirationTimer.schedule(new TimerTask() {

			@Override
			public void run() {
				stopUpdate();
				this.cancel();
				expirationTimer.purge();
			}
		}, expirationTime, expirationTime);
	}

	public void updateWithGoogleServiceAPI(Context context,
			long expirationTime, final CustomLocationListener locationListener) {
		locationRequest = LocationRequest.create();
		locationRequest.setInterval(UPDATE_INTERVAL);
		locationRequest.setFastestInterval(FASTEST_INTERVAL);
		locationRequest
				.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
		locationRequest.setExpirationDuration(expirationTime);

		locationClient = new LocationClient(context, this, this);
		locationClient.connect();
	}

	private int count;

	public void updateWithLocationManager(final Context context,
			String provider, long expirationTime,
			CustomLocationListener locationListenner) {
		locationManagerListener = new LocationListener() {

			@Override
			public void onStatusChanged(String provider, int status,
					Bundle extras) {

			}

			@Override
			public void onProviderEnabled(String provider) {

			}

			@Override
			public void onProviderDisabled(String provider) {

			}

			@Override
			public void onLocationChanged(final Location location) {
				isUpdated = true;
				((Activity) context).runOnUiThread(new Runnable() {

					@Override
					public void run() {
						LocationService.this.locationListener
								.onLocationChanged(location);
					}
				});
			}
		};

		locationManager.requestLocationUpdates(provider, 0, 0,
				locationManagerListener);
	}

	public boolean isProviderDisable() {
		if (checkProvider().size() == 0) {
			return true;
		} else {
			return false;
		}
	}

	private List<String> checkProvider() {
		ArrayList<String> listProviders = new ArrayList<String>();
		LocationManager locationManager = (LocationManager) context
				.getSystemService(Context.LOCATION_SERVICE);
		if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
			listProviders.add(LocationManager.GPS_PROVIDER);
		}

		if (locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
			listProviders.add(LocationManager.NETWORK_PROVIDER);
		}

		return listProviders;
	}

	private boolean checkGooglePlayServiceAvailable(Context context) {
		int resultCode = GooglePlayServicesUtil
				.isGooglePlayServicesAvailable(context);
		if (resultCode == ConnectionResult.SUCCESS) {
			return true;
		} else {
			return false;
		}
	}

	public void stopUpdate() {
		if (locationManagerListener != null) {
			locationManager.removeUpdates(locationManagerListener);
		}

		if (locationClient != null) {
			locationClient.disconnect();
		}

		isUpdating = false;
		if (context == null)
			return;
		((Activity) context).runOnUiThread(new Runnable() {

			@Override
			public void run() {
				locationListener.onLocationStopUpdate();
			}
		});

	}

	public static boolean isUpdating() {
		return isUpdating;
	}

	@Override
	public void onConnectionFailed(final ConnectionResult result) {
		((Activity) context).runOnUiThread(new Runnable() {

			@Override
			public void run() {
				locationListener.onLocationUpdateFailed(result);
			}
		});
	}

	@Override
	public void onConnected(Bundle connectionHint) {
		com.google.android.gms.location.LocationListener listener = new com.google.android.gms.location.LocationListener() {

			@Override
			public void onLocationChanged(final Location location) {
				isUpdated = true;
				((Activity) context).runOnUiThread(new Runnable() {

					@Override
					public void run() {
						locationListener.onLocationChanged(location);
					}
				});
			}
		};

		if (locationClient.isConnected()) {
			locationClient.requestLocationUpdates(locationRequest, listener);
			((Activity) context).runOnUiThread(new Runnable() {

				@Override
				public void run() {
					locationListener.onLocationServiceConnect();
				}
			});
		} else {
			locationClient.connect();
		}
	}

	@Override
	public void onDisconnected() {
		((Activity) context).runOnUiThread(new Runnable() {

			@Override
			public void run() {
				locationListener.onLocationServiceDisconnect();
			}
		});
	}
}
