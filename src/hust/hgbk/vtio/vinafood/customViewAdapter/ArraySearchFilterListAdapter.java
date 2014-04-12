package hust.hgbk.vtio.vinafood.customViewAdapter;

import hust.hgbk.vtio.vinafood.R;
import hust.hgbk.vtio.vinafood.ontology.simple.ClassDataSimple;

import java.util.ArrayList;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class ArraySearchFilterListAdapter extends BaseAdapter {

	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================
	Context context;
	ArrayList<ClassDataSimple> dataSimples;
	public ClassDataSimple selectedData;

	// ===========================================================
	// Constructors
	// ===========================================================
	public ArraySearchFilterListAdapter(Context _context,
			ArrayList<ClassDataSimple> _dataSimples) {
		this.context = _context;
		this.dataSimples = _dataSimples;
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	public int getCount() {
		return dataSimples.size();
	}

	@Override
	public Object getItem(int position) {
		return dataSimples.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		View view;
		if (convertView == null) {
			view = View.inflate(context, R.layout.multichoise_item, null);
		} else {
			view = convertView;
		}

		TextView name = (TextView) view.findViewById(R.id.txtFilterItem);
		name.setText(dataSimples.get(position).getLabel());
		view.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				selectedData = dataSimples.get(position);
			}
		});
		return view;
	}
	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
