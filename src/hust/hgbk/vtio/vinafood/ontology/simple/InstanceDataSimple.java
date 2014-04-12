package hust.hgbk.vtio.vinafood.ontology.simple;

public class InstanceDataSimple {
	String label;
	String URI;
	int type;
	String instanceType;
	Double longitude;
	Double latitude;
	
	public InstanceDataSimple(){
		
	}
	
	
	
	
	public InstanceDataSimple(String label, String uRI) {
		this.label = label;
		URI = uRI;
	}




	public Double getLongitude() {
		return longitude;
	}
	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}
	public Double getLatitude() {
		return latitude;
	}
	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	public String getURI() {
		return URI;
	}
	public void setURI(String uRI) {
		URI = uRI;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public String getInstanceType() {
		return instanceType;
	}
	public void setInstanceType(String instanceType) {
		this.instanceType = instanceType;
	}
}
