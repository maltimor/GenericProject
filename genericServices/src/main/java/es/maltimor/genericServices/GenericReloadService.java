package es.maltimor.genericServices;

import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import es.maltimor.genericRest.GenericMapperInfo;
import es.maltimor.genericUser.User;
import es.maltimor.genericUser.UserDao;

@Path("/genericRestService/")
@Consumes("application/json")
@Produces("application/json")
public class GenericReloadService {
	private GenericMapperInfo ginfo;
	private UserDao userDao;
	private String rol;

	public String getRol() {
		return rol;
	}
	public void setRol(String rol) {
		this.rol = rol;
	}
	public GenericMapperInfo getGinfo() {
		return ginfo;
	}
	public void setGinfo(GenericMapperInfo ginfo) {
		this.ginfo = ginfo;
	}
	public UserDao getUserDao() {
		return userDao;
	}
	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}

	@POST
	@Path("/reload")
	public Response reload(Map<String,Object> data){
		System.out.println("reload:");
		for(String key:data.keySet()){
			System.out.println(key+"="+data.get(key));
		}
		
		System.out.println("-----------");
		
		try {
			String login = userDao.getLogin();
			User user = userDao.getUser(login, null);
			if (!user.hasRol(rol)){
				System.out.println("---- no tiene permisos");
				return Response.status(Status.UNAUTHORIZED).entity("No tiene permisos").build();
			}
			ginfo.reload();
			return Response.ok().build();
		} catch (Exception e) {
			e.printStackTrace();
			return Response.serverError().entity(e.getMessage()).build();
		}
	}
}
