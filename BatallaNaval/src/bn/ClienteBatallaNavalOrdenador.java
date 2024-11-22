package bn;

import java.io.*;
import java.net.*;

public class ClienteBatallaNavalOrdenador {
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;

    public void iniciarCliente(String host, int puerto) {
        try {
            socket = new Socket(host, puerto);
            System.out.println("Conectado al servidor.");

            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);

            // Ejemplo de interacción con el servidor
            enviarMensaje("Disparo: (3,4)");
            String respuesta = recibirRespuesta();
            System.out.println("Respuesta del servidor: " + respuesta);

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            cerrarConexion();
        }
    }

    public void enviarMensaje(String mensaje) {
        out.println(mensaje);
    }

    public String recibirRespuesta() throws IOException {
        return in.readLine();
    }

    private void cerrarConexion() {
        try {
            if (in != null) in.close();
            if (out != null) out.close();
            if (socket != null) socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        ClienteBatallaNavalOrdenador cliente = new ClienteBatallaNavalOrdenador();
        cliente.iniciarCliente("localhost", 12345);
    }
}

