package hust.hgbk.vtio.vinafood.main;

import hust.hgbk.vtio.vinafood.R;
import hust.hgbk.vtio.vinafood.database.SQLiteAdapter;
import hust.hgbk.vtio.vinafood.entities.Cookbook;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class DiscoveryCookbookActivity extends Activity {

	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================
	public Context ctx;
	public ListView listView;
	public TextView indexPage;
	DiscoveryHelthAdapter adapter;

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
		this.ctx = DiscoveryCookbookActivity.this;
		setContentView(R.layout.discovery_cuisine_cookbook_main);
		listView = (ListView) this.findViewById(R.id.category);
		indexPage = (TextView) this.findViewById(R.id.index_page);
		adapter = new DiscoveryHelthAdapter();
		adapter.load(adapter.LIMIT, 0);
		listView.setAdapter(adapter);
	}

	// ===========================================================
	// Methods
	// ===========================================================

	public void onSearchButtonClick(View v) {
		Intent intent = new Intent(DiscoveryCookbookActivity.this,
				DiscoveryCookbookSearchActivity.class);
		startActivity(intent);
		overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
	}

	public void onNext(View v) {
		if (adapter.CURR_PAGE != adapter.NUM_PAGE) {
			adapter.CURR_PAGE++;
			adapter.load(adapter.LIMIT, adapter.OFFSET + adapter.LIMIT);
			adapter.notifyDataSetChanged();
		}
	}

	public void onPrev(View v) {
		if (adapter.CURR_PAGE != 1) {
			adapter.CURR_PAGE--;
			adapter.load(adapter.LIMIT, adapter.OFFSET - adapter.LIMIT);
			adapter.notifyDataSetChanged();
		}
	}

	public void onBack(View v) {
		Intent intent = new Intent(DiscoveryCookbookActivity.this,
				MenuActivity.class);
		startActivity(intent);
		finish();
		overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			Intent intent = new Intent(DiscoveryCookbookActivity.this,
					DiscoveryMainActivity.class);
			startActivity(intent);
			overridePendingTransition(R.anim.slide_in_left,
					R.anim.slide_out_right);
			finish();
		}
		return super.onKeyDown(keyCode, event);
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================

	public class DiscoveryHelthAdapter extends BaseAdapter {
		public int TOTAL = 0;
		public int NUM_PAGE = 0;
		public int CURR_PAGE = 1;
		public int LIMIT = 10;
		public int OFFSET = 0;
		ArrayList<Cookbook> topics = new ArrayList<Cookbook>();

		public DiscoveryHelthAdapter() {
			TOTAL = SQLiteAdapter.getInstance(ctx).cookbookCount();
			NUM_PAGE = (int) Math.ceil(TOTAL / LIMIT);
			indexPage.setText(CURR_PAGE + "/" + NUM_PAGE);
		}

		@Override
		public void notifyDataSetChanged() {
			indexPage.setText(CURR_PAGE + "/" + NUM_PAGE);
			super.notifyDataSetChanged();
		}

		public void load(int limit, int offset) {
			topics = SQLiteAdapter.getInstance(ctx).getCookbookTopics(limit,
					offset);
			OFFSET = offset;
		}

		@Override
		public int getCount() {
			return topics.size();
		}

		@Override
		public Object getItem(int paramInt) {
			return topics.get(paramInt);
		}

		@Override
		public long getItemId(int paramInt) {
			return paramInt;
		}

		@Override
		public View getView(final int paramInt, View convertView,
				ViewGroup paramViewGroup) {
			View v = convertView;
			ViewHolder holder;
			if (v == null) {
				v = View.inflate(ctx,
						R.layout.discovery_cuisine_topic_row_layout, null);
				holder = new ViewHolder();
				holder.icon = (ImageView) v.findViewById(R.id.img_icon);
				holder.title = (TextView) v.findViewById(R.id.title);
				holder.description = (TextView) v
						.findViewById(R.id.description);

				v.setTag(holder);
			} else {
				holder = (ViewHolder) v.getTag();
			}
			try {
				if (topics.get(paramInt).imgLink != null)
					Picasso.with(ctx).load(topics.get(paramInt).imgLink)
							.resize(130, 130).centerCrop().into(holder.icon);
			} catch (Exception ex) {
			}
			holder.title.setText(topics.get(paramInt).title);
			String txtDescription = topics.get(paramInt).description.substring(
					0, topics.get(paramInt).description.length() > 200 ? 200
							: topics.get(paramInt).description.length());
			holder.description.setText(txtDescription
					+ (topics.get(paramInt).description.length() > 200 ? "..."
							: ""));
			v.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					Intent intent = new Intent(DiscoveryCookbookActivity.this,
							DiscoveryCookbookViewTopicActivity.class);
					intent.putExtra("topic", topics.get(paramInt));
					startActivity(intent);
					overridePendingTransition(R.anim.slide_in_right,
							R.anim.slide_out_left);
					finish();
				}
			});
			return v;
		}

		class ViewHolder {
			ImageView icon;
			TextView title;
			TextView description;
		}
	}
}
