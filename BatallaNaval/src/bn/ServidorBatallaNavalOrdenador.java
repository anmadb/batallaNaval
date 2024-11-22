package bn; // Paquete que agrupa las clases relacionadas con Batalla Naval.

import java.io.*; // Importa clases para operaciones de entrada y salida.
import java.net.*; // Importa clases para conexiones de red.

public class ServidorBatallaNavalOrdenador {
    private ServerSocket serverSocket; // ServerSocket para esperar conexiones de clientes.
    private Socket clientSocket; // Socket que representa la conexión con un cliente.
    private BufferedReader in; // Flujo para leer datos enviados por el cliente.
    private PrintWriter out; // Flujo para enviar datos al cliente.

    // Método para iniciar el servidor en un puerto específico.
    public void iniciarServidor(int puerto) {
        try {
            // Crea un ServerSocket que escucha en el puerto especificado.
            serverSocket = new ServerSocket(puerto);
            System.out.println("Servidor iniciado en el puerto " + puerto);

            // Espera a que un cliente se conecte.
            clientSocket = serverSocket.accept();
            System.out.println("Cliente conectado.");

            // Inicializa los flujos de entrada y salida para la comunicación.
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream())); // Lee mensajes del cliente.
            out = new PrintWriter(clientSocket.getOutputStream(), true); // Envía mensajes al cliente.

            // Bucle para manejar la comunicación con el cliente.
            String inputLine;
            while ((inputLine = in.readLine()) != null) { // Lee cada línea enviada por el cliente.
                System.out.println("Mensaje recibido: " + inputLine);

                // Si el cliente envía "SALIR", finaliza la conexión.
                if ("SALIR".equalsIgnoreCase(inputLine)) {
                    break; // Sale del bucle.
                }

                // Procesa el mensaje recibido y genera una respuesta.
                String respuesta = procesarDisparo(inputLine);

                // Envía la respuesta al cliente.
                out.println(respuesta);
            }

        } catch (IOException e) {
            // Captura y muestra cualquier error relacionado con la entrada/salida.
            e.printStackTrace();
        } finally {
            // Asegura que los recursos se liberen después de finalizar la comunicación.
            cerrarConexion();
        }
    }

    // Método para procesar un disparo enviado por el cliente.
    private String procesarDisparo(String mensaje) {
        // Aquí se podría implementar la lógica del juego.
        System.out.println("Procesando disparo: " + mensaje); // Muestra el disparo recibido.
        return "Resultado del disparo en " + mensaje; // Devuelve una respuesta simulada.
    }

    // Método para cerrar las conexiones y liberar recursos.
    private void cerrarConexion() {
        try {
            if (in != null) in.close(); // Cierra el flujo de entrada si está abierto.
            if (out != null) out.close(); // Cierra el flujo de salida si está abierto.
            if (clientSocket != null) clientSocket.close(); // Cierra el socket del cliente si está abierto.
            if (serverSocket != null) serverSocket.close(); // Cierra el ServerSocket si está abierto.
        } catch (IOException e) {
            // Captura y muestra errores ocurridos durante el cierre de recursos.
            e.printStackTrace();
        }
    }

    // Método principal para iniciar el servidor.
    public static void main(String[] args) {
        // Crea una instancia del servidor.
        ServidorBatallaNavalOrdenador servidor = new ServidorBatallaNavalOrdenador();

        // Inicia el servidor en el puerto 12345.
        servidor.iniciarServidor(12345);
    }
}
