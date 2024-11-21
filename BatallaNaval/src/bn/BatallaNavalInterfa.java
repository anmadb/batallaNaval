package bn;
 
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashSet;
import java.util.Set;
 
public class BatallaNavalInterfa {
    private JFrame frame;
    private JPanel tableroJugadorPanel;
    private JPanel tableroComputadoraPanel;
    private Tablero tableroJugador;
    private Tablero tableroComputadora;
    private JButton[][] botonesJugador;
    private JButton[][] botonesComputadora;
    private JLabel statusLabel;
    private Set<Integer> barcosColocados; // Para rastrear los tamaños de los barcos colocados por el jugador.
 
    public BatallaNavalInterfa() {
        tableroJugador = new Tablero();
        tableroComputadora = new Tablero();
        tableroComputadora.colocarBarcoAleatorio(4);
        tableroComputadora.colocarBarcoAleatorio(3);
        tableroComputadora.colocarBarcoAleatorio(2);
        tableroComputadora.colocarBarcoAleatorio(1);
        barcosColocados = new HashSet<>(); // Inicializamos el set de tamaños colocados.
        initialize();
    }
 
    private void initialize() {
        frame = new JFrame("Batalla Naval");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setLayout(new BorderLayout());
 
        // Status label
        statusLabel = new JLabel("Coloca tus barcos para comenzar.", SwingConstants.CENTER);
        frame.add(statusLabel, BorderLayout.NORTH);
 
        // Tableros
        tableroJugadorPanel = crearPanelTablero(true);
        tableroComputadoraPanel = crearPanelTablero(false);
 
        JPanel tablerosPanel = new JPanel(new GridLayout(1, 2));
        tablerosPanel.add(tableroJugadorPanel);
        tablerosPanel.add(tableroComputadoraPanel);
 
        frame.add(tablerosPanel, BorderLayout.CENTER);
 
        // Botón para reiniciar
        JButton resetButton = new JButton("Reiniciar");
        resetButton.addActionListener(e -> reiniciarJuego());
        frame.add(resetButton, BorderLayout.SOUTH);
 
        frame.setVisible(true);
    }
 
    private JPanel crearPanelTablero(boolean esJugador) {
        JPanel panel = new JPanel(new GridLayout(Tablero.TAMANNO, Tablero.TAMANNO));
        JButton[][] botones = new JButton[Tablero.TAMANNO][Tablero.TAMANNO];
 
        for (int i = 0; i < Tablero.TAMANNO; i++) {
            for (int j = 0; j < Tablero.TAMANNO; j++) {
                JButton boton = new JButton();
                boton.setPreferredSize(new Dimension(40, 40));
 
                if (esJugador) {
                    boton.setBackground(Color.CYAN);
                    boton.addActionListener(new ColocarBarcoAction(i, j));
                } else {
                    boton.setBackground(Color.GRAY);
                    boton.addActionListener(new DispararAction(i, j));
                }
 
                botones[i][j] = boton;
                panel.add(boton);
            }
        }
 
        if (esJugador) {
            botonesJugador = botones;
        } else {
            botonesComputadora = botones;
        }
 
        return panel;
    }
 
    private class ColocarBarcoAction implements ActionListener {
        private final int fila;
        private final int columna;
 
        public ColocarBarcoAction(int fila, int columna) {
            this.fila = fila;
            this.columna = columna;
        }
 
        @Override
        public void actionPerformed(ActionEvent e) {
            String[] opciones = {"1", "2", "3", "4"};
            String tamanoStr = (String) JOptionPane.showInputDialog(frame,
                    "Selecciona el tamaño del barco:",
                    "Colocar Barco",
                    JOptionPane.PLAIN_MESSAGE,
                    null,
                    opciones,
                    opciones[0]);
 
            if (tamanoStr != null) {
                int tamano = Integer.parseInt(tamanoStr);
 
                // Verificamos si el tamaño ya ha sido colocado
                if (barcosColocados.contains(tamano)) {
                    JOptionPane.showMessageDialog(frame,
                            "Ya has colocado un barco de tamaño " + tamano + ".",
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }
 
                int orientacion = JOptionPane.showConfirmDialog(frame, "¿Horizontal?", "Orientación",
                        JOptionPane.YES_NO_OPTION);
 
                boolean horizontal = (orientacion == JOptionPane.YES_OPTION);
 
                if (tableroJugador.puedeColocarBarco(fila, columna, tamano, horizontal)) {
                    tableroJugador.colocarBarco(fila, columna, tamano, horizontal);
                    barcosColocados.add(tamano); // Agregamos el tamaño al conjunto de barcos colocados.
                    actualizarTableroJugador();
                    statusLabel.setText("Barco de tamaño " + tamano + " colocado.");
                } else {
                    JOptionPane.showMessageDialog(frame, "No se puede colocar el barco aquí.");
                }
            }
        }
    }
 
    private class DispararAction implements ActionListener {
        private final int fila;
        private final int columna;
 
        public DispararAction(int fila, int columna) {
            this.fila = fila;
            this.columna = columna;
        }
 
        @Override
        public void actionPerformed(ActionEvent e) {
            if (tableroComputadora.recibirDisparo(fila, columna)) {
                botonesComputadora[fila][columna].setBackground(Color.RED);
                statusLabel.setText("¡Acierto!");
            } else {
                botonesComputadora[fila][columna].setBackground(Color.BLUE);
                statusLabel.setText("Fallo.");
            }
 
            botonesComputadora[fila][columna].setEnabled(false);
 
            if (tableroComputadora.todosLosBarcosHundidos()) {
                statusLabel.setText("¡Ganaste! Hundiste todos los barcos enemigos.");
                bloquearTablero(botonesComputadora);
            } else {
                disparoComputadora();
            }
        }
    }
 
    private void disparoComputadora() {
        int fila, columna;
 
        do {
            fila = (int) (Math.random() * Tablero.TAMANNO);
            columna = (int) (Math.random() * Tablero.TAMANNO);
        } while (tableroJugador.getTablero()[fila][columna] == Tablero.ACIERTO || tableroJugador.getTablero()[fila][columna] == Tablero.FALLO);
 
        if (tableroJugador.recibirDisparo(fila, columna)) {
            botonesJugador[fila][columna].setBackground(Color.RED);
            statusLabel.setText("¡La computadora acertó en (" + fila + "," + columna + ")!");
        } else {
            botonesJugador[fila][columna].setBackground(Color.BLUE);
            statusLabel.setText("La computadora falló en (" + fila + "," + columna + ").");
        }
 
        if (tableroJugador.todosLosBarcosHundidos()) {
            statusLabel.setText("La computadora ha ganado. Hundió todos tus barcos.");
            bloquearTablero(botonesJugador);
        }
    }
 
    private void actualizarTableroJugador() {
        for (int i = 0; i < Tablero.TAMANNO; i++) {
            for (int j = 0; j < Tablero.TAMANNO; j++) {
                if (tableroJugador.getTablero()[i][j] == Tablero.BARCO) {
                    botonesJugador[i][j].setBackground(Color.GREEN);
                }
            }
        }
    }
 
    private void bloquearTablero(JButton[][] botones) {
        for (JButton[] fila : botones) {
            for (JButton boton : fila) {
                boton.setEnabled(false);
            }
        }
    }
 
    private void reiniciarJuego() {
        frame.dispose();
        new BatallaNavalInterfa();
    }
 
    public static void main(String[] args) {
        SwingUtilities.invokeLater(BatallaNavalInterfa::new);
    }
}