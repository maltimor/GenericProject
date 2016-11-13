package es.maltimor.webUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * Clase de utilidad para serializar y deserializar objetos Java.
 */
public class SerializationUtils {
	
	/**
	 * Serializa un objeto java en un array de bytes.
	 * 
	 * @param object Objeto a serializar.
	 * @return
	 * @throws IOException
	 */
	public static byte[] serializaObjeto(Object object) throws IOException {
		
		ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
		ObjectOutputStream objOut = new ObjectOutputStream(byteOut);
		objOut.writeObject(object);
		objOut.close();
		byteOut.close();
		return byteOut.toByteArray();
	}
	
	/**
	 * Deseriza un array de bytes en un objeto java del tipo indicado.
	 * 
	 * @param bytes Array de bytes del que leer el objeto.
	 * @param tipo Tipo del objeto a retornar.
	 * @return
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public static <T extends Serializable> T deserializaObjeto(byte[] bytes, Class<T> tipo) throws IOException, ClassNotFoundException {
		
		if (bytes == null) {
            return null;
        }
		
        ByteArrayInputStream byteIn = new ByteArrayInputStream(bytes);
        ObjectInputStream in = new ObjectInputStream(byteIn);
        
        @SuppressWarnings("unchecked")
		T obj = (T) in.readObject();
        in.close();
        return obj;
	}
	
	/**
	 * Deseriza un objeto leido de in InputStream en un objeto java del tipo indicado.
	 * 
	 * @param is Stream del que leer el objeto.
	 * @param tipo Tipo del objeto a retornar.
	 * @return
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public static <T extends Serializable> T deserializaObjeto(InputStream is, Class<T> tipo) throws IOException, ClassNotFoundException {
		
		if (is == null) {
            return null;
        }
		
        ObjectInputStream in = new ObjectInputStream(is);
        
        @SuppressWarnings("unchecked")
		T obj = (T) in.readObject();
        in.close();
        return obj;
	}
}
