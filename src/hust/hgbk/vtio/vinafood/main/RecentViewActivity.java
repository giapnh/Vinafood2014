package hust.hgbk.vtio.vinafood.main;

import hust.hgbk.vtio.vinafood.R;
import hust.hgbk.vtio.vinafood.constant.XmlAdapter;
import hust.hgbk.vtio.vinafood.customViewAdapter.NewArrayPlaceSimpleAdapter;
import hust.hgbk.vtio.vinafood.database.SQLiteAdapter;
import hust.hgbk.vtio.vinafood.vtioservice.FullDataInstance;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.ListView;
import android.widget.TextView;

public class RecentViewActivity extends Activity {
	String titleSearch;
	Float radius = 0f;
	ListView listResultView;
	TextView noFavorPlaceTextView;
	ArrayList<FullDataInstance> listFullDataInstance = new ArrayList<FullDataInstance>();

	String message;
	NewArrayPlaceSimpleAdapter arrayPlaceSimpleAdapter;
	LoadAllInstanceTask loadAllInstanceTask;
	SQLiteAdapter sqLiteAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		XmlAdapter.synConfig(this);
		setContentView(R.layout.result_place_search_layout);
		sqLiteAdapter = SQLiteAdapter.getInstance(RecentViewActivity.this);
		sqLiteAdapter.checkAndCreateDatabase();
		message = getResources().getString(R.string.please_wait);
		// Log.v("TEST2", ""+ radius);
		// titleSearchTextView = (TextView)
		// findViewById(R.id.serchTitleTextView);
		listResultView = (ListView) findViewById(R.id.listResultView);
		noFavorPlaceTextView = (TextView) findViewById(R.id.no_search_result);

		loadAllInstanceTask = new LoadAllInstanceTask();
		loadAllInstanceTask.execute();

		TextView subMenuTitle = (TextView) findViewById(R.id.txt_sub_menu);
		subMenuTitle.setText(getString(R.string.txt_recent_view));
	}

	public class LoadAllInstanceTask extends AsyncTask<Void, Void, Void> {

		Dialog progressLayout;

		@Override
		protected Void doInBackground(Void... arg0) {
			FullDataInstance[] dataInstances = SQLiteAdapter.getInstance(
					RecentViewActivity.this).getAllRecentViewPlace(20, 0);
			for (int i = dataInstances.length - 1; i >= 0; i--) {
				listFullDataInstance.add(dataInstances[i]);
			}
			arrayPlaceSimpleAdapter = new NewArrayPlaceSimpleAdapter(
					RecentViewActivity.this, R.layout.place_item_layout,
					listFullDataInstance, sqLiteAdapter);
			return null;
		}

		protected void onCancelled() {
		}

		@Override
		protected void onPreExecute() {
			progressLayout = new Dialog(RecentViewActivity.this,
					android.R.style.Theme_Translucent_NoTitleBar);
			progressLayout.setContentView(R.layout.loading_layout);
			progressLayout.show();
		}

		@Override
		protected void onPostExecute(Void result) {
			if (listFullDataInstance.size() <= 0) {
				noFavorPlaceTextView.setText(getResources().getString(
						R.string.no_search_result));
				noFavorPlaceTextView.setVisibility(View.VISIBLE);
			} else {
				noFavorPlaceTextView.setVisibility(View.GONE);
			}
			listResultView.setAdapter(arrayPlaceSimpleAdapter);

			progressLayout.dismiss();
			listResultView.setVisibility(View.VISIBLE);
		}
	}

	public void onStop() {
		super.onStop();

	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			Intent intent = new Intent(RecentViewActivity.this,
					MenuActivity.class);
			startActivity(intent);
			finish();
			overridePendingTransition(R.anim.slide_in_right,
					R.anim.slide_out_left);
			super.onKeyDown(keyCode, event);
		}

		return super.onKeyDown(keyCode, event);
	}

	public void onBack(View v) {
		Intent intent = new Intent(RecentViewActivity.this, MenuActivity.class);
		startActivity(intent);
		finish();
		overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
	}

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnSearch:
			Intent intent = new Intent(RecentViewActivity.this,
					DinningServiceSearch.class);
			startActivity(intent);
			finish();
			overridePendingTransition(R.anim.slide_in_right,
					R.anim.slide_out_left);

			break;
		}
	}

	protected void showhelpDialog(String message) {
		new AlertDialog.Builder(this)
				.setTitle(getResources().getString(R.string.help))
				.setMessage(message)
				.setCancelable(false)
				.setPositiveButton(getResources().getString(R.string.close),
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
							}
						}).show();
	}
}