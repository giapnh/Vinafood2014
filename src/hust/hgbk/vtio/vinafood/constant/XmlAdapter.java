package hust.hgbk.vtio.vinafood.constant;

import hust.hgbk.vtio.vinafood.config.ServerConfig;

import java.util.Locale;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.util.Log;

public class XmlAdapter {
	public static void saveLanguageSign(Context ctx, String langSign) {
		SharedPreferences preferences = ctx.getSharedPreferences("Config",
				Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = preferences.edit();
		editor.putString("lang", langSign);
		editor.commit();
		editor.clear();
		editor = null;
		Runtime.getRuntime().gc();
	}

	public static String getLanguage(Context ctx) {
		SharedPreferences preferences = ctx.getSharedPreferences("Config",
				Context.MODE_PRIVATE);
		return preferences.getString("lang", null);
	}

	public static void saveNewVersion(Context ctx, String newVersion) {
		SharedPreferences preferences = ctx.getSharedPreferences("Config",
				Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = preferences.edit();
		editor.putString("newversion", newVersion);
		editor.commit();
		editor.clear();
		editor = null;
		Runtime.getRuntime().gc();
	}

	public static String getNewVersion(Context ctx) {
		SharedPreferences preferences = ctx.getSharedPreferences("Config",
				Context.MODE_PRIVATE);
		return preferences.getString("newversion", null);
	}

	public static void saveCityUri(Context ctx, String cityUri) {
		SharedPreferences preferences = ctx.getSharedPreferences("Config",
				Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = preferences.edit();
		editor.putString("city", cityUri);
		editor.commit();
		editor.clear();
		editor = null;
		Runtime.getRuntime().gc();
	}

	public static String getCityUri(Context ctx) {
		SharedPreferences preferences = ctx.getSharedPreferences("Config",
				Context.MODE_PRIVATE);
		return preferences.getString("city", null);
	}

	public static void synConfig(Context ctx) {
		String lang = getLanguage(ctx);
		if (lang != null) {

			if (lang.equals(LanguageCode.VI_LOCALE)) {
				ServerConfig.LANGUAGE_CODE = LanguageCode.VIETNAMESE;
			} else {
				ServerConfig.LANGUAGE_CODE = LanguageCode.ENGLISH;
				lang = "en";
			}
			setLanguage(lang, ctx);
		} else {
			ServerConfig.LANGUAGE_CODE = LanguageCode.VIETNAMESE;
			lang = "vi";
			setLanguage(lang, ctx);
			saveLanguageSign(ctx, lang);
		}
		String city = getCityUri(ctx);

		if (city != null) {
			for (int i = 0; i < ServerConfig.cityString.length; i++) {
				if (city.equals(ServerConfig.cityString[i][2])) {
					ServerConfig.currentCityUri = ServerConfig.cityString[i][2];
					ServerConfig.currentCityLabel = ServerConfig.cityString[i][1];
					Log.v("CITY", ServerConfig.currentCityLabel);
					break;
				}
			}
		} else {
			ServerConfig.currentCityUri = ServerConfig.HANOI_URI;
			ServerConfig.currentCityLabel = "Hà Nội";
			saveCityUri(ctx, ServerConfig.HANOI_URI);
		}
	}

	private static void setLanguage(String lang, Context ctx) {
		Locale locale;
		locale = new Locale(lang);

		// Locale.setDefault(locale);
		Configuration config = new Configuration();
		config.locale = locale;
		ctx.getResources().updateConfiguration(config,
				ctx.getResources().getDisplayMetrics());
	}

	public static void saveIsChooseGrid(Context ctx, boolean isGrid) {
		SharedPreferences preferences = ctx.getSharedPreferences("Config",
				Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = preferences.edit();
		editor.putBoolean("isGrid", isGrid);
		editor.commit();
		editor.clear();
		editor = null;
	}

	public static boolean isChooseGrid(Context ctx) {
		SharedPreferences preferences = ctx.getSharedPreferences("Config",
				Context.MODE_PRIVATE);
		return preferences.getBoolean("isGrid", true);
	}

	public static boolean isUseMapQuess(Context ctx) {
		SharedPreferences preferences = ctx.getSharedPreferences("Config",
				Context.MODE_PRIVATE);
		return preferences.getBoolean("mapquess", false);
	}

	public static void useMapQuess(Context ctx, boolean isUse) {
		SharedPreferences preferences = ctx.getSharedPreferences("Config",
				Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = preferences.edit();
		editor.putBoolean("mapquess", isUse);
		editor.commit();
		editor.clear();
		editor = null;
	}

	// public static Class getShowOnMapActivity(Context ctx) {
	// try {
	// MapActivity m = new MapActivity() {
	//
	// @Override
	// protected boolean isRouteDisplayed() {
	// // TODO Auto-generated method stub
	// return false;
	// }
	// };
	// } catch (NoClassDefFoundError e) {
	// return ShowPlaceOnOsmap.class;
	// }
	// if (isUseMapQuess(ctx))
	// return ShowPlaceOnOsmap.class;
	// else
	// return ShowPlaceOnMapsActivity.class;
	// }

	// public static Class getShowAllOnMapActivity(Context ctx) {
	// try {
	// MapActivity m = new MapActivity() {
	//
	// @Override
	// protected boolean isRouteDisplayed() {
	// // TODO Auto-generated method stub
	// return false;
	// }
	// };
	// } catch (NoClassDefFoundError e) {
	// return ShowPlaceOnOsmap.class;
	// }
	// if (isUseMapQuess(ctx))
	// return ShowAllPlace_Osm.class;
	// else
	// return ShowAllPlaceOnMap.class;
	// }
}
