package general;

public class MensajeCerrarConexion extends Mensaje {

	private static final long serialVersionUID = 1L;
	
	public MensajeCerrarConexion(String origen, String destino) {
		super(TipoMensaje.CERRAR_CONEXION, origen, destino);
	}

}
