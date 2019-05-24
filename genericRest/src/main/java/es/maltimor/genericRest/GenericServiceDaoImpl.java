package es.maltimor.genericRest;

import java.sql.Blob;
import java.sql.Clob;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.ws.rs.core.UriInfo;

import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import es.maltimor.genericUser.User;

public class GenericServiceDaoImpl implements GenericServiceDao {
	private GenericServiceMapper mapper;
	private GenericMapperInfo info;
	
	public GenericMapperInfo getInfo() {
		return info;
	}
	public void setInfo(GenericMapperInfo info) {
		this.info = info;
	}
	public GenericServiceMapper getMapper() {
		return mapper;
	}
	public void setMapper(GenericServiceMapper mapper) {
		this.mapper = mapper;
	}

	
	//TODO Incluir el login/usuario en cada llamada al servicio
	
	private Map<String,String> getQueryMap(UriInfo ui){
		//System.out.println("*********************");
		Map<String,String> query = new HashMap<String,String>();
		if (ui!=null){
			for(Entry<String, List<String>> e:ui.getQueryParameters().entrySet()){
				//System.out.print(e.getKey()+"=");
				for(String s:e.getValue()) {
					//System.out.print(s+",");
					query.put(e.getKey(),s);
				}
				//System.out.println();
			}
		}
		//System.out.println("*********************");
		return query;
	}
	
	private String getMessage(Exception e){
		String cad=e.getMessage();
		Throwable t = e;
		while (t.getCause()!=null){
			t=t.getCause();
			cad=t.getMessage()+" <br/>\n "+cad;
		}
		return cad;
	}
	
	//sustituir los info ppor info.getinfotable
	public long cntAll(User user, String table, String filter,UriInfo ui) throws Exception {
		GenericMapperInfoTable infoTable = info.getInfoTable(table);
		//System.out.println("cntALL: user="+user+" table="+table+" filter="+filter+" info="+infoTable);
		long startTime = System.currentTimeMillis();
		if (infoTable==null) throw new Exception("No existe la tabla "+table);
		try{
			Map<String,Object> params = new HashMap<String,Object>();
			params.put("user", user);
			params.put("table", infoTable.getTable());
			params.put("info", infoTable);
			params.put("filter", filter);
			params.put("ui", ui);
			params.put("query", getQueryMap(ui));
			long res = mapper.cntAll(params);//user,infoTable.getTable(),infoTable,filter);
			//System.out.println("****cntALL: "+(System.currentTimeMillis()-startTime));
			return res;
		} catch (Exception e){
			throw new Exception(getMessage(e));
		}
	}

	public List<Map<String, Object>> getAll(User user, String table, String filter, long limit, long offset, String orderby, String order,String fields,UriInfo ui) throws Exception {
		GenericMapperInfoTable infoTable = info.getInfoTable(table);
		//System.out.println("getALL: user="+user+" table="+table+" filter="+filter+" limit="+limit+" offset="+offset+" orderBy="+orderby+" order="+order+" fields="+fields+" info="+infoTable);
		long startTime = System.currentTimeMillis();
		if (infoTable==null) throw new Exception("No existe la tabla "+table);
		try{
			Map<String,Object> params = new HashMap<String,Object>();
			params.put("user", user);
			params.put("table", infoTable.getTable());
			params.put("info", infoTable);
			params.put("filter", filter);
			params.put("limit", limit);
			params.put("offset", offset);
			params.put("order", order);
			params.put("orderby", orderby);
			params.put("fields", fields);
			params.put("ui", ui);
			params.put("query", getQueryMap(ui));
			List<Map<String, Object>> res = mapper.getAll(params);//user,infoTable.getTable(),infoTable,filter,limit,offset,orderby,order,fields);
			
			//TODO tratar los tipos de datos de salida! sobre todo los blob y clob convertirlos a algo
			if (res!=null){
				List<String> keys = new ArrayList<String>();
				for(GenericMapperInfoColumn col:infoTable.getFields()){
					if (col.getType().equals("B")||col.getType().equals("C")) keys.add(col.getName());
				}
				if (keys.size()>0){
					for(Map<String,Object> map:res){
						for(String key:keys) getLOB(map,key);
					}
				}
			}
			
			//System.out.println("****getALL: "+(System.currentTimeMillis()-startTime));
			return res;
		} catch (Exception e){
			throw new Exception(getMessage(e));
		}
	}

