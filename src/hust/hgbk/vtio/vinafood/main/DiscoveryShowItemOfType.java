package hust.hgbk.vtio.vinafood.main;

import hust.hgbk.vtio.vinafood.R;
import hust.hgbk.vtio.vinafood.config.log;
import hust.hgbk.vtio.vinafood.main.DiscoveryHanoiFamousActivity.FoodLocation;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class DiscoveryShowItemOfType extends Activity {

	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================
	Context ctx;

	int typeID;
	ArrayList<String> keys = new ArrayList<String>();
	Hashtable<String, FoodLocation> hashtable;
	ListView listView;
	ProgressDialog progressDialog;

	// ===========================================================
	// Constructors
	// ===========================================================

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.ctx = DiscoveryShowItemOfType.this;
		// Load data
		typeID = getIntent().getExtras().getInt("type");
		hashtable = DiscoveryHanoiFamousActivity.hashtable.get(Integer
				.valueOf(typeID));
		Iterator<String> iterator = hashtable.keySet().iterator();
		while (iterator.hasNext()) {
			keys.add(iterator.next());
		}

		setContentView(R.layout.discovery_show_all_item_of_type);
		((TextView) findViewById(R.id.txt_sub_menu)).setText(getResources()
				.getString(R.string.txt_discovery));
		// List type of food
		listView = (ListView) this.findViewById(R.id.listView);
		listView.setAdapter(new DiscoveryAdapter());
	}

	public void onStop() {
		super.onStop();
	}

	// ===========================================================
	// Methods
	// ===========================================================
	public void onBack(View v) {
		Intent intent = new Intent(DiscoveryShowItemOfType.this,
				MenuActivity.class);
		startActivity(intent);
		finish();
		overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			Intent intent = new Intent(DiscoveryShowItemOfType.this,
					DiscoveryHanoiFamousActivity.class);
			startActivity(intent);
			finish();
			overridePendingTransition(R.anim.slide_in_left,
					R.anim.slide_out_right);

		}
		return super.onKeyDown(keyCode, event);
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
	public class DiscoveryAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return hashtable.size();
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView = View.inflate(ctx, R.layout.discovery_item_layout,
						null);
				WebView content = (WebView) convertView
						.findViewById(R.id.webview);
				content.setWebViewClient(new WebViewClient() {
					@Override
					public void onReceivedError(WebView view, int errorCode,
							String description, String failingUrl) {

						super.onReceivedError(view, errorCode, description,
								failingUrl);
					}

					@Override
					public void onPageStarted(WebView view, String url,
							Bitmap favicon) {
						log.m("start load");
						super.onPageStarted(view, url, favicon);
					}

					@Override
					public void onPageFinished(WebView view, String url) {
						log.m("loaded");
						super.onPageFinished(view, url);
					}
				});

				TextView title = (TextView) convertView
						.findViewById(R.id.title);
				TextView address = (TextView) convertView
						.findViewById(R.id.address);

				title.setText(keys.get(position));
				address.setText("Địa chỉ: "
						+ hashtable.get(keys.get(position)).address);
				String header = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>";
				content.loadData(header
						+ hashtable.get(keys.get(position)).content,
						"text/html", "UTF-8");
			}
			return convertView;
		}
	}

	public void onSearchButtonClick(View v) {
		Intent intent = new Intent(this, DinningServiceSearch.class);
		startActivity(intent);
		overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
		finish();
	}

}
