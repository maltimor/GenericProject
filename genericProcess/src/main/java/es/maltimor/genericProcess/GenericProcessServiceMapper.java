package es.maltimor.genericProcess;

import java.util.*;

import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.StatementType;

public interface GenericProcessServiceMapper {
	@Select("Select * FROM dual")	
	String getTypeVarChar();
	
	@Select("Select * FROM dual")	
	List<Map<String,Object>> getTypeCursor();
	
	@Select("Select * FROM dual")	
	Date getTypeDate();
	
	@Select("Select * FROM dual")	
	List<Map<String,Object>> getTypeNumberic();
	
	@Select("Select * FROM dual")	
	List<Map<String,Object>> getTypeGeneric();
	
	@Select("Select * FROM dual")	
	List<Map<String,Object>> getTypeBLOB();
	
	//appcontext.xml
	//VPROC:PROC#TipoReturn@RESOLVER|P1#Tipo,....|
	
	//TipoReturn: por defecto C
	//C -> #{out, jdbcType=CURSOR, mode=OUT, resultMap=getTypeCursor-void}
	//T -> #{out, jdbcType=VARCHAR2, mode=OUT}
	//F -> #{out, jdbcType=DATE, mode=OUT}
	//N -> #{out, jdbcType=NUMBER, mode=OUT}

	
	@SelectProvider(type=es.maltimor.genericProcess.GenericProcessServiceMapperProvider.class, method="doProcess")
	@Options(statementType = StatementType.CALLABLE)
//	public Object doProcess(@Param("user") User user,@Param("nombre") String nombre,@Param("info") GenericProcessMapperInfoProcess info,@Param("id") Object id,@Param("data") Map<String, Object> data,@Param("out") Object out);
	public Object doProcess(Map<String, Object> params);
}
