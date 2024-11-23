package bn;

import java.io.*;
import java.net.*;

public class ServidorBatallaNaval {
    private static final int PUERTO = 12345; // Puerto en el que el servidor estará escuchando

    // Método principal para iniciar el servidor
    public void iniciar() throws IOException {
        System.out.println("Servidor iniciado. Esperando conexiones...");
        ServerSocket servidor = new ServerSocket(PUERTO); // Crear el servidor

        // Aceptar la conexión del primer jugador
        Socket jugador1 = servidor.accept();
        System.out.println("Jugador 1 conectado desde: " + jugador1.getInetAddress());

        // Aceptar la conexión del segundo jugador
        Socket jugador2 = servidor.accept();
        System.out.println("Jugador 2 conectado desde: " + jugador2.getInetAddress());

        // Configuración de canales de entrada y salida para ambos jugadores
        BufferedReader entradaJugador1 = new BufferedReader(new InputStreamReader(jugador1.getInputStream()));
        BufferedWriter salidaJugador1 = new BufferedWriter(new OutputStreamWriter(jugador1.getOutputStream()));

        BufferedReader entradaJugador2 = new BufferedReader(new InputStreamReader(jugador2.getInputStream()));
        BufferedWriter salidaJugador2 = new BufferedWriter(new OutputStreamWriter(jugador2.getOutputStream()));

        // Notificar a los jugadores que están conectados
        salidaJugador1.write("Conexión establecida. Eres el Jugador 1.\n");
        salidaJugador1.flush();
        salidaJugador2.write("Conexión establecida. Eres el Jugador 2.\n");
        salidaJugador2.flush();

        // Crear tableros para cada jugador
        Tablero tableroJugador1 = new Tablero();
        Tablero tableroJugador2 = new Tablero();

        // Configurar los tableros de ambos jugadores
        configurarTableros(tableroJugador1, salidaJugador1, entradaJugador1, "Jugador 1");
        configurarTableros(tableroJugador2, salidaJugador2, entradaJugador2, "Jugador 2");

        // Alternar turnos entre los jugadores
        boolean turnoJugador1 = true;
        while (true) {
            if (turnoJugador1) {
                // Turno del Jugador 1
                salidaJugador1.write("Tu turno. Ingresa fila,columna para disparar:\n");
                salidaJugador1.flush();
                String disparo = entradaJugador1.readLine(); // Leer disparo del jugador 1
                procesarDisparo(tableroJugador2, disparo, salidaJugador1, salidaJugador2, "Jugador 1");
                
                // Verificar si el jugador 1 ha ganado
                if (tableroJugador2.todosLosBarcosHundidos()) {
                    salidaJugador1.write("FIN_DEL_JUEGO\n");
                    salidaJugador1.flush();
                    salidaJugador2.write("FIN_DEL_JUEGO_PERDIDA\n");
                    salidaJugador2.flush();
                    break; // Terminar el juego
                }
            } else {
                // Turno del Jugador 2
                salidaJugador2.write("Tu turno. Ingresa fila,columna para disparar:\n");
                salidaJugador2.flush();
                String disparo = entradaJugador2.readLine(); // Leer disparo del jugador 2
                procesarDisparo(tableroJugador1, disparo, salidaJugador2, salidaJugador1, "Jugador 2");
                
                // Verificar si el jugador 2 ha ganado
                if (tableroJugador1.todosLosBarcosHundidos()) {
                    salidaJugador2.write("FIN_DEL_JUEGO\n");
                    salidaJugador2.flush();
                    salidaJugador1.write("FIN_DEL_JUEGO_PERDIDA\n");
                    salidaJugador1.flush();
                    break; // Terminar el juego
                }
            }
            turnoJugador1 = !turnoJugador1; // Alternar turnos
        }

        // Cerrar conexiones después de terminar el juego
        jugador1.close();
        jugador2.close();
        servidor.close();
    }

    // Método para configurar los barcos de un jugador
    private void configurarTableros(Tablero tablero, BufferedWriter salida, BufferedReader entrada, String jugador)
            throws IOException {
        int[] tamanosBarcos = {2, 1}; // Tamaños de los barcos que deben colocarse

        for (int tamano : tamanosBarcos) {
            boolean colocado = false;
            while (!colocado) { // Intentar hasta que el barco sea colocado correctamente
                
                String[] partes = entrada.readLine().split(","); // Leer las coordenadas y orientación del cliente
                int fila = Integer.parseInt(partes[0]);
                int columna = Integer.parseInt(partes[1]);
                boolean horizontal = partes[2].equalsIgnoreCase("H");
                
                // Verificar si el barco puede colocarse en las coordenadas dadas
                if (tablero.puedeColocarBarco(fila, columna, tamano, horizontal)) {
                    tablero.colocarBarco(fila, columna, tamano, horizontal); // Colocar el barco
                    colocado = true;        
                } 
            }
        }
    }

    // Método para procesar un disparo
    private boolean procesarDisparo(Tablero tablero, String disparo, BufferedWriter atacante, BufferedWriter defensor,
                                    String jugadorAtacante) throws IOException {
        String[] partes = disparo.split(","); // Separar fila y columna
        int fila = Integer.parseInt(partes[0]);
        int columna = Integer.parseInt(partes[1]);
        boolean acierto = tablero.recibirDisparo(fila, columna); // Registrar el disparo en el tablero

        // Notificar al atacante si acertó o falló
        atacante.write(acierto ? "¡Acierto!\n" : "Fallo.\n");
        atacante.flush();

        // Informar al defensor sobre el disparo
        defensor.write("El " + jugadorAtacante + " disparó a " + fila + "," + columna + " y fue un "
                + (acierto ? "¡acierto!" : "fallo.") + "\n");
        defensor.flush();

        // Informar al defensor sobre dónde fue el disparo del enemigo
        defensor.write("DISPARO_ENEMIGO," + fila + "," + columna + "," + (acierto ? "X" : "O") + "\n");
        defensor.flush();

        return acierto;
    }
}
