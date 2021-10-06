package general;

public class MensajePreparadoClienteServidor extends Mensaje {

	private static final long serialVersionUID = 1L;
	
	/** Dirección IP para la transmisión. */
	private String direccionIP;
	/** Puerto para la transmisión. */
	private int puerto;

	public MensajePreparadoClienteServidor(String origen, String destino, String direccionIP, int puerto) {
		super(TipoMensaje.PREPARADO_CLIENTESERVIDOR, origen, destino);
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
