package hust.hgbk.vtio.vinafood.ontology.simple;

import java.util.ArrayList;

public class NodeTreeDataSimple {
	public String Uri;
	public String labelen;
	public String labelvn;
	public ArrayList<NodeTreeDataSimple> child=new ArrayList<NodeTreeDataSimple>();
	public NodeTreeDataSimple parent;
	
	public void setDataNode(String Uri, String labelen, String labelvn){
		this.Uri=Uri;
		this.labelen=labelen;
		this.labelvn=labelvn;
	}
	
	public void setNodeParent(NodeTreeDataSimple parent){
		this.parent=parent;	
	}
	
	public void addChildNode(NodeTreeDataSimple childNode){
		this.child.add(childNode);		
	}
	
	public String getLabel(String language){
		if(language.equals("en")){
			return labelen;
		}else{
			return labelvn;			
		}
	}
}
