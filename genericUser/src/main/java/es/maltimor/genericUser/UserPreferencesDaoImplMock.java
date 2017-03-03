package es.maltimor.genericUser;

import java.util.HashMap;
import java.util.Map;

public class UserPreferencesDaoImplMock implements UserPreferencesDao {
	private Map<String,Map<String,Object>> prefs;

	public UserPreferencesDaoImplMock() {
		this.prefs = new HashMap<String,Map<String,Object>>();
	}

	public Map<String, Object> getPreferences(String login, String app) throws Exception {
		return prefs.get(login+"|"+app);
	}

	public void setPreferences(String login, String app, Map<String, Object> data) throws Exception {
		prefs.put(login+"|"+app,data);
	}

}
