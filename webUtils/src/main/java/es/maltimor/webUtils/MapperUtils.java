package es.maltimor.webUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;


/**
 * Librer�a que pasa los datos que vienen en un map como String al tipo de dato elegido
 */
public class MapperUtils {
	public static final int MODO_NOP = 0;
	public static final int MODO_DEFAULT_VALUE = 1;
	public static final int MODO_EXCEPTION = 2;
		
	/*
	 * 
	 */
	public static Map<String,Object> toLong(Map<String,Object> filter, String field) throws Exception{
		return toLong(filter,field,null,MODO_NOP);
	}
	
	public static Map<String,Object> toLong(Map<String,Object> filter, String field,Object dv,int modo) throws Exception {
		if (filter.containsKey(field)){
			Object data = filter.get(field);
			if ((data!=null)&&(!data.toString().equals(""))){
				long nl= Long.parseLong(data.toString());
				filter.put(field, nl);
				System.out.println("TO_LONG:("+field+"):"+data.toString()+"->"+nl);
			}
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
			if ((data!=null)&&(!data.toString().equals(""))){
				int nl= Integer.parseInt(data.toString());
				filter.put(field, nl);
				System.out.println("TO_INT:("+field+"):"+data.toString()+"->"+nl);
			}
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
			if ((data!=null)&&(!data.toString().equals(""))){
				float nl= Float.parseFloat(data.toString());
				filter.put(field, nl);
				System.out.println("TO_FLOAT:("+field+"):"+data.toString()+"->"+nl);
			}
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
			if ((data!=null)&&(!data.toString().equals(""))){
				Double nl= Double.parseDouble(data.toString());
				filter.put(field, nl);
				System.out.println("TO_DOUBLE:("+field+"):"+data.toString()+"->"+nl);
			}
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
				Date fechaDevuelta;
				//POSIBLES FORMATOS:
				SimpleDateFormat formatoFechaYMD = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss"); 
				SimpleDateFormat formatoFechaDMY = new SimpleDateFormat("dd-MM-yyyy'T'HH:mm:ss"); 		
				SimpleDateFormat formatoFechaYMD2 = new SimpleDateFormat("yyyy-MM-dd"); 
				//TODO ver si funciona con fechas con / y con -
		        formatoFechaYMD.setLenient(false); //siempre a falso para que el usuario no meta fechas no validas.
		        formatoFechaDMY.setLenient(false); 
		        
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