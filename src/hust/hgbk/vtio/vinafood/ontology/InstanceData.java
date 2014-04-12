package hust.hgbk.vtio.vinafood.ontology;

import java.util.ArrayList;

public class InstanceData {
	String instanceLabel;
	String instanceURI;
	ClassData instanceClass;
	ArrayList<PropertyWithValue> listPropertyValues;
	
	public InstanceData() {
		// TODO Auto-generated constructor stub
	}
	
	
	public String getInstanceLabel() {
		return instanceLabel;
	}


	public void setInstanceLabel(String instanceLabel) {
		this.instanceLabel = instanceLabel;
	}


	public ClassData getInstanceClass() {
		return instanceClass;
	}
	public void setInstanceClass(ClassData instanceClass) {
		this.instanceClass = instanceClass;
	}
	public ArrayList<PropertyWithValue> getListPropertyValues() {
		return listPropertyValues;
	}
	public void setListPropertyValues(
			ArrayList<PropertyWithValue> listPropertyValues) {
		this.listPropertyValues = listPropertyValues;
	}

	public String getInstanceURI() {
		return instanceURI;
	}

	public void setInstanceURI(String instanceURI) {
		this.instanceURI = instanceURI;
	}
	
}
