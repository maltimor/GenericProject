package es.maltimor.genericProcess;

import java.util.Map;

import es.maltimor.genericUser.User;

public class GenericProcessJavaResolverImpl implements GenericProcessJavaResolver{

	public Map<String, Object> doJavaProcess(User user, String table, GenericProcessMapperInfo info, Object id, Map<String, Object> data, GenericProcessServiceMapper mapper,Map<String,Object> params) {
		params.put("user", user);
		params.put("table", table);
		params.put("id", (String)id);
		params.put("info", info.getInfoProcess(table));
		params.put("data", data);
		params.put("out", null);
		
		mapper.doProcess(params);
		return params;
	}
	public Object doJavaProcess(User user, String table, GenericProcessMapperInfoProcess info, String id, Map<String, Object> data){
		//TODO
		/* cuando lanzo desde jenkins un proceso de recopilacion de datos esto provoca la inserción en la BBDD de nuevos registros.
		 * hay que crear un proceso en java que, dad una fecha o rango de fechas, recopile esa información.
		 * ese proceso debe invocarse desde una pantalla del front de cmdb: de momento tendra uno o dos campos de fecha y un boton que invocara el proceso.
		 */
		
		/*
		 * 
		 */
		
		return null;
	}
}
