package hust.hgbk.vtio.vinafood.network;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

public class ConnectionManager {

	//private static ConnectionManager instance = new ConnectionManager();
	static ConnectivityManager connectivityManager;
	static NetworkInfo wifiInfo, mobileInfo;
	//static Context context;
	static boolean connected = false;
    /* 
	public static ConnectionManager getInstance(Context ctx) {

		context = ctx;
		return instance;
	}
    */
	public static boolean isOnline(Context con) {

		try {
			connectivityManager = (ConnectivityManager) con
					.getSystemService(Context.CONNECTIVITY_SERVICE);

			NetworkInfo networkInfo = connectivityManager
					.getActiveNetworkInfo();
			connected = networkInfo != null && networkInfo.isAvailable()
					&& networkInfo.isConnected();
			return connected;

		} catch (Exception e) {
			System.out
					.println("CheckConnectivity Exception: " + e.getMessage());
			Log.v("connectivity", e.toString());
		}

		return connected;
	}
}