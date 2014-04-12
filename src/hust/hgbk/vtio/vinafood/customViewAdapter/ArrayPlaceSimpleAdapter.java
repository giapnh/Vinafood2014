package hust.hgbk.vtio.vinafood.customViewAdapter;

import hust.hgbk.vtio.vinafood.R;
import hust.hgbk.vtio.vinafood.config.ServerConfig;
import hust.hgbk.vtio.vinafood.config.log;
import hust.hgbk.vtio.vinafood.constant.OntologyCache;
import hust.hgbk.vtio.vinafood.customview.PlaceItemView;
import hust.hgbk.vtio.vinafood.database.SQLiteAdapter;
import hust.hgbk.vtio.vinafood.main.PlaceDetails;
import hust.hgbk.vtio.vinafood.vtioservice.FullDataInstance;
import hust.hgbk.vtio.vinafood.vtioservice.ICoreService;

import java.util.ArrayList;

import ken.soapservicelib.proxy.SoapServiceProxy;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;

public class ArrayPlaceSimpleAdapter extends ArrayAdapter<FullDataInstance> {
	final static int LIMIT = 8;
	private int offset = 0;
	int offsetOnList;
	Context context;
	public ArrayList<FullDataInstance> listPlaceDataSimple;
	int countLoopQuery = 0;
	private LoadInstanceTask loadInstanceTask;
	private int currentPosition = 0;

	SQLiteAdapter sqLiteAdapter;
	boolean isStop = false;
	private boolean isLoading;
	SoapServiceProxy<ICoreService> soapServiceProxy;

	boolean hasConstraint = false;
	private String classUri;
	private String cityUri;
	private double geoLat;
	private double geoLon;
	private float radius;
	private boolean hasPreference;
	private String keyWord;
	private String locationKeyword = "";
	private String nearbyKeyword;
	private String cuisineStyle = "";
	private int numStar;
	private float numberRanking;
	private boolean isWellKnown;

	public ArrayPlaceSimpleAdapter(Context context, int textViewResourceId,
			ArrayList<FullDataInstance> objects) {
		super(context, textViewResourceId, objects);
		this.context = context;
		this.listPlaceDataSimple = objects;
		this.sqLiteAdapter = SQLiteAdapter.getInstance(context);
		this.sqLiteAdapter.checkAndCreateDatabase();
		soapServiceProxy = new SoapServiceProxy<ICoreService>(
				ICoreService.class, ServerConfig.SERVICE_NAMESPACE,
				ServerConfig.getWSDLURL());
		notifyDataSetChanged();
	}

	public void loadPlaceDataInFirst(String classUri, String cityUri,
			double geoLat, double geoLon, float radius, boolean hasPreference,
			String keyWord) {
		this.classUri = classUri;
		this.cityUri = cityUri;
		this.geoLat = geoLat;
		this.geoLon = geoLon;
		this.radius = radius;
		this.hasPreference = hasPreference;
		this.keyWord = keyWord;
		loadPlaceDataInFirst();
	}

	public void loadPlaceDataInFirst(String classUri, String cityUri,
			double geoLat, double geoLon, float radius, boolean hasPreference,
			String keyword, String locationKeyword, String nearbyKeyword,
			String cuisineStyle, int numStar, float numRanking) {
		hasConstraint = true;
		this.classUri = classUri;
		this.cityUri = cityUri;
		this.locationKeyword = locationKeyword;
		this.nearbyKeyword = nearbyKeyword;
		this.numStar = numStar;
		this.numberRanking = numRanking;
		loadPlaceDataInFirst(classUri, cityUri, geoLat, geoLon, radius,
				hasPreference, locationKeyword);
	}

