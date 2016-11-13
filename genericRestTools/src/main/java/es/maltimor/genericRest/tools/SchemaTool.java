package es.maltimor.genericRest.tools;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.apache.commons.dbcp2.BasicDataSource;

public class SchemaTool {
	private String userBBDD, passBBDD, urlBBDD;
	private BasicDataSource datasource;
	private File archivoDestino;
	// listas internas sin get ni set:
	private List<String> tablas = new ArrayList<String>();
	private List<String> tablasCompletadas = new ArrayList<String>();

	public SchemaTool(){
		userBBDD="";
		passBBDD= "";
		urlBBDD = "";
		datasource=null;
		archivoDestino= null;
	}

	//se puede pasar un null por datasource
	public SchemaTool(String userBBDD, String passBBDD, String urlBBDD, BasicDataSource datasource, File archivoDestino) {
		this.userBBDD = userBBDD;
		this.passBBDD = passBBDD;
		this.urlBBDD = urlBBDD;
		this.archivoDestino = archivoDestino;
		if (datasource==null)
			iniciarDataSource();
		else
			this.datasource = datasource;		
	}
	
	public SchemaTool(BasicDataSource datasource, File archivoDestino) {
		this(datasource.getUsername(), datasource.getPassword(), datasource.getUrl(), datasource, archivoDestino);
	}
	
	public BasicDataSource getDatasource() {
		return datasource;
	}

	public void setDatasource(BasicDataSource datasource) {
		this.datasource = datasource;
	}

	public String getPassBBDD() {
		return passBBDD;
	}

	public void setPassBBDD(String passBBDD) {
		this.passBBDD = passBBDD;
	}

	public String getUserBBDD() {
		return userBBDD;
	}

	public void setUserBBDD(String userBBDD) {
		this.userBBDD = userBBDD;
	}

	public File getArchivoDestino() {
		return archivoDestino;
	}

	public void setArchivoDestino(File archivoDestino) {
		this.archivoDestino = archivoDestino;
	}

	
	public String getUrlBBDD() {
		return urlBBDD;
	}

	public void setUrlBBDD(String urlBBDD) {
		this.urlBBDD = urlBBDD;
	}

	/*
	 * convierte los tipos en formato T, F, N
	 */
	private static String convertirTipo(String tipo) {
		if (tipo.equals("CHAR") || tipo.equals("VARCHAR2"))
			tipo = "T";
		if (tipo.equals("DATE"))
			tipo = "F";
		if (tipo.equals("LONG") || tipo.equals("LONG RAW") || tipo.equals("NUMBER"))
			tipo = "N";
		return tipo;
	}
	
	public void procesaSQL(String tabla,String sql) throws Exception {
		// Establecemos informacion de conexion:
		iniciarDataSource();
		// Creamos conexion:
		Connection conn = null;
		conn = datasource.getConnection();
		Statement stmt = conn.createStatement();
		// obtenemos informacion de la conexion>
		DatabaseMetaData meta = conn.getMetaData();
		ResultSet res = meta.getTables(null, getUserBBDD(), null, new String[] { "TABLE" });

			String tablaOut = tabla + "|";
			ResultSet rs;
			try {
				rs = stmt.executeQuery(sql);
			} catch (Exception e) {
				// Salta si la tabla no existe:
				e.printStackTrace();
				throw new Exception("Error en la sintaxis, sql=" + sql+" . "+e.getMessage());
			}
			String key = "";
			ResultSetMetaData md = rs.getMetaData();
			for (int k = 1; k <= md.getColumnCount(); k++) {
				if (k == 1)
					key = md.getColumnName(k);
				tablaOut += md.getColumnName(k) + "#" + convertirTipo(md.getColumnTypeName(k)) + "#"
						+ md.getColumnDisplaySize(k) + ",";
			}
			tablaOut = tablaOut.substring(0, tablaOut.length() - 1);
			tablaOut += "|" + key;
			tablasCompletadas.add(tablaOut);
			System.out.println(tablaOut);

		guardarInformacion();

	}



