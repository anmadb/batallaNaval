package bn;

import java.io.IOException;
import java.util.Scanner;

public class JuegoBatallaNaval {
	private Scanner scanner;

	private static final int PUERTO_MULTIJUGADOR = 12345;
	private static final int PUERTO_CONTRA_COMPUTADORA = 12346;

	public JuegoBatallaNaval() {
		scanner = new Scanner(System.in);
	}

	public void iniciarJuego() throws IOException {
		System.out.println("¡Bienvenido a Batalla Naval!");
		System.out.println("Seleccione una opción:");
		System.out.println("1. Jugar contra la computadora");
		System.out.println("2. Iniciar y jugar en modo multijugador (primer jugador)");
		System.out.println("3. Conectar segundo jugador en modo multijugador");
		System.out.print("Opción: ");

		int opcion = scanner.nextInt();
		scanner.nextLine();

		switch (opcion) {
		case 1 -> iniciarJuegoContraComputadora();
		case 2 -> iniciarServidorYPrimerJugadorMultijugador();
		case 3 -> conectarSegundoJugadorMultijugador();
		default -> System.out.println("Opción no válida. Por favor, elija una opción entre 1 y 3.");
		}
	}

	private void iniciarJuegoContraComputadora() throws IOException {
		System.out.println("Iniciando el servidor y cliente para jugar contra la computadora...");

		Thread servidorThread = new Thread(() -> {
			try {
				ServidorContraComputadora servidor = new ServidorContraComputadora();
				servidor.iniciar();
			} catch (IOException e) {
				System.err.println("Error al iniciar el servidor contra la computadora: " + e.getMessage());
			}
		});
		servidorThread.start();

		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}

		ClienteBatallaNaval cliente = new ClienteBatallaNaval();
		cliente.conectarServidor("localhost", PUERTO_CONTRA_COMPUTADORA);
	}

	private void iniciarServidorYPrimerJugadorMultijugador() throws IOException {
		System.out.println("Iniciando el servidor multijugador y conectando al primer jugador...");

		Thread servidorThread = new Thread(() -> {
			try {
				ServidorBatallaNaval servidor = new ServidorBatallaNaval();
				servidor.iniciar();
			} catch (IOException e) {
				System.err.println("Error al iniciar el servidor multijugador: " + e.getMessage());
			}
		});
		servidorThread.start();

		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}

		ClienteBatallaNaval cliente = new ClienteBatallaNaval();
		System.out.print("Ingrese la dirección IP del servidor multijugador: ");
		String ip = scanner.nextLine();
		
		cliente.conectarServidor(ip, PUERTO_MULTIJUGADOR);
	}

	private void conectarSegundoJugadorMultijugador() throws IOException {
		System.out.print("Ingrese la dirección IP del servidor multijugador: ");
		String ip = scanner.nextLine();

		ClienteBatallaNaval cliente = new ClienteBatallaNaval();
		cliente.conectarServidor(ip, PUERTO_MULTIJUGADOR);
	}

	public static void main(String[] args) throws IOException {
		JuegoBatallaNaval juego = new JuegoBatallaNaval();
		juego.iniciarJuego();
	}
}
