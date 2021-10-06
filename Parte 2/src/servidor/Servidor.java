package servidor;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import general.Usuario;
 
public class Servidor {
 
	/** Server socket para las peticiones de red. */
	private ServerSocket servidor;
	/** Socket para comunicarse con los clientes. */
    private Socket socket;
    /** Puerto del servidor. */
    private final static int port = 5000;
    
    private MonitorDatos monitor;
    
    /** Constructor del servidor. Inicializa las distintas variables. */
    protected Servidor() throws IOException {
    			
    	monitor = new MonitorDatos();
    	
		// Inicializo el server socket
		servidor = new ServerSocket(port);
    }
    
    /** Inicia el servidor. */
	protected void iniciaServidor() throws IOException {  	
		
		// Mensaje de aviso
		System.out.println("El servidor se ha iniciado.");
		
		// Inicia el escaneo de la red
		while(true) {
			
			// Espero a que me llegue una petición
			socket = servidor.accept();
			
			// Creo un hilo OyenteCliente para establecer la conexión con un cliente
			(new OyenteCliente(socket, this)).start();
    	}
    	
    }

	/** Añade un nuevo usuario a la base de datos. 
	 * @throws InterruptedException */
	protected void addUser(Usuario usuario, ObjectOutputStream out, ObjectInputStream in) throws InterruptedException {
		monitor.addUser(usuario, out, in);
	}
	
	/** Elimina un usuario de la base de datos. 
	 * @throws InterruptedException */
	protected void deleteUser(String userName) throws InterruptedException {
		monitor.deleteUser(userName);;
	}
	
	/** Devuelve una lista con los nombres de todos los usuarios. 
	 * @throws InterruptedException */
	protected List<String> getUserList() throws InterruptedException{
		
		return monitor.getUserList();
	}
	
	/** Devuelve una lista con listas de todos los ficheros de los usuarios. 
	 * @throws InterruptedException */
	protected List<List<String>> getFileList() throws InterruptedException {
		
		return monitor.getFileList();
	}
	
	/** Devuelve el identificador del usuario que contiene el fichero nombre_fichero. Si el fichero no existe, devuelve -1. 
	 * @throws InterruptedException */
	protected String getUserNamefromNameFile(String nombre_fichero) throws InterruptedException {
		
		return monitor.getUserNamefromFile(nombre_fichero);
	}

	/** Devuelve el flujo de salida de un usuario concreto. 
	 * @throws InterruptedException */
	protected ObjectOutputStream getUserOutputStream(String userName) throws InterruptedException {
		return monitor.getUserOutputStream(userName);
	}
	
	/** Devuelve el flujo de entrada de un usuario concreto. 
	 * @throws InterruptedException */
	protected ObjectInputStream getUserInputStream(String userName) throws InterruptedException {
		return monitor.getUserInputStream(userName);
	}
 
}
