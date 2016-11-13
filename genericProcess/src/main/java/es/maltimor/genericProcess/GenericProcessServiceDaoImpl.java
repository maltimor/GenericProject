package es.maltimor.genericProcess;

import java.util.*;

import es.maltimor.genericUser.User;

public class GenericProcessServiceDaoImpl implements GenericProcessServiceDao {
	private GenericProcessServiceMapper mapper;
	private GenericProcessMapperInfo info;

	public GenericProcessServiceMapper getMapper() {
		return mapper;
	}
	public void setMapper(GenericProcessServiceMapper mapper) {
		this.mapper = mapper;
	}
	public GenericProcessMapperInfo getInfo() {
		return info;
	}
	public void setInfo(GenericProcessMapperInfo info) {
		this.info = info;
	}

	public Object doProcess(User user, String table, Object id, Map<String, Object> data) throws Exception {
		System.out.println("doProcess: user="+user+" table="+table+" id="+id+" data="+data.size()+" info="+info.getInfoProcess(table));
		long startTime = System.currentTimeMillis();
		if (info.getInfoProcess(table)==null) throw new Exception("No existe la tabla "+table);
		//TODO pasar resolver o javaResolver al metodo
		Map<String, Object> params = new HashMap<String, Object>();
		
/*		
 		params.put("user", user);
		params.put("table", table);
		params.put("id", id);
		params.put("info", info.getInfoProcess(table));
		params.put("data", data);
		params.put("out", null);
		
		mapper.doProcess(params);
*/		
		
		if(!info.getInfoProcess(table).getType().equals("J")){//llamada al Resolver de PL SQL
			GenericProcessMapperInfoProcessResolver resolver=info.getInfoProcess(table).getResolver();
			params=resolver.doProcess(user,table,info,id,data,mapper,params);
		 }else{//llamada al Resolver de Java
			 GenericProcessJavaResolver javaResolver=info.getInfoProcess(table).getJavaResolver();
			 params=javaResolver.doJavaProcess(user,table,info,id,data,mapper,params);
		 }
		
		Object out = params.get("out");
		
		System.out.println(out);
		System.out.println("****doProcess: "+(System.currentTimeMillis()-startTime));
		return out;
	}
	
	public boolean isProcess(String table) throws Exception {
		return info.getInfoProcess(table)!=null;
	}

}
