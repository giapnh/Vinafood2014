package hust.hgbk.vtio.vinafood.media;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.util.Log;
import org.apache.http.util.ByteArrayBuffer;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;

public class DownloadAdapter {

	public static String PATH; // put the downloaded file here


	public static void setSoundPath() {
		if (hasSDCard()) {
			PATH = Environment.getExternalStorageDirectory().getAbsolutePath()
					+ "/data/hust.se.vtio/files/";
		} else {
			PATH = "/data/data/hust.se.vtio/files/";
		}
		File file = new File(PATH);
		file.mkdirs();
			
	}

	public static void DownloadFromUrl(Context ctx, String fileUrl){
		try {
			String fileName = fileUrl.replace("http://icompanion.vn/resources/icon/", "");
			File file = new File(PATH + fileName);
			if (file.exists()){
//				Log.v("TEST", "Exist: " + fileName);
				return;
			}
			
			try {
				copyFile(ctx, fileName);
				if (file.exists()){
					Log.v("TEST", "Exist copied: " + fileName);
					return;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
				
			URL url = new URL(fileUrl); // you can write here any link
			
			
			long startTime = System.currentTimeMillis();
			Log.d("ImageManager", "download begining");
			Log.d("ImageManager", "download url:" + url);
			Log.d("ImageManager", "downloaded file name: " + PATH + fileName);
			/* Open a connection to that URL. */
			URLConnection ucon = url.openConnection();

			/*
			 * Define InputStreams to read from the URLConnection.
			 */
			InputStream is = ucon.getInputStream();
			BufferedInputStream bis = new BufferedInputStream(is);

			/*
			 * Read bytes to the Buffer until there is nothing more to read(-1).
			 */
			ByteArrayBuffer baf = new ByteArrayBuffer(50);
			int current = 0;
			while ((current = bis.read()) != -1) {
				baf.append((byte) current);
			}

			/* Convert the Bytes read to a String. */
		//	FileOutputStream fos = ctx.openFileOutput(PATH + fileName,
	//				ctx.MODE_WORLD_READABLE);
			
			FileOutputStream fos = new FileOutputStream(file);
			
			fos.write(baf.toByteArray());
			fos.close();
			Log.d("ImageManager",
					"download ready in"
							+ ((System.currentTimeMillis() - startTime) / 1000)
							+ " sec");

		} catch (IOException e) {
			Log.d("ImageManager", "Error: " + fileUrl + " : " + e);
		}

	}

	public static void copyFile(Context ctx, String dataName) throws Exception {

		InputStream myInput = ctx.getAssets().open("files/"+dataName);
		
		String outFileName = PATH + dataName;
		OutputStream myOutput = new FileOutputStream(outFileName);
		
		byte[] buffer = new byte[1024];
		int length;
		int i =0;
		
		while ((length = myInput.read(buffer)) > 0) {
			
			i++;
			myOutput.write(buffer, 0, length);

		}
		myOutput.flush();
		myOutput.close();
		myInput.close();
	}
	public static boolean hasSDCard() {

		File root = Environment.getExternalStorageDirectory();

		return (root.exists() && root.canWrite());

	}
	
	public static Drawable getIcon(Context ctx, String url){
		if (url.contains("null") || url.equals(null)) return null;
		DownloadFromUrl(ctx, url);
		String fileName = url.replace("http://icompanion.vn/resources/icon/", "");
		try {
			Drawable icon = Drawable.createFromPath(PATH + fileName); 
//			Log.v("TEST2", "open: " + PATH + fileName + " : " + url);
			return icon;
		} catch (Exception e) {
			Log.v("TEST2", "error: " + fileName);
			return null;
		}
		
	}
	
}
