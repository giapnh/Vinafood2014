package hust.hgbk.vtio.vinafood.entities;

import android.content.Context;
import android.content.Intent;

public class SubApplication {
	String appName;
	String appDescription;
	int appIconId;
	Intent   intent; 
	public SubApplication() {
	}
	public String getAppName() {
		return appName;
	}
	public void setAppName(String appName) {
		this.appName = appName;
	}
	public String getAppDescription() {
		return appDescription;
	}
	public void setAppDescription(String appDescription) {
		this.appDescription = appDescription;
	}
	public int getAppIconId() {
		return appIconId;
	}
	public void setAppIconId(int appIconId) {
		this.appIconId = appIconId;
	}
	public Intent getIntent() {
		return intent;
	}
	public void setIntent(Context context, Class<?> fowardClass) {
		intent = new Intent(context, fowardClass);
	}
}