	public void loadPlaceDataInFirst() {
		loadInstanceTask = new LoadInstanceTask(null);
		loadInstanceTask.doInBackground();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		currentPosition = position;
		final PlaceItemView view;
		if (convertView == null) {
			view = new PlaceItemView(context);
		} else {
			view = (PlaceItemView) convertView;
		}

		view.getProgressBar().setVisibility(View.GONE);
		if (position == getCount() - 1) {
			if (!isStop && !isLoading) {
				if (loadInstanceTask != null) {
					loadInstanceTask.cancel(true);
				}
				loadInstanceTask = new LoadInstanceTask(view.getProgressBar());
				loadInstanceTask.execute();
			}
			if (isLoading) {
				view.getProgressBar().setVisibility(View.VISIBLE);
			}
		}

		final FullDataInstance placeItem = listPlaceDataSimple.get(position);
		view.setData(placeItem);
		view.setClickable(true);
		view.findViewById(R.id.for_click_view).setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View v) {
						Intent intent = new Intent(context, PlaceDetails.class);
						Bundle bundle = new Bundle();
						bundle.putString("abstractInfo",
								placeItem.getAbstractInfo());
						bundle.putString("address", placeItem.getAddress());
						bundle.putString("imageURL", placeItem.getImageURL());
						bundle.putString("label", placeItem.getLabel());
						bundle.putDouble("latitude", placeItem.getLatitude());
						bundle.putDouble("longitude", placeItem.getLongitude());
						bundle.putString("location", placeItem.getLocation());
						bundle.putString("phone", placeItem.getPhone());
						bundle.putInt("ratingNum", placeItem.getRatingNum());
						bundle.putString("type", placeItem.getType());
						bundle.putString("uri", placeItem.getUri());
						bundle.putString("wellKnown", placeItem.getWellKnown());
						intent.putExtras(bundle);
						context.startActivity(intent);
					}
				});

		return view;
	}

	class LoadInstanceTask extends AsyncTask<Void, Void, Void> {
		private ProgressBar progressBar;

		public LoadInstanceTask(ProgressBar progressBar) {
			this.progressBar = progressBar;
		}

		@Override
		protected Void doInBackground(Void... arg0) {
			try {
				FullDataInstance[] fullDataInstances;
				if (!hasConstraint) {
					fullDataInstances = callServiceResult(LIMIT, offset);
				} else {
					fullDataInstances = callServiceResultWithConstraint(LIMIT,
							offset);
				}
				for (int i = 0; i < fullDataInstances.length; i++) {
					listPlaceDataSimple.add(fullDataInstances[i]);
				}
				if (fullDataInstances.length > 0) {
					offset += LIMIT;
				}
				if (fullDataInstances.length == 0) {
					countLoopQuery++;
				} else {
					countLoopQuery = 0;
				}
				if ((fullDataInstances.length > 0 && fullDataInstances.length < LIMIT - 2)
						|| countLoopQuery == 2) {
					isStop = true;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}

		protected void onCancelled() {
		}

		@Override
		protected void onPreExecute() {
			isLoading = true;
			progressBar.setVisibility(View.VISIBLE);
		}

		@Override
		protected void onPostExecute(Void result) {
			progressBar.setVisibility(View.GONE);
			notifyDataSetChanged();
			isLoading = false;
		}
	}

	String[] preferences;

	protected FullDataInstance[] callServiceResult(int limit, int offset) {
		if (hasPreference && preferences == null) {
			try {
				preferences = new String[OntologyCache.preferUser.size()];
				for (int i = 0; i < OntologyCache.preferUser.size(); i++) {
					preferences[i] = OntologyCache.preferUser.get(i).getUri();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return soapServiceProxy.getiComCoreService()
				.getFullDataInstaceWithPreference(classUri, geoLat, geoLon,
						radius, hasPreference, keyWord, preferences, LIMIT,
						offset, ServerConfig.LANGUAGE_CODE,
						ServerConfig.VTIO_REPOSITORY_KEY);
	}

	private FullDataInstance[] callServiceResultWithConstraint(int limit,
			int offset) {
		if (hasPreference && preferences == null) {
			try {
				preferences = new String[OntologyCache.preferUser.size()];
				for (int i = 0; i < OntologyCache.preferUser.size(); i++) {
					preferences[i] = OntologyCache.preferUser.get(i).getUri();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		log.m("Load data with uri:" + classUri + "  ; radius = " + radius);
		if (cityUri.equals(ServerConfig.currentCityUri)) {
			return soapServiceProxy.getiComCoreService()
					.getDiningServiceDataWithDistance(
							classUri,//
							ServerConfig.currentCityUri,
							keyWord,
							locationKeyword,//
							nearbyKeyword, cuisineStyle, numStar,
							numberRanking,//
							isWellKnown, geoLat, geoLon, radius, limit, offset,//
							ServerConfig.LANGUAGE_CODE,//
							ServerConfig.VTIO_REPOSITORY_KEY);
		} else {
			return soapServiceProxy.getiComCoreService()
					.getDiningServiceDataWithoutDistance(classUri, cityUri,
							keyWord, locationKeyword, nearbyKeyword,
							cuisineStyle, numStar, numberRanking, isWellKnown,
							limit, offset, ServerConfig.LANGUAGE_CODE,
							ServerConfig.VTIO_REPOSITORY_KEY);
		}

	}

	public int getCurrentPosition() {
		return currentPosition;
	}

	public void setCurrentPosition(int currentPosition) {
		this.currentPosition = currentPosition;
	}

}
