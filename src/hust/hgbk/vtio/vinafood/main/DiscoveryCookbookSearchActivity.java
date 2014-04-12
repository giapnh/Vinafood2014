package hust.hgbk.vtio.vinafood.main;

import hust.hgbk.vtio.vinafood.R;
import hust.hgbk.vtio.vinafood.database.SQLiteAdapter;
import hust.hgbk.vtio.vinafood.entities.Cookbook;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class DiscoveryCookbookSearchActivity extends Activity {

	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================
	public Context ctx;
	public ListView listView;
	CuisineCookbookSearchAdapter adapter;
	AutoCompleteTextView autoCompleteTextView;
	ArrayAdapter<String> strAdapter;

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
		this.ctx = DiscoveryCookbookSearchActivity.this;
		setContentView(R.layout.discovery_cuisine_topic_search_main);
		autoCompleteTextView = (AutoCompleteTextView) this
				.findViewById(R.id.autoCompleteText);
		String titles[] = SQLiteAdapter.getInstance(this).getAllCookbookTopic();
		strAdapter = new ArrayAdapter<String>(this, R.layout.single_item,
				R.id.itemName, titles);
		autoCompleteTextView.setAdapter(strAdapter);
		listView = (ListView) this.findViewById(R.id.category);
		adapter = new CuisineCookbookSearchAdapter();
		listView.setAdapter(adapter);
		autoCompleteTextView.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {
				adapter.clear();
				adapter.addAll(getTopicsFromNames(strAdapter));
				adapter.notifyDataSetChanged();
			}
		});

	}

	public void onClearText(View v) {
		autoCompleteTextView.setText("");
	}

	// ===========================================================
	// Methods
	// ===========================================================
	public ArrayList<Cookbook> getTopicsFromNames(
			ArrayAdapter<String> strAdapter) {
		ArrayList<Cookbook> returnList = new ArrayList<Cookbook>();
		for (int i = 0; i < (strAdapter.getCount() > 10 ? 10 : strAdapter
				.getCount()); i++) {
			Cookbook topic = SQLiteAdapter.getInstance(ctx)
					.getCookbookTopicWithName(strAdapter.getItem(i));
			if (topic != null) {
				returnList.add(topic);
			}
		}
		return returnList;
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================

	public class CuisineCookbookSearchAdapter extends BaseAdapter {
		ArrayList<Cookbook> topics = new ArrayList<Cookbook>();

		public CuisineCookbookSearchAdapter() {
		}

		public void clear() {
			topics.clear();
		}

		public void addAll(ArrayList<Cookbook> list) {
			this.topics.addAll(list);
		}

		@Override
		public void notifyDataSetChanged() {
			super.notifyDataSetChanged();
		}

		@Override
		public int getCount() {
			return topics.size();
		}

		@Override
		public Object getItem(int position) {
			return topics.get(position);
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
			if (topics.get(paramInt).imgLink != null)
				Picasso.with(ctx).load(topics.get(paramInt).imgLink)
						.resize(130, 130).centerCrop().into(holder.icon);
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
					Intent intent = new Intent(
							DiscoveryCookbookSearchActivity.this,
							DiscoveryCookbookViewTopicActivity.class);
					intent.putExtra("topic", topics.get(paramInt));
					startActivity(intent);
					overridePendingTransition(R.anim.slide_in_right,
							R.anim.slide_out_left);
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
