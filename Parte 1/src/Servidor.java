import java.io.DataInputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
 
public class Servidor {
 
	@SuppressWarnings("resource")
	public static void main(String[] args) throws IOException, InterruptedException {
 
    	ServerSocket servidor = null;
        Socket socket = null;
        final int PUERTO = 5000;

    	servidor = new ServerSocket(PUERTO);
    	System.out.println("El servidor se ha iniciado");
    	
    	while(true) {
    		socket = servidor.accept();
    		atiendePeticion(socket);
    	}
    }
	
	public static void atiendePeticion(Socket socket) throws IOException, InterruptedException {
		
		Thread th = new Thread() {
			public void run() {
				
				System.out.println("Cliente conectado");
				
				try {
					DataInputStream in = new DataInputStream(socket.getInputStream());
					ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
					String nombreFichero = in.readUTF();
					System.out.println("El cliente ha solicitado el fichero: " + nombreFichero);				
					out.writeObject(new Fichero(nombreFichero, "C:/Users/Javier/eclipse-workspace/Practica5/src/" + nombreFichero));
					socket.close();
					
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				System.out.println("Cliente descontectado");
			}
		};
		
		th.run();
		th.join();
      
	}
 
}
