package hust.hgbk.vtio.vinafood.ontology.simple;

public class ClassDataSimple {
	String Uri;
	String label;
	
	
	
	public ClassDataSimple() {
		super();
	}
	public ClassDataSimple(String uri, String label) {
		super();
		Uri = uri;
		this.label = label;
	}
	public String getUri() {
		return Uri;
	}
	public void setUri(String uri) {
		Uri = uri;
	}
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
}
