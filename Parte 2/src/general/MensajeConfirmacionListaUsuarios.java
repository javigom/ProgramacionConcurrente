package general;

import java.util.List;

public class MensajeConfirmacionListaUsuarios extends Mensaje {

	private static final long serialVersionUID = 1L;

	/** Lista de usuarios conectados. */
	private List<String> lista_usuarios;
	/** Lista con listas de ficheros de cada usuario. */
	private List<List<String>> lista_ficheros;
	
	public MensajeConfirmacionListaUsuarios(String origen, String destino, List<String> lista_usuarios, List<List<String>> lista_ficheros) {
		super(TipoMensaje.CONFIRMACION_LISTA_USUARIOS, origen, destino);
		this.lista_ficheros = lista_ficheros;
		this.lista_usuarios = lista_usuarios;
	}
	
	public List<String> getListaUsuarios(){
		return this.lista_usuarios;
	}

	public List<List<String>> getListaFicheros(){
		return this.lista_ficheros;
	}
}
