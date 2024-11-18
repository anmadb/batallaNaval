package bn;

import java.io.*;
import java.net.*;
import java.util.Scanner;

public class ClienteBatallaNaval {

	private Tablero tableroPropio;
	private Tablero tableroEnemigo;
	private BufferedWriter salida;
	private BufferedReader entrada;
	private Scanner scanner;

	public ClienteBatallaNaval() {
		scanner = new Scanner(System.in);
	}


	void conectarServidor(String ip, int puerto) throws IOException {
		Socket socket = new Socket(ip, puerto);
		System.out.println("Conectado al servidor en " + ip + ":" + puerto);

		entrada = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		salida = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

		tableroPropio = new Tablero();
		tableroEnemigo = new Tablero();

		configurarBarcos();
		escucharServidor();
	}

	private void configurarBarcos() throws IOException {
		System.out.println(entrada.readLine());
		int[] tamanosBarcos = {2, 1 };

		for (int tamano : tamanosBarcos) {
			boolean colocado = false;
			while (!colocado) {
				System.out.println("Coloca un barco de tamaño " + tamano);
				tableroPropio.mostrarTablero();
				System.out.print("Ingresa fila,columna,orientación (H o V): ");
				String[] entradaUsuario = scanner.nextLine().split(",");
				int fila = Integer.parseInt(entradaUsuario[0]);
				int columna = Integer.parseInt(entradaUsuario[1]);
				boolean horizontal = entradaUsuario[2].equalsIgnoreCase("H");

				if (tableroPropio.puedeColocarBarco(fila, columna, tamano, horizontal)) {
					tableroPropio.colocarBarco(fila, columna, tamano, horizontal);
					salida.write(fila + "," + columna + "," + (horizontal ? "H" : "V") + "\n");
					salida.flush();
					colocado = true;
				} else {
					System.out.println("No se puede colocar el barco aquí. Intenta de nuevo.");
				}
			}
		}
	}

	private void escucharServidor() throws IOException {
		while (true) {
			String mensaje = entrada.readLine();
			System.out.println(mensaje);

			if (mensaje.equals("FIN_DEL_JUEGO")) {
				System.out.println("El juego ha terminado. Cerrando conexión...");
				break;
			}else if(mensaje.equals("FIN_DEL_JUEGO_PERDIDA")) {
				System.out.println("El juego ha terminado. Cerrando conexión...");
				break;
			}

			if (mensaje.contains("Ingresa fila,columna para disparar")) {
				realizarDisparo();
			} else if (mensaje.startsWith("DISPARO_ENEMIGO")) {
				String[] partes = mensaje.split(",");
				int fila = Integer.parseInt(partes[1]);
				int columna = Integer.parseInt(partes[2]);
				boolean acierto = partes[3].equals("X");
				tableroPropio.marcarDisparoEnemigo(fila, columna, acierto);
			}

			System.out.println("Tu tablero:");
			tableroPropio.mostrarTablero();
			System.out.println("Tablero del enemigo (sin barcos):");
			tableroEnemigo.mostrarTablero();
		}

		entrada.close();
		salida.close();
	}

	private void realizarDisparo() throws IOException {
		System.out.print("Ingresa tu disparo (formato: fila,columna): ");
		String disparo = scanner.nextLine();
		salida.write(disparo + "\n");
		salida.flush();

		String[] partes = disparo.split(",");
		int fila = Integer.parseInt(partes[0]);
		int columna = Integer.parseInt(partes[1]);

		tableroEnemigo.actualizarConFallo(fila, columna);
	}
}
