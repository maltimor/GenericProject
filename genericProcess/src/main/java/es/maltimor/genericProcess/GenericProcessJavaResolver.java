package es.maltimor.genericProcess;

import java.util.Map;

import es.maltimor.genericUser.User;

public interface GenericProcessJavaResolver {
	public Map<String, Object> doJavaProcess(User user,String table,GenericProcessMapperInfo info, Object id, Map<String, Object> data, GenericProcessServiceMapper mapper,Map<String,Object> params);
	public Object doJavaProcess(User user, String table, GenericProcessMapperInfoProcess info, String id, Map<String, Object> data);
}
//User user, String table, Object id, Map<String, Object> data
//mapper,