package es.maltimor.genericProcess;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.context.ApplicationContext;

import es.maltimor.springUtils.ContextAware;


//NOTA: la indexacion se hace por medio del campo virtualTable
public class GenericProcessMapperInfo {
	private Map<String, GenericProcessMapperInfoProcess> data;
	private String info;
	private String security;
	private ApplicationContext context;

	private GenericProcessMapperInfoProcessResolver defaultResolver;
	private GenericProcessJavaResolver defaultJavaResolver;
	private GenericProcessMapperSecurityProcessResolver defaultSecResolver;

	public GenericProcessMapperInfo() {
		this.context = ContextAware.getContext();
		this.data = new HashMap<String, GenericProcessMapperInfoProcess>();
		this.defaultResolver = new GenericProcessMapperInfoProcessResolverImpl();			//resolver por defecto
		this.defaultSecResolver = new GenericProcessMapperSecurityProcessResolverImpl();	
		this.defaultJavaResolver=new GenericProcessJavaResolverImpl(); //resolver por defecto
	}

	
	public GenericProcessMapperInfoProcessResolver getDefaultResolver() {
		return defaultResolver;
	}
	public void setDefaultResolver(
			GenericProcessMapperInfoProcessResolver defaultResolver) {
		this.defaultResolver = defaultResolver;
	}
	public GenericProcessMapperSecurityProcessResolver getDefaultSecResolver() {
		return defaultSecResolver;
	}
	public void setDefaultSecResolver(
			GenericProcessMapperSecurityProcessResolver defaultSecResolver) {
		this.defaultSecResolver = defaultSecResolver;
	}
	public GenericProcessJavaResolver getJavaResolver(){
		return this.defaultJavaResolver;
	}
	public void setJavaResolver(GenericProcessJavaResolver defaultJavaResolver){
		this.defaultJavaResolver=defaultJavaResolver;
	}

	public String getInfo() {
		return info;
	}

	// este metodo es el que se usara para simplificar la entrada de datos desde
	// spring
	public void setInfo(String info) throws Exception {
		System.out.println("## GenericProcessMapperInfo:" + info);
		this.info = info;
		String[] tables = info.split("\n");
		for (int j = 0; j < tables.length; j++) {
			// ahora lo añado en data
			String t = tables[j].replace("\r", "").trim();
			if (!t.equals("")) {
				//TODO Comprobar que no exista en la tabla previamente, permite ampliar la definicion de seguridad despues
				//TODO Localizar la tabla en funcion de su clave que ahora es la virtualTable
				//TOD Ver si crear o recoger del array de tablas
				GenericProcessMapperInfoProcess tableData = new GenericProcessMapperInfoProcess();
				tableData.setResolver(defaultResolver);
				tableData.setSecResolver(defaultSecResolver);
				tableData.setJavaResolver(defaultJavaResolver);
				tableData.setContext(context);
				tableData.setInfo(t);
				//NOTA: la indexacion se hace por medio del virtualTable
				//Al hacer un put se machaca la tabla que hubiera antes.
				data.put(tableData.getVirtualNombre().toLowerCase(), tableData);
			}
		}
		System.out.println("## GenericProcessMapperInfo RES:" + this.toString());
//		comprobador();
	}
	
	//este metodo se encarga de la definicion de la seguridad
	public void setSecurity(String security) throws Exception{
		System.out.println("## GenericCrudMapperInfo.SetSecurity:" + security);
		this.security = security;
		String[] tables = security.split("\n");
		for (int j = 0; j < tables.length; j++) {
			// ahora lo añado en data
			String t = tables[j].replace("\r", "").trim();
			if (!t.equals("")) {
				GenericProcessMapperInfoProcess tableData = new GenericProcessMapperInfoProcess();
				tableData.setResolver(defaultResolver);
				tableData.setSecResolver(defaultSecResolver);
				tableData.setJavaResolver(defaultJavaResolver);
				tableData.setContext(context);
				tableData.setSecurity(t);
				//NOTA: la indexacion se hace por medio del virtualTable
				//Al hacer un put se machaca la tabla que hubiera antes.
				//antes de insertar la tabla veo si ya estaba creada por medio de setInfo.
				//Si es asi, tomo la informacion de seguridad y la fusiono con la tabla ya existente
				if (data.containsKey(tableData.getVirtualNombre().toLowerCase())){
					GenericProcessMapperInfoProcess table = data.get(tableData.getVirtualNombre().toLowerCase());
					table.changeSecurity(tableData);
				} else data.put(tableData.getVirtualNombre().toLowerCase(), tableData);
			}
		}
		System.out.println("## GenericProcessMapperInfo RES:" + this.toString());
	}
	
	public String toString() {
		String res = data.size() + "[\n";
		for (String key : data.keySet()) {
			res += key + "(" + data.get(key).toString() + ")\n";
		}
		res += "]";
		return res;
	}

	public List<String> getFields(String table) {
		if (data.containsKey(table.toLowerCase())) {
			List<String> fields = new ArrayList<String>();
			List<GenericProcessMapperInfoParam> columns = data.get(table.toLowerCase()).getFields();
			for (GenericProcessMapperInfoParam column : columns) {
				fields.add(column.getName());
			}
			return fields;
		}
		return null;
	}

	public String getKey(String table) {
		// TODO: COGE SOLO LA PRIMERA KEY
		if (data.containsKey(table.toLowerCase()))
			return data.get(table.toLowerCase()).getKeys().get(0);
		return null;
	}

	public GenericProcessMapperInfoProcess getInfoProcess(String table) {
		if (data.containsKey(table.toLowerCase()))
			return data.get(table.toLowerCase());
		return null;
	}

	public List<String> getListProcess() {
		data.keySet();
		ArrayList<String> nomTabla = new ArrayList<String>();

		for (String s : data.keySet()) {
			nomTabla.add(s);
		}

		return nomTabla;
	}
}
