package cliente;

import java.io.IOException;

public class MainCliente {

	public static void main(String[] args) {
		
		Cliente c;
		try {
			
			c = new Cliente();
			c.iniciaCliente();
			
		} catch (IOException | InterruptedException e) {
			System.err.println("Ha ocurrido un error durante la ejecución del Hilo Cliente... ");
			e.printStackTrace();
		}
		
	}

}
