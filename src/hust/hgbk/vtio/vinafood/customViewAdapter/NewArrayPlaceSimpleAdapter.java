package hust.hgbk.vtio.vinafood.customViewAdapter;

import hust.hgbk.vtio.vinafood.R;
import hust.hgbk.vtio.vinafood.config.ServerConfig;
import hust.hgbk.vtio.vinafood.customview.PlaceItemView;
import hust.hgbk.vtio.vinafood.database.SQLiteAdapter;
import hust.hgbk.vtio.vinafood.main.PlaceDetails;
import hust.hgbk.vtio.vinafood.vtioservice.FullDataInstance;
import hust.hgbk.vtio.vinafood.vtioservice.ICoreService;
import hust.hgbk.vtio.vinafood.vtioservice.VtioCoreService;

import java.util.ArrayList;
import java.util.HashMap;

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

public class NewArrayPlaceSimpleAdapter extends ArrayAdapter<FullDataInstance> {
	final static int LIMIT = 5;
	int offsetOnList = 0;
	Context context;
	ArrayList<FullDataInstance> listPlaceDataSimple;
	VtioCoreService services = new VtioCoreService();
	HashMap<Integer, Boolean> hashMapView;
	int countLoopQuery = 0;
	private int currentPosition = 0;
	boolean cache = false;
	boolean uriReasoning = false;
	boolean isStop = false;
	String queryString;
	LoadInstanceTask loadInstanceTask;
	SQLiteAdapter sqLiteAdapter;
	private SoapServiceProxy<ICoreService> soapServiceProxy;

	public NewArrayPlaceSimpleAdapter(Context context, int textViewResourceId,
			ArrayList<FullDataInstance> objects, String queryString,
			boolean cache, boolean uriReasoning) {
		super(context, textViewResourceId, objects);
		this.context = context;
		this.queryString = queryString;
		soapServiceProxy = new SoapServiceProxy<ICoreService>(
				ICoreService.class, ServerConfig.SERVICE_NAMESPACE,
				ServerConfig.getWSDLURL());

		// Add all fulldatainstance to list
		listPlaceDataSimple = objects;
		this.sqLiteAdapter = SQLiteAdapter.getInstance(context);
		this.sqLiteAdapter.checkAndCreateDatabase();
		hashMapView = new HashMap<Integer, Boolean>();
		this.cache = cache;
		this.uriReasoning = uriReasoning;
		doLoadInstanceTask(0, cache, uriReasoning);
		notifyDataSetChanged();
	}

	public NewArrayPlaceSimpleAdapter(Context context, int textViewResourceId,
			ArrayList<FullDataInstance> objects, SQLiteAdapter sqLiteAdapter) {
		super(context, textViewResourceId, objects);
		this.context = context;
		this.isStop = true;
		soapServiceProxy = new SoapServiceProxy<ICoreService>(
				ICoreService.class, ServerConfig.SERVICE_NAMESPACE,
				ServerConfig.getWSDLURL());

		// Add all fulldatainstance to list
		listPlaceDataSimple = objects;
		this.sqLiteAdapter = sqLiteAdapter;
		hashMapView = new HashMap<Integer, Boolean>();
		notifyDataSetChanged();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		currentPosition = position;
		final PlaceItemView view;
		view = new PlaceItemView(context);
		if (position == getCount() - 1) {
			if (!isStop
					&& (hashMapView.get(position) == null || !hashMapView
							.get(position))) {
				if (loadInstanceTask != null) {
					loadInstanceTask.cancel(true);
				}
				loadInstanceTask = new LoadInstanceTask(queryString,
						view.getProgressBar());
				loadInstanceTask.execute();
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
		// int offset;
		ProgressBar progressBar;

		public LoadInstanceTask(String query, ProgressBar progressBar) {
			this.progressBar = progressBar;
		}

		@Override
		protected Void doInBackground(Void... arg0) {
			doLoadInstanceTask(offsetOnList, cache, uriReasoning);
			return null;
		}

		protected void onCancelled() {
		}

		@Override
		protected void onPreExecute() {
			progressBar.setVisibility(View.VISIBLE);
		}

		@Override
		protected void onPostExecute(Void result) {
			progressBar.setVisibility(View.GONE);
			// setTitleHelpResult(queryResult.size());
			notifyDataSetChanged();
		}
	}

	public void doLoadInstanceTask(int offset, boolean cache,
			boolean uriReasoning) {
		String query = queryString + " LIMIT " + LIMIT + " OFFSET " + offset;
		long a = System.currentTimeMillis();

		FullDataInstance[] listPlaces = soapServiceProxy.getiComCoreService()
				.getFullDataInstace(query, ServerConfig.LANGUAGE_CODE,
						uriReasoning, ServerConfig.VTIO_REPOSITORY_KEY);
		for (FullDataInstance fullDataInstance : listPlaces) {
			listPlaceDataSimple.add(fullDataInstance);
		}

		if (listPlaces.length < LIMIT - 2) {
			isStop = true;
		}
		if (listPlaces.length > 0) {
			offsetOnList = offsetOnList + LIMIT;

		}
	}

	public int getCurrentPosition() {
		return currentPosition;
	}

	public void setCurrentPosition(int currentPosition) {
		this.currentPosition = currentPosition;
	}
}
