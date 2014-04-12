package hust.hgbk.vtio.vinafood.main;

import hust.hgbk.vtio.vinafood.R;
import hust.hgbk.vtio.vinafood.config.ServerConfig;
import hust.hgbk.vtio.vinafood.config.log;
import hust.hgbk.vtio.vinafood.constant.NameSpace;
import hust.hgbk.vtio.vinafood.constant.XmlAdapter;
import hust.hgbk.vtio.vinafood.maps.CustomLocationListener;
import hust.hgbk.vtio.vinafood.maps.LocationService;
import hust.hgbk.vtio.vinafood.ontology.simple.ClassDataSimple;
import hust.hgbk.vtio.vinafood.vtioservice.VtioCoreService;

import java.util.ArrayList;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.google.android.gms.common.ConnectionResult;

public class DinningServiceSearch extends Activity {
	String keyword = "";
	String purpose = "";
	ClassDataSimple businessType = null;
	String cuisineStyle = "";
	Context ctx;
	ImageButton btnSearch;
	EditText edtSearch;
	Button advancedButton;
	ImageView clearButton;

	// Dialog to show filter list
	Dialog filterDialog;

	// Input manager: show keyboard
	InputMethodManager imm;

	static String currentLanguage = "";

	public static final String CLASS_URI = NameSpace.vtio + "Dining-Service";

	VtioCoreService service = new VtioCoreService();
	// Subclass of dining service
	public static ArrayList<ClassDataSimple> listSubClass = new ArrayList<ClassDataSimple>();