	public Map<String, Object> getById(User user, String table, Object id,UriInfo ui) throws Exception {
		GenericMapperInfoTable infoTable = info.getInfoTable(table);
		//System.out.println("getById: user="+user+" table="+table+" id="+id+" info="+infoTable);
		long startTime = System.currentTimeMillis();
		if (infoTable==null) throw new Exception("No existe la tabla "+table);
		try{
			Map<String,Object> params = new HashMap<String,Object>();
			params.put("user", user);
			params.put("table", infoTable.getTable());
			params.put("id", id);
			params.put("info", infoTable);
			params.put("ui", ui);
			params.put("query", getQueryMap(ui));
	
			Map<String, Object> res = mapper.getById(params);//user,infoTable.getTable(),infoTable,id);
			//TODO tratar los tipos de datos de salida! sobre todo los blob y clob convertirlos a algo
			if (res!=null){
				for(GenericMapperInfoColumn col:infoTable.getFields()){
					if (col.getType().equals("B")||col.getType().equals("C")) getLOB(res,col.getName());
				}
			}
			//System.out.println("****getById: "+(System.currentTimeMillis()-startTime));
			return res;
		} catch (Exception e){
			throw new Exception(getMessage(e));
		}
	}
	
	private Object doLOB(Object value) throws Exception{
		if (value!=null) {
			if (value instanceof Blob){
				Blob blob = (Blob) value;
				byte[] buff= blob.getBytes(1,(int) blob.length());
				return buff;
			} else if (value instanceof Clob){
				Clob clob = (Clob) value;
				String buff= clob.getSubString(1,(int) clob.length());
				return buff;
			} else return value;
		} else return null;
	}
	
	private Object doRS(Object value) throws Exception{
		if (value!=null) {
			//System.out.println(value);
			if (value instanceof ResultSet){
				ResultSet rs = (ResultSet) value;
				ResultSetMetaData rsmd = rs.getMetaData();
				int columns = rsmd.getColumnCount();
				//System.out.println("COLS:"+columns);
				List<Map<String,Object>> lmap = new ArrayList<Map<String,Object>>();
				while(rs.next()) {
					Map<String,Object> map = new HashMap<String,Object>();
					for(int i=1;i<=columns;i++){
						map.put(rsmd.getColumnName(i), rs.getObject(i));
					}
					lmap.add(map);
				}
				return lmap;
			} else return value;
		} else return null;
	}

	private void getLOB(Map<String,Object> map,String key) throws Exception{
		Object value = map.get(key);
		map.put(key, doLOB(value));
	}

	private void getRS(Map<String,Object> map,String key) throws Exception{
		//System.out.println("GET RS KEY="+key);
		Object value = map.get(key);
		map.put(key, doRS(value));
	}
	
	@Transactional(
	        propagation = Propagation.REQUIRED,
	        isolation = Isolation.DEFAULT,
	        readOnly = false)
	public boolean insert(User user, String table, Map<String, Object> data,UriInfo ui) throws Exception {
		GenericMapperInfoTable infoTable = info.getInfoTable(table);
		//System.out.println("insert: user="+user+" table="+table+" data="+data.size()+" info="+infoTable);
		long startTime = System.currentTimeMillis();
		if (infoTable==null) throw new Exception("No existe la tabla "+table);
		try{
			Map<String,Object> params = new HashMap<String,Object>();
			params.put("user", user);
			params.put("table", infoTable.getTable());
			params.put("info", infoTable);
			params.put("data", data);
			params.put("ui", ui);
			params.put("query", getQueryMap(ui));
	
			mapper.insert(params);//user,infoTable.getTable(),infoTable,data);
			
			//tratamiento de los campos especiales #S#Secuencia, pues no habia forma de recuperarlos
			for(GenericMapperInfoColumn column : infoTable.getFields()){
				String key = column.getName();
				if (column.getType().equals("S")){
					if (column.getType().equals("S")) {
						Map<String, Object> res = mapper.getSecuenceValue(column.getSecuenceName());		//caso de numero de secuencia
						//System.out.println("RESULTADO:"+res);
						Object value = res.get("VALUE");
						if (value==null || value.equals("")) value = res.get("value");
						if (value!=null && !value.equals("")) data.put(key, value);
					}
				}
			}
			
			//System.out.println("****insert: "+(System.currentTimeMillis()-startTime));
			return true;
		} catch (Exception e){
			throw new Exception(getMessage(e));
		}
	}

	public boolean update(User user, String table, Object id, Map<String, Object> data,UriInfo ui) throws Exception {
		GenericMapperInfoTable infoTable = info.getInfoTable(table);
		//System.out.println("update: user="+user+" table="+table+" id="+id+" data="+data.size()+" info="+infoTable);
		long startTime = System.currentTimeMillis();
		if (infoTable==null) throw new Exception("No existe la tabla "+table);
		try{
			Map<String,Object> params = new HashMap<String,Object>();
			params.put("user", user);
			params.put("table", infoTable.getTable());
			params.put("info", infoTable);
			params.put("id", id);
			params.put("data", data);
			params.put("ui", ui);
			params.put("query", getQueryMap(ui));
	
			mapper.update(params);//user,infoTable.getTable(),infoTable,id,data);
			//System.out.println("****update: "+(System.currentTimeMillis()-startTime));
			return true;
		} catch (Exception e){
			throw new Exception(getMessage(e));
		}
	}

