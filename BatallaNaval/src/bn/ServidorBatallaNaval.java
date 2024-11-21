package bn;

import java.io.*;
import java.net.*;

public class ServidorBatallaNaval {
	private static final int PUERTO = 12345;

	public void iniciar() throws IOException {
		System.out.println("Servidor iniciado. Esperando conexiones...");
		ServerSocket servidor = new ServerSocket(PUERTO);

		Socket jugador1 = servidor.accept();
		System.out.println("Jugador 1 conectado desde: " + jugador1.getInetAddress());

		Socket jugador2 = servidor.accept();
		System.out.println("Jugador 2 conectado desde: " + jugador2.getInetAddress());

		BufferedReader entradaJugador1 = new BufferedReader(new InputStreamReader(jugador1.getInputStream()));
		BufferedWriter salidaJugador1 = new BufferedWriter(new OutputStreamWriter(jugador1.getOutputStream()));

		BufferedReader entradaJugador2 = new BufferedReader(new InputStreamReader(jugador2.getInputStream()));
		BufferedWriter salidaJugador2 = new BufferedWriter(new OutputStreamWriter(jugador2.getOutputStream()));

		salidaJugador1.write("Conexión establecida. Eres el Jugador 1.\n");
		salidaJugador1.flush();
		salidaJugador2.write("Conexión establecida. Eres el Jugador 2.\n");
		salidaJugador2.flush();

		Tablero tableroJugador1 = new Tablero();
		Tablero tableroJugador2 = new Tablero();

		configurarTableros(tableroJugador1, salidaJugador1, entradaJugador1, "Jugador 1");
		configurarTableros(tableroJugador2, salidaJugador2, entradaJugador2, "Jugador 2");

		boolean turnoJugador1 = true;
		while (true) {
			if (turnoJugador1) {
				salidaJugador1.write("Tu turno. Ingresa fila,columna para disparar:\n");
				salidaJugador1.flush();
				String disparo = entradaJugador1.readLine();
				procesarDisparo(tableroJugador2, disparo, salidaJugador1, salidaJugador2, "Jugador 1");
				if (tableroJugador2.todosLosBarcosHundidos()) {
					salidaJugador1.write("FIN_DEL_JUEGO\n");
					salidaJugador1.flush();
					salidaJugador2.write("FIN_DEL_JUEGO_PERDIDA\n");
					salidaJugador2.flush();
					
					break;
				}
			} else {
				salidaJugador2.write("Tu turno. Ingresa fila,columna para disparar:\n");
				salidaJugador2.flush();
				String disparo = entradaJugador2.readLine();
				procesarDisparo(tableroJugador1, disparo, salidaJugador2, salidaJugador1, "Jugador 2");
				if (tableroJugador1.todosLosBarcosHundidos()) {
					salidaJugador2.write("FIN_DEL_JUEGO\n");
					salidaJugador2.flush();
					salidaJugador1.write("FIN_DEL_JUEGO_PERDIDA\n");
					salidaJugador1.flush();
					
					break;
				}
			}
			turnoJugador1 = !turnoJugador1;
		}

		jugador1.close();
		jugador2.close();
		servidor.close();
	}

	private void configurarTableros(Tablero tablero, BufferedWriter salida, BufferedReader entrada, String jugador)
			throws IOException {
		salida.write(jugador + ", coloca tus barcos en el tablero.\n");
		salida.flush();
		int[] tamanosBarcos = { 2, 1 };

		for (int tamano : tamanosBarcos) {
			boolean colocado = false;
			while (!colocado) {
				salida.write("Coloca un barco de tamaño " + tamano + ". Ingresa fila,columna,orientación (H o V):\n");
				salida.flush();
				String[] partes = entrada.readLine().split(",");
				int fila = Integer.parseInt(partes[0]);
				int columna = Integer.parseInt(partes[1]);
				boolean horizontal = partes[2].equalsIgnoreCase("H");
				if (tablero.puedeColocarBarco(fila, columna, tamano, horizontal)) {
					tablero.colocarBarco(fila, columna, tamano, horizontal);
					colocado = true;
					salida.write("Barco colocado.\n");
					salida.flush();
				} else {
					salida.write("No se puede colocar el barco aquí. Intenta de nuevo.\n");
					salida.flush();
				}
			}
		}
	}

	private boolean procesarDisparo(Tablero tablero, String disparo, BufferedWriter atacante, BufferedWriter defensor,
			String jugadorAtacante) throws IOException {
		String[] partes = disparo.split(",");
		int fila = Integer.parseInt(partes[0]);
		int columna = Integer.parseInt(partes[1]);
		boolean acierto = tablero.recibirDisparo(fila, columna);
		atacante.write(acierto ? "¡Acierto!\n" : "Fallo.\n");
		atacante.flush();
		defensor.write("El " + jugadorAtacante + " disparó a " + fila + "," + columna + " y fue un "
				+ (acierto ? "¡acierto!" : "fallo.") + "\n");
		defensor.flush();

		defensor.write("DISPARO_ENEMIGO," + fila + "," + columna + "," + (acierto ? "X" : "O") + "\n");
		defensor.flush();
		return acierto;
	}
}
