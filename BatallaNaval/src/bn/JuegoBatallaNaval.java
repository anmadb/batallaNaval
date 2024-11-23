package bn;
 
import javax.swing.*;
import java.awt.*;    
import java.io.IOException;
 
public class JuegoBatallaNaval {
    private JFrame frame; // Ventana principal del programa.
 
    private static final int PUERTO_MULTIJUGADOR = 12345; // Puerto por defecto para las conexiones multijugador.
 
    
    public JuegoBatallaNaval() {
        initializeMenu(); // Llama al método para inicializar el menú principal.
    }
 
    // Configura el menú principal de la interfaz gráfica.
    private void initializeMenu() {
        frame = new JFrame("Batalla Naval"); // Crea la ventana principal con el título "Batalla Naval".
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Configura el cierre de la ventana para que termine el programa.
        frame.setSize(400, 300); // Establece el tamaño de la ventana.
        frame.setLayout(new BorderLayout()); // Usa un diseño por bordes para organizar los elementos.
 
        // Título del menú, centrado y con estilo.
        JLabel titleLabel = new JLabel("¡Bienvenido a Batalla Naval!", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18)); // Fuente en negrita y tamaño 18.
        frame.add(titleLabel, BorderLayout.NORTH); // Coloca el título en la parte superior de la ventana.
 
