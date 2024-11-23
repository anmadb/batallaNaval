package bn;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashSet;
import java.util.Set;

public class BatallaNavalInterfa {
    private JFrame frame; // Ventana principal que contiene toda la interfaz del juego
    private JPanel tableroJugadorPanel; // Panel gráfico para mostrar el tablero del jugador
    private JPanel tableroComputadoraPanel; // Panel gráfico para mostrar el tablero de la computadora
    private Tablero tableroJugador; // Tablero lógico donde se gestionan los barcos y disparos del jugador
    private Tablero tableroComputadora; // Tablero lógico de la computadora para manejar sus barcos y disparos
    private JButton[][] botonesJugador; // Botones gráficos asociados al tablero del jugador
    private JButton[][] botonesComputadora; // Botones gráficos asociados al tablero de la computadora
    private JLabel statusLabel; // Etiqueta para mostrar mensajes e instrucciones al jugador
    private Set<Integer> barcosColocados; // Conjunto que almacena los tamaños de barcos ya colocados por el jugador

    // Constructor que inicializa el juego y la interfaz gráfica
    public BatallaNavalInterfa() {
        tableroJugador = new Tablero(); // Se crea un tablero lógico vacío para el jugador
        tableroComputadora = new Tablero(); // Se crea un tablero lógico vacío para la computadora

        // Se colocan barcos aleatoriamente en el tablero de la computadora
        tableroComputadora.colocarBarcoAleatorio(4);
        tableroComputadora.colocarBarcoAleatorio(3);
        tableroComputadora.colocarBarcoAleatorio(2);
        tableroComputadora.colocarBarcoAleatorio(1);

        barcosColocados = new HashSet<>(); // Se inicializa el conjunto para rastrear los tamaños de barcos colocados
        inicioInter(); // Se configura y despliega la interfaz gráfica
    }

    // Método para inicializar la interfaz gráfica del juego
    private void inicioInter() {
        frame = new JFrame("Batalla Naval"); // Se crea la ventana principal del juego
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Configura el cierre del programa al cerrar la ventana
        frame.setSize(800, 600); // Define el tamaño de la ventana
        frame.setLayout(new BorderLayout()); // Usa un diseño de borde para organizar los elementos en la ventana

        // Etiqueta en la parte superior de la ventana para mensajes del juego
        statusLabel = new JLabel("Coloca tus barcos para comenzar.", SwingConstants.CENTER);
        frame.add(statusLabel, BorderLayout.NORTH); // Se agrega al norte de la ventana

        // Creación de los paneles para los tableros del jugador y de la computadora
        tableroJugadorPanel = crearPanelTablero(true); // Tablero para colocar barcos
        tableroComputadoraPanel = crearPanelTablero(false); // Tablero para disparar a la computadora

        // Panel contenedor para los dos tableros, organizados en paralelo
        JPanel tablerosPanel = new JPanel(new GridLayout(1, 2));
        tablerosPanel.add(tableroJugadorPanel); // Agregar tablero del jugador al lado izquierdo
        tablerosPanel.add(tableroComputadoraPanel); // Agregar tablero de la computadora al lado derecho

        frame.add(tablerosPanel, BorderLayout.CENTER); // Agrega el contenedor de tableros al centro de la ventana

        // Botón para reiniciar el juego
        JButton resetButton = new JButton("Reiniciar");
        resetButton.addActionListener(e -> reiniciarJuego()); // Acción que reinicia el juego
        frame.add(resetButton, BorderLayout.SOUTH); // Coloca el botón en la parte inferior de la ventana

        frame.setVisible(true); // Hace visible la ventana del juego
    }

    // Método para crear un panel de tablero (puede ser para el jugador o la computadora)
    private JPanel crearPanelTablero(boolean esJugador) {
        JPanel panel = new JPanel(new GridLayout(Tablero.TAMANNO, Tablero.TAMANNO)); // Organiza los botones en una cuadrícula
        JButton[][] botones = new JButton[Tablero.TAMANNO][Tablero.TAMANNO]; // Matriz de botones para el tablero

        // Itera sobre las filas y columnas para inicializar los botones
        for (int i = 0; i < Tablero.TAMANNO; i++) {
            for (int j = 0; j < Tablero.TAMANNO; j++) {
                JButton boton = new JButton(); // Crea un botón para cada celda del tablero
                boton.setPreferredSize(new Dimension(40, 40)); // Define el tamaño de cada botón

                if (esJugador) {
                    boton.setBackground(Color.CYAN); // Color inicial para el tablero del jugador
                    boton.addActionListener(new ColocarBarcoInter(i, j)); // Agrega acción para colocar barcos
                } else {
                    boton.setBackground(Color.GRAY); // Color inicial para el tablero de la computadora
                    boton.addActionListener(new DispararAction(i, j)); // Agrega acción para disparar
                }

                botones[i][j] = boton; // Almacena el botón en la matriz
                panel.add(boton); // Agrega el botón al panel
            }
        }

        if (esJugador) {
            botonesJugador = botones; // Asigna los botones al tablero gráfico del jugador
        } else {
            botonesComputadora = botones; // Asigna los botones al tablero gráfico de la computadora
        }

        return panel; // Devuelve el panel creado
    }

    // Clase interna para manejar la colocación de barcos en el tablero del jugador
    private class ColocarBarcoInter implements ActionListener {
        private final int fila; // Fila del botón seleccionado
        private final int columna; // Columna del botón seleccionado

