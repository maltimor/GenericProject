package es.maltimor.genericUser;

import java.util.Map;

public interface UserPreferencesDao {
	public Map<String,Object> getPreferences(String login, String app) throws Exception;
	public void setPreferences(String login, String app,Map<String,Object> data) throws Exception;
}
