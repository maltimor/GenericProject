package es.maltimor.genericProcess;

public class GenericProcessMapperInfoParam {
	private String name;
	private String type;
	private String size;
	private String secuenceName;
	private String description;

	public GenericProcessMapperInfoParam() {
		this.name = "";
		this.type = "";
		this.size = "";
		this.secuenceName = "";
		this.description = "";
	}

	public GenericProcessMapperInfoParam(String name, String type, String size, boolean fullText, String secuence, String desc) {
		this.name = name;
		this.type = type;
		this.size = size;
		this.secuenceName = secuence;
		this.description = desc;
	}
	
	public String toString(){
		String res = name+"("+type+"#"+size+"#"+secuenceName+"#"+description+")";
		return res;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getSize() {
		return size;
	}

	public void setSize(String size) {
		this.size = size;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	public String getSecuenceName() {
		return secuenceName;
	}

	public void setSecuenceName(String secuenceName) {
		this.secuenceName = secuenceName;
	}

}
