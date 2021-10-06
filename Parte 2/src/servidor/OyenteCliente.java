package servidor;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import general.*;

public class OyenteCliente extends Thread {

	/** Socket utilizado en la comunicaci�n. */
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
	
	/** M�todo run del hilo. */
	public void run() {

		try {
	
			// Obtengo los flujos de la comunicaci�n
			this.out = new ObjectOutputStream(this.socket.getOutputStream());
			this.in = new ObjectInputStream(this.socket.getInputStream());

			// Variable de salida para finalizar la conexi�n
			boolean continuar = true;
			
			while (continuar) {
				
				/** Leo un mensaje que he recibido. */
				Mensaje m = (Mensaje) in.readObject();
				
				/** Switch en funci�n del tipo. */
				switch(m.getTipo()) {
				
			   		case CONEXION:
			  		
			   			// Hago un casteo al mensaje concreto para acceder a sus m�todos
			   			MensajeConexion m1 = (MensajeConexion) m;
			   			
				  		// Genero un nuevo usuario con los datos proporcionados
			   			Usuario newUser = new Usuario(m1.getOrigen(), m1.getUserIP(),  m1.getFicheros());
			   			
			   			// A�ado al usuario a mi base de datos
			   			server.addUser(newUser, out, in);
			   			
				  		// Env�o mensaje de confirmaci�n al usuario
						out.writeObject(new MensajeConfirmacionConexion("Servidor", m.getOrigen()));
						out.flush();
						
						// Imprimo mensaje por consola 
			   			System.out.println("Conexion establecida con el usuario " + newUser.getUserName() + ".");
			   			
						break;
			  	
			   		case LISTA_USUARIOS:
			  				   			
				  		// Env�o un nuevo mensaje con las listas de usuarios y ficheros al cliente
			   			out.writeObject(new MensajeConfirmacionListaUsuarios("Servidor", m.getOrigen(), server.getUserList(), server.getFileList()));
			   			out.flush();
			   			
				  		break;
			  		
			   		case CERRAR_CONEXION:
			  		
			   			// Hago un casteo al mensaje concreto para acceder a sus m�todos
			   			MensajeCerrarConexion m2 = (MensajeCerrarConexion) m;
			   			
				  		// Elimino al usuario de la base de datos
			   			server.deleteUser(m2.getOrigen());
			   			
						// Env�o mensaje de confirmaci�n al usuario para cerrar la sesi�n
			   			out.writeObject(new MensajeConfirmacionCerrarConexion("Servidor", m.getOrigen()));
			   			out.flush();
			   			
			   			// Imprimo mensaje por consola 
			   			System.out.println("Conexion cerrada con el usuario " + m2.getOrigen() + ".");
			   			
			   			// Variable de salida del bucle
			   			continuar = false;
			   			
				  		break;
			  		
			   		case PEDIR_FICHERO:
			  		
			   			// Hago un casteo al mensaje concreto para acceder a sus m�todos
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
			   				
			   				// Env�o mensaje fichero inexistente al usuario solicitante
			   				out.writeObject(new MensajeFicheroInexistente("Servidor", m.getOrigen()));
			   				out.flush();
			   				
			   				// Imprimo el error por la consola
			   				System.err.println("Se ha solicitado un fichero inexistente con nombre '" + m3.getNombreFichero() + "'. Se ignora la petici�n. ");
			   			}
			   			
						
				  		break;
			  		
			   		case PREPARADO_CLIENTESERVIDOR:
			  		
			   			// Hago un casteo al mensaje concreto para acceder a sus m�todos
			   			MensajePreparadoClienteServidor m4 = (MensajePreparadoClienteServidor) m;		 
			   			
			   			// Obtengo flujo de salida del cliente que solicit� el fichero
			   			ObjectOutputStream out2 = server.getUserOutputStream(m.getDestino());
			   			
			   			// Env�o mensaje para iniciar la transmisi�n entre clientes
			   			out2.writeObject(new MensajePreparadoServidorCliente(m.getOrigen(), m.getDestino(), m4.getIP(), m4.getPuerto()));
			   			out2.flush();
			   			
			   			break;
			  		
			   		default:
			   			break;
				  
			  }
			}
			
		} catch (IOException | ClassNotFoundException | InterruptedException e) {
			System.err.println("Ha ocurrido un error durante la ejecuci�n en el Hilo Oyente Cliente... ");
			e.printStackTrace();
		}

	}
	
}

