package hust.hgbk.vtio.vinafood.customview;

import hust.hgbk.vtio.vinafood.R;
import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.VideoView;

public class StarVideoView extends LinearLayout {
	Context context;
	String url;

	public StarVideoView(Context context, String url) {
		super(context);
		this.context = context;
		this.url = url;

		LayoutInflater li = (LayoutInflater) context
		        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		li.inflate(R.layout.video_layout, this, true);

		VideoView videoView = (VideoView) findViewById(R.id.videoView);
		videoView.setVideoURI(Uri.parse(url));
		videoView.setMediaController(new MediaController(context));
	}

}
