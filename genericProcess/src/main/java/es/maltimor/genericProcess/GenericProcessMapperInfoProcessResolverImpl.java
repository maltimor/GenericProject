package es.maltimor.genericProcess;

import java.util.*;

import es.maltimor.genericUser.User;

public class GenericProcessMapperInfoProcessResolverImpl implements GenericProcessMapperInfoProcessResolver {

	public Map<String, Object> doProcess(User user, String table, GenericProcessMapperInfo info, Object id, Map<String, Object> data, GenericProcessServiceMapper mapper, Map<String, Object> params) {
		params.put("user", user);
		params.put("table", table);
		params.put("id", (String)id);
		params.put("info", info.getInfoProcess(table));
		params.put("data", data);
		params.put("out", null);
		
		mapper.doProcess(params);
		return params;
	}
	public String doProcess(User user, String table, GenericProcessMapperInfoProcess info, String id, Map<String, Object> data) {
		List<String> keys = info.getKeys();
		String colSets = "";
		boolean firstSet = true;

		for(GenericProcessMapperInfoParam param : info.getFields()){
			String key = param.getName();
			if (data.containsKey(key)){
				if (!firstSet) colSets+=",";
				colSets+="#{data."+key+"}";
				firstSet=false;
			} else {
				if (!firstSet) colSets+=",";
				colSets+="null";
				firstSet=false;
			}
		}
		//#{IDPERSONA},#{FVIGENCIA},#{TIPO_IDENTIFICACION},#{NUM_IDENTIFICACION},#{APELLIDO1},#{APELLIDO2},#{NOMBRE},#{FNACIMIENTO},#{CTELEFONO1},#{NTELEFONO1},#{CTELEFONO2},#{NTELEFONO2},#{EMAIL},#{FBAJA},#{OBSERVACIONES}
		String query = "call "+info.getNombre()+"("+colSets+")";
		System.out.println("MapperInfoProcessRsolver: "+query);
		return query;
	}

}
