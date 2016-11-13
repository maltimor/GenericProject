package es.maltimor.genericServices;

import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import es.maltimor.genericRest.GenericMapperInfo;
import es.maltimor.genericUser.User;
import es.maltimor.genericUser.UserDao;
import es.maltimor.genericUser.UserPreferencesDao;

@Path("/genericRestService/")
@Consumes("application/json")
@Produces("application/json")
public class PreferencesService {
	private UserPreferencesDao userPreferencesDao;
	private UserDao userDao;
	private String app;

	public String getApp() {
		return app;
	}
	public void setApp(String app) {
		this.app = app;
	}
	public UserPreferencesDao getUserPreferencesDao() {
		return userPreferencesDao;
	}
	public void setUserPreferencesDao(UserPreferencesDao userPreferencesDao) {
		this.userPreferencesDao = userPreferencesDao;
	}
	public UserDao getUserDao() {
		return userDao;
	}
	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}

	@POST
	@Path("/setPreferences")
	public Response setPreferences(Map<String,Object> data){
		try {
			String login = userDao.getLogin();
			userPreferencesDao.setPreferences(login, app, data);
			return Response.ok().build();
		} catch (Exception e) {
			e.printStackTrace();
			return Response.serverError().entity(e.getMessage()).build();
		}
	}

	@GET
	@Path("/getPreferences")
	public Response getPreferences(){
		try {
			String login = userDao.getLogin();
			Map<String,Object> map = userPreferencesDao.getPreferences(login, app);
			return Response.ok(map).build();
		} catch (Exception e) {
			e.printStackTrace();
			return Response.serverError().entity(e.getMessage()).build();
		}
	}
}
