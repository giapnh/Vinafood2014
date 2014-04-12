package hust.hgbk.vtio.vinafood.main;

import hust.hgbk.vtio.vinafood.R;
import hust.hgbk.vtio.vinafood.config.ServerConfig;
import hust.hgbk.vtio.vinafood.config.log;
import hust.hgbk.vtio.vinafood.constant.OntologyCache;
import hust.hgbk.vtio.vinafood.constant.VLocation;
import hust.hgbk.vtio.vinafood.constant.XmlAdapter;
import hust.hgbk.vtio.vinafood.database.SQLiteAdapter;
import hust.hgbk.vtio.vinafood.maps.CustomLocationListener;
import hust.hgbk.vtio.vinafood.maps.LocationService;
import hust.hgbk.vtio.vinafood.media.OnlineContentReader;
import hust.hgbk.vtio.vinafood.network.ConnectionManager;
import hust.hgbk.vtio.vinafood.ontology.simple.ClassDataSimple;
import hust.hgbk.vtio.vinafood.ontology.simple.LabelIconUriSimple;
import hust.hgbk.vtio.vinafood.vtioservice.ICoreService;
import hust.hgbk.vtio.vinafood.vtioservice.VtioCoreService;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Locale;
import java.util.StringTokenizer;

import ken.soapservicelib.proxy.SoapServiceProxy;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;

public class Splash extends Activity implements OnClickListener {
	public AsyncTask<Void, Void, Void> loadIconThread;
	public AsyncTask<Void, Void, Void> loadHostFileTask;
	public Context ctx;
	public static boolean isNewNetWork = false;
	TextView txtLoading;

