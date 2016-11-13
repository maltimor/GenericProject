package es.maltimor.genericServices;

import java.io.InputStream;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.jar.Attributes;
import java.util.jar.Manifest;

import javax.servlet.ServletContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;

@Path("/genericRestService/")
@Consumes("application/json")
@Produces("application/json")
public class InfoService {

	@GET
	@Path("/getInfo")
	public Response getInfo(@Context ServletContext context){
		System.out.println("getInfo:");
		
		try {
			String version="?";
			String ip="";
			String host="";
			String ips="";
			try{
				InputStream input = context.getResourceAsStream("/META-INF/MANIFEST.MF");
				Manifest manifest = new Manifest(input);
				Attributes attributes = manifest.getMainAttributes();
				
				version = attributes.getValue("Implementation-Version");
			} catch (Exception e){}
			
			try{
				InetAddress address = InetAddress.getLocalHost();
                host = address.getHostName();
                byte[] bIPAddress = address.getAddress();
                ip = ""+(bIPAddress[bIPAddress.length-1]&255);
			} catch (Exception e3){}
			
			try{
				Enumeration e = NetworkInterface.getNetworkInterfaces();
				while(e.hasMoreElements()) {
					NetworkInterface n = (NetworkInterface) e.nextElement();
					Enumeration ee = n.getInetAddresses();
					while (ee.hasMoreElements()) {
						InetAddress i = (InetAddress) ee.nextElement();
						String aux=i.getHostAddress();
						/*System.out.println(aux);
						System.out.println("isAny? "+i.isAnyLocalAddress());
						System.out.println("isLink? "+i.isLinkLocalAddress());
						System.out.println("isLoopback? "+i.isLoopbackAddress());
						System.out.println("isMulticast? "+i.isMulticastAddress());
						System.out.println("isSite? "+i.isSiteLocalAddress());
						System.out.println("isIP4? "+(i instanceof Inet4Address));*/
							
							
						//elimino la parte izquierda por seguridad
						if ((i instanceof Inet4Address) && !i.isLoopbackAddress()) {
							ips+=aux.substring(aux.lastIndexOf(".")+1)+" ";
						}
					}
				}
			} catch (Exception ex2){
				ex2.printStackTrace();
			}
			
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("version", version);
			map.put("ip",ip);
			map.put("host",host);
			map.put("ips",ips.trim());
			
			return Response.ok(map).build();
		} catch (Exception e) {
			e.printStackTrace();
			return Response.serverError().entity(e.getMessage()).build();
		}
	}

}
