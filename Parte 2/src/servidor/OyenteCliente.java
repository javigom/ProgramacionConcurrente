package servidor;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import general.*;

public class OyenteCliente extends Thread {

	/** Socket utilizado en la comunicación. */
	private Socket socket = null;
	/** Flujo de entrada. */
	private ObjectInputStream in;
	/** Flujo de salida. */
	private ObjectOutputStream out;
	/** Servidor propietario. */
	private Servidor server;
	
	/** Constructor de OyenteCliente. */
	protected OyenteCliente(Socket s, Servidor server) throws IOException {
		this.socket = s;
		this.server = server;		
	}
	
	/** Método run del hilo. */
	public void run() {

		try {
	
			// Obtengo los flujos de la comunicación
			this.out = new ObjectOutputStream(this.socket.getOutputStream());
			this.in = new ObjectInputStream(this.socket.getInputStream());

			// Variable de salida para finalizar la conexión
			boolean continuar = true;
			
			while (continuar) {
				
				/** Leo un mensaje que he recibido. */
				Mensaje m = (Mensaje) in.readObject();
				
				/** Switch en función del tipo. */
				switch(m.getTipo()) {
				
			   		case CONEXION:
			  		
			   			// Hago un casteo al mensaje concreto para acceder a sus métodos
			   			MensajeConexion m1 = (MensajeConexion) m;
			   			
				  		// Genero un nuevo usuario con los datos proporcionados
			   			Usuario newUser = new Usuario(m1.getOrigen(), m1.getUserIP(),  m1.getFicheros());
			   			
			   			// Añado al usuario a mi base de datos
			   			server.addUser(newUser, out, in);
			   			
				  		// Envío mensaje de confirmación al usuario
						out.writeObject(new MensajeConfirmacionConexion("Servidor", m.getOrigen()));
						out.flush();
						
						// Imprimo mensaje por consola 
			   			System.out.println("Conexion establecida con el usuario " + newUser.getUserName() + ".");
			   			
						break;
			  	
			   		case LISTA_USUARIOS:
			  				   			
				  		// Envío un nuevo mensaje con las listas de usuarios y ficheros al cliente
			   			out.writeObject(new MensajeConfirmacionListaUsuarios("Servidor", m.getOrigen(), server.getUserList(), server.getFileList()));
			   			out.flush();
			   			
				  		break;
			  		
			   		case CERRAR_CONEXION:
			  		
			   			// Hago un casteo al mensaje concreto para acceder a sus métodos
			   			MensajeCerrarConexion m2 = (MensajeCerrarConexion) m;
			   			
				  		// Elimino al usuario de la base de datos
			   			server.deleteUser(m2.getOrigen());
			   			
						// Envío mensaje de confirmación al usuario para cerrar la sesión
			   			out.writeObject(new MensajeConfirmacionCerrarConexion("Servidor", m.getOrigen()));
			   			out.flush();
			   			
			   			// Imprimo mensaje por consola 
			   			System.out.println("Conexion cerrada con el usuario " + m2.getOrigen() + ".");
			   			
			   			// Variable de salida del bucle
			   			continuar = false;
			   			
				  		break;
			  		
			   		case PEDIR_FICHERO:
			  		
			   			// Hago un casteo al mensaje concreto para acceder a sus métodos
			   			MensajePedirFichero m3 = (MensajePedirFichero) m;
			   			
				  		// Obtengo el nombre del fichero solicitado
			   			String fichero_solicitado = m3.getNombreFichero();
			   			
			   			// Busco el nombre del cliente que contiene dicho fichero
			   			String identificador = server.getUserNamefromNameFile(fichero_solicitado);
			   			
			   			// Si el fichero existe en mi base de datos
			   			if(identificador != null) {
			   				
			   				// Obtengo flujo de salida para el usuario que contiene el fichero
			   				ObjectOutputStream out2 = server.getUserOutputStream(identificador);
			   				
			   				// Envio mensaje emitir fichero
			   				out2.writeObject(new MensajeEmitirFichero(m.getOrigen(), identificador, fichero_solicitado));
			   				out2.flush();
			   				
			   			}
			   			
			   			// Si el fichero no existe
			   			else {
			   				
			   				// Envío mensaje fichero inexistente al usuario solicitante
			   				out.writeObject(new MensajeFicheroInexistente("Servidor", m.getOrigen()));
			   				out.flush();
			   				
			   				// Imprimo el error por la consola
			   				System.err.println("Se ha solicitado un fichero inexistente con nombre '" + m3.getNombreFichero() + "'. Se ignora la petición. ");
			   			}
			   			
						
				  		break;
			  		
			   		case PREPARADO_CLIENTESERVIDOR:
			  		
			   			// Hago un casteo al mensaje concreto para acceder a sus métodos
			   			MensajePreparadoClienteServidor m4 = (MensajePreparadoClienteServidor) m;		 
			   			
			   			// Obtengo flujo de salida del cliente que solicitó el fichero
			   			ObjectOutputStream out2 = server.getUserOutputStream(m.getDestino());
			   			
			   			// Envío mensaje para iniciar la transmisión entre clientes
			   			out2.writeObject(new MensajePreparadoServidorCliente(m.getOrigen(), m.getDestino(), m4.getIP(), m4.getPuerto()));
			   			out2.flush();
			   			
			   			break;
			  		
			   		default:
			   			break;
				  
			  }
			}
			
		} catch (IOException | ClassNotFoundException | InterruptedException e) {
			System.err.println("Ha ocurrido un error durante la ejecución en el Hilo Oyente Cliente... ");
			e.printStackTrace();
		}

	}
	
}

