package hust.hgbk.vtio.vinafood.customViewAdapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

import java.util.ArrayList;

public class ContinuousAdapter extends BaseAdapter {
	ArrayList<ImageView> listImageViews;
	ArrayList<Bitmap> listBitmaps;
	Bitmap bitmap;
	Bitmap bitmapSelected;
	
	public ContinuousAdapter(Context context, ArrayList<Bitmap> bitmaps) {
		listBitmaps = bitmaps;
		listImageViews = new ArrayList<ImageView>();
		float width = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 80, context.getResources().getDisplayMetrics());
		float height = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 80, context.getResources().getDisplayMetrics());
		
		for(Bitmap bitmap : bitmaps) {
			this.bitmap = bitmap;
			ImageView imageView = new ImageView(context);
			imageView.setScaleType(ScaleType.CENTER_CROP);
			imageView.setImageBitmap(bitmap);
			imageView.setLayoutParams(new Gallery.LayoutParams((int) width, (int) height));
			listImageViews.add(imageView);
		}
	}
	

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return Integer.MAX_VALUE;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		int id = 0;
		if(listImageViews.size() > 0) {
		    id = position % listImageViews.size();
		}
		
		ImageView result = listImageViews.get(id);
		
		return result;
	}
	
	public Bitmap getBitmapSelected(int position) {
		int id = position % listBitmaps.size();
		
		return listBitmaps.get(id);
	}
}
