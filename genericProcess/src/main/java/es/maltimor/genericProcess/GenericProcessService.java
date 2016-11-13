package es.maltimor.genericProcess;

import java.util.*;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;

import es.maltimor.genericUser.User;
import es.maltimor.genericUser.UserDao;

@Path("/genericProcessService/")
@Consumes("application/json")
@Produces("application/json")
public class GenericProcessService {
	private GenericProcessServiceDao service;
	private GenericProcessSecurityDao securityDao;
	private UserDao userDao;
	private String app;

	public GenericProcessServiceDao getService() {
		return service;
	}
	public void setService(GenericProcessServiceDao service) {
		this.service = service;
	}
	public GenericProcessSecurityDao getSecurityDao() {
		return securityDao;
	}
	public void setSecurityDao(GenericProcessSecurityDao securityDao) {
		this.securityDao = securityDao;
	}
	public UserDao getUserDao() {
		return userDao;
	}
	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}
	public String getApp() {
		return app;
	}
	public void setApp(String app) {
		this.app = app;
	}

	@POST
	@Path("/{table}/{id:.*}")
	public Response doProcess(@PathParam("table")String table,@PathParam("id") String id,Map<String,Object> data){
		System.out.println("REST: PUT "+table+":"+id+":"+data.size());
		for(String key:data.keySet()){
			System.out.println("KEYS = "+key+" VALUE = "+data.get(key));
		}

		try {
			//comprobar que exista
			if (!service.isProcess(table)){
				System.out.println("---- no EXISTE");
				return Response.status(HttpServletResponse.SC_NOT_FOUND).entity("No existe el proceso").build();
			}
			
			//comprobar la seguridad
			User user = userDao.getUser(userDao.getLogin(), app);
			if (!securityDao.canDoProcess(user, table, id, data)){
				System.out.println("---- no tiene permisos");
				return Response.status(HttpServletResponse.SC_UNAUTHORIZED).entity("No tiene permisos").build();
			}

			Object res = service.doProcess(user,table,id,data);
			return Response.ok(res).build();
		} catch (Exception e) {
			e.printStackTrace();
			return Response.serverError().entity(e.getMessage()).build();
		}
	}
}
