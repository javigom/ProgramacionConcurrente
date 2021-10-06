package cliente;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;
import java.util.concurrent.Semaphore;

import general.*;

public class OyenteServidor extends Thread {

	/** Flujo de entrada. */
	private ObjectInputStream in;
	/** Flujo de salida. */
	private ObjectOutputStream out;
	/** Cliente propietario. */
	private Cliente cliente;
	/** Semáforo para evitar imprimir el menú antes de la salida de algunos mensajes. */
	Semaphore semPrinted;
	
	/** Constructor de OyenteServidor. */
	protected OyenteServidor(Semaphore semPrinted, Cliente cliente) throws IOException {
		this.cliente = cliente;
		this.semPrinted = semPrinted;
		this.out = cliente.getOutputStream();
		this.in = cliente.getInputStream();
	}
	
	/** Método run del hilo. */
	public void run() {
		
		try {

			/** Booleano para indicar si el usuario quiere cerrar la conexión. */
			boolean continuar = true;
			
			while (continuar) {
				
				/** Leo un mensaje que he recibido. */
				Mensaje m  = (Mensaje) in.readObject();
			   
				/** Switch en función del tipo. */
				switch(m.getTipo()) {
				
				  	case CONFIRMACION_CONEXION:
				  					  		
				  		// Imprimo por consola que la conexión ha sido establecida.
				  		System.out.println("Conexión establecida entre OyenteServidor y OyenteCliente.");
				  		
				  		// Libero 1 permiso del semáforo para mostrar el menú
				  		semPrinted.release();
				  		
						break;
				  	
				  	case CONFIRMACION_LISTA_USUARIOS:
				  		
				  		// Hago un casteo al mensaje concreto para acceder a sus métodos
				  		MensajeConfirmacionListaUsuarios m1 = (MensajeConfirmacionListaUsuarios) m;
				  		
				  		// Imprimo por consola la lista de usuarios
				  		System.out.println("=========================================================================================");
						System.out.println("Lista de usuarios conectados: ");
						System.out.println("=========================================================================================");
						System.out.println("");
				  		
						// Obtengo una lista de usuarios
				  		List<String> usuarios = m1.getListaUsuarios();
				  		
				  		// Obtengo una lista de ficheros
				  		List<List<String>> ficheros = m1.getListaFicheros();
				  		
				  		// Doble bucle for para recorrer todos los ficheros de cada usuario
				  		for(int i = 0; i < usuarios.size(); i++) {
				  			System.out.print("Usuario: " + usuarios.get(i) + "\nFicheros: ");
				  			for(int j = 0;  j < ficheros.get(i).size(); j++) {
				  				System.out.print(ficheros.get(i).get(j));
				  				if(j != ficheros.get(i).size() - 1) {
				  					System.out.print(", ");
				  				}
				  			}
				  			
				  			System.out.println("");
				  			System.out.println("");
				  			System.out.println("=========================================================================================");
				  			System.out.println("");
				  		}
				  		
				  		// Libero 1 permiso del semáforo para mostrar el menú
				  		semPrinted.release();
				  		
				  		break;
				  		
				  	case EMITIR_FICHERO:
				  		
				  		// Hago un casteo al mensaje concreto para acceder a sus métodos
				  		MensajeEmitirFichero m2 = (MensajeEmitirFichero) m;
				  		
				  		// Genero un nuevo puerto aleatorio entre 5000 y 10000
				  		int nuevo_puerto =  (int) (Math.floor(Math.random()*5000)) + 5000;
				  		
				        // Envío mensaje preparado servidor con mi IP y puerto elegido
				  		out.writeObject(new MensajePreparadoClienteServidor(cliente.getClientName(), m.getOrigen(), cliente.getClientIP(), nuevo_puerto));
				  		
				    	// Creo un proceso emisor para la transmisión con el fichero solicitado
				  		(new Emisor(nuevo_puerto, m2.getFicheroSolicitado(), cliente)).start();
				  		
				  		break;
				  		
				  	case PREPARADO_SERVIDORCLIENTE:
				  		
				  		// Hago un casteo al mensaje concreto para acceder a sus métodos
				  		MensajePreparadoServidorCliente m3 = (MensajePreparadoServidorCliente) m;
				  		
				        // Creo proceso receptor con ip y puerto para la transmisión
				  		(new Receptor(m3.getIP(), m3.getPuerto(), semPrinted)).start();
				  		
				  		break;
				  		
				  	case CONFIRMACION_CERRAR_CONEXION:
				  		
				  		// Imprimo por consola que la conexión ha finalizado
				  		System.out.println("Conexión cerrada con éxito.");
				  		
				  		// Condición de salida del bucle a false.
				  		continuar = false;
				  		
				  		break;
				  	
				  	case FICHERO_INEXISTENTE:
				  		
				  		// Imprimo por pantalla que el fichero no ha podido ser localizado por el servidor
				  		System.err.println("El fichero no existe en la base de datos. Comprueba que exista un fichero con ese nombre. ");
				  		
				  		// Libero 1 permiso del semáforo para mostrar el menú
				  		semPrinted.release();
				  		
				  		break;
				default:
					break;
				  
			  }
   
			}
		   
		} catch (ClassNotFoundException | IOException e) {
			System.err.println("Ha ocurrido un error durante la ejecución en el Hilo Oyente Servidor... ");
			e.printStackTrace();
		}
	}
}
