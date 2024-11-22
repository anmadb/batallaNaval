package bn;

import java.io.*;
import java.net.*;
import java.util.Scanner;

public class ClienteBatallaNaval {

    private Tablero tableroPropio; // Tablero donde se colocan los barcos del jugador
    private Tablero tableroEnemigo; // Tablero donde se registran los disparos al enemigo
    private BufferedWriter salida; // Para enviar datos al servidor
    private BufferedReader entrada; // Para recibir datos del servidor
    private Scanner scanner; // Para leer la entrada del usuario

    // Constructor que inicializa el scanner
    public ClienteBatallaNaval() {
        scanner = new Scanner(System.in);
    }

    // Método para conectar al servidor
    void conectarServidor(String ip, int puerto) throws IOException {
        Socket socket = new Socket(ip, puerto); // Conexión al servidor
        System.out.println("Conectado al servidor en " + ip + ":" + puerto);

        // Inicialización de entrada y salida para comunicación con el servidor
        entrada = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        salida = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

        // Inicialización de los tableros
        tableroPropio = new Tablero();
        tableroEnemigo = new Tablero();

        // Configuración de los barcos en el tablero propio
        configurarBarcos();

        // Escucha mensajes del servidor durante el juego
        escucharServidor();
    }

    // Método para configurar los barcos del jugador
    private void configurarBarcos() throws IOException {
        System.out.println(entrada.readLine()); // Mensaje del servidor
        int[] tamanosBarcos = {2, 1}; // Tamaños de los barcos a colocar

        for (int tamano : tamanosBarcos) { // Para cada tamaño de barco
            boolean colocado = false;
            while (!colocado) { // Repetir hasta colocar correctamente el barco
                try {
                    System.out.println("Coloca un barco de tamaño " + tamano);
                    tableroPropio.mostrarTablero(); // Mostrar el tablero actual
                    System.out.print("Ingresa fila,columna,orientación (H o V): ");
                    String input = scanner.nextLine();
                    String[] entradaUsuario = input.split(",");
                    if (entradaUsuario.length != 3) {
                        throw new IllegalArgumentException("Debe ingresar fila, columna y orientación (H o V).");
                    }

                    // Parsear entrada del usuario
                    int fila = Integer.parseInt(entradaUsuario[0]);
                    int columna = Integer.parseInt(entradaUsuario[1]);
                    boolean horizontal = entradaUsuario[2].equalsIgnoreCase("H");

                    // Validar si las coordenadas son válidas
                    if (fila < 0 || fila >= Tablero.TAMANNO || columna < 0 || columna >= Tablero.TAMANNO) {
                        throw new IllegalArgumentException("Las coordenadas deben estar entre 0 y " + (Tablero.TAMANNO - 1));
                    }

                    // Intentar colocar el barco
                    if (tableroPropio.puedeColocarBarco(fila, columna, tamano, horizontal)) {
                        tableroPropio.colocarBarco(fila, columna, tamano, horizontal); // Colocar barco en el tablero
                        salida.write(fila + "," + columna + "," + (horizontal ? "H" : "V") + "\n"); // Informar al servidor
                        salida.flush();
                        colocado = true;
                    } else {
                        System.out.println("No se puede colocar el barco aquí. Intenta de nuevo.");
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Entrada no válida. Asegúrese de ingresar números válidos para fila y columna.");
                } catch (IllegalArgumentException e) {
                    System.out.println(e.getMessage());
                }
            }
        }
    }

    // Método que escucha mensajes del servidor durante el juego
    private void escucharServidor() throws IOException {
        while (true) { // Bucle principal del juego
            String mensaje = entrada.readLine(); // Leer mensaje del servidor
            System.out.println(mensaje);

            // Verificar si el juego ha terminado
            if (mensaje.equals("FIN_DEL_JUEGO")) {
                System.out.println("El juego ha terminado. Cerrando conexión...");
                System.out.println("GANASTE");
                break;
            } else if (mensaje.equals("FIN_DEL_JUEGO_PERDIDA")) {
                System.out.println("El juego ha terminado. Cerrando conexión...");
                System.out.println("PERDISTE");
                break;
            }

            // Si es el turno del jugador para disparar
            if (mensaje.contains("Ingresa fila,columna para disparar")) {
                realizarDisparo();
            } else if (mensaje.startsWith("DISPARO_ENEMIGO")) { // Si el enemigo disparó
                String[] partes = mensaje.split(",");
                int fila = Integer.parseInt(partes[1]);
                int columna = Integer.parseInt(partes[2]);
                boolean acierto = partes[3].equals("X");
                tableroPropio.marcarDisparoEnemigo(fila, columna, acierto); // Registrar disparo enemigo
            }

            // Mostrar los tableros actualizados
            System.out.println("Tu tablero:");
            tableroPropio.mostrarTablero();
            System.out.println("Tablero del enemigo (sin barcos):");
            tableroEnemigo.mostrarTablero();
        }

        // Cerrar conexiones al terminar el juego
        entrada.close();
        salida.close();
    }

    // Método para realizar un disparo
    private void realizarDisparo() throws IOException {
        boolean disparoValido = false;
        while (!disparoValido) { // Repetir hasta que el disparo sea válido
            try {
                System.out.print("Ingresa tu disparo (formato: fila,columna): ");
                String disparo = scanner.nextLine();
                String[] partes = disparo.split(",");
                if (partes.length != 2) {
                    throw new IllegalArgumentException("Debe ingresar fila y columna separadas por coma.");
                }

                // Parsear las coordenadas del disparo
                int fila = Integer.parseInt(partes[0]);
                int columna = Integer.parseInt(partes[1]);

                // Validar si las coordenadas son válidas
                if (fila < 0 || fila >= Tablero.TAMANNO || columna < 0 || columna >= Tablero.TAMANNO) {
                    throw new IllegalArgumentException("Las coordenadas deben estar entre 0 y " + (Tablero.TAMANNO - 1));
                }

                // Enviar disparo al servidor
                salida.write(disparo + "\n");
                salida.flush();
                String respuesta = entrada.readLine(); // Leer respuesta del servidor
                System.out.println(respuesta);

                // Actualizar tablero enemigo según el resultado del disparo
                if (respuesta.contains("¡Acierto!")) {
                    tableroEnemigo.actualizarConAcierto(fila, columna);
                } else {
                    tableroEnemigo.actualizarConFallo(fila, columna);
                }

                disparoValido = true;
            } catch (NumberFormatException e) {
                System.out.println("Entrada no válida. Asegúrese de ingresar números válidos para fila y columna.");
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        }
    }

}
