package es.maltimor.webUtils;

import java.sql.Clob;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;


/**
 * Libreria que pasa los datos que vienen en un map como String al tipo de dato elegido
 */
public class MapperUtils {
	public static final int MODO_NOP = 0;
	public static final int MODO_DEFAULT_VALUE = 1;
	public static final int MODO_EXCEPTION = 2;
	public static final String[] formats= {
			"yyyy-MM-dd'T'HH:mm:ss",
			"yyyy/MM/dd'T'HH:mm:ss",
			"dd-MM-yyyy'T'HH:mm:ss",
			"dd/MM/yyyy'T'HH:mm:ss",
			
			"yyyy-MM-dd'T'HH:mm",
			"yyyy/MM/dd'T'HH:mm",
			"dd-MM-yyyy'T'HH:mm",
			"dd/MM/yyyy'T'HH:mm",
			
			"yyyy-MM-dd HH:mm:ss",
			"yyyy/MM/dd HH:mm:ss",
			"dd-MM-yyyy HH:mm:ss",
			"dd/MM/yyyy HH:mm:ss",
			
			"yyyy-MM-dd HH:mm",
			"yyyy/MM/dd HH:mm",
			"dd-MM-yyyy HH:mm",
			"dd/MM/yyyy HH:mm",
			
			"yyyy-MM-dd",
			"yyyy/MM/dd",
			"dd-MM-yyyy",
			"dd/MM/yyyy"
			};
	public static final String[] matches= {
			"\\d{4}-\\d{1,2}-\\d{1,2}T\\d{1,2}:\\d{1,2}:\\d{1,2}",
			"\\d{4}/\\d{1,2}/\\d{1,2}T\\d{1,2}:\\d{1,2}:\\d{1,2}",
			"\\d{1,2}-\\d{1,2}-\\d{4}T\\d{1,2}:\\d{1,2}:\\d{1,2}",
			"\\d{1,2}/\\d{1,2}/\\d{4}T\\d{1,2}:\\d{1,2}:\\d{1,2}",
			
			"\\d{4}-\\d{1,2}-\\d{1,2}T\\d{1,2}:\\d{1,2}",
			"\\d{4}/\\d{1,2}/\\d{1,2}T\\d{1,2}:\\d{1,2}",
			"\\d{1,2}-\\d{1,2}-\\d{4}T\\d{1,2}:\\d{1,2}",
			"\\d{1,2}/\\d{1,2}/\\d{4}T\\d{1,2}:\\d{1,2}",
			
			"\\d{4}-\\d{1,2}-\\d{1,2} \\d{1,2}:\\d{1,2}:\\d{1,2}",
			"\\d{4}/\\d{1,2}/\\d{1,2} \\d{1,2}:\\d{1,2}:\\d{1,2}",
			"\\d{1,2}-\\d{1,2}-\\d{4} \\d{1,2}:\\d{1,2}:\\d{1,2}",
			"\\d{1,2}/\\d{1,2}/\\d{4} \\d{1,2}:\\d{1,2}:\\d{1,2}",
			
			"\\d{4}-\\d{1,2}-\\d{1,2} \\d{1,2}:\\d{1,2}",
			"\\d{4}/\\d{1,2}/\\d{1,2} \\d{1,2}:\\d{1,2}",
			"\\d{1,2}-\\d{1,2}-\\d{4} \\d{1,2}:\\d{1,2}",
			"\\d{1,2}/\\d{1,2}/\\d{4} \\d{1,2}:\\d{1,2}",
			
			"\\d{4}-\\d{1,2}-\\d{1,2}",
			"\\d{4}/\\d{1,2}/\\d{1,2}",
			"\\d{1,2}-\\d{1,2}-\\d{4}",
			"\\d{1,2}/\\d{1,2}/\\d{4}"
			};
	
	public static Map<String,Object> toLong(Map<String,Object> filter, String field) throws Exception{
		return toLong(filter,field,null,MODO_NOP);
	}
	
