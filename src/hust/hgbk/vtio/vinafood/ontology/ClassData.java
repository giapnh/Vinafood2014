package hust.hgbk.vtio.vinafood.ontology;

public class ClassData {
	private String 	classLabel;
	private String  classComment;
	private String 	classURI;
	private boolean hasSubClass;
	private boolean hasSuperClass;
	
	public ClassData() {
	}


	public String getClassURI() {
		return classURI;
	}

	public void setClassURI(String classURI) {
		this.classURI = classURI;
	}

	public boolean isHasSubClass() {
		return hasSubClass;
	}

	public void setHasSubClass(boolean hasSubClass) {
		this.hasSubClass = hasSubClass;
	}

	public boolean isHasSuperClass() {
		return hasSuperClass;
	}

	public void setHasSuperClass(boolean hasSuperClass) {
		this.hasSuperClass = hasSuperClass;
	}


	public String getClassLabel() {
		return classLabel;
	}


	public void setClassLabel(String classLabel) {
		this.classLabel = classLabel;
	}


	public String getClassComment() {
		return classComment;
	}


	public void setClassComment(String classComment) {
		this.classComment = classComment;
	}
	
}
