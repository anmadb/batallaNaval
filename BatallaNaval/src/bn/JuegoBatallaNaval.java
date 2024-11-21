package bn;
 
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
 
public class JuegoBatallaNaval {
    private JFrame frame;
 
    private static final int PUERTO_MULTIJUGADOR = 12345;
 
    public JuegoBatallaNaval() {
        initializeMenu();
    }
 
    private void initializeMenu() {
        frame = new JFrame("Batalla Naval");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300);
        frame.setLayout(new BorderLayout());
 
        JLabel titleLabel = new JLabel("¡Bienvenido a Batalla Naval!", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        frame.add(titleLabel, BorderLayout.NORTH);
 
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(3, 1, 10, 10));
 
        JButton jugarContraComputadoraButton = new JButton("Jugar contra la computadora");
        jugarContraComputadoraButton.addActionListener(e -> {
            frame.dispose(); // Cierra el menú principal
            iniciarJuegoContraComputadora();
        });
 
        JButton primerJugadorMultijugadorButton = new JButton("Modo multijugador (iniciar servidor)");
        primerJugadorMultijugadorButton.addActionListener(e -> {
            frame.dispose();
            try {
                iniciarServidorYPrimerJugadorMultijugador();
            } catch (IOException ex) {
                mostrarError("Error al iniciar el servidor multijugador: " + ex.getMessage());
            }
        });
 
        JButton segundoJugadorMultijugadorButton = new JButton("Modo multijugador (conectarse)");
        segundoJugadorMultijugadorButton.addActionListener(e -> {
            frame.dispose();
            try {
                conectarSegundoJugadorMultijugador();
            } catch (IOException ex) {
                mostrarError("Error al conectar al servidor multijugador: " + ex.getMessage());
            }
        });
 
        buttonPanel.add(jugarContraComputadoraButton);
        buttonPanel.add(primerJugadorMultijugadorButton);
        buttonPanel.add(segundoJugadorMultijugadorButton);
 
        frame.add(buttonPanel, BorderLayout.CENTER);
 
        frame.setVisible(true);
    }
 
    private void iniciarJuegoContraComputadora() {
        javax.swing.SwingUtilities.invokeLater(() -> new BatallaNavalInterfa());
    }
 
    private void iniciarServidorYPrimerJugadorMultijugador() throws IOException {
        JFrame servidorFrame = new JFrame("Servidor Multijugador");
        servidorFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        servidorFrame.setSize(400, 200);
        servidorFrame.setLayout(new BorderLayout());
 
        JLabel statusLabel = new JLabel("Iniciando el servidor multijugador...", SwingConstants.CENTER);
        servidorFrame.add(statusLabel, BorderLayout.CENTER);
 
        servidorFrame.setVisible(true);
 
        Thread servidorThread = new Thread(() -> {
            try {
                ServidorBatallaNaval servidor = new ServidorBatallaNaval();
                servidor.iniciar();
                statusLabel.setText("Servidor iniciado. Conecte un segundo jugador.");
            } catch (IOException e) {
                statusLabel.setText("Error al iniciar el servidor: " + e.getMessage());
            }
        });
        servidorThread.start();
    }
 
    private void conectarSegundoJugadorMultijugador() throws IOException {
        JFrame conectarFrame = new JFrame("Conectar a Servidor");
        conectarFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        conectarFrame.setSize(300, 150);
        conectarFrame.setLayout(new BorderLayout());
 
        JLabel instructionLabel = new JLabel("Ingrese la dirección IP del servidor:", SwingConstants.CENTER);
        JTextField ipField = new JTextField();
        JButton connectButton = new JButton("Conectar");
 
        connectButton.addActionListener(e -> {
            String ip = ipField.getText();
            try {
                ClienteBatallaNaval cliente = new ClienteBatallaNaval();
                cliente.conectarServidor(ip, PUERTO_MULTIJUGADOR);
                conectarFrame.dispose();
            } catch (IOException ex) {
                mostrarError("Error al conectar al servidor: " + ex.getMessage());
            }
        });
 
        conectarFrame.add(instructionLabel, BorderLayout.NORTH);
        conectarFrame.add(ipField, BorderLayout.CENTER);
        conectarFrame.add(connectButton, BorderLayout.SOUTH);
 
        conectarFrame.setVisible(true);
    }
 
    private void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(null, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
    }
 
    public static void main(String[] args) {
        SwingUtilities.invokeLater(JuegoBatallaNaval::new);
    }
}