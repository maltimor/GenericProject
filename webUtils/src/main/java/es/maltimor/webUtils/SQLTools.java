package es.maltimor.webUtils;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

//Clase para montar el numero de parametros que recibe una funcion PL/SQL
public class SQLTools {
	/*
	 * Remedios Gonzalez Hernandez (BT) 23/05/2014
	 * Funcion que recibe 3 parametros:
	 * packageName --> Esquema.Nombre (paquete)
	 * procedureName --> Funcion o procedimiento
	 * paramCount --> Total de parametros que recibe la funcion plsql
	 */
    public static String buildProcedureCall(String packageName, String procedureName, int paramCount) {
    	return buildProcedureCall(packageName,procedureName,paramCount,false);
    }
    
    public static String buildProcedureCall(String packageName, String procedureName, int paramCount, boolean procedureType) {
    	StringBuffer sb;
    	if (procedureType){
    		sb = new StringBuffer("{ call "+packageName+"."+procedureName+"(");
    	}else{
    		sb = new StringBuffer("{ ? = call "+packageName+"."+procedureName+"(");
    	}
        for (int n = 1; n <= paramCount; n++) {
            sb.append("?");
            if (n < paramCount) sb.append(",");
        }
        sb.append(")}");
        System.out.println("buildProcedureCall: "+sb.toString());
        return sb.toString();
    }

    public static void close(ResultSet rs, Statement s, Connection c) {
        try { if (rs != null) rs.close(); } catch (Exception e) {}
        try { if (s != null) s.close(); } catch (Exception e) {}
        try { if (c != null) c.close(); } catch (Exception e) {}
    }
}