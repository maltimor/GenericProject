package es.maltimor.shiroUtils;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.cas.CasRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.apache.shiro.subject.Subject;

import es.maltimor.genericUser.User;
import es.maltimor.genericUser.UserDao;

public class MyCasRealmWS extends CasRealm {
//	private ControlAplicAPI controlAplicAPI;
	private String currentApp;					//Esta variable se puede cambiar en tiempo de ejecucion
	//private Subject currentUser;
	//private String roles;
	//private Set<String> roleNames;
	//private boolean needRefresh;
	protected boolean debug = false;
//	protected String autorizationCall=null;
	
	private UserDao userDao;
	
	
	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}

	public void setDebug(boolean debug) {
        this.debug = debug;
    }

//	public void setControlAplicAPI(ControlAplicAPI controlAplicAPI) {
//		this.controlAplicAPI = controlAplicAPI;
//	}
    public void setCurrentApp(String currentApp) {
		this.currentApp = currentApp;
	}

//	public void setAutorizationCall(String autorizationCall) {
//		this.autorizationCall = autorizationCall;
//	}
	
	private Set<String> getRoleNames(String app,String user) {
		Set<String> roleNames = new LinkedHashSet<String>();
    	String roles="";
		

			System.out.println("########################### CONTROL APLIC API GET ROLES INIT: "+user);
			try{
				User u = userDao.initUser(user, app);
				List<String> res = u.getRoles();
				for(String rol: res){
					roleNames.add(rol);
					roles += "," + rol;
				}
				roles+=" U:"+u.toString();
			} catch (Exception e){
				e.printStackTrace();
			}
			
//			try {
//		    	controlAplicAPI.recopilaPermisosUsuario(user);
//				List<Map<String,Object>> col = controlAplicAPI.getProcesosUsuario(user,app);
//				for(int i=0;i<col.size();i++){
//					String rol=(String) col.get(i).get("PROCESO");
//					roleNames.add(rol);
//					if (roles.equals("")) roles = rol;
//					else roles=roles+"~"+rol;
//				}
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
		
		System.out.println("########################### CONTROL APLIC API GET ROLES RESP: " + roles);
		return roleNames;
	}
	
	@Override
	@SuppressWarnings({ "unchecked", "unused" })
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		long startTimeGeneral = System.nanoTime();
        SimplePrincipalCollection principalCollection = (SimplePrincipalCollection) principals;
        List<Object> listPrincipals = principalCollection.asList();
    	Object p = principalCollection.getPrimaryPrincipal();
    	String username = p.toString(); 

		//obtengo la lista de roles del datasource
		SimpleAuthorizationInfo res = (SimpleAuthorizationInfo) super.doGetAuthorizationInfo(principals);
		try{
			res.addRoles(getRoleNames(currentApp,username));
		} catch (Exception e){
			e.printStackTrace();
		}
    	System.out.println("TIEMPO GENERAL doGetAuthorizationInfo:"+((System.nanoTime() - startTimeGeneral)/ 1000000));
		return res;
	}

}
