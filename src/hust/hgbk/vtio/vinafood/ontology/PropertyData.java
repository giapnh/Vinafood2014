package hust.hgbk.vtio.vinafood.ontology;

public class PropertyData {
	private String 	propertyLabel;
	private String 	propertyURI;
	private boolean isObjectProperty;
	
	public PropertyData() {
	}
	
	public String getPropertyURI() {
		return propertyURI;
	}
	public void setPropertyURI(String propertyURI) {
		this.propertyURI = propertyURI;
	}
	public boolean isObjectProperty() {
		return isObjectProperty;
	}
	public void setObjectProperty(boolean isObjectProperty) {
		this.isObjectProperty = isObjectProperty;
	}
	public String getPropertyLabel() {
		return propertyLabel;
	}
	public void setPropertyLabel(String propertyLabel) {
		this.propertyLabel = propertyLabel;
	}
	
}