	// deben estar inicializados los atributos: user, pass, y archivoDestino, si
	// no se quiere inicializar manualmente el data source se puede utilizar el
	// metodo iniciarDataSource()
	public void procesaAll() throws Exception {
		// Establecemos informacion de conexion:
		iniciarDataSource();
		// Creamos conexion:
		Connection conn = null;
		conn = datasource.getConnection();
		Statement stmt = conn.createStatement();
		// obtenemos informacion de la conexion>
		DatabaseMetaData meta = conn.getMetaData();
		ResultSet res = meta.getTables(null, getUserBBDD(), null, new String[] { "TABLE" });

		while (res.next()) {
			tablas.add(res.getString("TABLE_NAME"));
		}
		for (String tabla : tablas) {
			String tablaOut = tabla + "|";
			ResultSet rs;
			try {
				rs = stmt.executeQuery("SELECT * FROM " + tabla);
			} catch (Exception e) {
				// Salta si la tabla no existe:
				throw new Exception("Error en la sintaxis, la tabla " + tabla + " no existe");
			}
			String key = "";
			ResultSetMetaData md = rs.getMetaData();
			for (int k = 1; k <= md.getColumnCount(); k++) {
				if (k == 1)
					key = md.getColumnName(k);
				tablaOut += md.getColumnName(k) + "#" + convertirTipo(md.getColumnTypeName(k)) + "#"
						+ md.getColumnDisplaySize(k) + ",";
			}
			tablaOut = tablaOut.substring(0, tablaOut.length() - 1);
			tablaOut += "|" + key;
			tablasCompletadas.add(tablaOut);
			System.out.println(tablaOut);
		}

		guardarInformacion();

	}

	public void guardarInformacion() throws IOException {
		FileWriter fw = new FileWriter(archivoDestino.getAbsoluteFile());
		BufferedWriter bw = new BufferedWriter(fw);
		for (String string : tablasCompletadas) {
			bw.write(string);
			bw.newLine();
		}
		bw.close();
		// Ejemplo de direccion donde los archivos se guardan se guardan:
		// G:\Proyecto
		// Java\Workspace\TGenerales\TGenerales-back\TGenerales-back-dao
	}

	public void iniciarDataSource() {
		datasource = new BasicDataSource();
		datasource.setDriverClassName("oracle.jdbc.driver.OracleDriver");
		datasource
				.setUrl(getUrlBBDD());
		datasource.setUsername(getUserBBDD());
		datasource.setPassword(getPassBBDD());
	}

	// args:
	// 0: userBBDD
	// 1: passBBDD
	// 2: archivoDestino
	public static void main(String[] arg) throws Exception {
		SchemaTool st = new SchemaTool();
		if (arg.length < 4) {
			System.out.println("Los argumentos no son los adecuados. \n");
			System.exit(-1);
		} else {
			st.setUserBBDD(arg[0]);
			st.setPassBBDD(arg[1]);
			File file = new File(arg[2]);
			st.setArchivoDestino(file);
			st.setUrlBBDD(arg[3]);
			st.procesaAll();
		}
	}
	
	private void scannerUserPassBBDD() {
		Scanner scan = new Scanner(System.in);
		System.out.println("Introduzca los datos: \n");
		System.out.print("Enter a user name: ");
		userBBDD = scan.nextLine();
		System.out.print("Enter a user pass: ");
		passBBDD = scan.nextLine();
		scan.close();
	}

	private void scannerArchivoDestino() throws IOException {
		@SuppressWarnings("resource")
		Scanner scanner = new Scanner(System.in);
		System.out.print("Enter a file name: ");
		System.out.flush();
		String filename = scanner.nextLine();
		File file = new File(filename);
		if (!file.exists()) {
			file.createNewFile();
		}
		archivoDestino = file;
	}
}