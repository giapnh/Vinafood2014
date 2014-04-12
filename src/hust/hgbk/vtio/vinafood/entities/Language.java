package hust.hgbk.vtio.vinafood.entities;

public class Language {
	String code;
	String name;
	int flagResource;
	
	public Language(String name, String code, int flagResource) {
		this.code = code;
		this.name = name;
		this.flagResource = flagResource;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getFlagResource() {
		return flagResource;
	}
	public void setFlagResource(int flagResource) {
		this.flagResource = flagResource;
	}
}
