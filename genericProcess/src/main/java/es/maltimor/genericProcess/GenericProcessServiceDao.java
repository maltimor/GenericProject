package es.maltimor.genericProcess;

import java.util.*;

import es.maltimor.genericUser.User;

public interface GenericProcessServiceDao {
	public Object doProcess(User user, String table,Object id,Map<String,Object> data) throws Exception;
	public boolean isProcess(String table) throws Exception;
}
