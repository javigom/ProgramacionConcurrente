import java.io.IOException;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Paths;

@SuppressWarnings("serial")
public class Fichero implements Serializable {
	
	private String nombreFichero = "";
	private byte[] contenido;
	
	public Fichero(String nombre, String path) {
		this.nombreFichero = nombre;
		try {
			this.contenido = Files.readAllBytes(Paths.get(path));
		} catch (IOException e) {
			System.err.println("No se ha podido leer el fichero. Excepcion: " + e.getLocalizedMessage());
		}
	}
	  
	public String getNombre() {
		return nombreFichero;
	}
	
	public byte[] getContenido() {
		return contenido;
	}
	
	public void print() throws UnsupportedEncodingException {
		System.out.print(new String(contenido, "UTF-8"));
	}
}
	