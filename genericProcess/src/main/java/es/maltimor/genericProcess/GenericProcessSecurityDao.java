package es.maltimor.genericProcess;

import java.util.*;

import es.maltimor.genericUser.User;

public interface GenericProcessSecurityDao {
	public boolean canDoProcess(User user, String table,Object id,Map<String,Object> data) throws Exception;
}
