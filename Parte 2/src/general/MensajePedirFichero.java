package general;

public class MensajePedirFichero extends Mensaje {

	private static final long serialVersionUID = 1L;

	/** Nombre del fichero solicitado. */
	private String nombre_fichero;
	
	public MensajePedirFichero(String origen, String destino, String nombre_fichero) {
		super(TipoMensaje.PEDIR_FICHERO, origen, destino);
		this.nombre_fichero = nombre_fichero;
	}

	
	public String getNombreFichero() {
		return this.nombre_fichero;
	}
}
