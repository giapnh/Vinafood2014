package hust.hgbk.vtio.vinafood.config;

import android.util.Log;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

public class log {
	private static boolean DEBUG = true;
	public static String TAG = "Vinafood";

	public static synchronized void m(String msg) {
		if (DEBUG)
			Log.d(TAG, msg);
	}

	public static void bug(String msg) {
		try {
			throw new Exception(msg);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static synchronized void e(Throwable e, String msg) {
		if (DEBUG)
			Log.e(TAG, msg, e);
	}

	public static synchronized void e(String msg) {
		if (DEBUG)
			Log.e(TAG, msg);
	}
}
