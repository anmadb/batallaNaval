/*package bn;

import java.io.*;
import java.net.*;
import java.util.Random;

public class ServidorContraComputadora {
	private static final int PUERTO = 12346;
	private Tablero tableroComputadora;
	private Tablero tableroJugador;
	private Random random;

	public void iniciar() throws IOException {
		System.out.println("Servidor contra la computadora iniciado. Esperando conexión...");
		ServerSocket servidor = new ServerSocket(PUERTO);

		Socket cliente = servidor.accept();
		System.out.println("Cliente conectado desde: " + cliente.getInetAddress());

		BufferedReader entradaCliente = new BufferedReader(new InputStreamReader(cliente.getInputStream()));
		BufferedWriter salidaCliente = new BufferedWriter(new OutputStreamWriter(cliente.getOutputStream()));

		salidaCliente.write("Conexión establecida. Bienvenido al juego contra la computadora.\n");
		salidaCliente.flush();

		tableroJugador = new Tablero();
		tableroComputadora = new Tablero();
		random = new Random();

		configurarTableros(salidaCliente, entradaCliente);

		boolean turnoJugador = true;
		while (true) {
			if (turnoJugador) {

				salidaCliente.write("Tu turno. Ingresa fila,columna para disparar:\n");
				salidaCliente.flush();

				String disparo = entradaCliente.readLine();
				procesarDisparo(tableroComputadora, disparo, salidaCliente, "Jugador");
				if (tableroComputadora.todosLosBarcosHundidos()) {
					salidaCliente.write("¡Ganaste! Hundiste todos los barcos de la computadora.\n");
					salidaCliente.write("FIN_DEL_JUEGO\n");
					salidaCliente.flush();
					break;
				}
			} else {

				salidaCliente.write("Turno de la computadora...\n");
				salidaCliente.flush();

				realizarDisparoComputadora();
				if (tableroJugador.todosLosBarcosHundidos()) {
					salidaCliente.write("La computadora ha ganado. Hundió todos tus barcos.\n");
					salidaCliente.write("FIN_DEL_JUEGO\n");
					salidaCliente.flush();
					break;
				}
			}

			turnoJugador = !turnoJugador;
		}

		cliente.close();
		servidor.close();
	}

	private void configurarTableros(BufferedWriter salida, BufferedReader entrada) throws IOException {
		salida.write("Coloca tus barcos en el tablero.\n");
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

				if (tableroJugador.puedeColocarBarco(fila, columna, tamano, horizontal)) {
					tableroJugador.colocarBarco(fila, columna, tamano, horizontal);
					salida.write("Barco colocado.\n");
					salida.flush();
					colocado = true;
				} else {
					salida.write("No se puede colocar el barco aquí. Intenta de nuevo.\n");
					salida.flush();
				}
			}
		}

		for (int tamano : tamanosBarcos) {
			boolean colocado = false;
			while (!colocado) {
				int fila = random.nextInt(10);
				int columna = random.nextInt(10);
				boolean horizontal = random.nextBoolean();
				if (tableroComputadora.puedeColocarBarco(fila, columna, tamano, horizontal)) {
					tableroComputadora.colocarBarco(fila, columna, tamano, horizontal);
					colocado = true;
				}
			}
		}
	}

	private void procesarDisparo(Tablero tablero, String disparo, BufferedWriter salida, String atacante)
			throws IOException {
		String[] partes = disparo.split(",");
		int fila = Integer.parseInt(partes[0]);
		int columna = Integer.parseInt(partes[1]);
		boolean acierto = tablero.recibirDisparo(fila, columna);

		salida.write(acierto ? "¡Acierto!\n" : "Fallo.\n");
		salida.flush();
	}

	private void realizarDisparoComputadora() throws IOException {
		int fila, columna;
		boolean acierto;

		do {
			fila = random.nextInt(10);
			columna = random.nextInt(10);
			acierto = tableroJugador.recibirDisparo(fila, columna);
		} while (!acierto && tableroJugador.getTablero()[fila][columna] != Tablero.AGUA);

		System.out.println("Computadora disparó a (" + fila + "," + columna + ") y fue un "
				+ (acierto ? "acierto" : "fallo") + ".");
	}

	public static void main(String[] args) throws IOException {
		ServidorContraComputadora servidor = new ServidorContraComputadora();
		servidor.iniciar();
	}
}*/
