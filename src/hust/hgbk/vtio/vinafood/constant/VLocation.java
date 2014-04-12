package hust.hgbk.vtio.vinafood.constant;


public class VLocation {
	public static Float GEO_LAT_DEFAULT = 21.0287f;
	public static Float GEO_LON_DEFAULT = 105.8522f;

	private Float geoLat = GEO_LAT_DEFAULT;
	private Float geoLon = GEO_LON_DEFAULT;
	private static VLocation currentLocation = new VLocation();
	boolean isAccessing = false;

	synchronized public static VLocation getInstance() {
		return currentLocation;
	}

	synchronized public void setGeo(Float lat, Float lon)
			throws InterruptedException {
		if (isAccessing) {
			wait();
		}
		isAccessing = true;
		this.geoLat = lat;
		this.geoLon = lon;
		isAccessing = false;
		notifyAll();
	}

	synchronized public float getLatitude() throws InterruptedException {
		if (isAccessing) {
			wait();
		}
		return geoLat;
	}

	synchronized public float getLongtitude() throws InterruptedException {
		if (isAccessing) {
			wait();
		}
		return geoLon;
	}

	synchronized public static boolean isGetedLocation() {
		try {
			if (currentLocation.geoLat == 0f || currentLocation.geoLon == 0f) {
				return false;
			} else if (currentLocation.getLatitude() == GEO_LAT_DEFAULT
					&& currentLocation.getLongtitude() == GEO_LON_DEFAULT) {
				return false;
			} else
				return true;
		} catch (Exception e) {
			return false;
		}

	}
}
