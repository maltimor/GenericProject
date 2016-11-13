package es.maltimor.genericUser;

import java.util.HashMap;
import java.util.Map;

public class UserDaoCache implements UserDao {
	private long expiration;		//milisegundos de validez de la cache
	private UserDao userDao;
	private Map<String,User> map;
	private Map<String,Long> times;
	
	public UserDaoCache(){
		this.map = new HashMap<String,User>();
		this.times = new HashMap<String,Long>();
		this.expiration = 30*60*1000;		//30 minutos
	}
	
	private String hash(String login,String app){
		String res = "|"+login+"|"+app+"|";
		return res;
	}
	
	public User initUser(String login, String app) throws Exception {
		//no cacheao y elimino la posible cache
		String h = hash(login,app);
		if (map.containsKey(h)) map.remove(h);
		return userDao.initUser(login, app);
	}

	public User getUser(String login, String app) throws Exception {
		String h = hash(login,app);
		checkTime(h);
		
		if (map.containsKey(h)) return map.get(h);
		User user = userDao.getUser(login, app);
		
		putUser(h,user);
		return user;
	}

	public String getLogin() throws Exception {
		return userDao.getLogin();
	}
	
	private void checkTime(String h){
		//elimina del map si supera el tiempo maximo
		long timeAct=System.currentTimeMillis();
		Long time=times.get(h);
		System.out.println("CHECK: "+h);
		if ((time==null)||(timeAct-time<expiration)) return;
		System.out.println("INVALIDANDO: "+h);
		map.remove(h);
		times.remove(h);
	}
	
	private void putUser(String h,User user){
		map.put(h, user);
		times.put(h, System.currentTimeMillis());
	}
	
	public UserDao getUserDao() {
		return userDao;
	}
	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}
	public long getExpiration() {
		return expiration;
	}
	public void setExpiration(long expiration) {
		this.expiration = expiration;
	}
}
