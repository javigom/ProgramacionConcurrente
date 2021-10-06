import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
 
public class Cliente {
 
    public static void main(String[] args) throws IOException, ClassNotFoundException {
 
    	final String HOST = "127.0.0.1";
    	final int PUERTO = 5000;
    	
    	ObjectInputStream in;
        DataOutputStream out;
         
    	Socket socket = new Socket(HOST, PUERTO);
    	
    	in = new ObjectInputStream(socket.getInputStream());
		out = new DataOutputStream(socket.getOutputStream());
		
		out.writeUTF("fichero1.txt");
		
		Fichero fichero = (Fichero) in.readObject();
		
		fichero.print();
		
		socket.close();
     
    }
 
}