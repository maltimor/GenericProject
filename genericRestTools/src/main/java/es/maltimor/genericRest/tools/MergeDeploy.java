package es.maltimor.genericRest.tools;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

public class MergeDeploy {

	private String appName;
	private String nomFicheroDep;
	private String nomFicheroProp;
	private String nomFicheroSal;	
	
	public MergeDeploy(){
		
	}
	public MergeDeploy(String ficheroDep, String ficheroProp, String nombreApp, String ficheroSalida){
		nomFicheroDep=ficheroDep;
		nomFicheroProp=ficheroProp;
		appName=nombreApp;
		nomFicheroSal=ficheroSalida;
	}
	
	public String getAppName() {
		return appName;
	}


	public void setAppName(String appName) {
		this.appName = appName;
	}


	public String getNomFicheroDep() {
		return nomFicheroDep;
	}


	public void setNomFicheroDep(String nomFicheroDep) {
		this.nomFicheroDep = nomFicheroDep;
	}


	public String getNomFicheroProp() {
		return nomFicheroProp;
	}


	public void setNomFicheroProp(String nomFicheroProp) {
		this.nomFicheroProp = nomFicheroProp;
	}


	public String getNomFicheroSal() {
		return nomFicheroSal;
	}


	public void setNomFicheroSal(String nomFicheroSal) {
		this.nomFicheroSal = nomFicheroSal;
	}

	//crea una copia de seguridad del archivo deploy con la fecha y hora de la ejecucion del metodo
	//falta implementarlo en el metodo procesa2 o procesaGnal
	//
public void copiaDeploy(File deploy,String ficheroDeploy) throws IOException{
		
		SimpleDateFormat fecha = new SimpleDateFormat("yyyyMMdd-HMmmss", Locale.getDefault());
		Date ahora = new Date();
		String nombreFichero = fecha.format(ahora);
		String comando=null;
		String rutaCopia=null;
		String SO=System.getProperty("os.name").toLowerCase();
		
		String path=deploy.getParent();
		
		if(SO.indexOf("windows")>=1){
			rutaCopia=path+nombreFichero+"-"+deploy.getName();
			comando="copy "+ficheroDeploy+" "+rutaCopia;
			Runtime.getRuntime().exec(comando);
		}else if (SO.indexOf("nux")>=1){
			rutaCopia=path+nombreFichero+"-"+deploy.getName();
			comando="cp "+ficheroDeploy+" "+rutaCopia;
			Runtime.getRuntime().exec(comando);
		}
	}
	
	/*
	 * Procesa 2
	 * */
	public void procesa2(MergeDeploy mg2,BufferedReader br2, PrintWriter pw){
		try {
			String linea2;
			while((linea2= br2.readLine())!=null){
				if(!(linea2.startsWith("#"))&&(linea2.contains("="))){
					String[] partes=linea2.split("=");
					if(!partes[0].startsWith(appName)){
						System.err.println("La sección no se corresponde con el nombre de la aplicación\n"+linea2);
						System.exit(-1);
					}
				}
				pw.println(linea2);
			}
		} catch (Exception e) {
				e.printStackTrace();
		}		
	}
	
	
	/*
	 * Procesa General
	 * */
	public void procesaGnal(MergeDeploy md){
		File fichero = null;
		FileReader fr = null;
		BufferedReader br = null;

		File fichero2 = null;
		FileReader fr2 = null;
		BufferedReader br2 = null;
		
		FileWriter fw = null;
        PrintWriter pw = null;
        
        try {
			fichero = new File(md.getNomFicheroDep());
			fr = new FileReader (fichero);
	        br = new BufferedReader(fr);

	        fichero2 = new File(md.getNomFicheroProp());
			fr2 = new FileReader (fichero2);
	        br2 = new BufferedReader(fr2);


	        fw = new FileWriter(md.getNomFicheroSal(), false);
            pw = new PrintWriter(fw);
	        
	      // Lectura del fichero
	         String linea;
	         boolean inserta=false;
	         
	         while((linea = br.readLine())!=null){
	        	 System.out.println(linea);
	             if (linea.equals("#["+md.getAppName()+"]")){
	            	pw.println(linea);
	            	md.procesa2(md,br2,pw);
	            	
	            	boolean fin=false;
	   	         	while(!fin){
	   	         		linea = br.readLine();
	   	         		if (linea==null) fin=true;
	   	         		else if (linea.startsWith("#[")){
	   	         			fin=true;
	   	         			pw.println();
	   	         			pw.println(linea);
	   	         		}
	   	         	}
	   	         	inserta=true;
	             } else {
	            	pw.println(linea);
	             }
	          }
	         
	          if(!inserta){
	        	 pw.println();
	        	 pw.println("#["+md.getAppName()+"]");
	        	 md.procesa2(md,br2,pw); 
	          }
	          pw.println("\n ");
	         
	          br.close();
	          fr.close();
	          br2.close();
	          fr2.close();
	          fw.close();
	          pw.close();
	         
	         
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	public static void main(String[] arg) throws IOException {
        
        MergeDeploy mergeDep=new MergeDeploy(arg[0],arg[1],arg[2],arg[3]);
		
        mergeDep.procesaGnal(mergeDep);
        
	}
}