	private boolean isLoadedIcon;
	boolean isLoadSuccess = false;
	TextView textView;
	LocationManager locationManager;
	LocationListener locationListener;
	private Class MainClass = MenuActivity.class;
	protected boolean isResume = true;
	private boolean isWaitingNetwork = false;
	private boolean isWaitingLocation = false;
	ArrayList<String> listHostContentStrings;
	VtioCoreService service = new VtioCoreService();
	private SoapServiceProxy<ICoreService> soapServiceProxy;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.splash);
		ctx = this;
		txtLoading = (TextView) findViewById(R.id.txt_loading);
		txtLoading.setText("Đang cấu hình hệ thống...");
		XmlAdapter.synConfig(this);
		if (ConnectionManager.isOnline(getBaseContext()) == false) {
			String msgtext = "";
			msgtext = getResources().getString(R.string.no_network_msg);
			Toast.makeText(getBaseContext(), msgtext, Toast.LENGTH_SHORT)
					.show();
			DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					switch (which) {
					case DialogInterface.BUTTON_POSITIVE:
						isWaitingNetwork = true;
						startActivity(new Intent(
								Settings.ACTION_WIRELESS_SETTINGS));
						break;

					case DialogInterface.BUTTON_NEGATIVE:
						startActivity(new Intent(ctx, MainClass));
						finish();
						break;
					}
				}
			};

			AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
			builder.setMessage(msgtext)
					.setPositiveButton(
							getResources().getString(R.string.network_setting),
							dialogClickListener)
					.setNegativeButton(
							getResources().getString(R.string.view_favorite),
							dialogClickListener).show();
		} else {
			doWhenStart();
		}

	}

	private void tryAgain() {
		if (ConnectionManager.isOnline(getBaseContext()) == false) {
			String msgtext = "";
			msgtext = getResources().getString(R.string.no_network_msg);
			DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					switch (which) {
					case DialogInterface.BUTTON_NEUTRAL:
						tryAgain();
						break;
					}
				}
			};

			AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
			builder.setMessage(msgtext)
					.setNeutralButton(
							getResources().getString(R.string.try_again),
							dialogClickListener).show();
		} else {
			doWhenStart();

		}
	}

	private void doWhenStart() {
		if (DinningServiceSearch.listSubClass == null) {
			DinningServiceSearch.listSubClass = new ArrayList<ClassDataSimple>();
		}
		log.m("Check and Create database");
		SQLiteAdapter sqLiteAdapter = SQLiteAdapter.getInstance(ctx);
		try {
			OntologyCache.preferUser = sqLiteAdapter.getAllPreferenceClass();
		} catch (Exception e) {
		}
		detectLocation();
		String city = XmlAdapter.getCityUri(ctx);
		if (city != null) {
			for (int i = 0; i < ServerConfig.cityString.length; i++) {
				if (city.equals(ServerConfig.cityString[i][2])) {
					ServerConfig.currentCityUri = ServerConfig.cityString[i][2];
					ServerConfig.currentCityLabel = ServerConfig.cityString[i][1];
					break;
				}
			}
		} else {
			ServerConfig.currentCityUri = ServerConfig.HANOI_URI;
			ServerConfig.currentCityLabel = "Hà Nội";
			XmlAdapter.saveCityUri(ctx, ServerConfig.HANOI_URI);
		}
		if (loadIconThread != null) {
			loadIconThread.cancel(true);
			loadIconThread = null;
		}
		loadIconThread = new LoadIconThread();
		if (!isLoadedIcon)
			loadIconThread.execute();

		if (loadHostFileTask != null) {
			loadHostFileTask.cancel(true);
			loadHostFileTask = null;
		}
		loadHostFileTask = new LoadHostFile();
		loadHostFileTask.execute();

		if (isNewNetWork) {
			OntologyCache.clearCache();
		}
	}

	private void setLanguage(String lang) {
		Locale locale;
		locale = new Locale(lang);

		// Locale.setDefault(locale);
		Configuration config = new Configuration();
		config.locale = locale;
		getBaseContext().getResources().updateConfiguration(config,
				getBaseContext().getResources().getDisplayMetrics());
	}

	class LoadIconThread extends AsyncTask<Void, Void, Void> {
		boolean isDisplayToast = false;

		protected void onPreExecute() {
			txtLoading.setText("Đang tải dữ liệu...");
		}

		@Override
		protected Void doInBackground(Void... arg0) {
			long a = System.currentTimeMillis();
			isLoadedIcon = doLoadIcon();
			long b = System.currentTimeMillis();
			while (b - a < 5500 || (!isResume && (b - a < 30000))
					|| (!VLocation.isGetedLocation() && (b - a < 35000))) {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				b = System.currentTimeMillis();
				if (isCancelled()) {
					break;
				}
			}

			return null;
		}

		@Override
		protected void onCancelled() {
			super.onCancelled();
		}

		@Override
		protected void onPostExecute(Void result) {
			try {
				locationManager.removeUpdates(locationListener);
			} catch (Exception e) {
				// TODO: handle exception
			}
			// Chay luon chuong trinh khi ket thuc loading
			if (!VLocation.isGetedLocation()) {
				Toast.makeText(ctx, "Can't detech your location by Provider!",
						Toast.LENGTH_SHORT).show();
			}
			// Wait for loading data
			startActivity(new Intent(ctx, MainClass));
			overridePendingTransition(R.anim.slide_in_right,
					R.anim.slide_out_left);
			finish();
		}

	}

	class LoadHostFile extends AsyncTask<Void, Void, Void> {
		@Override
		protected void onPreExecute() {
		}

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			listHostContentStrings = new OnlineContentReader()
					.getStringArrayFromURL("https://sites.google.com/site/diemdenviet/newhost.txt");

			return null;
		}

		@Override
		protected void onCancelled() {
			// TODO Auto-generated method stub
			super.onCancelled();
		}

		@Override
		protected void onPostExecute(Void result) {
			try {
				if (listHostContentStrings != null
						&& listHostContentStrings.get(1).equals("1")
						&& !isLoadedIcon) {
					ServerConfig.setServerSpecificIp(listHostContentStrings
							.get(0));
					if (loadIconThread != null) {
						loadIconThread.cancel(true);
					}
					doWhenStart();
				}
				if (listHostContentStrings != null) {
					float curVer = Float.parseFloat(getResources().getString(
							R.string.current_version));
					float newVer = Float.parseFloat(listHostContentStrings
							.get(2));
					if (newVer - curVer > 0.0f) {
						XmlAdapter.saveNewVersion(ctx,
								listHostContentStrings.get(2));
						// Toast.makeText(
						// ctx,
						// getResources().getString(
						// R.string.new_version_available)
						// .replace("xxx",
						// listHostContentStrings.get(2)),
						// Toast.LENGTH_LONG).show();
					}
				}
			} catch (IndexOutOfBoundsException e) {
				// TODO: handle exception
			} catch (NumberFormatException e) {
				// TODO: handle exception
			}

		}

	}

	public boolean doLoadIcon() {
		ArrayList<ArrayList<String>> allConceptIconArraylist = null;
		for (int i = 0; i < 3; i++) {
			allConceptIconArraylist = new VtioCoreService()
					.getAllConceptWithIcon(ctx);
			if (allConceptIconArraylist.size() > 0) {
				break;
			}
			if (i == 1) {
				ArrayList<String> list = new OnlineContentReader()
						.getStringArrayFromURL("https://sites.google.com/site/diemdenviet/newhost.txt");
				if (list.size() > 0) {
					ServerConfig.setServerSpecificIp(list.get(0));
				}
			}
		}
		Log.v("CONTEXT", "finish load concept");
		if (allConceptIconArraylist.size() == 0) {
			return false;
		}
		for (Iterator i = allConceptIconArraylist.iterator(); i.hasNext();) {
			ArrayList<String> s = (ArrayList<String>) i.next();
			OntologyCache.hashMapTypeLabelToUri.put(s.get(2), s.get(0));

			if (!OntologyCache.uriOfIcon.containsKey(s.get(0))) {

				String iconUrl = s.get(1);
				String iconUri = s.get(0);
				String label = s.get(2);
				String tempString = iconUrl;
				String iconName = "";
				StringTokenizer tokenizer = new StringTokenizer(tempString, "/");

				while (tokenizer.hasMoreTokens()) {
					iconName = tokenizer.nextToken();
				}

				tokenizer = new StringTokenizer(iconName, ".");
				tempString = tokenizer.nextToken();

				int iconID = getResources().getIdentifier(tempString,
						"drawable", getPackageName());

				if (iconID != 0) {
					// tức là đã có ảnh này trong thư mục drawable
					LabelIconUriSimple objectIcon = new LabelIconUriSimple(
							label, iconUri, iconUrl, iconID);
					OntologyCache.uriOfIcon.put(iconUri, objectIcon);
				} else {
					LabelIconUriSimple objectIcon = new LabelIconUriSimple(
							label, iconUri, iconUrl, R.drawable.new_category);
					OntologyCache.uriOfIcon.put(iconUri, objectIcon);
				}
			}

		}
		return true;
	}

	protected void onPause() {
		super.onPause();
		isResume = false;
	}

	protected void onResume() {
		super.onResume();
		if (isWaitingNetwork) {
			isWaitingNetwork = false;
			tryAgain();
		}
		if (isWaitingLocation && !isResume) {
			isWaitingLocation = false;
			detectLocation();
		}
		isResume = true;
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			try {
				loadIconThread.cancel(true);
			} catch (Exception e) {
				// TODO: handle exception
			}
			finish();

		}

		return super.onKeyDown(keyCode, event);
	}

	public void createDialogIfFail() {
		DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				switch (which) {
				case DialogInterface.BUTTON_POSITIVE:
					// Yes button clicked
					// pauseSound();
					loadIconThread = new LoadIconThread();
					loadIconThread.execute();
					break;

				case DialogInterface.BUTTON_NEGATIVE:
					// No button clicked
					finish();
					break;
				}
			}
		};

		AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
		builder.setMessage(getResources().getString(R.string.connect_fail))
				.setPositiveButton("Retry", dialogClickListener)
				.setNegativeButton("Exit", dialogClickListener).show();
	}

	private void detectLocation() {
		txtLoading.setText("Đang cập nhật vị trí...");
		LocationService locationService = new LocationService(ctx);
		locationService.updateLocation(ctx, LocationService.EXPIRATION_TIME,
				new CustomLocationListener() {

					@Override
					public void onProviderDisabled() {
						System.out.println("disable");
					}

					@Override
					public void onLocationUpdateFailed(ConnectionResult result) {
					}

					@Override
					public void onLocationStopUpdate() {
					}

					@Override
					public void onLocationServiceDisconnect() {
					}

					@Override
					public void onLocationServiceConnect() {
					}

					@Override
					public void onLocationChanged(Location location) {
						setGeoLocation(location);
					}

					@Override
					public void onGetLastLocation(Location location) {
					}
				});
	}

	@Override
	public void onClick(View v) {
		startActivity(new Intent(ctx, MainClass));
		finish();
	}

	private void setGeoLocation(Location location) {
		String lat = String.valueOf(location.getLatitude());
		String lon = String.valueOf(location.getLongitude());
		try {
			lat = lat.substring(0, 10);
		} catch (Exception e) {
		}
		try {
			lon = lon.substring(0, 10);
		} catch (Exception e) {
		}
		try {
			VLocation.getInstance().setGeo(Float.valueOf(lat),
					Float.valueOf(lon));
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
