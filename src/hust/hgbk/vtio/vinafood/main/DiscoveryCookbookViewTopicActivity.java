package hust.hgbk.vtio.vinafood.main;

import hust.hgbk.vtio.vinafood.R;
import hust.hgbk.vtio.vinafood.entities.Cookbook;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class DiscoveryCookbookViewTopicActivity extends Activity {

	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

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
		Cookbook topic = (Cookbook) getIntent().getSerializableExtra("topic");
		setContentView(R.layout.discovery_cuisine_helth_view_topic);
		ImageView imgIcon = (ImageView) this.findViewById(R.id.img_icon);
		Picasso.with(this).load(topic.imgLink).centerCrop().resize(130, 130)
				.into(imgIcon);
		TextView title = (TextView) this.findViewById(R.id.title);
		TextView description = (TextView) this.findViewById(R.id.description);
		WebView webView = (WebView) this.findViewById(R.id.webview);
		title.setText(topic.title);
		description.setText(topic.description);
		String header = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>";
		webView.loadData(header + topic.content, "text/html", "UTF-8");
	}

	// ===========================================================
	// Methods
	// ===========================================================
	public void onBack(View v) {
		Intent intent = new Intent(DiscoveryCookbookViewTopicActivity.this,
				MenuActivity.class);
		startActivity(intent);
		overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
		finish();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			Intent intent = new Intent(DiscoveryCookbookViewTopicActivity.this,
					DiscoveryCookbookActivity.class);
			startActivity(intent);
			overridePendingTransition(R.anim.slide_in_right,
					R.anim.slide_out_left);
			finish();
		}
		return super.onKeyDown(keyCode, event);
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
