package bn;

import java.util.HashSet;
import java.util.Random;
import java.util.Scanner;
import java.util.Set;

public class BatallaNaval {
    private Tablero tableroJugador;
    private Tablero tableroComputadora;
    private Set<String> disparosComputadora;
    private Random random;

    public BatallaNaval(Tablero tableroJugador, Tablero tableroComputadora) {
        this.tableroJugador = tableroJugador;
        this.tableroComputadora = tableroComputadora;
        this.disparosComputadora = new HashSet<>();
        this.random = new Random();
    }

    public void configurarTableroJugador() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Coloca tus barcos en el tablero:");

        int[] tamanosBarcos = {2, 1}; 
        for (int tamano : tamanosBarcos) {
            boolean colocado = false;

            while (!colocado) {
                System.out.println("Coloca un barco de tamaño " + tamano);
                tableroJugador.mostrarTablero();

                System.out.print("Ingresa la fila inicial (0 a 9): ");
                int fila = scanner.nextInt();
                System.out.print("Ingresa la columna inicial (0 a 9): ");
                int columna = scanner.nextInt();
                System.out.print("¿Horizontal (H) o Vertical (V)?: ");
                char orientacion = scanner.next().toUpperCase().charAt(0);

                boolean horizontal = (orientacion == 'H');
                if (tableroJugador.puedeColocarBarco(fila, columna, tamano, horizontal)) {
                    tableroJugador.colocarBarco(fila, columna, tamano, horizontal);
                    colocado = true; 
                } else {
                    System.out.println("No se puede colocar el barco aquí. Intenta de nuevo.");
                }
            }
        }
        System.out.println("Todos los barcos se han colocado en el tablero.");
    }


    public void configurarTableroComputadora() {
        System.out.println("La computadora está colocando sus barcos...");
        int[] tamanosBarcos = {4, 3, 2, 1};
        for (int tamano : tamanosBarcos) {
            tableroComputadora.colocarBarcoAleatorio(tamano);
        }
    }

    public boolean realizarDisparoJugador(int fila, int columna) {
        return tableroComputadora.recibirDisparo(fila, columna);
    }

    public boolean realizarDisparoComputadora() {
        int fila, columna;
        String coordenada;
        do {
            fila = random.nextInt(Tablero.TAMANNO);
            columna = random.nextInt(Tablero.TAMANNO);
            coordenada = fila + "," + columna;
        } while (disparosComputadora.contains(coordenada));

        disparosComputadora.add(coordenada);
        return tableroJugador.recibirDisparo(fila, columna);
    }

    public boolean todosLosBarcosHundidos(Tablero tablero) {
        return tablero.todosLosBarcosHundidos();
    }
}
