package hust.hgbk.vtio.vinafood.main;

import hust.hgbk.vtio.vinafood.R;
import hust.hgbk.vtio.vinafood.config.log;
import hust.hgbk.vtio.vinafood.file.FileManager;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class DiscoveryHanoiFamousActivity extends Activity {

	// ===========================================================
	// Constants
	// ===========================================================
	public static final String[] typeVal = new String[] { "Bún ngan",
			"Bánh đúc", "Bún riêu", "Bánh cuốn", "Bún đậu", "Bún cá",
			"Mỳ vằn thắn", "Bún bò Huế", "Bún bò Nam Bộ", "Bánh gối",
			"Phở cuốn", "Bún chả" };

	// ===========================================================
	// Fields
	// ===========================================================
	Context ctx;

	ArrayList<Integer> keys = new ArrayList<Integer>();
	public static Hashtable<Integer, Hashtable<String, FoodLocation>> hashtable = new Hashtable<Integer, Hashtable<String, FoodLocation>>();

	ListView category;

	// ===========================================================
	// Constructors
	// ===========================================================

	public class FoodLocation {
		String address;
		String content;

		public FoodLocation(String address, String content) {
			this.address = address;
			this.content = content;
		}
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.ctx = DiscoveryHanoiFamousActivity.this;

		importData(FileManager.loadFromRaw(R.raw.data, this));
		parserKeys();

		setContentView(R.layout.discovery_hanoi_famous_category);
		((TextView) findViewById(R.id.txt_sub_menu)).setText(getResources()
				.getString(R.string.txt_discovery));
		// List type of food
		category = (ListView) this.findViewById(R.id.category);
		category.setAdapter(new DiscoveryAdapter());

		TextView subMenuTitle = (TextView) findViewById(R.id.txt_sub_menu);
		subMenuTitle.setText(getString(R.string.txt_discovery));
	}

	public void onStop() {
		super.onStop();
	}

	// ===========================================================
	// Methods
	// ===========================================================
	void importData(byte[] data) {
		hashtable.clear();
		ByteBuffer buffer = ByteBuffer.wrap(data);
		int numType = buffer.get();// 1byte
		log.m("Number of type = " + numType);
		for (int n = 0; n < numType; n++) {
			int keyId = buffer.get();// 1byte
			hashtable.put(keyId, new Hashtable<String, FoodLocation>());
			int numberItem = buffer.get();// 1byte
			log.m("Number of item = " + numberItem);
			for (int i = 0; i < numberItem; i++) {
				int lenTitle = buffer.get();// 1byte
				byte[] bTitle = new byte[lenTitle];// get len byte
				buffer.get(bTitle);
				String title = new String(bTitle);
				log.m("========>Item: " + title);
				int lenAddr = buffer.get();
				byte[] bAddr = new byte[lenAddr];
				buffer.get(bAddr);
				String address = new String(bAddr);
				log.m("          Address:" + address);
				int lenContent = buffer.getInt();// content len
				byte[] bContent = new byte[lenContent];
				buffer.get(bContent);
				String content = new String(bContent);
				log.m("        Content: " + content);
				hashtable.get(keyId).put(title,
						new FoodLocation(address, content));
			}
		}
	}

	private void parserKeys() {
		Iterator<Integer> iterator = hashtable.keySet().iterator();
		while (iterator.hasNext()) {
			keys.add(iterator.next());
		}
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
	public class DiscoveryAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			// int count = 0;
			// Iterator<Integer> iterator = hashtable.keySet().iterator();
			// while (iterator.hasNext()) {
			// int key = iterator.next();
			// count += hashtable.get(key).size();
			// }
			// log.m("Num item = " + count);
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
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			if (convertView == null) {
				convertView = View.inflate(ctx,
						R.layout.discovery_category_item, null);
				// Name of food type
				TextView name = (TextView) convertView
						.findViewById(R.id.itemName);
				name.setText(typeVal[keys.get(position)]);
				// Number of item
				TextView number = (TextView) convertView
						.findViewById(R.id.numItem);
				number.setText("" + hashtable.get(keys.get(position)).size());

				convertView.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						Bundle bundle = new Bundle();
						bundle.putInt("type", keys.get(position));
						Intent intent = new Intent(
								DiscoveryHanoiFamousActivity.this,
								DiscoveryShowItemOfType.class);
						intent.putExtras(bundle);
						startActivity(intent);
						finish();
					}
				});
			}
			return convertView;
		}

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			hashtable.clear();
			Intent intent = new Intent(DiscoveryHanoiFamousActivity.this,
					DiscoveryMainActivity.class);
			startActivity(intent);
			finish();
			overridePendingTransition(R.anim.slide_in_left,
					R.anim.slide_out_right);
		}
		return super.onKeyDown(keyCode, event);
	}

	public void onBack(View v) {
		Intent intent = new Intent(DiscoveryHanoiFamousActivity.this,
				MenuActivity.class);
		startActivity(intent);
		finish();
		overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
	}

	public void onSearchButtonClick(View v) {
		Intent intent = new Intent(this, DinningServiceSearch.class);
		startActivity(intent);
		overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
	}
}
