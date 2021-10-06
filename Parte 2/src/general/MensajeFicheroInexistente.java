package general;

public class MensajeFicheroInexistente extends Mensaje {

	private static final long serialVersionUID = 1L;

	public MensajeFicheroInexistente(String origen, String destino) {
		super(TipoMensaje.FICHERO_INEXISTENTE, origen, destino);
	}

}
