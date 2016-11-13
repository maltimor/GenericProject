package es.maltimor.genericProcess;

import java.util.*;

import org.springframework.context.ApplicationContext;

public class GenericProcessMapperInfoProcess {
	private String nombre;
	private String virtualNombre;
	private List<GenericProcessMapperInfoParam> fields;
	private List<String> keys;
	private String keySeparator;
	private Map<String,List<String>> actionRoles;
	private ApplicationContext context;
	private GenericProcessMapperInfoProcessResolver resolver;
	private GenericProcessJavaResolver javaResolver;
	private GenericProcessMapperSecurityProcessResolver secResolver;
	
	private String type;

	// por ahora solo se coge el keys[0]
	public GenericProcessMapperInfoProcess() {
		this.fields = new ArrayList<GenericProcessMapperInfoParam>();
		this.keys = new ArrayList<String>();
		this.keySeparator = "/";								//separador por defecto de la plataforma
		this.actionRoles = new HashMap<String,List<String>>();
		this.resolver = new GenericProcessMapperInfoProcessResolverImpl();			//resolver por defecto
		this.secResolver = new GenericProcessMapperSecurityProcessResolverImpl();
		this.javaResolver=new GenericProcessJavaResolverImpl();//resolver por defecto
	}

	public void setInfo(String info) throws Exception {
		System.out.println("## GenericProcessMapperInfoProcess:" + info);
		String[] tokens = info.split("\\|");
		if (tokens.length < 3) throw new Exception("Error en la sintaxis GenericProcessMapperInfo.setInfo( " + info + " )");

		//establezco los parametros de la tabla: nombre y separador de keys y resolvedorPordefecto
		String head=tokens[0];
		if ((head.contains(":")&&(!head.contains("@")))){
			System.out.println("CASO1");
			String[] vNombre=head.split(":");
			virtualNombre=vNombre[0];
			if (vNombre[1].contains("#")){
				String tipoR[]=vNombre[1].split("#");
				nombre=tipoR[0];
				type=tipoR[1];
			}else{
				nombre=vNombre[1];
				type="P";
			}
		} else if (head.contains("@")){
			String[] vNombre=head.split("@");
			if(vNombre[0].contains(":")){
				System.out.println("CASO2");
				String[] vNombre2=vNombre[0].split(":");
				virtualNombre=vNombre2[0];
				if (vNombre[1].contains("#")){
					String tipoR[]=vNombre[0].split("#");
					nombre=tipoR[0];
					type=tipoR[1];
				}else{
					nombre=vNombre[1];
					type="P";
				}
			} else {
				System.out.println("CASO3");
				if (vNombre[0].contains("#")){
					String tipoR[]=vNombre[0].split("#");
					nombre=tipoR[0];
					virtualNombre=tipoR[0];
					type=tipoR[1];
				}else{
					nombre=vNombre[0];
					virtualNombre=vNombre[0];
					type="P";
				}
			}
			if (context==null) throw new Exception("Context No inicializada");
			Object bean=context.getBean(vNombre[1]);
			if (bean instanceof GenericProcessMapperInfoProcessResolver){//TODO javaResolver bean
				resolver = (GenericProcessMapperInfoProcessResolver) bean;
			} else if(bean instanceof GenericProcessJavaResolver){
				javaResolver =(GenericProcessJavaResolver) bean;
			}else throw new Exception("Error. Clase no implementa interfaz Resolver. setInfo( " + info + " )");
			
			
		} else {
			System.out.println("CASO4");
			nombre=head;
			virtualNombre=head;
		}
		
		//Convierto a mayusculas: nombre de la tabla, nombre de las keys, nombre de las columnas
		//TODO PONER TABLE A MAYUSCULAS?
		//this.table = this.table.toUpperCase();
		
		if (tokens.length>3) this.setKeySeparator(tokens[3]);

		//TODO PONER KEYS A MAYUSCULAS?
		String[] keys = tokens[2].split(",");
		for (int i = 0; i < keys.length; i++)
			this.keys.add(keys[i]);

		String[] fields = tokens[1].split(",");
		for (int i = 0; i < fields.length; i++) {
			// campo[:descripcion][#[T|F|N|S][#SIZE|#secuence]]
			String[] t = fields[i].split("#");

			// mirar si hay descripcion
			String[] fd = t[0].split(":");
			String field = t[0];
			String descripcion = "";
			if (fd.length >= 2) {
				field = fd[0];
				descripcion = fd[1];
			}
			//determinar si este campo debe ser fullText o no (si el nombre del campo acaba en '-' No, si acaba en '+' si, si no pone nada->si
			boolean fullText = field.endsWith("-")?false:true; 

			String type = "";
			String size = "";
			String secuence = "";
			if (t.length > 1) {
				type = t[1];
				if (!(type.equals("T") || type.equals("N") || type.equals("F") || type.equals("S") || type.equals("B")))
					throw new Exception("Error en la sintaxis GenericProcessMapperInfo.setInfo( " + info + " ), TIPOS VALIDOS: T,N,F,S,B");
				if (t.length>2) {
					if (type.equals("T")) size = t[2];
					else if (type.equals("S")) secuence = t[2];
				}
			}
			//TODO PONER FIELD A MAYSUCULAS?
			GenericProcessMapperInfoParam param = new GenericProcessMapperInfoParam(field, type, size, fullText, secuence, descripcion);
			this.fields.add(param);
		}
		System.out.println("## GenericProcessMapperInfoProcess RES:" + this.toString());
	}
	