	float radius = 1f;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_dinning_service_search);
		XmlAdapter.synConfig(this);
		ctx = this;
		init();
		detectLocation();
	}

	public void searchWithKey(String keyWord) {
		Intent intent = new Intent(ctx, PlaceSearchResultActivity.class);
		intent.putExtra("keyword", keyWord);
		intent.putExtra("radius", 10f);
		startActivity(intent);
		finish();
	}

	public void init() {
		edtSearch = (EditText) findViewById(R.id.edtSearch);
		btnSearch = (ImageButton) findViewById(R.id.btnSearch);
		clearButton = (ImageView) findViewById(R.id.btnClearText);

		edtSearch.postDelayed(new Runnable() {
			@Override
			public void run() {
				InputMethodManager mInputMethodManager = (InputMethodManager) DinningServiceSearch.this
						.getSystemService(Context.INPUT_METHOD_SERVICE);
				mInputMethodManager.showSoftInput(edtSearch, 0);
			}
		}, 300);
		clearButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				edtSearch.setText("");
			}
		});

		edtSearch.setOnEditorActionListener(new OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView v, int actionId,
					KeyEvent event) {
				if (actionId == EditorInfo.IME_ACTION_SEARCH) {
					searchWithKey(edtSearch.getText().toString());
				}
				return false;
			}
		});
		new GetAllSubClassTask().execute();
		setScope();
	}

	public void onSearch(View v) {
		this.radius = Float
				.parseFloat(((TextView) findViewById(R.id.range_text))
						.getText().toString());
		Intent intent = new Intent(ctx, PlaceSearchResultActivity.class);
		Bundle bundle = new Bundle();
		bundle.putString("city", cityName);
		bundle.putString("keyword", edtSearch.getText().toString().trim());
		bundle.putString("purpose", purpose);
		bundle.putString("cuisineStype", cuisineStyle);
		if (businessType != null) {
			bundle.putString("classUri", businessType.getUri());
		} else {
			bundle.putString("classUri", CLASS_URI);
		}
		bundle.putFloat("radius", this.radius);
		bundle.putBoolean("hasConstraint", true);
		intent.putExtras(bundle);
		startActivity(intent);
		finish();
	}

	ArrayAdapter<String> filterListAdapter;
	String cityName = ServerConfig.listOfCity()[0];

	public void onCityFilter(View v) {
		final Dialog dialog = new Dialog(this,
				android.R.style.Theme_Translucent_NoTitleBar);
		dialog.setContentView(R.layout.single_choise_listview_layout);
		ListView listView = (ListView) dialog.findViewById(R.id.listView);
		final String cityNames[] = ServerConfig.listOfCity();
		cityName = cityNames[0];
		listView.setAdapter(new ArrayAdapter<String>(this,
				R.layout.single_item, R.id.itemName, cityNames));
		TextView layName = (TextView) dialog.findViewById(R.id.layoutName);
		layName.setText(getResources().getString(R.string.txt_city));
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				cityName = cityNames[position];
				((TextView) DinningServiceSearch.this
						.findViewById(R.id.cityValue)).setText(cityName);
				// String cityInfos[] = ServerConfig.infoOfCity(cityName);
				// ServerConfig.currentCityLabel = cityInfos[1];
				// ServerConfig.currentCityUri = cityInfos[2];
				dialog.dismiss();
			}

		});

		Button btnCancel = (Button) dialog.findViewById(R.id.btnCancel);
		btnCancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
		dialog.show();
	}

	public void onBusineseFilter(View v) {
		filterDialog = new Dialog(DinningServiceSearch.this,
				android.R.style.Theme_NoTitleBar);
		filterDialog.setContentView(R.layout.single_choise_listview_layout);
		ListView filterList = (ListView) filterDialog
				.findViewById(R.id.listView);
		TextView layName = (TextView) filterDialog
				.findViewById(R.id.layoutName);
		layName.setText(getResources().getString(R.string.txt_businese_type));
		String[] str = new String[listSubClass.size() + 1];
		str[0] = getResources().getString(R.string.txt_all);
		for (int i = 0; i < listSubClass.size(); i++) {
			str[i + 1] = listSubClass.get(i).getLabel();
		}
		// Bind data
		filterListAdapter = new ArrayAdapter<String>(this,
				R.layout.single_item, R.id.itemName, str);
		filterList.setAdapter(filterListAdapter);
		filterList.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				if (position == 0) {
					businessType = null;
					((TextView) DinningServiceSearch.this
							.findViewById(R.id.typeValue))
							.setText(getString(R.string.txt_all));
				} else {
					businessType = listSubClass.get(position - 1);
					((TextView) DinningServiceSearch.this
							.findViewById(R.id.typeValue)).setText(listSubClass
							.get(position - 1).getLabel());
				}
				filterDialog.dismiss();
			}
		});
		filterDialog.findViewById(R.id.btnCancel).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						filterDialog.dismiss();
					}
				});
		filterDialog.show();
	}

	public void onPurposeFilter(View v) {
		filterDialog = new Dialog(DinningServiceSearch.this,
				android.R.style.Theme_NoTitleBar);
		filterDialog.setContentView(R.layout.single_choise_listview_layout);
		TextView layName = (TextView) filterDialog
				.findViewById(R.id.layoutName);
		layName.setText(getResources().getString(R.string.txt_purpose));
		ListView filterList = (ListView) filterDialog
				.findViewById(R.id.listView);

		// Bind data
		final String[] data = getResources().getStringArray(
				R.array.purpose_list);
		filterListAdapter = new ArrayAdapter<String>(this,
				R.layout.single_item, R.id.itemName, data);
		filterList.setAdapter(filterListAdapter);
		filterList.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				((TextView) DinningServiceSearch.this
						.findViewById(R.id.purposeValue))
						.setText(data[position]);
				filterDialog.dismiss();
			}
		});

		filterDialog.findViewById(R.id.btnCancel).setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View v) {
						filterDialog.dismiss();
					}
				});
		filterDialog.show();
	}

	public void onCuisineStyle(View v) {
		filterDialog = new Dialog(DinningServiceSearch.this,
				android.R.style.Theme_NoTitleBar);
		filterDialog.setContentView(R.layout.single_choise_listview_layout);
		TextView layName = (TextView) filterDialog
				.findViewById(R.id.layoutName);
		layName.setText(getResources().getString(R.string.txt_cuisine));
		ListView filterList = (ListView) filterDialog
				.findViewById(R.id.listView);

		// Bind data
		final String[] data = getResources().getStringArray(
				R.array.cuisine_style_list);
		filterListAdapter = new ArrayAdapter<String>(this,
				R.layout.single_item, R.id.itemName, data);
		filterList.setAdapter(filterListAdapter);
		filterList.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				cuisineStyle = data[position];
				((TextView) DinningServiceSearch.this
						.findViewById(R.id.styleValue)).setText(data[position]);
				filterDialog.dismiss();
			}
		});
		filterDialog.show();

		// Button cancel click listener
		filterDialog.findViewById(R.id.btnCancel).setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View v) {
						filterDialog.dismiss();
					}
				});
	}

	float temp = 0;

	public void setScope() {
		final TextView value = (TextView) findViewById(R.id.range_text);
		Button plus = (Button) findViewById(R.id.btn_plus);
		Button minus = (Button) findViewById(R.id.btn_minus);
		plus.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				temp += 1;
				value.setText("" + temp);
				try {
					radius = Integer.parseInt(value.getText().toString());
					log.m("Radius = " + radius);
				} catch (NumberFormatException exception) {
					radius = 1;
				}
			}
		});
		minus.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (temp > 1) {
					temp -= 1;
				}
				value.setText("" + temp);
				try {
					radius = Integer.parseInt(value.getText().toString());
					log.m("Radius = " + radius);
				} catch (NumberFormatException exception) {
					radius = 1;
				}
			}
		});
	}

	// On icon back inside clicked listener
	public void onBack(View v) {
		Intent intent = new Intent(DinningServiceSearch.this,
				MenuActivity.class);
		startActivity(intent);
		finish();
		overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			Intent intent = new Intent(DinningServiceSearch.this,
					MenuActivity.class);
			startActivity(intent);
			finish();
			overridePendingTransition(R.anim.slide_in_right,
					R.anim.slide_out_left);
		}

		return super.onKeyDown(keyCode, event);
	}

	class GetAllSubClassTask extends AsyncTask<Void, Void, Void> {
		@Override
		protected void onPreExecute() {
		}

		@Override
		protected Void doInBackground(Void... arg0) {
			while (listSubClass.size() == 0)
				listSubClass = service.getAllAdaptedSubClassOf(CLASS_URI);
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
		}
	}

	private void detectLocation() {
		LocationService locationService = new LocationService(ctx);
		locationService.updateLocation(ctx, LocationService.EXPIRATION_TIME,
				new CustomLocationListener() {

					@Override
					public void onProviderDisabled() {
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

	private void setGeoLocation(Location location) {
		String lat = String.valueOf(location.getLatitude());
		String lon = String.valueOf(location.getLongitude());
		log.m("Update location: lat = " + lat + ";long = " + lon);
		try {
			lat = lat.substring(0, 10);
		} catch (Exception e) {
		}
		try {
			lon = lon.substring(0, 10);
		} catch (Exception e) {
		}
		try {
			hust.hgbk.vtio.vinafood.constant.VLocation.getInstance().setGeo(
					Float.valueOf(lat), Float.valueOf(lon));
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
