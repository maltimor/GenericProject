package es.maltimor.genericProcess;

//import org.apache.ibatis.annotations.Select;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.maltimor.genericUser.User;
import es.maltimor.webUtils.MapperUtils;


@SuppressWarnings("rawtypes")
public class GenericProcessServiceMapperProvider {
	final Logger log = LoggerFactory.getLogger(GenericProcessServiceMapperProvider.class);
	
	//user,table,data,info,id
	@SuppressWarnings("unchecked")
	public String doProcess(Map params) throws Exception{
		User user = (User) params.get("user");
		String nombre = (String) params.get("nombre");	
		String id = (String) params.get("id"); 

		GenericProcessMapperInfoProcess info = (GenericProcessMapperInfoProcess) params.get("info");
		GenericProcessMapperInfoProcessResolver resolver = info.getResolver();
		Map<String, Object> map = (Map<String, Object>) params.get("data");
		
		if(info.getType().equals("J")){//GenericProcessJavaResolver
			return doJavaProcess(params);
			//TODO exception
		}else{//GenericProcessResolver (llamada a PL SQL)
		
		for (GenericProcessMapperInfoParam param : info.getFields()) {
			trataTipo(info, map, param.getName());
		}
		String rM=null;
		String jT=null;
		String out=null;
		if(info.getType().equals("C")){
			jT="CURSOR";
			rM="getTypeCursor-void";
			out ="#{out, jdbcType="+jT+", mode=OUT, resultMap="+rM+"} = ";
		}else if(info.getType().equals("T")){
			jT="VARCHAR";
			rM="getTypeVarChar-void";
			out ="#{out, jdbcType="+jT+", mode=OUT, resultMap="+rM+"} = ";
		}else if(info.getType().equals("F")){
			jT="DATE";
			rM="getTypeDate-void";
			out ="#{out, jdbcType="+jT+", mode=OUT, resultMap="+rM+"} = ";
		}else if(info.getType().equals("N")){
			jT="NUMERIC";
			rM="getTypeNumberic-void";
			out ="#{out, jdbcType="+jT+", mode=OUT, resultMap="+rM+"} = ";
		}else if(info.getType().equals("B")){
			jT="BLOB";
			rM="getTypeBLOB-void";
			out ="#{out, jdbcType="+jT+", mode=OUT, resultMap="+rM+"} = ";
		}else if(info.getType().equals("P")){
			out="";
		}else {
			//TODO
			throw new Exception();
		}
		//out ="#{out, jdbcType="+jT+", mode=OUT, resultMap="+rM+"}";
		
		String query= "{"+out+resolver.doProcess(user,nombre,info,id,map)+"}";
		log.debug("do Process:"+query);
		System.out.println("MapperProvider: "+query);
		
		return query;
		}
	}
//	"{#{out, jdbcType=CURSOR, mode=OUT, resultMap=typePersonas-void} = call "+info.getNombre()+"(#{IDPERSONA},#{FVIGENCIA},#{TIPO_IDENTIFICACION},#{NUM_IDENTIFICACION},#{APELLIDO1},#{APELLIDO2},#{NOMBRE},#{FNACIMIENTO},#{CTELEFONO1},#{NTELEFONO1},#{CTELEFONO2},#{NTELEFONO2},#{EMAIL},#{FBAJA},#{OBSERVACIONES})}"
	public String doJavaProcess(Map params){
		User user = (User) params.get("user");
		String nombre = (String) params.get("nombre");	
		String id = (String) params.get("id"); 

		GenericProcessMapperInfoProcess info = (GenericProcessMapperInfoProcess) params.get("info");
		Map<String, Object> map = (Map<String, Object>) params.get("data");
		
		GenericProcessJavaResolver javaResolver = info.getJavaResolver();
		log.debug("do JavaProcess:");
		//TODO
		javaResolver.doJavaProcess(user, nombre, info, id, map);
		
		return null;
	}
	
	private void trataTipo(GenericProcessMapperInfoProcess table, Map<String, Object> map, String key) {
		trataTipo(table,map,key,key);
	}
	
	private void trataTipo(GenericProcessMapperInfoProcess table, Map<String, Object> map, String keyIn, String keyOut) {
		Map<String,String> types = new HashMap<String, String>();
		for (GenericProcessMapperInfoParam column : table.getFields()) {
			types.put(column.getName().toLowerCase(), column.getType());
		}
		 if (types.get(keyIn.toLowerCase()).equals("F")) {
			try {
				MapperUtils.toDate(map, keyOut);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else if (types.get(keyIn.toLowerCase()).equals("N")){
			try {
				MapperUtils.toDouble(map, keyOut);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else if (types.get(keyIn.toLowerCase()).equals("T")) {
			//TODO : dara error si supera el tamaño establecido
			try {
				//TODO : convertir a un varchar de tamaño dado 
			} catch (Exception e) {
				e.printStackTrace();
			}
		}else if (types.get(keyIn.toLowerCase()).equals("C")) {
			//TODO : dara error si supera el tamaño establecido
			try {
				//TODO : convertir a un cursor
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