        // Panel que contendrá los botones del menú.
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(3, 1, 10, 10)); // Diseño de rejilla (3 filas, 1 columna, con espacios).
 
        // Botón para jugar contra la computadora.
        JButton jugarContraComputadoraButton = new JButton("Jugar contra la computadora");
        jugarContraComputadoraButton.addActionListener(e -> {
            frame.dispose(); // Cierra el menú principal.
            try {
				iniciarJuegoContraComputadora();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} // Llama al método para iniciar el modo de juego contra la computadora.
        });
 
        // Botón para iniciar el servidor multijugador (primer jugador).
        JButton primerJugadorMultijugadorButton = new JButton("Modo multijugador (iniciar servidor)");
        primerJugadorMultijugadorButton.addActionListener(e -> {
            frame.dispose(); // Cierra el menú principal.
            try {
                iniciarServidorYPrimerJugadorMultijugador(); // Inicia el servidor.
            } catch (IOException ex) {
                mostrarError("Error al iniciar el servidor multijugador: " + ex.getMessage()); // Muestra un error si falla.
            }
        });
 
        // Botón para conectarse al servidor como segundo jugador.
        JButton segundoJugadorMultijugadorButton = new JButton("Modo multijugador (conectarse)");
        segundoJugadorMultijugadorButton.addActionListener(e -> {
            frame.dispose(); // Cierra el menú principal.
            try {
                conectarSegundoJugadorMultijugador(); // Intenta conectarse al servidor.
            } catch (IOException ex) {
                mostrarError("Error al conectar al servidor multijugador: " + ex.getMessage()); // Muestra un error si falla.
            }
        });
 
        // Añade los botones al panel.
        buttonPanel.add(jugarContraComputadoraButton);
        buttonPanel.add(primerJugadorMultijugadorButton);
        buttonPanel.add(segundoJugadorMultijugadorButton);
 
        frame.add(buttonPanel, BorderLayout.CENTER); // Coloca el panel en el centro de la ventana.
 
        frame.setVisible(true); // Muestra la ventana al usuario.
    }
 
 // Método para iniciar el modo de juego contra la computadora (servidor y cliente locales).
    private void iniciarJuegoContraComputadora() throws IOException {
        JFrame frameComputadora = new JFrame("Juego Contra la Computadora");
        frameComputadora.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frameComputadora.setSize(400, 200);
        frameComputadora.setLayout(new BorderLayout());

        JLabel statusLabel = new JLabel("Iniciando el servidor local...", SwingConstants.CENTER);
        frameComputadora.add(statusLabel, BorderLayout.CENTER);

        frameComputadora.setVisible(true);

        // Hilo para manejar el servidor en segundo plano.
        Thread servidorThread = new Thread(() -> {
            ServidorBatallaNavalOrdenador servidor = new ServidorBatallaNavalOrdenador(); // Servidor local.
			servidor.iniciarServidor(PUERTO_MULTIJUGADOR);
			statusLabel.setText("Servidor local iniciado. Conectando cliente...");

			
        });
        servidorThread.start();
        
        
     // Después de iniciar el servidor, conecta al cliente.
        ClienteBatallaNavalOrdenador cliente = new ClienteBatallaNavalOrdenador(); // Cliente local.
		cliente.iniciarCliente("localhost", PUERTO_MULTIJUGADOR);

		frameComputadora.dispose(); // Cierra el marco temporal.
		new BatallaNavalInterfa(); // Abre la interfaz de juego contra la computadora.
    }
 
    // Método para iniciar el servidor multijugador y manejar al primer jugador.
    private void iniciarServidorYPrimerJugadorMultijugador() throws IOException {
        JFrame servidorFrame = new JFrame("Servidor Multijugador"); // Ventana para mostrar el estado del servidor.
        servidorFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Configura el cierre de la ventana.
        servidorFrame.setSize(400, 200); // Tamaño de la ventana.
        servidorFrame.setLayout(new BorderLayout()); // Diseño por bordes.
 
        JLabel statusLabel = new JLabel("Iniciando el servidor multijugador...", SwingConstants.CENTER); // Etiqueta de estado inicial.
        servidorFrame.add(statusLabel, BorderLayout.CENTER); // Añade la etiqueta al centro.
 
        servidorFrame.setVisible(true); // Muestra la ventana.
 
        // Hilo para manejar el servidor en segundo plano.
        Thread servidorThread = new Thread(() -> {
            try {
                ServidorBatallaNaval servidor = new ServidorBatallaNaval(); // Crea una instancia del servidor.
                servidor.iniciar(); // Inicia el servidor.
                statusLabel.setText("Servidor iniciado. Conecte un segundo jugador."); // Cambia el estado cuando está listo.
            } catch (IOException e) {
                statusLabel.setText("Error al iniciar el servidor: " + e.getMessage()); // Muestra un error si falla.
            }
        });
        servidorThread.start(); // Inicia el hilo del servidor.
        
        conectarSegundoJugadorMultijugador();
    }
 
    // Método para conectarse al servidor como segundo jugador.
    private void conectarSegundoJugadorMultijugador() throws IOException {
        JFrame conectarFrame = new JFrame("Conectar a Servidor"); // Ventana para introducir la IP del servidor.
        conectarFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Configura el cierre de la ventana.
        conectarFrame.setSize(300, 150); // Tamaño de la ventana.
        conectarFrame.setLayout(new BorderLayout()); // Diseño por bordes.
 
        JLabel instructionLabel = new JLabel("Ingrese la dirección IP del servidor:", SwingConstants.CENTER); // Instrucción para el usuario.
        JTextField ipField = new JTextField(); // Campo de texto para la IP.
        JButton connectButton = new JButton("Conectar"); // Botón para conectarse.
 
        connectButton.addActionListener(e -> {
            String ip = ipField.getText(); // Obtiene la IP ingresada por el usuario.
            try {
                ClienteBatallaNaval cliente = new ClienteBatallaNaval(); // Crea una instancia del cliente.
                cliente.conectarServidor(ip, PUERTO_MULTIJUGADOR); // Conecta al servidor usando la IP y puerto.
                conectarFrame.dispose(); // Cierra la ventana después de conectarse.
            } catch (IOException ex) {
                mostrarError("Error al conectar al servidor: " + ex.getMessage()); // Muestra un error si falla.
            }
        });
 
        // Añade los elementos a la ventana.
        conectarFrame.add(instructionLabel, BorderLayout.NORTH);
        conectarFrame.add(ipField, BorderLayout.CENTER);
        conectarFrame.add(connectButton, BorderLayout.SOUTH);
 
        conectarFrame.setVisible(true); // Muestra la ventana.
    }
 
    // Método para mostrar errores en un cuadro de diálogo emergente.
    private void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(null, mensaje, "Error", JOptionPane.ERROR_MESSAGE); // Muestra el mensaje de error.
    }
 
    // Método principal, punto de entrada del programa.
    public static void main(String[] args) {
        SwingUtilities.invokeLater(JuegoBatallaNaval::new); // Inicia el programa asegurando que todo se ejecute en el hilo de la interfaz gráfica.
    }
}