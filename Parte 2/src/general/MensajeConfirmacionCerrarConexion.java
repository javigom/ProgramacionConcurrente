package general;

public class MensajeConfirmacionCerrarConexion extends Mensaje {

	private static final long serialVersionUID = 1L;

	public MensajeConfirmacionCerrarConexion(String origen, String destino) {
		super(TipoMensaje.CONFIRMACION_CERRAR_CONEXION, origen, destino);
	}

}
