package es.maltimor.genericUser;

import es.maltimor.genericUser.User;

public interface UserDao {
	public User initUser(String login, String app) throws Exception;
	public User getUser(String login, String app) throws Exception;
	public String getLogin() throws Exception;
}
