package hust.hgbk.vtio.vinafood.ontology;

public class PropertyWithValue {
	PropertyData 	property;
	String   		value;
	String 			valueLabel;
	
	public PropertyData getProperty() {
		return property;
	}
	public void setProperty(PropertyData property) {
		this.property = property;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public String getValueLabel() {
		return valueLabel;
	}
	public void setValueLabel(String valueLabel) {
		this.valueLabel = valueLabel;
	}
	
}
