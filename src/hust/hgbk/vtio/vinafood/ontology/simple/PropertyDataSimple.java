package hust.hgbk.vtio.vinafood.ontology.simple;

public class PropertyDataSimple {
	String url;
	String label;
	boolean isObjectProperty;
	
	public PropertyDataSimple() {
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	public boolean isObjectProperty() {
		return isObjectProperty;
	}
	public void setObjectProperty(boolean isObjectProperty) {
		this.isObjectProperty = isObjectProperty;
	}
}
