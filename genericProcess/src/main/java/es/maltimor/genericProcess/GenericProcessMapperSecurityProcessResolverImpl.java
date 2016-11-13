package es.maltimor.genericProcess;

import java.util.*;

import es.maltimor.genericUser.User;


public class GenericProcessMapperSecurityProcessResolverImpl implements GenericProcessMapperSecurityProcessResolver {
	private boolean defaultResponse=false;

	public boolean canDoProcess(User user, String table, GenericProcessMapperInfoProcess info, Object id, Map<String, Object> data) {
		if (info.getActionRoles().containsKey("E")){
			//recorro la lista de roles que pueden hacer el select
			List<String> roles = info.getActionRoles().get("E"); 
			for(String rol:roles){
				if (user.hasRol(rol)) return true;
			}
			return false;
		}
		return defaultResponse;
	}

	public void setDefaultResponse(Boolean defaultResponse) {
		this.defaultResponse = defaultResponse;
	}
}
