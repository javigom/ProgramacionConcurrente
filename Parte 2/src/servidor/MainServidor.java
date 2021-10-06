package servidor;

import java.io.IOException;

public class MainServidor {

	public static void main(String[] args) {

		try {
			Servidor s = new Servidor();
			s.iniciaServidor();
		} catch (IOException e) {
			System.err.println("Ha ocurrido un error durante la ejecución del Hilo Servidor... ");
			e.printStackTrace();
		}

	}
}
