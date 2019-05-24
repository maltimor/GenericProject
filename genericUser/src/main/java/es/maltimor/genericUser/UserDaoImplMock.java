package es.maltimor.genericUser;

import java.util.HashMap;
import java.util.Map;

public class UserDaoImplMock implements UserDao, UserPreferencesDao {
	private String defaultUser;
	private String separator;
	private String defaultRoles;
	private Map<String,String> grupos;
	private Map<String,Object> attr;
	private Map<String,Object> preferences;
	private Map<String,String> preferencesMapping;

	public UserDaoImplMock(){
		this.preferences = new HashMap<String, Object>();
		this.preferencesMapping = new HashMap<String,String>();
	}
	
	public User initUser(String login, String app) throws Exception {
		//System.out.println("Init user:"+login+" "+app+" := "+preferences.get(login));
		//no cacheao y elimino la posible cache
		User user = new User();
		user.setLogin(login);
		String[] roles=defaultRoles.split(separator);
		for (String rol:roles) user.addRol(rol);
		//para tener un caso completo añado esto
		if (attr!=null) user.setAttr(attr);
		if (grupos!=null) user.setGrupos(grupos);
		
		//aqui se hace un merge selectivo de las preferences sin tener el cuenta la seguridad que de ello implica
		//en preferencesMapping tengo un array: keyOrigen(en preferences), keyDestino(en attr)
		System.out.println("** pm="+preferencesMapping);
		System.out.println("** p="+preferences);
		Map<String,Object> userAttr = user.getAttr();
		Map<String, Object> userPreferences = (Map<String, Object>) preferences.get(login);
		if (userPreferences==null) userPreferences = new HashMap<String,Object>();
		for(String key:preferencesMapping.keySet()){
			//System.out.println("** KEY="+key+" pm="+preferencesMapping.get(key)+" pv="+userPreferences.get(key));
			userAttr.put(preferencesMapping.get(key), userPreferences.get(key));
		}
		
		return user;
	}

	public User getUser(String login, String app) throws Exception {
		return initUser(login,app);
	}

	public String getLogin() throws Exception {
		return defaultUser;
	}
	
	public Map<String, Object> getPreferences(String login, String app) throws Exception {
		//System.out.println("GetPreferences:"+login+" "+app+" := "+preferences.get(login));
		Map<String,Object> userPreferences = (Map<String, Object>) preferences.get(login);
		if (userPreferences==null) userPreferences = new HashMap<String,Object>();
		return (Map<String, Object>) userPreferences;
	}

	public void setPreferences(String login, String app, Map<String, Object> data) throws Exception {
		//System.out.println("SetPreferences:"+login+" "+app+" : "+data);
		this.preferences.put(login, data);
	}

	public String getDefaultUser() {
		return defaultUser;
	}
	public void setDefaultUser(String defaultUser) {
		this.defaultUser = defaultUser;
	}
	public String getSeparator() {
		return separator;
	}
	public void setSeparator(String separator) {
		this.separator = separator;
	}
	public String getDefaultRoles() {
		return defaultRoles;
	}
	public void setDefaultRoles(String defaultRoles) {
		this.defaultRoles = defaultRoles;
	}
	public Map<String, String> getGrupos() {
		return grupos;
	}
	public void setGrupos(Map<String, String> grupos) {
		this.grupos = grupos;
	}
	public Map<String, Object> getAttr() {
		return attr;
	}
	public void setAttr(Map<String, Object> attr) {
		this.attr = attr;
	}
	public Map<String, String> getPreferencesMapping() {
		return preferencesMapping;
	}
	public void setPreferencesMapping(Map<String, String> preferencesMapping) {
		this.preferencesMapping = preferencesMapping;
	}
	public Map<String, Object> getPreferences() {
		return preferences;
	}
	public void setPreferences(Map<String, Object> preferences) {
		this.preferences = preferences;
	}
}
