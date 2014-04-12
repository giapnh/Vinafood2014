package hust.hgbk.vtio.vinafood.vtioservice;

import hust.hgbk.vtio.vinafood.constant.VLocation;

import java.io.Serializable;
import java.text.DecimalFormat;

public class FullDataInstance implements Serializable {
	private static final long serialVersionUID = 1L;
	private String abstractInfo;
	private String address;
	private String imageURL;
	private String label;
	private double latitude;
	private double longitude;
	private String location;
	private String phone;
	private int ratingNum;
	private String type;
	private String uri;
	private String wellKnown;

	public String getAbstractInfo() {
		return abstractInfo;
	}

	public void setAbstractInfo(String abstractInfo) {
		if (abstractInfo.contains("anyType")) {
			this.abstractInfo = "";
		} else {
			this.abstractInfo = abstractInfo;
		}
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getImageURL() {
		return imageURL;
	}

	public void setImageURL(String imageURL) {
		this.imageURL = imageURL;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public int getRatingNum() {
		return ratingNum;
	}

	public void setRatingNum(int ratingNum) {
		this.ratingNum = ratingNum;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getUri() {
		return uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}

	public String getWellKnown() {
		return wellKnown;
	}

	public void setWellKnown(String wellKnown) {
		this.wellKnown = wellKnown;
	}

	private String distanceString = "";

	public String getDistanceString() {
		if (distanceString.length() > 0)
			return distanceString;
		try {
			final float longitude = VLocation.getInstance().getLongtitude();
			final float latitude = VLocation.getInstance().getLatitude();
			if (this.latitude == 0f || this.longitude == 0f) {
				return "Unknow";
			}
			final android.location.Location currentLocation = new android.location.Location(
					"current");
			currentLocation.setLatitude(latitude);
			currentLocation.setLongitude(longitude);
			final android.location.Location location1 = new android.location.Location(
					"1");
			location1.setLatitude(this.latitude);
			location1.setLongitude(this.longitude);
			float distance1 = currentLocation.distanceTo(location1);
			distance1 = distance1 / 1000f;
			DecimalFormat myFormatter = new DecimalFormat("#.##");
			String output = myFormatter.format(distance1);
			distanceString = output + "km";
			return distanceString;
		} catch (Exception e) {
			// TODO: handle exception
		}
		return "Unknow";
	}

	public void createData(String uRI, String label, String hasAbstract,
			String type, int hasRating, String imageURL, String isWellknown,
			String address, String phoneNumber, double longtitude,
			double latitude) {
		this.uri = uRI;
		this.label = label;
		this.abstractInfo = hasAbstract;
		this.type = type;
		this.ratingNum = hasRating;
		this.imageURL = imageURL;
		this.wellKnown = isWellknown;
		if (!address.contains("anyType")) {
			this.address = address;
		}
		this.phone = phoneNumber;
		this.longitude = longtitude;
		this.latitude = latitude;
	}
}
