package bn; // Paquete para organizar el código de Batalla Naval.

import java.io.*; // Importa clases para operaciones de entrada/salida.
import java.net.*; // Importa clases para conexiones de red.

public class ClienteBatallaNavalOrdenador {
    private Socket socket; // Socket para la conexión con el servidor.
    private BufferedReader in; // Para leer datos desde el servidor.
    private PrintWriter out; // Para enviar datos al servidor.

    // Método para iniciar el cliente y conectarse al servidor.
    public void iniciarCliente(String host, int puerto) {
        try {
            // Intenta conectar al servidor usando el host y puerto especificados.
            socket = new Socket(host, puerto);
            System.out.println("Conectado al servidor.");

            // Inicializa los flujos de entrada y salida.
            in = new BufferedReader(new InputStreamReader(socket.getInputStream())); // Lee datos del servidor.
            out = new PrintWriter(socket.getOutputStream(), true); // Envia datos al servidor.

            // Ejemplo de interacción con el servidor: envía un mensaje de disparo.
            enviarMensaje("Disparo: (3,4)");

            // Recibe la respuesta del servidor y la imprime en consola.
            String respuesta = recibirRespuesta();
            System.out.println("Respuesta del servidor: " + respuesta);

        } catch (IOException e) {
            // Captura excepciones de entrada/salida y muestra el error.
            e.printStackTrace();
        } finally {
            // Asegura el cierre de la conexión y los recursos.
            cerrarConexion();
        }
    }

    // Método para enviar un mensaje al servidor.
    public void enviarMensaje(String mensaje) {
        // Escribe el mensaje en el flujo de salida.
        out.println(mensaje);
    }

    // Método para recibir una respuesta del servidor.
    public String recibirRespuesta() throws IOException {
        // Lee una línea desde el flujo de entrada y la retorna.
        return in.readLine();
    }

    // Método para cerrar la conexión y liberar recursos.
    private void cerrarConexion() {
        try {
            // Cierra el flujo de entrada si está abierto.
            if (in != null) in.close();

            // Cierra el flujo de salida si está abierto.
            if (out != null) out.close();

            // Cierra el socket si está abierto.
            if (socket != null) socket.close();
        } catch (IOException e) {
            // Captura excepciones durante el cierre y las imprime.
            e.printStackTrace();
        }
    }

    // Método principal para ejecutar el cliente.
    public static void main(String[] args) {
        // Crea una instancia del cliente.
        ClienteBatallaNavalOrdenador cliente = new ClienteBatallaNavalOrdenador();

        // Intenta conectarse al servidor en localhost (máquina local) en el puerto 12345.
        cliente.iniciarCliente("localhost", 12345);
    }
}
