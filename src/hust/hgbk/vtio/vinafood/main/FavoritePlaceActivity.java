package hust.hgbk.vtio.vinafood.main;

import hust.hgbk.vtio.vinafood.R;
import hust.hgbk.vtio.vinafood.customViewAdapter.NewArrayPlaceSimpleAdapter;
import hust.hgbk.vtio.vinafood.database.SQLiteAdapter;
import hust.hgbk.vtio.vinafood.vtioservice.FullDataInstance;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

public class FavoritePlaceActivity extends Activity {
	Context ctx;

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
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.ctx = FavoritePlaceActivity.this;
		setContentView(R.layout.result_place_search_layout);
		listResultView = (ListView) findViewById(R.id.listResultView);
		noFavorPlaceTextView = (TextView) this
				.findViewById(R.id.no_search_result);
		loadAllInstanceTask = new LoadAllInstanceTask();
		loadAllInstanceTask.execute();
		message = getResources().getString(R.string.please_wait);
		TextView subMenuTitle = (TextView) findViewById(R.id.txt_sub_menu);
		subMenuTitle.setText(getString(R.string.txt_favorite));
	}

	public class LoadAllInstanceTask extends AsyncTask<Void, Void, Void> {

		Dialog progressLayout;

		@Override
		protected Void doInBackground(Void... arg0) {
			FullDataInstance[] dataInstances = SQLiteAdapter.getInstance(ctx)
					.getAllFavoritePlace(20, 0);
			for (int i = 0; i < dataInstances.length; i++) {
				listFullDataInstance.add(dataInstances[i]);
			}
			arrayPlaceSimpleAdapter = new NewArrayPlaceSimpleAdapter(ctx,
					R.layout.place_item_layout, listFullDataInstance,
					sqLiteAdapter);
			return null;
		}

		@Override
		protected void onPreExecute() {
			progressLayout = new Dialog(ctx,
					android.R.style.Theme_Translucent_NoTitleBar);
			progressLayout.setContentView(R.layout.loading_layout);
			progressLayout.show();
		}

		@Override
		protected void onPostExecute(Void result) {

			if (listFullDataInstance.size() <= 0) {
				noFavorPlaceTextView.setText(getResources().getString(
						R.string.no_favorite_place));
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

	protected void showhelpDialog(String message) {
		new AlertDialog.Builder(ctx)
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

	public void onBack(View v) {
		Intent intent = new Intent(FavoritePlaceActivity.this,
				MenuActivity.class);
		startActivity(intent);
		finish();
		overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
	}

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnSearch:
			Intent intent = new Intent(FavoritePlaceActivity.this,
					DinningServiceSearch.class);
			startActivity(intent);
			finish();
			overridePendingTransition(R.anim.slide_in_right,
					R.anim.slide_out_left);

			break;
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			Intent intent = new Intent(FavoritePlaceActivity.this,
					MenuActivity.class);
			startActivity(intent);
			finish();
			overridePendingTransition(R.anim.slide_in_left,
					R.anim.slide_out_right);

		}
		return super.onKeyDown(keyCode, event);
	}
}