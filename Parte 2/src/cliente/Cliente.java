package cliente;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.Semaphore;

import general.*;
 
public class Cliente {
 
	/** String con la IP del servidor, com�n para todos los clientes. */
	private final static String serverIP = "127.0.0.1";
	/** Entero con el puerto que utilizan los sockets. */
	private final static int port = 5000;
	
	/** Socket que se utiliza en la comunicaci�n. */
	private Socket socket;
	/** String con el nombre del cliente. */
	private String clientName;
	/** String con la IP del cliente. */
	private String clientIP;
	
	/** Lista con los nombres de los ficheros que contiene el cliente. */
	private List<String> ficheros = new ArrayList<String>();
	/** Flujo de salida. */
	private ObjectOutputStream out;
	/** Flujo de entrada. */
	private ObjectInputStream in;
	
	/** Scanner utilizado para la lectura de consola. */
	private static Scanner scanner = new Scanner(System.in);
	/** Sem�foro utilizado para retrasar la impresi�n del men� de opciones. */
	private Semaphore semPrinted;
	
	/** Constructor del cliente. Inicializa las variables principales. */
	protected Cliente() throws UnknownHostException, IOException {
		this.socket = new Socket(serverIP, port);
		this.out = new ObjectOutputStream(socket.getOutputStream());
		this.in = new ObjectInputStream(socket.getInputStream());
		this.semPrinted = new Semaphore(0, true);
	}
	
	/** Inicia el cliente. */
	protected void iniciaCliente() throws InterruptedException, IOException {

		// M�todo para preguntar el nombre
		preguntaNombre();

		// Inicia un nuevo hilo para comunicarse con el servidor 
		Thread os = new OyenteServidor(semPrinted, this);
		os.start();
		
		// Mira que ficheros tiene disponibles para compartir
		leeFicheros();
		
		// Lanza un mensaje para avisar al servidor de que quiere establecer la conexi�n
		MensajeConexion mensaje_enviar = new MensajeConexion(clientName, "Servidor", clientIP, new ArrayList<String>(ficheros));
		out.writeObject(mensaje_enviar);
		out.flush();
		
		// Opci�n utilizada en el men�
		int opcion;
		
		do {
			
			// Espero a que se muestre el mensaje solicitado en OyenteServidor
			semPrinted.acquire();
			
			// Muestra el men� y devuelve una opci�n v�lida
			opcion = menuOpciones();
			
			// Opci�n para ver los usuarios conectados y sus ficheros
			if(opcion == 1) {
				
				System.out.println(" -> Has elegido ver los usuarios online y sus reespectivos ficheros.\n");
				
				// Mando el mensaje al servidor
				out.writeObject(new MensajeListaUsuarios(clientName, "Servidor"));
				out.flush();
			}
			
			// Opci�n para descargar un fichero
			else if(opcion == 2) {
				
				System.out.println(" -> Has elegido solicitar un fichero. Por favor, escribe a continuaci�n el fichero deseado.");
				
				// Pregunto el nombre del fichero
				System.out.print(" >> Nombre del fichero: ");
				String nombre_fichero = scanner.next();
				System.out.println("");
				
				// Mando el mensaje al servidor
				out.writeObject(new MensajePedirFichero(clientName, "Servidor", nombre_fichero));
				out.flush();

			}
			
			// Opci�n para salir y cerrar sesi�n
			else {
				
				System.out.println(" -> Has elegido salir.\n");
				
				// Mando el mensaje al servidor
				out.writeObject(new MensajeCerrarConexion(clientName, "Servidor"));
				out.flush();
			}
			
		} while(opcion != 0);
		
		// Espero a que termine el hilo OyenteServidor
		os.join();
		
		// Cierro el scanner y el socket
		scanner.close();
		socket.close();
			
	}
	
	/** M�todo para preguntar el nombre al  usuario y darle la bienvenida. */
	private void preguntaNombre() throws UnknownHostException {
		
		System.out.print("Introduce tu nombre: ");
		String input = scanner.nextLine();
		
		// File para comprobar si exxiste la ruta con el nombre de usuario proporcionado
		File f = null;
		
		do {
			
			// Creo un nuevo fichero
			f = new File(System.getProperty("user.dir") + "/usuarios/" + input + "/ficheros.txt");
			
			// Si no existe, muestro el error
			if(!f.exists()) {
				System.out.println("");
				System.out.println("Error. No existe ninguna ruta para ese nombre de usaurio en " + System.getProperty("user.dir") + "/usuarios/" );
				System.out.print("Introduce tu nombre: ");
				input = scanner.nextLine();
			}
					
		} while(!f.exists());
		
		// Almaceno el nombre del usuario y su IP
		this.clientName = input;
		InetAddress address = InetAddress.getLocalHost();
		this.clientIP = address.getHostAddress();
		System.out.println("Bienvenido/a " + this.clientName + ", tu direcci�n IP es " + this.clientIP);
	}
	
	/** M�todo para leer que ficheros tiene disponible. */
	private void leeFicheros() throws IOException {

		// Ruta donde se alojan los ficheros
	     FileReader fr = new FileReader (new File(System.getProperty("user.dir") + "/usuarios/" + this.clientName + "/ficheros.txt"));
	     BufferedReader br = new BufferedReader(fr);
         String linea;
         while((linea=br.readLine())!=null)
            this.ficheros.add(linea);
	}
		
	/** M�todo para mostrar el men� con las opciones disponibles y hacer que el usuario elija una opci�n v�lida. */
	private int menuOpciones() {
		
		// Muestra el men�
		System.out.println("");
		System.out.println("-----------------------------------------------------------------------------------------");
		System.out.println("| �Qu� acci�n deseas realizar?								|");
		System.out.println("| 											|");
		System.out.println("| 1 - Consultar el nombre de los usuarios conectados y sus ficheros disponibles.	|");
		System.out.println("| 2 - Descargar un fichero.								|");
		System.out.println("| 											|");
		System.out.println("| 0 - Salir.										|");
		System.out.println("| 											|");
		System.out.println("-----------------------------------------------------------------------------------------");
		System.out.print(" >> Opci�n: ");
		
		int opcion = 0;
		Boolean correcto;
		
		do {
			correcto = true;
			try {
				 opcion = Integer.parseInt(scanner.next()); 
			} catch(NumberFormatException e) {
				correcto = false;
			}
			
			// Si no es un entero o no est� entre los valores deseados
			if(!correcto || opcion < 0 || opcion > 2) {
				System.out.println("\nError, tienes que introducir un entero entre 0 y 2.");
				System.out.print(" >> Opci�n: ");
			}
			
		} while(!correcto || opcion < 0 || opcion > 2);

		System.out.println("");
		
		return opcion;
	}

	/** Devuelve la IP del cliente. */
	protected String getClientIP() {
		return this.clientIP;
	}
	
	/** Devuelve el nombre del cliente. */
	protected String getClientName() {
		return this.clientName;
	}
	
	/** Devuelve el flujo de entrada. */
	protected ObjectInputStream getInputStream() {
		return in;
	}
	
	/** Devuelve el flujo de salida. */
	protected ObjectOutputStream getOutputStream() {
		return out;
	}
	
}