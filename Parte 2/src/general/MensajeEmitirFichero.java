package general;

public class MensajeEmitirFichero extends Mensaje {

	private static final long serialVersionUID = 1L;

	/** Nombre del fichero solicitado. */
	private String fichero_solicitado;
	
	public MensajeEmitirFichero(String origen, String destino, String fichero_solicitado) {
		super(TipoMensaje.EMITIR_FICHERO, origen, destino);
		this.fichero_solicitado = fichero_solicitado;
	}
	
	public String getFicheroSolicitado() {
		return this.fichero_solicitado;
	}

}
