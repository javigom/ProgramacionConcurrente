package general;

import java.io.Serializable;
import java.util.List;

public class MensajeConexion extends Mensaje implements Serializable {

	private static final long serialVersionUID = 1L;

	/** Lista de ficheros de los que dispone el usuario. */
	private List<String> ficheros;
	/** IP del usuario. */
	private String userIP;
	
	public MensajeConexion(String origen, String destino, String userIP, List<String> ficheros) {
		super(TipoMensaje.CONEXION, origen, destino);
		this.userIP = userIP;
		this.ficheros = ficheros;
	}
	
	public String getUserIP() {
		return this.userIP;
	}
	
	public List<String> getFicheros(){
		return this.ficheros;
	}

}
