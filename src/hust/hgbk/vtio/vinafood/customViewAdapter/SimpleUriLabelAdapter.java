package hust.hgbk.vtio.vinafood.customViewAdapter;

import hust.hgbk.vtio.vinafood.R;
import hust.hgbk.vtio.vinafood.ontology.simple.InstanceDataSimple;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class SimpleUriLabelAdapter extends ArrayAdapter<InstanceDataSimple> {
	Context context;
	ArrayList<InstanceDataSimple> instances;
	
	public SimpleUriLabelAdapter(Context context, int textViewResourceId,
			ArrayList<InstanceDataSimple> objects) {
		super(context, textViewResourceId, objects);
		this.context = context;
		instances = objects;
	}
	public View getView(int position, View convertView, ViewGroup parent) {
		String label = instances.get(position).getLabel();
		Log.v("TEST", label);
		TextView view;
		if (convertView == null){
			view = new TextView(context);
		} else {
			view = (TextView) convertView;
		}
		view.setTextColor(Color.BLUE);
		view.setText(label);
		
		return view;
	}
	@Override
	public View getDropDownView(int position, View convertView, ViewGroup parent) {
		TextView view;
		LayoutInflater inflater=(LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		view = (TextView) inflater.inflate(R.layout.simple_row_item, parent, false);
		String label = instances.get(position).getLabel();
		view.setText(label);
		
		return view;
	}
}
