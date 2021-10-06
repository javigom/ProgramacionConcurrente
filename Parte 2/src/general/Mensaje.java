package general;

import java.io.Serializable;

public abstract class Mensaje implements Serializable {

	private static final long serialVersionUID = 1L;

	/** Enumerado con el tipo de mensaje. */
	private TipoMensaje tipo;
	/** Emisor del mensaje. */
	private String origen;
	/** Receptor del mensaje. */
	private String destino;

	/** Constructor de la clase Mensaje. */
	public Mensaje(TipoMensaje tipo, String origen, String destino) {
		this.tipo = tipo;
		this.origen = origen;
		this.destino = destino;
	}
	
	/** Devuelve el tipo del mensaje. */
	public TipoMensaje getTipo() {
		return this.tipo;
	}
	
	/** Devuelve el nombre del emisor del mensaje. */
	public String getOrigen() {
		return this.origen;
	}

	/** Devuelve el nombre del receptor del mensaje. */
	public String getDestino() {
		return this.destino;
	}
}