	public static Map<String,Object> toLong(Map<String,Object> filter, String field,Object dv,int modo) throws Exception {
		if (filter.containsKey(field)){
			Object data = filter.get(field);
			String val = data!=null?data.toString():"null";
			//GESTION DE NULLS
			if (val.equals("NULL")||val.equals("null")||val.equals("")) {
				filter.put(field, null);
				return filter;
			}
			long nl= Long.parseLong(val);
			filter.put(field, nl);
			System.out.println("TO_LONG:("+field+"):"+data.toString()+"->"+nl);
		} else {
			modo(filter, field, dv, modo);
		}
		return filter;
		
	}
	
	public static Map<String,Object> toInt(Map<String,Object> filter, String field) throws Exception{
		return toInt(filter,field,null,MODO_NOP);
	}
	
	public static Map<String,Object> toInt(Map<String,Object> filter, String field,Object dv,int modo) throws Exception {
		if (filter.containsKey(field)){
			Object data = filter.get(field);
			String val = data!=null?data.toString():"null";
			//GESTION DE NULLS
			if (val.equals("NULL")||val.equals("null")||val.equals("")) {
				filter.put(field, null);
				return filter;
			}
			int nl= Integer.parseInt(val);
			filter.put(field, nl);
			System.out.println("TO_INT:("+field+"):"+data.toString()+"->"+nl);
		} else {
			modo(filter, field, dv, modo);
		}
		return filter;
		
	}
	
	public static Map<String,Object> toFloat(Map<String,Object> filter, String field) throws Exception{
		return toFloat(filter,field,null,MODO_NOP);
	}
	
	public static Map<String,Object> toFloat(Map<String,Object> filter, String field,Object dv,int modo) throws Exception {
		if (filter.containsKey(field)){
			Object data = filter.get(field);
			String val = data!=null?data.toString():"null";
			//GESTION DE NULLS
			if (val.equals("NULL")||val.equals("null")||val.equals("")) {
				filter.put(field, null);
				return filter;
			}
			float nl= Float.parseFloat(val);
			filter.put(field, nl);
			System.out.println("TO_FLOAT:("+field+"):"+data.toString()+"->"+nl);
		} else {
			modo(filter, field, dv, modo);
		}
		return filter;
		
	}
	
	public static Map<String,Object> toDouble(Map<String,Object> filter, String field) throws Exception{
		return toDouble(filter,field,null,MODO_NOP);
	}
	
	public static Map<String,Object> toDouble(Map<String,Object> filter, String field,Object dv,int modo) throws Exception {
		if (filter.containsKey(field)){
			Object data = filter.get(field);
			String val = data!=null?data.toString():"null";
			//GESTION DE NULLS
			if (val.equals("NULL")||val.equals("null")||val.equals("")) {
				filter.put(field, null);
				return filter;
			}
			Double nl= Double.parseDouble(val);
			filter.put(field, nl);
			System.out.println("TO_DOUBLE:("+field+"):"+data.toString()+"->"+nl);
		} else {
			modo(filter, field, dv, modo);
		}
		return filter;
		
	}
	
	public static Map<String,Object> toDate(Map<String,Object> filter, String field) throws Exception{
		return toDate(filter,field,null,MODO_NOP);
	}
	
