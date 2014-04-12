package hust.hgbk.vtio.vinafood.main;

import hust.hgbk.vtio.vinafood.R;
import hust.hgbk.vtio.vinafood.config.log;
import hust.hgbk.vtio.vinafood.constant.DrawableGetter;
import hust.hgbk.vtio.vinafood.constant.VLocation;
import hust.hgbk.vtio.vinafood.database.SQLiteAdapter;
import hust.hgbk.vtio.vinafood.vtioservice.FullDataInstance;
import hust.hgbk.vtio.vinafood.vtioservice.VtioCoreService;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore.Images;
import android.support.v4.app.FragmentActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;
import android.widget.ViewFlipper;

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

public class PlaceDetails extends FragmentActivity implements
		ConnectionCallbacks, OnConnectionFailedListener, LocationListener,
		OnInfoWindowClickListener, OnMarkerClickListener, RoutingListener {
	final Activity activity = this;
	Context context;
	// Init for map
	private GoogleMap mMap;
	private LocationClient mLocationClient;
	private LatLng currentLatlng;
	LatLng cuisineLocation;
	private static final LocationRequest REQUEST = LocationRequest.create()
			.setInterval(5000) // 5 seconds
			.setFastestInterval(16) // 16ms = 60fps
			.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

	TextView waitTextView;
	VtioCoreService services;

	FullDataInstance fullDataInstance;

	TextView textViewTitle;
	ListView listViewProperties;

	Button btnBack, btnNext;
	ViewFlipper viewFlipper;
	String[] listIgnoreProperty = { "hasLongtitude", "hasGeoPoint",
			"hasLatitude", "hasMedia", "hasLocation" };
	private final int SHOW_NO_DATA_DIALOG = 0;

	TextView txtType;

	SQLiteAdapter sqLiteAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.place_details_layout);
		this.context = PlaceDetails.this;
		Bundle extra = getIntent().getExtras();
		fullDataInstance = new FullDataInstance();
		fullDataInstance.setUri(extra.getString("uri"));
		fullDataInstance.setAbstractInfo(extra.getString("abstractInfo"));
		fullDataInstance.setAddress(extra.getString("address"));
		fullDataInstance.setImageURL(extra.getString("imageURL"));
		fullDataInstance.setLabel(extra.getString("label"));
		fullDataInstance.setLatitude(extra.getDouble("latitude"));
		fullDataInstance.setLongitude(extra.getDouble("longitude"));
		fullDataInstance.setLocation(extra.getString("location"));
		fullDataInstance.setPhone(extra.getString("phone"));
		fullDataInstance.setRatingNum(extra.getInt("ratingNum"));
		fullDataInstance.setType(extra.getString("type"));
		fullDataInstance.setUri(extra.getString("uri"));
		fullDataInstance.setWellKnown(extra.getString("wellKnown"));
		/* Set up google map */
		cuisineLocation = new LatLng(fullDataInstance.getLatitude(),
				fullDataInstance.getLongitude());
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
		services = new VtioCoreService();
		// float longitude = -1;
		// float latitude = -1;
		// ArrayList<PropertyWithValue> listTemp = new
		// ArrayList<PropertyWithValue>();
		// new LoadInstanceDetails(listTemp).execute();
		this.sqLiteAdapter = SQLiteAdapter.getInstance(context);
		this.sqLiteAdapter.checkAndCreateDatabase();
		// Save this location information to RecentView data
		sqLiteAdapter.addPlaceToRecentViewTable(fullDataInstance);

		loadData();
	}

	public void routing(Routing.TravelMode mode) {
		Routing routing = new Routing(mode);
		routing.registerListener(this);
		routing.execute(currentLatlng, cuisineLocation);
	}

	ImageView imageView;
	WebView webView;

	private void loadData() {
		ToggleButton favoristLay = (ToggleButton) findViewById(R.id.btn_bookmark);

		// LinearLayout favoristLay = (LinearLayout)
		// findViewById(R.id.btnFavorist);
		// final TextView favoristText = (TextView) favoristLay
		// .findViewById(R.id.btnText);
		if (sqLiteAdapter.isAFavoritePlace(fullDataInstance.getUri())) {
			favoristLay.setChecked(true);
		} else {
			favoristLay.setChecked(false);
		}
		favoristLay.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (!sqLiteAdapter.isAFavoritePlace(fullDataInstance.getUri())) {
					sqLiteAdapter.addPlaceToFavoriteTable(fullDataInstance);
				} else {
					sqLiteAdapter.deletePlaceFromFavoriteTable(fullDataInstance
							.getUri());
				}
			}
		});

		webView = (WebView) findViewById(R.id.placeImageView);
		imageView = (ImageView) findViewById(R.id.imgNoImage);

		if (fullDataInstance.getImageURL().equals("")
				|| fullDataInstance.getImageURL().contains("anyType")) {
			imageView.setVisibility(View.VISIBLE);
			webView.setVisibility(View.GONE);
		} else {
			imageView.setVisibility(View.GONE);
			webView.setVisibility(View.VISIBLE);
			int width = (int) context.getResources().getDimension(
					R.dimen.layx480);
			int height = (int) context.getResources().getDimension(
					R.dimen.layy300);

			// Set image for webview image
			// String data = "";
			// data = "<html><head></head><body><img  src=\""
			// + fullDataInstance.getImageURL()
			// + "\" style=' background-color:transparent;' width='"
			// + width + "px;'height = '"
			// + getResources().getDimension(R.dimen.layy300)
			// + "px'    /></body></html>";
			JavaScriptInterface JSInterface = new JavaScriptInterface(context,
					fullDataInstance.getImageURL());
			webView.addJavascriptInterface(JSInterface, "JSInterface");
			String data = "<html>"
					+ "<head>"
					+ "<meta name=\"viewport\" content=\"target-densitydpi=device-dpi, width=device-width, height=device-height\" />"
					+ "<style>* {margin:0;padding:0;}</style>"
					+ "<script language=\"JavaScript\" type=\"text/javascript\">"
					+ "function ImgError(source){"
					+ "    JSInterface.changeActivity();"
					+ "	return true;"
					+ "}"
					+

					"function ScaleImage(srcwidth, srcheight, targetwidth, targetheight) {"
					+ "var rateX = targetwidth / srcwidth;"
					+ "var rateY = targetheight / srcheight;"
					+ "var rate = 1;"
					+ "if(rateX > 1 || rateY > 1) {"
					+ "if(rateX < rateY) {"
					+ " rate = rateY;"
					+ "} else {"
					+ "rate = rateX;"
					+ "}"
					+ "} else if(rateX < 1 || rateY < 1) {"
					+ "if(rateX < rateY) {"
					+ "rate = rateY;"
					+ "} else {"
					+ "rate = rateX;"
					+ "}"
					+ "}"
					+

					"document.getElementById('image2').style.width = srcwidth * rate + \"px\";"
					+ "document.getElementById('image2').style.height = srcheight * rate + \"px\";"
					+ "document.getElementById('image2').style.left = (targetwidth - srcwidth * rate) / 2 + \"px\";"
					+ "document.getElementById('image2').style.top = (targetheight - srcheight * rate) / 2 + \"px\";"
					+

					"document.getElementById('div1').style.width = targetwidth + \"px\";"
					+ "document.getElementById('div1').style.height = targetheight + \"px\";"
					+ "}"
					+ "</script>"
					+ "<body>"
					+ "<form>"
					+ "<div style=\"width: "
					+ width
					+ "px; height: "
					+ height
					+ "px; border: overflow: hidden; position: relative;\">"
					+ "<img id=\"image2\" style=\"position:absolute; align=\"center\";\" src=\""
					+ fullDataInstance.getImageURL()
					+ "\" alt=\"Pulpit rock\" onload=\"ScaleImage(this.width, this.height, "
					+ width + ", " + height
					+ ", false)\"  onerror=\"ImgError(this)\">" + "</div>"
					+ "</form>" + "</body>" + "</html>";

			webView.getSettings().setJavaScriptEnabled(true);
			webView.getSettings().setAllowFileAccess(true);
			webView.setScrollBarStyle(View.SCROLLBARS_OUTSIDE_OVERLAY);
			webView.loadData(data, "text/html", "utf-8");
			webView.setWebViewClient(new WebViewClient() {
				@Override
				public void onReceivedError(WebView view, int errorCode,
						String description, String failingUrl) {
					log.e("Webview error");
					super.onReceivedError(view, errorCode, description,
							failingUrl);
				}
			});
		}

		txtType = (TextView) this.findViewById(R.id.txtType);
		txtType.setText(fullDataInstance.getType());

		textViewTitle = (TextView) findViewById(R.id.placeTypesTextView);
		textViewTitle.setText(fullDataInstance.getLabel());
		// Rating
		LinearLayout ratingLay = (LinearLayout) findViewById(R.id.ratingLayout);
		final int rate = fullDataInstance.getRatingNum();
		ratingLay.removeAllViews();
		for (int i = 0; i < rate; i++) {
			ImageView ratingIcon = new ImageView(PlaceDetails.this);
			ratingIcon.setImageResource(R.drawable.star_rating_full_dark);
			ratingLay.addView(ratingIcon);
		}
		for (int i = 0; i < (5 - rate); i++) {
			ImageView ratingIcon = new ImageView(PlaceDetails.this);
			ratingIcon.setImageResource(R.drawable.star_rating_empty_dark);
			ratingLay.addView(ratingIcon);
		}
		// Address
		TextView txtAddress = (TextView) findViewById(R.id.placeAddressTextView);
		txtAddress.setText(fullDataInstance.getAddress());
		// Phone
		TextView txtPhone = (TextView) findViewById(R.id.txt_phone);
		if (!fullDataInstance.getPhone().contains("anyType")) {
			txtPhone.setText(fullDataInstance.getPhone());
		} else {
			txtPhone.setText("Không có số điện thoại địa điểm này");
		}
		// Infor detail
		TextView txtInforDetail = (TextView) findViewById(R.id.txt_infor_detail);
		if (fullDataInstance.getAbstractInfo().length() == 0) {
			txtInforDetail.setText(getString(R.string.not_update_yet));
		} else {
			txtInforDetail.setText(fullDataInstance.getAbstractInfo().replace(
					"@vn", " "));
		}
	}

	public class JavaScriptInterface {
		Context mContext;
		String url;

		/** Instantiate the interface and set the context */
		JavaScriptInterface(Context c, String url) {
			mContext = c;
			this.url = url;
		}

		public void changeActivity() {
			((Activity) context).runOnUiThread(new Runnable() {

				@Override
				public void run() {
					webView.setVisibility(View.GONE);
					imageView.setVisibility(View.VISIBLE);
				}
			});
		}
	}

	/**
	 * On Share button click
	 * 
	 * @param v
	 */
	public void onSharePlace(View v) {
		Bitmap img;
		try {
			img = BitmapFactory.decodeStream(new URL(fullDataInstance
					.getImageURL()).openStream());
			String path = Images.Media.insertImage(getContentResolver(), img,
					"image", null);
			Uri imageUri = Uri.parse(path);
			Intent intent = new Intent(Intent.ACTION_SEND);
			intent.setType("image/*");
			intent.putExtra(Intent.EXTRA_TITLE, fullDataInstance.getLabel());
			intent.putExtra(Intent.EXTRA_SUBJECT, fullDataInstance.getLabel());
			intent.putExtra(
					Intent.EXTRA_TEXT,
					"Địa chỉ:"
							+ fullDataInstance.getAddress()
							+ "\n"
							+ fullDataInstance.getAbstractInfo().replace("@vn",
									""));
			intent.putExtra(Intent.EXTRA_STREAM, imageUri);
			startActivity(Intent.createChooser(intent, "Share"));
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	protected Dialog onCreateDialog(final int pID) {

		switch (pID) {
		case SHOW_NO_DATA_DIALOG: {
			return new AlertDialog.Builder(this)
					.setCancelable(false)
					.setMessage(
							getResources().getString(R.string.data_not_update))
					.setNeutralButton("Ok",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(
										final DialogInterface pDialog,
										final int pWhich) {
									finish();
								}
							}).create();
		}
		default:
			return super.onCreateDialog(pID);
		}
	}

	public void onViewMap(View v) {
		// Intent intent = new Intent(context,
		// XmlAdapter.getShowOnMapActivity(context));
		System.out.println("Show on map");
		Intent intent = new Intent(context, ShowPlaceOnMap.class);
		Bundle bundle = new Bundle();
		bundle.putSerializable("fullinstance", fullDataInstance);
		intent.putExtras(bundle);
		this.context.startActivity(intent);
	}

	public void onPhoneCall(View v) {
		String phoneNumber = fullDataInstance.getPhone();
		if (phoneNumber != null && !phoneNumber.equals("")
				&& !phoneNumber.contains("anyType")) {
			try {
				Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"
						+ phoneNumber));
				((Activity) context).startActivity(intent);
			} catch (Exception e) {
				Toast.makeText(
						context,
						context.getResources().getString(
								R.string.not_support_call), Toast.LENGTH_SHORT)
						.show();
			}
		} else {
			Toast.makeText(context, "Không có số điện thoại của địa điểm này!",
					Toast.LENGTH_SHORT).show();
		}
	}

	public void onRating(View v) {
	}

	public void onSearchButtonClick(View v) {
		Intent intent = new Intent(PlaceDetails.this,
				DinningServiceSearch.class);
		startActivity(intent);
		finish();
	}

	public void onBack(View v) {
		finish();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			finish();
		}
		return super.onKeyDown(keyCode, event);
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
	public boolean onMarkerClick(Marker arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onInfoWindowClick(Marker arg0) {
		// TODO Auto-generated method stub

	}

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
	public void onConnectionFailed(ConnectionResult arg0) {
		System.out.println("Connection fail");
	}

	@Override
	public void onConnected(Bundle arg0) {
		mLocationClient.requestLocationUpdates(REQUEST, this); //
		System.out.println("Connected success");
	}

	@Override
	public void onDisconnected() {
		// TODO Auto-generated method stub

	}
}
