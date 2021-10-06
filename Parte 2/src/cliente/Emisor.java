package cliente;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import general.Fichero;

public class Emisor extends Thread {

	/** Nombre del fichero que se desea transmitir. */
	private String fichero;
	/** Puerto para la transmisión. */
	private int puerto;
	/** Cliente del que obtenemos el fichero. */
	Cliente cliente;
	
	/** Constructor de la clase Emisor. */
	protected Emisor(int puerto, String fichero, Cliente cliente) {
		this.puerto = puerto;
		this.fichero = fichero;
		this.cliente = cliente;
	}
	
	/** Método run del hilo. */
	public void run() {
		
		try {
			
			// Creamos un nuevo Serversocket y esperamos a que se reciba algo. 
			ServerSocket sc = new ServerSocket(puerto);
			Socket socket = sc.accept();
			
			// Creamos un nuevo objeto de la clase fichero con la ruta adecuada al cliente
			Fichero f = new Fichero(System.getProperty("user.dir") + "/usuarios/" + cliente.getClientName() + "/ficheros/" + fichero);
			
			// Transmitimos el fichero
			ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
			out.writeObject(f);
			out.flush();
			
			// Cerramos el ServerSocket y el Socket
			sc.close();
			socket.close();
			
		} catch (IOException e) {
			System.err.println("Ha ocurrido un error durante la ejecución del Hilo Emisor... ");
			e.printStackTrace();
		}
		
	}
	
}
