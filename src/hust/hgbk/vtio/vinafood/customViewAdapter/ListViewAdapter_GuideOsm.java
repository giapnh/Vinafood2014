package hust.hgbk.vtio.vinafood.customViewAdapter;

import hust.hgbk.vtio.vinafood.R;
import hust.hgbk.vtio.vinafood.ontology.simple.ClassDataSimple;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ListViewAdapter_GuideOsm extends ArrayAdapter<ClassDataSimple>{
	Context ctx;
	int layoutResoureId;
	ClassDataSimple[] data=null;

	public ListViewAdapter_GuideOsm(Context context, int textViewResourceId,
			ClassDataSimple[] objects) {
		super(context, textViewResourceId, objects);
		// TODO Auto-generated constructor stub
		this.ctx=context;
		this.layoutResoureId=textViewResourceId;
		this.data=objects;
	}
	
	@Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        
            LayoutInflater inflater = ((Activity)ctx).getLayoutInflater();
            row = inflater.inflate(layoutResoureId, parent, false);
            
            ImageView imgIcon = (ImageView)row.findViewById(R.id.imgIcon);
            TextView txtTitle = (TextView)row.findViewById(R.id.txtTitle);
        
            txtTitle.setText(data[position].getLabel());
            if(data[position].getUri().equals("http://content.mapquest.com/mqsite/turnsigns/icon-dirs-start_sm.gif")){
            	imgIcon.setImageResource(R.drawable.icon_dirs_start_sm);
            }else if(data[position].getUri().equals("http://content.mapquest.com/mqsite/turnsigns/icon-dirs-end_sm.gif")){
            	imgIcon.setImageResource(R.drawable.icon_dirs_end_sm);
            }else if(data[position].getUri().equals("http://content.mapquest.com/mqsite/turnsigns/rs_right_sm.gif")){
            	imgIcon.setImageResource(R.drawable.rs_right_sm);
            }else if(data[position].getUri().equals("http://content.mapquest.com/mqsite/turnsigns/rs_left_sm.gif")){
            	imgIcon.setImageResource(R.drawable.rs_left_sm);
            }else if(data[position].getUri().equals("http://content.mapquest.com/mqsite/turnsigns/rs_slight_right_sm.gif")){
            	imgIcon.setImageResource(R.drawable.rs_slight_right_sm);
            }else if(data[position].getUri().equals("http://content.mapquest.com/mqsite/turnsigns/rs_slight_left_sm.gif")){
            	imgIcon.setImageResource(R.drawable.rs_slight_left_sm);
            }else if(data[position].getUri().equals("http://content.mapquest.com/mqsite/turnsigns/rs_sharp_left_sm.gif")){
            	imgIcon.setImageResource(R.drawable.rs_sharp_left_sm);
            }else if(data[position].getUri().equals("http://content.mapquest.com/mqsite/turnsigns/rs_sharp_right_sm.gif")){
            	imgIcon.setImageResource(R.drawable.rs_sharp_right_sm);
            } 
//        try {
//			holder.imgIcon.setImageDrawable(drawableFromUrl(dta.Uri));
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
        
        return row;
    }
//    public Drawable drawableFromUrl(String url) throws IOException {
//        Bitmap x;
//
//        //HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
//       // connection.connect();
//       // InputStream input = connection.getInputStream();
//
//      //  String url = "http://soule.thomas.free.fr/appy.jpg";
//        InputStream in = new java.net.URL(url).openStream();
//        //Bitmap bmp = BitmapFactory.decodeStream(new PatchInputStream(in));
//        
//        x = BitmapFactory.decodeStream(new PatchInputStream(in));
//        return new BitmapDrawable(x);
//    }
//    
//    public Drawable drawable_from_url(String url, String src_name) throws java.net.MalformedURLException, java.io.IOException {
//        return Drawable.createFromStream(((java.io.InputStream)new java.net.URL(url).getContent()), src_name);
//
//    }
//    public class PatchInputStream extends FilterInputStream {
//    	  public PatchInputStream(InputStream in) {
//    	    super(in);
//    	  }
//    	  public long skip(long n) throws IOException {
//    	    long m = 0L;
//    	    while (m < n) {
//    	      long _m = in.skip(n-m);
//    	      if (_m == 0L) break;
//    	      m += _m;
//    	    }
//    	    return m;
//    	  }
//    	}

}
