package hust.hgbk.vtio.vinafood.customViewAdapter;

import hust.hgbk.vtio.vinafood.config.ServerConfig;
import hust.hgbk.vtio.vinafood.customview.PlaceItemView;
import hust.hgbk.vtio.vinafood.vtioservice.FullDataInstance;
import hust.hgbk.vtio.vinafood.vtioservice.ICoreService;

import java.util.ArrayList;
import java.util.List;

import ken.soapservicelib.proxy.SoapServiceProxy;
import android.content.Context;
import android.os.AsyncTask;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;

public abstract class ArrayPlaceResultAdapter extends
		ArrayAdapter<FullDataInstance> {
	final static int LIMIT = 5;
	private int offset = 0;
	protected Context context;
	// protected SoapServiceProxy<IUserService> logProxy;
	protected SoapServiceProxy<ICoreService> soapServiceProxy;
	ArrayList<FullDataInstance> listPlaceDataSimple;
	int countLoopQuery = 0;
	private int currentPosition = 0;
	boolean isStop = false;
	private LoadInstanceTask loadInstanceTask;
	private boolean isLoading;

	public ArrayPlaceResultAdapter(Context context, int textViewResourceId,
			List<FullDataInstance> objects) {
		super(context, textViewResourceId, objects);
		this.context = context;
		this.listPlaceDataSimple = (ArrayList<FullDataInstance>) objects;

		// logProxy = new SoapServiceProxy<IUserService>(IUserService.class,
		// ServerConfig.USER_SERVICE_NAME_SPACE,
		// ServerConfig.getUserServiceWSDLURL());
		soapServiceProxy = new SoapServiceProxy<ICoreService>(
				ICoreService.class, ServerConfig.SERVICE_NAMESPACE,
				ServerConfig.getWSDLURL());
	}

	public void loadPlaceDataInFirst() {
		loadInstanceTask = new LoadInstanceTask(null);
		loadInstanceTask.doInBackground();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		currentPosition = position;
		PlaceItemView view = (PlaceItemView) convertView;
		if (view == null) {
			view = new PlaceItemView(context);
			view.getProgressBar().setVisibility(View.GONE);
			if (position == getCount() - 1) {
				if (!isStop && !isLoading) {
					if (loadInstanceTask != null) {
						loadInstanceTask.cancel(true);
					}
					loadInstanceTask = new LoadInstanceTask(
							view.getProgressBar());
					loadInstanceTask.execute();
				}
				if (isLoading) {
					view.getProgressBar().setVisibility(View.VISIBLE);
				}
			}

			FullDataInstance placeItem = listPlaceDataSimple.get(position);
			view.setData(placeItem);
		}

		return view;
	}

	abstract protected FullDataInstance[] callServiceResult(int limit,
			int offset);

	abstract protected void callServiceLog(
			FullDataInstance[] fullDataInstances, int limit, int offset)
			throws Exception;

	class LoadInstanceTask extends AsyncTask<Void, Void, Void> {
		private ProgressBar progressBar;

		public LoadInstanceTask(ProgressBar progressBar) {
			this.progressBar = progressBar;
		}

		@Override
		protected Void doInBackground(Void... arg0) {
			try {
				FullDataInstance[] fullDataInstances = callServiceResult(LIMIT,
						offset);
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

	public int getCurrentPosition() {
		return currentPosition;
	}

	public void setCurrentPosition(int currentPosition) {
		this.currentPosition = currentPosition;
	}

	protected String[] getUriStrings(FullDataInstance[] fullDataInstances) {
		String[] returnStrings = new String[fullDataInstances.length];
		for (int i = 0; i < fullDataInstances.length; i++) {
			returnStrings[i] = fullDataInstances[i].getUri();
		}
		return returnStrings;
	}
}
