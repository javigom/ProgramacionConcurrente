package general;

public class MensajePreparadoServidorCliente extends Mensaje {

	private static final long serialVersionUID = 1L;

	/** Direcci�n IP para la transmisi�n. */
	private String direccionIP;
	/** Puerto para la transmisi�n. */
	private int puerto;;
	
	public MensajePreparadoServidorCliente(String origen, String destino, String direccionIP, int puerto) {
		super(TipoMensaje.PREPARADO_SERVIDORCLIENTE, origen, destino);
		this.direccionIP = direccionIP;
		this.puerto = puerto;
	}
	
	public String getIP() {
		return this.direccionIP;
	}
	
	public int getPuerto() {
		return this.puerto;
	}

}
