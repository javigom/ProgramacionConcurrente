package general;
import java.io.IOException;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Fichero implements Serializable {

	private static final long serialVersionUID = 1L;
	
	/** Array de bytes con el contenido del fichero. */
	private byte[] contenido;
	
	/** Constructor del fichero. Se le pasa la ruta donde se encuentra por parámetro. */
	public Fichero(String path) {
		try {
			this.contenido = Files.readAllBytes(Paths.get(path));
		} catch (IOException e) {
			System.err.println("No se ha podido leer el fichero. Excepcion: " + e.getLocalizedMessage());
		}
	}
	
	/** Devuelve el contenido del fichero. */
	public byte[] getContenido() {
		return contenido;
	}
	
	/** Muestra por pantalla el contenido del fichero. */
	public void print() {
		try {
			System.out.print(new String(contenido, "UTF-8"));
		} catch (UnsupportedEncodingException e) {
			System.err.println("No se ha podido imprimir el contenido del fichero. Excepcion: " + e.getLocalizedMessage());
		}
	}
}
	