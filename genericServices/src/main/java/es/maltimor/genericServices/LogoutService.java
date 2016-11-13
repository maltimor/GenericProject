package es.maltimor.genericServices;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;

@Path("/genericRestService/")
@Consumes("application/json")
@Produces("application/json")
public class LogoutService {

	@GET
	@Path("/logout")
	public Response logout(){
		System.out.println("Control.logout");
		System.out.println("###### DATOS DEL USUARIO.");
		
		long startTime = System.currentTimeMillis();
		
		Subject subject = SecurityUtils.getSubject();
		if (subject!=null) subject.logout();
		
		subject = SecurityUtils.getSubject();
		if (subject!=null) System.out.println("### UserBean.User3="+subject.getPrincipal());
		else System.out.println("### UserBean.User3=null");
		
		return Response.ok().build();
	}	
}