	public static Map<String,Object> toDate(Map<String,Object> filter, String field,Object dv,int modo) throws Exception {
		System.out.println("### MAPPER UTILS: field="+field);

		if (filter.containsKey(field)){
			Object data = filter.get(field);
			System.out.println("### MAPPER UTILS: data="+data);

			if (data instanceof java.lang.String) {
				String fechaRecibida = (String) data;
				System.out.println("TO_DATE_STRING:("+field+"):"+data.toString());
				//GESTION DE NULLS
				if (fechaRecibida.equals("NULL")||fechaRecibida.equals("null")||fechaRecibida.equals("")) {
					filter.put(field, null);
					return filter;
				}
				
				Date fechaDevuelta;
				int i;
				for(i=0;i<matches.length;i++) {
					if (fechaRecibida.matches(matches[i])) {
						SimpleDateFormat sdf = new SimpleDateFormat(formats[i]);
						sdf.setLenient(false);
						fechaDevuelta = sdf.parse(fechaRecibida);
						System.out.println(formats[i]+"->"+fechaDevuelta);
						filter.put(field, fechaDevuelta);
						break;
					}
				}
				if (i==matches.length) {
					System.out.println("->DEFAULT!!!");
					switch(modo){
					case MODO_DEFAULT_VALUE:
						filter.put(field, dv);
						break;
					case MODO_EXCEPTION:
						throw new Exception("Formato de fecha no permitido: "+fechaRecibida);
					}
				}
				
				/*
				//POSIBLES FORMATOS:
				SimpleDateFormat formatoFechaYMD = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss"); 
				SimpleDateFormat formatoFechaDMY = new SimpleDateFormat("dd-MM-yyyy'T'HH:mm:ss"); 		
				SimpleDateFormat formatoFechaYMD2 = new SimpleDateFormat("yyyy-MM-dd"); 
				SimpleDateFormat formatoFechaDMY2 = new SimpleDateFormat("dd-MM-yyyy");
				//TODO ver si funciona con fechas con / y con -
		        formatoFechaYMD.setLenient(false); //siempre a falso para que el usuario no meta fechas no validas.
		        formatoFechaDMY.setLenient(false); 
		        formatoFechaYMD2.setLenient(false);
		        formatoFechaDMY2.setLenient(false); 
		        
				try {
					fechaDevuelta = formatoFechaYMD.parse(fechaRecibida);
					filter.put(field, fechaDevuelta);
					System.out.println("YMD->"+fechaDevuelta);
				} catch (Exception e) {
			        try {
			        	fechaDevuelta = formatoFechaDMY.parse(fechaRecibida);
			        	filter.put(field, fechaDevuelta);
						System.out.println("DMY->"+fechaDevuelta);
					} catch (Exception x) {
				        try {
				        	fechaDevuelta = formatoFechaYMD2.parse(fechaRecibida);
				        	filter.put(field, fechaDevuelta);
							System.out.println("YMD2->"+fechaDevuelta);
						} catch (Exception x2) {
							try {
								fechaDevuelta = formatoFechaDMY2.parse(fechaRecibida);
					        	filter.put(field, fechaDevuelta);
								System.out.println("DMY2->" + fechaDevuelta);
							} catch (Exception x3) {
								System.out.println("->DEFAULT!!!");
								switch(modo){
								case MODO_DEFAULT_VALUE:
									filter.put(field, dv);
									break;
								case MODO_EXCEPTION:
									throw new Exception("Formato de fecha no permitido: "+fechaRecibida);
								}
							}
						}
					}
				}*/
			} else if (data instanceof java.lang.Long) {
				//TODO ver si es mejopr crea un objeto date
				Date date = new Date((Long) data);
				//SimpleDateFormat formatoFecha = new SimpleDateFormat("y-M-d");
				//formatoFecha.setLenient(false);
				//filter.put(field, formatoFecha.format(date));
				filter.put(field, date);
				System.out.println("TO_DATE_LONG:("+field+"):"+data.toString()+"->"+date);
			} else {
				//LANZO UN ERROR!
				System.out.println("TO_DATE_DESCONOCIDO:("+field+"):"+data.toString()+" "+data.getClass().getName());
			}
		} else {
			modo(filter, field, dv, modo);
			System.out.println("TO_DATE_DEFAULT!!!:("+field+"):"+filter.get(field));
		}
		return filter;
		
	}
	
	private static void modo(Map<String, Object> filter, String field,
			Object dv, int modo) throws Exception {
		switch(modo){
		case MODO_DEFAULT_VALUE:
			filter.put(field, dv);
			break;
		case MODO_EXCEPTION:
			throw new Exception("No existe el parametro: "+field);
		}
	}
}