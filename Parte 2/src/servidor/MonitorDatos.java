package servidor;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import general.Usuario;

public class MonitorDatos {
	
    /** Tabla hash con los usuarios conectados. 
     * Su clave es el nombre del usuario (único) y su valor es un objeto de tipo usuario. */
    private Map<String, Usuario> usuarios_conectados;
    /** Tabla hash con los flujos de salida de cada usuario. */
    private Map<String, ObjectOutputStream> flujos_salida;
    /** Tabla hash con los flujos de entrada de cada usuario. */
    private Map<String, ObjectInputStream> flujos_entrada;
    
    /** Enteros para la concurrencia. */
    private int numLectores = 0, numEscritores = 0;
    /** Lock utilizado en el monitor. */
    private final Lock lock = new ReentrantLock(true);
    /** Cola de lectores. */
    private final Condition colaLectores = lock.newCondition();
    /** Cola de escritores. */
    private final Condition colaEscritores = lock.newCondition();
    
    /** Constructor del monitor. */
    protected MonitorDatos() {
    	
    	// Inicializo las tablas
    	usuarios_conectados = new HashMap<String, Usuario>();
    	flujos_salida = new HashMap<String, ObjectOutputStream>();
    	flujos_entrada = new HashMap<String, ObjectInputStream>();
    }
	
    /** Método privado para solicitar la lectura. */
    private void solicitarLectura() throws InterruptedException {
    	
    	// Adquiero el lock
    	lock.lock();
    	
    	// Si hay algun escritor dentro, tengo que esperar
    	while(numEscritores > 0) {
    		colaLectores.wait();
    	}
    	
    	// Incremento el número de lectores
    	numLectores++;
    	
    	// Suelto el lock
    	lock.unlock();
    }
    
    /** Método privado para liberar la lectura. */
    private synchronized void liberarLectura() {
    	
    	// Adquiero el lock
    	lock.lock();
    	
    	// Decremento el número de lectores
    	numLectores--;
    	
    	// Si ya no tengo ningún lector, despierto a los escritores
    	if(numLectores == 0) {
    		colaEscritores.signal();
    	}
    	
    	// Suelto el lock
    	lock.unlock();
    }
    
    /** Método privado para solicitar la escritura. */
    private synchronized void solicitarEscritura() throws InterruptedException {
    	
    	// Adquiero el lock
    	lock.lock();
    	
    	// Mientras que haya lectores o escritores dentro, espero
    	while(numLectores > 0 || numEscritores > 0) {
    		colaEscritores.wait();
    	}
    	
    	// Incremento el número de escritores (nunca superará 1)
    	numEscritores++;
    	
    	// Suelto el lock
    	lock.unlock();
    }
    
    /** Método privado para liberar la escritura. */
    private synchronized void liberarEscritura() {
    	
    	// Adquiero el lock
    	lock.lock();
    	
    	// Decremento el número de escritores
    	numEscritores--;
    	
    	// Despierto a lectores y escritores puesto que no puedo comprobar si no hay mas escritores esperando
		colaLectores.signal();
		colaEscritores.signal();
		
		// Suelto el lock
		lock.unlock();
    }
    
    /** Añade un usuario a la base de datos. */
    protected void addUser(Usuario usuario, ObjectOutputStream out, ObjectInputStream in) throws InterruptedException {
    	
    	solicitarEscritura();
    	usuarios_conectados.put(usuario.getUserName(), usuario);
		flujos_salida.put(usuario.getUserName(), out);
		flujos_entrada.put(usuario.getUserName(), in);
		liberarEscritura();
    }
    
    /** Elimino un usuario de la base de datos dado su identificador. */
    protected void deleteUser(String userName) throws InterruptedException {
    	
    	solicitarEscritura();
		usuarios_conectados.remove(userName);
		flujos_salida.remove(userName);
		flujos_entrada.remove(userName);
		liberarEscritura();
	}
    
    /** Devuelve la lista de usuarios conectados. */
	protected List<String> getUserList() throws InterruptedException{
		
		solicitarLectura();
		
		// Creo una nueva lista 
		List<String> usuarios = new ArrayList<String>();
		
		// Recorro la tabla hash para obtener todos los nombres
		for (Usuario valor : usuarios_conectados.values()) {
			usuarios.add(valor.getUserName());
		}
		
		liberarLectura();
		
		return usuarios;
	}
	
	/** Devuelve la lista de ficheros disponibles. */
	protected List<List<String>> getFileList() throws InterruptedException {
		
		solicitarLectura();
		
		// Creo una nueva lista
		List<List<String>> ficheros_usuarios = new ArrayList<List<String>>();
		
		// Recorro la tabla hash
		for (Usuario valor : usuarios_conectados.values()) {
			
			List<String> lista_ficheros = new ArrayList<String>();
			
			// Para cada usuario, recorro sus ficheros disponibles
			for(String fichero : valor.getFicheros()) {
				lista_ficheros.add(fichero);
			}
			
			ficheros_usuarios.add(lista_ficheros);
			
		}
		
		liberarLectura();
		
		return ficheros_usuarios;
	}
	
	/** Obtiene el identificador de un usuario a partir de un nombre de fichero. */
	protected String getUserNamefromFile(String nombre_fichero) throws InterruptedException {
		
		solicitarLectura();
		
		// Inicializo el identificador
		String identificador = null;

		// Recorro la tabla hash
		for(Usuario user : usuarios_conectados.values()) {
			
			// Recorro la lista de los ficheros de cada usuario
			for(String fichero : user.getFicheros()) {
				
				// Si lo contiene en su lista de ficheros, me quedo con el usuario
				if(nombre_fichero.equals(fichero)) {
					identificador = user.getUserName();
					break;
				}
			}
			
			// Si ya lo tengo, salgo del bucle
			if(identificador != null) {
				break;
			}
		}
		
		liberarLectura();
		
		return identificador;
	}
	
	/** Devuelve el flujo de salida de un usuario concreto. 
	 * @throws InterruptedException */
	protected synchronized ObjectOutputStream getUserOutputStream(String userName) throws InterruptedException {
		
		solicitarLectura();
		ObjectOutputStream flujo = flujos_salida.get(userName);
		liberarLectura();
		
		return flujo;
	}
	
	/** Devuelve el flujo de entrada de un usuario concreto. 
	 * @throws InterruptedException */
	protected synchronized ObjectInputStream getUserInputStream(String userName) throws InterruptedException {
		
		solicitarLectura();
		ObjectInputStream flujo = flujos_entrada.get(userName);
		liberarLectura();
		
		return flujo;
	}
}
