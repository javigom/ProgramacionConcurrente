package general;
import java.util.ArrayList;
import java.util.List;

public class Usuario {

	/** Nombre/identificador del usuario. */
	private String userName;
	
	/** Dirección IP del usuario */
	private String direccion_IP;
	
	/** Lista de los nombres de los ficheros que posee el usuario */
	private List<String> ficheros;
	
	/** Constructor de la clase usuario. */
	public Usuario(String name, String direccion_IP, List<String> ficheros) {
		this.userName = name;
		this.direccion_IP = direccion_IP;
		this.ficheros = ficheros;
	}
		
	/** Devuelve el nombre del usuario. */
	public String getUserName() {
		return this.userName;
	}
	
	/** Devuelve la IP del usuario. */
	public String getIP() {
		return this.direccion_IP;
	}
	
	/** Devuelve una copia de la lista con los nombres de los ficheros que posee del usuario. */
	public List<String> getFicheros() {
		return new ArrayList<String> (this.ficheros);
	}
	
}
