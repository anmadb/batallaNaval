package bn;

import java.io.*;
import java.net.*;

public class ServidorBatallaNavalOrdenador {
    private ServerSocket serverSocket;
    private Socket clientSocket;
    private BufferedReader in;
    private PrintWriter out;

    public void iniciarServidor(int puerto) {
        try {
            serverSocket = new ServerSocket(puerto);
            System.out.println("Servidor iniciado en el puerto " + puerto);
            clientSocket = serverSocket.accept();
            System.out.println("Cliente conectado.");

            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            out = new PrintWriter(clientSocket.getOutputStream(), true);

            // Bucle para manejar la comunicación
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                System.out.println("Mensaje recibido: " + inputLine);
                if ("SALIR".equalsIgnoreCase(inputLine)) {
                    break;
                }

                // Responde al cliente
                String respuesta = procesarDisparo(inputLine);
                out.println(respuesta);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            cerrarConexion();
        }
    }

    private String procesarDisparo(String mensaje) {
        // Lógica para procesar un disparo del cliente (ejemplo)
        System.out.println("Procesando disparo: " + mensaje);
        return "Resultado del disparo en " + mensaje; // Respuesta simulada
    }

    private void cerrarConexion() {
        try {
            if (in != null) in.close();
            if (out != null) out.close();
            if (clientSocket != null) clientSocket.close();
            if (serverSocket != null) serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        ServidorBatallaNavalOrdenador servidor = new ServidorBatallaNavalOrdenador();
        servidor.iniciarServidor(12345);
    }
}
