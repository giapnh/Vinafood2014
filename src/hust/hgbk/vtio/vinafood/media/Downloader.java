package hust.hgbk.vtio.vinafood.media;

import android.util.Log;
import org.apache.http.util.ByteArrayBuffer;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;

public class Downloader {
	public Downloader() {
	}
	
	/**
	 * Download a file from internet
	 * @param urlString
	 * @param filePath
	 */
	public void downloadFromUrl(String urlString,String filePath) {
		try {
			int BYTE_UNIT = 128;
//			String fileName = fileIndex + DEFAULT_TYPE;
			URL url = new URL(urlString); // you can write here any link
			
			File file = new File(filePath);
			
			long startTime = System.currentTimeMillis();
			
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
			ByteArrayBuffer baf = new ByteArrayBuffer(BYTE_UNIT);
			int count = 0;
			int current = 0;
			while ((current = bis.read()) != -1) {
				baf.append((byte) current);
				count ++;
			}


			
			FileOutputStream fos = new FileOutputStream(file);
			
			fos.write(baf.toByteArray());
			fos.close();
			Log.v("Downloader",
					"downloaded " + count + "bytes in"
							+ ((System.currentTimeMillis() - startTime) / 1000)
							+ " sec");

		} catch (IOException e) {
			e.printStackTrace();
			Log.v("Downloader", "Error!");
		}

	}
}
