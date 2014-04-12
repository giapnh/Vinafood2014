package hust.hgbk.vtio.vinafood.ontology.simple;

import hust.hgbk.vtio.vinafood.constant.VLocation;

import java.text.DecimalFormat;

public class PlaceDataSimple {
	String URI ="";
	String label = "";
	String hasAbstract ="";
	String type ="";
	int hasRating;
	String imageURL ="";
	boolean isWellknown = false;
	String address ="";
	String phoneNumber = "";
	// long, lat - dungct
	double longtitude;
	double latitude;
	
	public double getLongtitude() {
		return longtitude;
	}
	public void setLongtitude(double longtitude) {
		this.longtitude = longtitude;
	}
	public double getLatitude() {
		return latitude;
	}
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}
	//
	
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	public String getHasAbstract() {
		if (hasAbstract.contains("anyType")){
			return "";
		} else 
			return hasAbstract;
	}
	public void setHasAbstract(String hasAbstract) {
		this.hasAbstract = hasAbstract;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	
	public int getHasRating() {
		return hasRating;
	}
	public void setHasRating(int hasRating) {
		this.hasRating = hasRating;
	}
	public String getImageURL() {
		return imageURL;
	}
	public void setImageURL(String imageURL) {
		this.imageURL = imageURL;
	}
	public boolean isWellknown() {
		return isWellknown;
	}
	public void setWellknown(boolean isWellknown) {
		this.isWellknown = isWellknown;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getURI() {
		return URI;
	}
	public void setURI(String uRI) {
		URI = uRI;
	}
	
	public String getPhoneNumber() {
		return phoneNumber;
	}
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	public String getDistanceString(){
		try {
			final float longitude = VLocation.getInstance().getLongtitude();
			final float latitude = VLocation.getInstance().getLatitude();
			if (this.latitude == 0f || this.longtitude == 0f){
				return "Unknow";
			}
			final android.location.Location currentLocation = new android.location.Location("current");
			currentLocation.setLatitude(latitude);
			currentLocation.setLongitude(longitude);
			final android.location.Location location1 = new android.location.Location("1");
			location1.setLatitude(this.latitude);
			location1.setLongitude(this.longtitude);
			float distance1 = currentLocation.distanceTo(location1);
			distance1 = distance1/1000f;
			DecimalFormat myFormatter = new DecimalFormat("#.##");
			String output = myFormatter.format(distance1);
			return output + "km";
		} catch (Exception e) {
			// TODO: handle exception
		}
		return "Unknow";
	}
	public void set(String uRI, String label, String hasAbstract,
			String type, int hasRating, String imageURL, boolean isWellknown,
			String address, String phoneNumber, double longtitude,
			double latitude) {
		URI = uRI;
		this.label = label;
		this.hasAbstract = hasAbstract;
		this.type = type;
		this.hasRating = hasRating;
		this.imageURL = imageURL;
		this.isWellknown = isWellknown;
		if (!address.contains("anyType")){
			this.address = address;
			
		}
		this.phoneNumber = phoneNumber;
		this.longtitude = longtitude;
		this.latitude = latitude;
	}
	
}
