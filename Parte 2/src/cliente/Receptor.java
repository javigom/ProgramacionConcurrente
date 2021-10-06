package cliente;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.concurrent.Semaphore;

import general.Fichero;

public class Receptor extends Thread {

	/** IP del cliente emisor. */
	private String emisorIP;
	/** Puerto de la transmisi�n. */
	private int puerto;
	/** Referencia al sem�foro para evitar imprimir el men� hasta no haber recibido el fichero. */
	Semaphore semPrinted;
	
	/** Constructor de la clase Receptor. */
	public Receptor(String emisorIP, int puerto, Semaphore semPrinted) {
		this.emisorIP = emisorIP;
		this.puerto = puerto;
		this.semPrinted = semPrinted;
	}
	
	/** M�todo run del hilo. */
	public void run() {
		
		try {
			
			// Creamos un nuevo socket a partir de la IP y el puerto
			Socket socket = new Socket(emisorIP, puerto);
			
			// Leemos el fichero del flujo de entrada
			ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
			Fichero f = (Fichero) in.readObject();
			
			System.out.println("=========================================================================================");
			System.out.println("Contenido del fichero solicitado: ");
			System.out.println("=========================================================================================");
			System.out.println("");
			
			// Lo mostramos por pantalla
			f.print();
			
			System.out.println("");
			System.out.println("");
			System.out.println("=========================================================================================");
			System.out.println("");
			
			// Cerramos el socket
			socket.close();
			
			// Liberamos un permiso del sem�foro
			semPrinted.release();
			
		} catch (IOException | ClassNotFoundException e) {
			System.err.println("Ha ocurrido un error durante la ejecuci�n del Hilo Receptor... ");
			e.printStackTrace();
		}
		
	}
	
}
