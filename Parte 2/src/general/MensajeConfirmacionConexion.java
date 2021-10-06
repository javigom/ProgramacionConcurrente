package general;

public class MensajeConfirmacionConexion extends Mensaje {

	private static final long serialVersionUID = 1L;
	
	public MensajeConfirmacionConexion(String origen, String destino) {
		super(TipoMensaje.CONFIRMACION_CONEXION, origen, destino);
	}
	
}
