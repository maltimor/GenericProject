package es.maltimor.genericProcess;

import java.util.Map;

import es.maltimor.genericUser.User;

/*
 * Clase que permite personalizar el mapperProvider asociado servicio rest Genericcrud
 * NOTA: si se quiere hacer referencia a los campos con ibatys hay que escribir "#{data.CAMPO}"
 */
public interface GenericProcessMapperInfoProcessResolver {
	public Map<String, Object> doProcess(User user,String table,GenericProcessMapperInfo info, Object id, Map<String, Object> data, GenericProcessServiceMapper mapper, Map<String,Object> params);
	public String doProcess(User user, String table, GenericProcessMapperInfoProcess info, String id, Map<String, Object> data);
}