	public boolean delete(User user, String table, Object id,UriInfo ui) throws Exception {
		GenericMapperInfoTable infoTable = info.getInfoTable(table);
		//System.out.println("delete: user="+user+" table="+table+" id="+id+" info="+infoTable);
		long startTime = System.currentTimeMillis();
		if (infoTable==null) throw new Exception("No existe la tabla "+table);
		try{
			Map<String,Object> params = new HashMap<String,Object>();
			params.put("user", user);
			params.put("table", infoTable.getTable());
			params.put("info", infoTable);
			params.put("id", id);
			params.put("ui", ui);
			params.put("query", getQueryMap(ui));
	
			mapper.delete(params);//user,infoTable.getTable(),infoTable,id);
			//System.out.println("****delete: "+(System.currentTimeMillis()-startTime));
			return true;
		} catch (Exception e){
			throw new Exception(getMessage(e));
		}
	}
	
	public Object execute(User user, String table, Map<String, Object> data,UriInfo ui) throws Exception {
		GenericMapperInfoTable infoTable = info.getInfoTable(table);
		//System.out.println("execute: user="+user+" table="+table+" data="+data.size()+" info="+infoTable);
		long startTime = System.currentTimeMillis();
		if (infoTable==null) throw new Exception("No existe la tabla "+table);
		try{
			Map<String,Object> params = new HashMap<String,Object>();
			params.put("user", user);
			params.put("table", infoTable.getTable());
			params.put("info", infoTable);
			params.put("data", data);
			params.put("ui", ui);
			params.put("query", getQueryMap(ui));
			params.put("out", null);
			
			//aqui introduzco los diferentes tipos de ejecuciones
			Object res=null;
			String type=infoTable.getType();
			if (type!=null && type.equals("SQL")){
				res = mapper.executeSQL(params);//user,infoTable.getTable(),infoTable,data);
			} else {
				//codigo original
				mapper.execute(params);//user,infoTable.getTable(),infoTable,data);
				res = params.get("out");
			}
			
			//TODO tratar los tipos de datos de salida! sobre todo los blob y clob convertirlos a algo
			if (res!=null){
				//primero veo los tipos basicos
				if (res instanceof Clob || res instanceof Blob){
					return doLOB(res);
				}
				
				if (res instanceof ResultSet){
					return doRS(res);
				}
				
				//intento hacer un cast a List<Map> si falla no busco LOBS
				try{
					List<Map<String,Object>> lmap = (List<Map<String, Object>>) res;
					//System.out.println(lmap);
					List<String> LOBkeys = new ArrayList<String>();
					List<String> RSkeys = new ArrayList<String>();
					for(GenericMapperInfoColumn col:infoTable.getFields()){
						if (col.getType().equals("B")||col.getType().equals("C")) LOBkeys.add(col.getName());
						if (col.getType().equals("R")) RSkeys.add(col.getName());
					}
					if (LOBkeys.size()>0 || RSkeys.size()>0){
						for(Map<String,Object> map:lmap){
							for(String key:LOBkeys) getLOB(map,key);
							for(String key:RSkeys) getRS(map,key);
						}
					}
					return lmap;
				} catch (Exception e){
					//intento hacer un cast a Map<String,Object>
					try{
						Map<String,Object> map = (Map<String, Object>) res;
						List<String> LOBkeys = new ArrayList<String>();
						List<String> RSkeys = new ArrayList<String>();
						for(GenericMapperInfoColumn col:infoTable.getFields()){
							if (col.getType().equals("B")||col.getType().equals("C")) LOBkeys.add(col.getName());
							if (col.getType().equals("R")) RSkeys.add(col.getName());
						}
						for(String key:LOBkeys) getLOB(map,key);
						for(String key:RSkeys) getRS(map,key);
						return map;
					} catch (Exception e2){} //intencionado
				}
			}
			
			//System.out.println("****insert: "+(System.currentTimeMillis()-startTime));
			return res;
		} catch (Exception e){
			throw new Exception(getMessage(e));
		}
	}
	
	public GenericMapperInfoTable getMapperInfoTable(User user, String table) throws Exception {
		GenericMapperInfoTable infoTable = info.getInfoTable(table);
		//System.out.println("getMapperInfoTable: user="+user+" table="+table+" info="+infoTable);
		long startTime = System.currentTimeMillis();
		if (infoTable==null) throw new Exception("No existe la tabla "+table);
		//System.out.println("****getMapperInfoTable: "+(System.currentTimeMillis()-startTime));
		return infoTable;
	}
	
	public List<String> getMapperInfoList(User user) {
		//System.out.println("getMapperInfoTable: user="+user);
		long startTime = System.currentTimeMillis();
		List<String> res = info.getListTable();
		//System.out.println("****getMapperInfoList: "+(System.currentTimeMillis()-startTime));
		return res;
	}
	public boolean isTable(String table) throws Exception {
		return info.getInfoTable(table)!=null;
	}
}