        public ColocarBarcoInter(int fila, int columna) {
            this.fila = fila; // Inicializa la fila
            this.columna = columna; // Inicializa la columna
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            String[] opciones = {"1", "2", "3", "4"}; // Opciones de tamaños de barcos
            String tamanoStr = (String) JOptionPane.showInputDialog(frame,
                    "Selecciona el tamaño del barco:", // Mensaje de selección
                    "Colocar Barco", // Título del cuadro de diálogo
                    JOptionPane.PLAIN_MESSAGE, // Tipo de mensaje
                    null, // Sin ícono personalizado
                    opciones, // Opciones disponibles
                    opciones[0]); // Selección predeterminada

            if (tamanoStr != null) { // Verifica que se haya seleccionado una opción
                int tamano = Integer.parseInt(tamanoStr); // Convierte el tamaño a entero

                // Verifica si el barco de este tamaño ya fue colocado
                if (barcosColocados.contains(tamano)) {
                    JOptionPane.showMessageDialog(frame,
                            "Ya has colocado un barco de tamaño " + tamano + ".", // Mensaje de error
                            "Error", // Título
                            JOptionPane.ERROR_MESSAGE); // Tipo de mensaje
                    return;
                }

                // Solicita orientación del barco (horizontal o vertical)
                int orientacion = JOptionPane.showConfirmDialog(frame, "¿Horizontal?", "Orientación",
                        JOptionPane.YES_NO_OPTION);
                boolean horizontal = (orientacion == JOptionPane.YES_OPTION); // Verifica si es horizontal

                // Intenta colocar el barco en el tablero lógico del jugador
                if (tableroJugador.puedeColocarBarco(fila, columna, tamano, horizontal)) {
                    tableroJugador.colocarBarco(fila, columna, tamano, horizontal); // Coloca el barco
                    barcosColocados.add(tamano); // Registra el tamaño como colocado
                    actualizarTableroJugador(); // Actualiza la vista del tablero
                    statusLabel.setText("Barco de tamaño " + tamano + " colocado."); // Actualiza el mensaje
                } else {
                    JOptionPane.showMessageDialog(frame, "No se puede colocar el barco aquí."); // Mensaje de error
                }
            }
        }
    }

    // Clase interna para manejar los disparos del jugador en el tablero de la computadora
   /* private class DispararAction implements ActionListener {
        private final int fila; // Fila del botón seleccionado
        private final int columna; // Columna del botón seleccionado

        public DispararAction(int fila, int columna) {
            this.fila = fila; // Inicializa la fila
            this.columna = columna; // Inicializa la columna
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            // Disparo en el tablero lógico de la computadora
            if (tableroComputadora.recibirDisparo(fila, columna)) {
                botonesComputadora[fila][columna].setBackground(Color.RED); // Marca acierto
                statusLabel.setText("¡Acierto!");
            } else {
                botonesComputadora[fila][columna].setBackground(Color.BLUE); // Marca fallo
                statusLabel.setText("Fallo.");
            }

            botonesComputadora[fila][columna].setEnabled(false); // Desactiva el botón disparado

            // Verifica si el jugador ganó
            if (tableroComputadora.todosLosBarcosHundidos()) {
                statusLabel.setText("¡Ganaste! Hundiste todos los barcos enemigos.");
                bloquearTablero(botonesComputadora); // Bloquea el tablero enemigo
            } else {
                disparoComputadora(); // Turno de la computadora
            }
        }
    }*/

    // Método que gestiona el disparo aleatorio de la computadora
    private void disparoComputadora() {
        int fila, columna;

        do {
            fila = (int) (Math.random() * Tablero.TAMANNO); // Selecciona fila aleatoria
            columna = (int) (Math.random() * Tablero.TAMANNO); // Selecciona columna aleatoria
        } while (tableroJugador.getTablero()[fila][columna] == Tablero.ACIERTO ||
                tableroJugador.getTablero()[fila][columna] == Tablero.FALLO); // Repite si ya disparó aquí

        // Disparo en el tablero lógico del jugador
        if (tableroJugador.recibirDisparo(fila, columna)) {
            botonesJugador[fila][columna].setBackground(Color.RED); // Marca acierto
            statusLabel.setText("¡La computadora acertó en (" + fila + "," + columna + ")!");
        } else {
            botonesJugador[fila][columna].setBackground(Color.BLUE); // Marca fallo
            statusLabel.setText("La computadora falló en (" + fila + "," + columna + ").");
        }

        // Verifica si la computadora ganó
        if (tableroJugador.todosLosBarcosHundidos()) {
            statusLabel.setText("La computadora ha ganado. Hundió todos tus barcos.");
            bloquearTablero(botonesJugador); // Bloquea el tablero del jugador
        }
    }

    // Actualiza la vista del tablero del jugador
    private void actualizarTableroJugador() {
        for (int i = 0; i < Tablero.TAMANNO; i++) {
            for (int j = 0; j < Tablero.TAMANNO; j++) {
                if (tableroJugador.getTablero()[i][j] == Tablero.BARCO) {
                    botonesJugador[i][j].setBackground(Color.GREEN); // Marca las celdas con barcos
                }
            }
        }
    }

    // Bloquea todos los botones de un tablero
    private void bloquearTablero(JButton[][] botones) {
        for (JButton[] fila : botones) {
            for (JButton boton : fila) {
                boton.setEnabled(false); // Deshabilita el botón
            }
        }
    }

    // Reinicia el juego cerrando la ventana actual y creando una nueva
    private void reiniciarJuego() {
        frame.dispose(); // Cierra la ventana actual
        new BatallaNavalInterfa(); // Crea una nueva instancia del juego
    }

    // Método principal para iniciar el juego
    public static void main(String[] args) {
        SwingUtilities.invokeLater(BatallaNavalInterfa::new); // Inicia la interfaz gráfica en un hilo seguro
    }
}
