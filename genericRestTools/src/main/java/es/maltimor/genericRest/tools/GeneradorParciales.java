package es.maltimor.genericRest.tools;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Scanner;

import org.apache.commons.dbcp2.BasicDataSource;

import es.maltimor.genericRest.GenericMapperInfo;
import es.maltimor.genericRest.GenericMapperInfoColumn;
import es.maltimor.genericRest.GenericMapperInfoTable;
import es.maltimor.webUtils.JDBCBridge;

public class GeneradorParciales {
	private GenericMapperInfo tabs;
	private String baseDir;
	private String fileName;
	private String charset;

	// private String linea = "";

	public GenericMapperInfo getTabs() {
		return tabs;
	}

	public String getCharset() {
		return charset;
	}

	public void setCharset(String charset) {
		this.charset = charset;
	}

	public void setTabs(GenericMapperInfo tabs) {
		this.tabs = tabs;
	}

	public String getBaseDir() {
		return baseDir;
	}

	public void setBaseDir(String baseDir) {
		this.baseDir = baseDir;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public GeneradorParciales() {
		this.tabs = new GenericMapperInfo();
	}

	public byte[] getBytesFromFile(String file) {
		byte[] res = null;
		try {
			File f = new File(file);
			FileInputStream fin = new FileInputStream(f);
			res = new byte[fin.available()];
			int b = fin.read(res);
			if (b != res.length) {
				return null;
			}
			fin.close();
			return res;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public String leerFichero(String nomFichero) {
		byte[] buff = getBytesFromFile(nomFichero);
		if (buff == null)
			return "";

		String res = "";
		try {
			res = new String(buff, charset);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return res;
	}

	public void cargaTablas() {
		String linea = leerFichero(fileName);
		try {
			tabs.setInfo(linea);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void procesaTabla(String tabla) {

	}
		
	
	
	public void generaAppconfig() throws IOException{
		
		File fichero = null;
		FileWriter escribir = null;
		
		//Genera AppConfig.js
		
		String procesosUri = "";
		String menusDebugData = "";
		String roles = "";
		String concat = this.baseDir + "\\";
		
		fichero = new File(concat,"app.config.js");
		fichero.createNewFile();
		String linea = leerFichero("Plantillas/PlantillaAppConfig/app.config.js");
		
		for (int i=0; i<tabs.getListTable().size(); i++){
			
			procesosUri += "\t"+'"' + tabs.getListTable().get(i).toUpperCase() + '"' + ':' +'"' + tabs.getListTable().get(i).toLowerCase()+ '"' + ',' + '\n'; 
			
			menusDebugData += "\t{" + '"' + "id" + '"' + ":" + '"' + i + '"' + "," + '\n'
								  +"\t" +'"' + "nombre" + '"' + ":" + '"' + tabs.getListTable().get(i).toLowerCase() + '"' + "," + '\n'
								  +"\t" +'"' + "proceso" + '"' + ":" + '"' + "C_" +tabs.getListTable().get(i).toUpperCase() + '"' + "," + '\n'
								  +"\t" +'"' + "submenu" + '"' + ":" + "[]}," + '\n';
			
			roles += "\t" + '"' + tabs.getListTable().get(i).toUpperCase()+ '"' + ",\n"; 
		}
		
		linea = linea.replace("#!PROCESOSURI#", procesosUri);
		linea = linea.replace("#!MENUDEBUGDATA#", menusDebugData);
		linea = linea.replace("#!ROLES#", roles);
		escribir = new FileWriter(fichero, true);
		escribir.write(linea);
		escribir.close();
			
	}
		

	
	public void procesaInfo() {

		File directorio = null;
		File fichero = null;
		FileWriter escribir = null;
		String campoActual = "";
		String tipoCampoActual = "";

		
		directorio = new File(baseDir);
		directorio.mkdir();
		
		try {
			for (int i = 0; i < tabs.getListTable().size(); i++) {
				
				String tablaActual = tabs.getListTable().get(i);
				String tablaActualMayuscula = tabs.getListTable().get(i);
				
				
				// Pasa la primera letra a mayuscula
				String mayuscula = tablaActualMayuscula.charAt(0) + "";
				mayuscula = mayuscula.toUpperCase();
				tablaActualMayuscula = tablaActualMayuscula.replaceFirst(tablaActualMayuscula.charAt(0) + "", mayuscula);

				String concat = this.baseDir + tablaActual + "\\";

				directorio = new File(concat);
				directorio.mkdir();
				


				
				String listaCamposForm="";
				String listaCampos="";
				String fieldKey="";
				
//				obtenerTipoCampos(tablaActual);

				GenericMapperInfoTable table = tabs.getInfoTable(tablaActual);
				List<GenericMapperInfoColumn> cols = table.getFields();
				for (int j = 0; j < cols.size(); j++) {
					GenericMapperInfoColumn colAct = cols.get(j);
					
					campoActual = colAct.getName();
					tipoCampoActual = colAct.getType();
					
					if(tipoCampoActual.equals("T")){
						tipoCampoActual = "text";
					}
					else if(tipoCampoActual.equals("N")){
						tipoCampoActual = "number";
					}
					else{
						tipoCampoActual = "date";
					}
					
										
					listaCampos += "{label: '" + campoActual + "', column: '"
							+ campoActual + "', weight: '10',type:'"+ tipoCampoActual +"'},\n";
		
					listaCamposForm += "{key: '"
							+ campoActual
							+ "',type: '"+ tipoCampoActual+"',min:3,col:'col-md-2',label: '"+ campoActual +"',placeholder: '',autofocus:'autofocus',required: true },\n";			
				}
				
				
				fieldKey = tabs.getKey(tablaActual);

				
				
				
				
				
				//Genera JS
				String linea = trasnform("Plantillas/PlantillaParcial1/plantilla.js",
						tablaActual,tablaActualMayuscula,fieldKey,listaCampos,listaCamposForm);
				escribeFichero(concat,tabs.getListTable().get(i) + ".js", linea);
				
				//Genera HTML
				linea = trasnform("Plantillas/PlantillaParcial1/plantilla.tpl.html",
						tablaActual,tablaActualMayuscula,fieldKey,listaCampos,listaCamposForm);
				escribeFichero(concat,tabs.getListTable().get(i) + ".tpl.html", linea);

				System.out.println("Carpeta y ficheros creados: "
						+ tabs.getListTable().get(i));

			}
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	
	
	private void escribeFichero(String dir, String name, String content) {
		File fichero = new File(dir, name);
		try{
		fichero.createNewFile();				
		FileWriter escribir = new FileWriter(fichero, true);
		escribir.write(content);
		escribir.close();
		} catch (Exception e){
			e.printStackTrace();
		}
	}

	private String trasnform(String fileName, String tablaActual,
			String tablaActualMayuscula, String fieldKey, String listaCampos,
			String listaCamposForm) {
		String linea = leerFichero(fileName);
		linea = linea.replace("#!TABLENAME#", tablaActual);
		linea = linea.replace("#!TABLENAME_PCASE#", tablaActualMayuscula);
		linea = linea.replace("#!FIELDKEY#", fieldKey);
		linea = linea.replace("#!LISTFIELDS#", listaCampos);
		linea = linea.replace("#!FORMFIELDS#", listaCamposForm);
		return linea;
	}

	public void inicializaJDBC(String userName, String userPassword,String driver, String url) {
		BasicDataSource datasource = new BasicDataSource();
		datasource.setDriverClassName(driver);
		datasource.setUrl(url);
		datasource.setUsername(userName);
		datasource.setPassword(userPassword);
//		datasource.setUsername("LT_WEB");
//		datasource.setPassword("desarrollo");

		JDBCBridge jdbc = new JDBCBridge();
		jdbc.setDataSource(datasource);

	}

	
	
	
	public static void main(String[] arg) throws IOException {
		
		GeneradorParciales generador = new GeneradorParciales();
		
		Scanner scanner = new Scanner(System.in);
		System.out.print("Enter a BBDD Name: ");
		System.out.flush();
		String userBBDD = "";
		String passBBDD = "";
		String driver = "";
		String url = "";
		String ficheroBBDD = "";
		String directorioSalida = "";
		String charsets = "";
		
		
		if(arg.length!=7){
			
			System.out.println("Los argumentos no son los adecuados. \n");
			System.out.println("Introduzca los datos: \n");
			
			System.out.print("Enter a user name: ");
			userBBDD = scanner.nextLine();
			System.out.print("Enter a user password: ");
			passBBDD = scanner.nextLine();
			System.out.print("Enter a driver: ");
			driver = scanner.nextLine();
			System.out.print("Enter a url: ");
			url = scanner.nextLine();
			System.out.print("Enter a file name: ");
			ficheroBBDD = scanner.nextLine();
			System.out.print("Enter a output directory: ");
			directorioSalida = scanner.nextLine();
			System.out.print("Enter a output directory: ");
			charsets = scanner.nextLine();
			
			if(userBBDD != null && passBBDD != null && ficheroBBDD != null && directorioSalida != null && charsets != null ){
				
				generador.inicializaJDBC(userBBDD, passBBDD,driver,url);
				generador.setFileName(ficheroBBDD);
				generador.setBaseDir(directorioSalida);
				generador.setCharset(charsets);
				
				generador.cargaTablas();
				generador.procesaInfo();
				generador.generaAppconfig();
				
			}
			
			
			
		}else{
		
		generador.inicializaJDBC(arg[0], arg[1],arg[2],arg[3]);
		generador.setFileName(arg[4]);
		generador.setBaseDir(arg[5]);
		generador.setCharset(arg[6]);

		generador.cargaTablas();
		generador.procesaInfo();
		generador.generaAppconfig();
		}
	}
}