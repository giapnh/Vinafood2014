package hust.hgbk.vtio.vinafood.customViewAdapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import hust.hgbk.vtio.vinafood.ontology.simple.InstanceDataSimple;

import java.util.ArrayList;
import java.util.List;

public class ArrayInstanceDataSimpleAdapter extends	ArrayAdapter<InstanceDataSimple> {
	
	ArrayList<InstanceDataSimple> listInstance = new ArrayList<InstanceDataSimple>();
	Context context;
	
	public ArrayInstanceDataSimpleAdapter(Context context, int textViewResourceId, List<InstanceDataSimple> objects) {
		super(context, textViewResourceId, objects);
		this.listInstance = (ArrayList<InstanceDataSimple>) objects;
		this.context = context;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		return super.getView(position, convertView, parent);
	}
	
	@Override
	public int getCount() {
		return listInstance.size();
	}
}
