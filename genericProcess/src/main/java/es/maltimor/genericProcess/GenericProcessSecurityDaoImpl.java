package es.maltimor.genericProcess;

import java.util.Map;

import es.maltimor.genericProcess.GenericProcessMapperInfo;
import es.maltimor.genericProcess.GenericProcessMapperInfoProcess;
import es.maltimor.genericUser.User;

/*
 * Esta clase es la que centraliza la seguridad de todo el servicio rest, delegara en GenericMapperInfoTable 
 * la implementacion para poder definir la seguridad a nivel individual de tabla
 */
public class GenericProcessSecurityDaoImpl implements GenericProcessSecurityDao {
	private GenericProcessMapperInfo info;
	
	public GenericProcessMapperInfo getInfo() {
		return info;
	}
	public void setInfo(GenericProcessMapperInfo info) {
		this.info = info;
	}

	public boolean canDoProcess(User user, String table, Object id, Map<String, Object> data) throws Exception {
		GenericProcessMapperInfoProcess infoP = info.getInfoProcess(table);
		return infoP.getSecResolver().canDoProcess(user, table, infoP, id, data);
	}

}
