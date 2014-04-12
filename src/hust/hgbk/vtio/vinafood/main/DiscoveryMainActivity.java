package hust.hgbk.vtio.vinafood.main;

import hust.hgbk.vtio.vinafood.R;
import hust.hgbk.vtio.vinafood.constant.OntologyCache;
import hust.hgbk.vtio.vinafood.database.SQLiteAdapter;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;

public class DiscoveryMainActivity extends Activity {

	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================
	public Context ctx;
	Dialog progressLayout;
	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 0:
				progressLayout.hide();
				Intent intent = new Intent(DiscoveryMainActivity.this,
						DiscoveryCuisineWithHelthActivity.class);
				startActivity(intent);
				finish();
				overridePendingTransition(R.anim.slide_in_right,
						R.anim.slide_out_left);
				break;
			case 1:
				progressLayout.hide();
				Intent intent2 = new Intent(DiscoveryMainActivity.this,
						DiscoveryCookbookActivity.class);
				startActivity(intent2);
				finish();
				overridePendingTransition(R.anim.slide_in_right,
						R.anim.slide_out_left);
				break;
			default:
				break;
			}

		};
	};

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
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.discovery_main_layout);
		ctx = DiscoveryMainActivity.this;
		progressLayout = new Dialog(DiscoveryMainActivity.this,
				android.R.style.Theme_Translucent_NoTitleBar);
		progressLayout.setContentView(R.layout.loading_layout);
	}

	// ===========================================================
	// Methods
	// ===========================================================
	public void onMonngon(View v) {
		Intent intent = new Intent(DiscoveryMainActivity.this,
				DiscoveryHanoiFamousActivity.class);
		startActivity(intent);
		overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
		finish();
	}

	public void onCuisineWithHelth(View v) {
		progressLayout.show();
		new CuisineHelthLoader().start();
	}

	public void onCookbook(View v) {
		progressLayout.show();
		new CookbookLoader().start();
	}

	class CuisineHelthLoader extends Thread {

		public void run() {
			SQLiteAdapter sqLiteAdapter = SQLiteAdapter.getInstance(ctx);
			sqLiteAdapter.createCuisineHelthTable();
			handler.sendEmptyMessage(0);
		}
	}

	class CookbookLoader extends Thread {
		public void run() {
			SQLiteAdapter sqLiteAdapter = SQLiteAdapter.getInstance(ctx);
			sqLiteAdapter.createCookbookTable();
			handler.sendEmptyMessage(1);
		}
	}

	/*
	 * On Click search button
	 */
	public void onSearchButtonClick(View v) {
		Intent intent = new Intent(DiscoveryMainActivity.this,
				DinningServiceSearch.class);
		startActivity(intent);
		overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
		finish();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			Intent intent = new Intent(DiscoveryMainActivity.this,
					MenuActivity.class);
			startActivity(intent);
			finish();
			overridePendingTransition(R.anim.slide_in_right,
					R.anim.slide_out_left);
		}
		return super.onKeyDown(keyCode, event);
	}
	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
