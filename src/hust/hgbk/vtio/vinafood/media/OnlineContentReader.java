package hust.hgbk.vtio.vinafood.media;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

public class OnlineContentReader {
	public ArrayList<String> getStringArrayFromURL(String urlString) {
		return getStringArrayFromURL(urlString, -1);
	}

	public ArrayList<String> getStringArrayFromURL(String urlString, int maxLine) {
		ArrayList<String> line = new ArrayList<String>();
		try {
			URL url = new URL(urlString);
			URLConnection ucon = url.openConnection();
			InputStream is = ucon.getInputStream();
			BufferedReader input = new BufferedReader(new InputStreamReader(is));
			try {
				String temp = input.readLine();
				if (maxLine >= 0) {
					int i = 0;
					while (temp != null && i < maxLine) {
						line.add(temp);
						temp = input.readLine();
						i++;
					}

				} else {
					while (temp != null) {
						line.add(temp);
						temp = input.readLine();
					}
				}
			} finally {
				input.close();
				is.close();
			}
		} catch (IOException ex) {
			ex.printStackTrace();
		}

		return line;
	}

	public ArrayList<String> getStringArrayFromURLByHttpClient(String urlString) {
		ArrayList<String> line = new ArrayList<String>();
		try {
			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost(urlString);
			HttpResponse response = httpclient.execute(httppost);
			HttpEntity entity = response.getEntity();
			InputStream is = entity.getContent();
			BufferedReader input = new BufferedReader(new InputStreamReader(is));
			try {
				int i = 0;
				String temp = input.readLine();
				while (temp != null) {
					line.add(temp);
					temp = input.readLine();
					i++;
				}
			} finally {
				input.close();
				is.close();
			}
		} catch (IOException ex) {
			ex.printStackTrace();
		}

		return line;
	}
}