	public void setSecurity(String secInfo) throws Exception {
		System.out.println("## GenericProcessMapperInfoProcess: SECURITY: " + secInfo);
		String[] tokens = secInfo.split("\\|");
		if (tokens.length < 2) throw new Exception("Error en la sintaxis GenericProcessMapperInfo.setSecurity( " + secInfo + " )");

		//establezco los parametros de la tabla: nombre y separador de keys y resolvedorPordefecto
		String head=tokens[0];
		if (head.contains("@")){
			String[] vNombre=head.split("@");
			System.out.println("CASO3");
			nombre=vNombre[0];
			virtualNombre=vNombre[0];
			if (context==null) throw new Exception("Context No inicializada");
			Object bean=context.getBean(vNombre[1]);
			if (bean instanceof GenericProcessMapperSecurityProcessResolver){
				secResolver = (GenericProcessMapperSecurityProcessResolver) bean;
			} else throw new Exception("Error. Clase no implementa interfaz GenericProcessMapperSecurityProcessResolver. setSecurity( " + secInfo + " )");
		} else {
			System.out.println("CASO4");
			nombre=head;
			virtualNombre=head;
		}
		
		//troceo los actions por ,
		System.out.println("Actions="+tokens[1]);
		String actions[] = tokens[1].split(",");
		for(String action:actions){
			//separo key=valor
			System.out.println("Action="+action);
			int i1= action.indexOf("=");
			if (i1==-1) throw new Exception("Error. Falta = en definicion security:"+action+" -> "+tokens[1]+" -> "+secInfo);
			String key=action.substring(0,i1);
			String valor=action.substring(i1+1);
			
			System.out.println("key="+key+" valor="+valor);
			//tomo la lista de procesos asociada a la accion o la creo nueva
			List<String> lproc = actionRoles.get(key);
			if (lproc==null) {
				lproc = new ArrayList<String>();
				actionRoles.put(key, lproc);
			}
			
			//separo valor en sus correspondientes tokens :
			String procesos[] = valor.split(":");
			for(String proceso:procesos){
				System.out.println("proc="+proceso);
				if (!lproc.contains(proceso)) lproc.add(proceso);
			}
		}
		System.out.println("------");

	}

	//se encarga de tomar los requisitos de seguridad de una tabla
	public void changeSecurity(GenericProcessMapperInfoProcess tableData) {
		setActionRoles(tableData.getActionRoles());
		setSecResolver(tableData.getSecResolver());
	}
	
	
	public String toString() {
		String res = nombre + "[";
		for (String key : keys) {
			res += key + ",";
		}
		res += keySeparator+"]";
		for (GenericProcessMapperInfoParam field : fields)
			res += field.toString() + ",";
		res+="\n---SEC:\n";
		if (actionRoles!=null){
			for (String key:actionRoles.keySet()){
				List<String> lproc = actionRoles.get(key);
				res+="KEY="+key+" ROLES=";
				for(String proc:lproc) res+=proc+",";
				res+="\n";
			}
		}
		return res;
	}

	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public List<GenericProcessMapperInfoParam> getFields() {
		return fields;
	}
	public void setFields(List<GenericProcessMapperInfoParam> fields) {
		this.fields = fields;
	}
	public List<String> getKeys() {
		return keys;
	}
	public void setKeys(List<String> keys) {
		this.keys = keys;
	}
	public String getKeySeparator() {
		return keySeparator;
	}
	public void setKeySeparator(String keySeparator) {
		this.keySeparator = keySeparator;
	}
	public String getVirtualNombre() {
		return virtualNombre;
	}
	public void setVirtualNombre(String virtualNombre) {
		this.virtualNombre = virtualNombre;
	}
	public GenericProcessMapperInfoProcessResolver getResolver() {
		return resolver;
	}
	public void setResolver(GenericProcessMapperInfoProcessResolver resolver) {
		this.resolver = resolver;
	}
	public ApplicationContext getContext() {
		return context;
	}
	public void setContext(ApplicationContext context) {
		this.context = context;
	}
	public Map<String, List<String>> getActionRoles() {
		return actionRoles;
	}
	public void setActionRoles(Map<String, List<String>> actionRoles) {
		this.actionRoles = actionRoles;
	}
	public GenericProcessMapperSecurityProcessResolver getSecResolver() {
		return secResolver;
	}
	public void setSecResolver(GenericProcessMapperSecurityProcessResolver secResolver) {
		this.secResolver = secResolver;
	}
	public String getType(){
		return this.type;
	}
	public GenericProcessJavaResolver getJavaResolver(){
		return this.javaResolver;
	}
	public void setJavaResolver(GenericProcessJavaResolver javaResolver){
		this.javaResolver=javaResolver;
	}
}
