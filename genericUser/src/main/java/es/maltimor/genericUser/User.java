package es.maltimor.genericUser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
 * Informacion del usuario, todas las keys estan en minisculas.
 * Los getters pasan a minusculas los keys
 */
public class User {
	private String login;
	private List<String> roles;
	private Map<String, String> grupos;
	private Map<String,Object> attr;
	
	public User(){
		this.login="";
		this.attr = new HashMap<String,Object>();
		this.roles = new ArrayList<String>();
		this.grupos = new HashMap<String,String>();
	}
	
	public String toString(){
		String res = "Usuario="+login+" Roles=[";
		for(String rol:roles) res+=rol+",";
		res+="] Grupos=[";
		for(String key:grupos.keySet()) res+=key+"="+grupos.get(key)+",";
		res+="] Atributos=[";
		for(String key:attr.keySet()) res+=key+"="+attr.get(key)+",";
		res+="]";
		return res;
	}
	
	public boolean hasRol(String rol){
		return this.roles.contains(rol);
	}
	public boolean hasGroup(String group){
		return this.grupos.containsKey(group);
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public Map<String, Object> getAttr() {
		return attr;
	}

	public List<String> getRoles() {
		return roles;
	}

	public Map<String, String> getGrupos() {
		return grupos;
	}

	public void setRoles(List<String> roles) {
		this.roles.clear();
		for(String rol:roles) addRol(rol);
	}
	public void addRol(String rol){
		this.roles.add(rol);
	}

	public void setAttr(Map<String, Object> attr) {
		this.attr.clear();
		for(String key:attr.keySet()){
			addAttr(key, attr.get(key));
		}
	}
	public void addAttr(String key,Object value){
		this.attr.put(key, value);
	}

	public Object getAttr(String key){
		return this.attr.get(key);
	}
	
	public void setGrupos(Map<String, String> grupos) {
		this.grupos.clear();
		for(String key:grupos.keySet()){
			addGrupo(key, grupos.get(key));
		}
	}
	public void addGrupo(String key,String value){
		this.grupos.put(key, value);
	}
	public String getGrupo(String key){
		return this.grupos.get(key);
	}
}
