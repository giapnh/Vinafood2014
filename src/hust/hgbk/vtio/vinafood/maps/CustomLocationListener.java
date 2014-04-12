package hust.hgbk.vtio.vinafood.maps;

import android.location.Location;

import com.google.android.gms.common.ConnectionResult;

public interface CustomLocationListener {
	void onProviderDisabled();

	void onLocationServiceConnect();

	void onLocationChanged(Location location);

	void onGetLastLocation(Location location);

	void onLocationUpdateFailed(ConnectionResult result);

	void onLocationServiceDisconnect();

	void onLocationStopUpdate();

	public class SimpleLocationListener implements CustomLocationListener {

		@Override
		public void onLocationServiceConnect() {
			// TODO Auto-generated method stub
		}

		@Override
		public void onLocationChanged(Location location) {
			// TODO Auto-generated method stub
		}

		@Override
		public void onLocationUpdateFailed(ConnectionResult result) {
			// TODO Auto-generated method stub
		}

		@Override
		public void onLocationServiceDisconnect() {
			// TODO Auto-generated method stub
		}

		@Override
		public void onProviderDisabled() {
			// TODO Auto-generated method stub
		}

		@Override
		public void onGetLastLocation(Location location) {
			// TODO Auto-generated method stub
		}

		@Override
		public void onLocationStopUpdate() {
			// TODO Auto-generated method stub

		}

	}
}
