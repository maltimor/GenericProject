package es.maltimor.genericProcess;

import java.util.Map;

import es.maltimor.genericUser.User;

public interface GenericProcessMapperSecurityProcessResolver {
	public boolean canDoProcess(User user, String table,GenericProcessMapperInfoProcess info,Object id,Map<String,Object> data);
}
