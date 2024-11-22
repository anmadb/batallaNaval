package bn;
 
import java.util.Random;
 
public class Tablero {
	// El tamaño del tablero se establece como 10x10
	static final int TAMANNO = 10;
 
	// Caracteres que representan el agua, barco, acierto y fallo
	static final char AGUA = '~';
	static final char BARCO = 'S';
	static final char ACIERTO = 'X';
	static final char FALLO = 'O';
 
	// La matriz que contiene las celdas del tablero
	private char[][] tablero;
 
	// El constructor inicializa el tablero vacío
	public Tablero() {
		tablero = new char[TAMANNO][TAMANNO];
		inicializarTablero(); // Se inicializa el tablero con agua
	}
 
	// Método que inicializa el tablero con agua en todas las celdas
	public void inicializarTablero() {
		for (int i = 0; i < TAMANNO; i++) {
			for (int j = 0; j < TAMANNO; j++) {
				tablero[i][j] = AGUA; // Cada casilla se inicializa con el símbolo de agua
			}
		}
	}
 
	// Método que verifica si es posible colocar un barco en las coordenadas dadas
	public boolean puedeColocarBarco(int fila, int columna, int tamano, boolean horizontal) {
		// Verifica si el barco cabe horizontalmente en el tablero
		if (horizontal) {
			if (columna + tamano > TAMANNO)
				return false; // Si el barco no cabe horizontalmente, devuelve falso
			for (int i = 0; i < tamano; i++) {
				if (tablero[fila][columna + i] != AGUA)
					return false; // Si alguna casilla está ocupada, devuelve falso
			}
		} else { // Verifica si el barco cabe verticalmente en el tablero
			if (fila + tamano > TAMANNO)
				return false; // Si el barco no cabe verticalmente, devuelve falso
			for (int i = 0; i < tamano; i++) {
				if (tablero[fila + i][columna] != AGUA)
					return false; // Si alguna casilla está ocupada, devuelve falso
			}
		}
		return true; // Si el barco cabe en la posición, devuelve verdadero
	}
 
	// Método que coloca un barco en las coordenadas dadas, si es posible
	public boolean colocarBarco(int fila, int columna, int tamano, boolean horizontal) {
		// Si no se puede colocar el barco, devuelve falso
		if (!puedeColocarBarco(fila, columna, tamano, horizontal)) {
			return false;
		}
 
		// Coloca el barco en las celdas correspondientes del tablero
		for (int i = 0; i < tamano; i++) {
			if (horizontal) {
				tablero[fila][columna + i] = BARCO; // Coloca el barco horizontalmente
			} else {
				tablero[fila + i][columna] = BARCO; // Coloca el barco verticalmente
			}
		}
		return true; // Devuelve verdadero si el barco fue colocado correctamente
	}
 
	// Método que coloca un barco de tamaño aleatorio en el tablero
	public boolean colocarBarcoAleatorio(int tamano) {
		Random random = new Random();
		boolean horizontal;
		int fila, columna;
		boolean colocado = false;
 
		// Intenta colocar el barco aleatoriamente en el tablero
		while (!colocado) {
			fila = random.nextInt(TAMANNO); // Genera una fila aleatoria
			columna = random.nextInt(TAMANNO); // Genera una columna aleatoria
			horizontal = random.nextBoolean(); // Determina si el barco será horizontal o vertical
 
			// Si se puede colocar el barco en la posición aleatoria, lo coloca
			if (puedeColocarBarco(fila, columna, tamano, horizontal)) {
				colocarBarco(fila, columna, tamano, horizontal);
				colocado = true; // El barco ha sido colocado correctamente
			}
		}
		return colocado; // Retorna si el barco fue colocado correctamente
	}
 
	// Método que simula recibir un disparo en las coordenadas dadas
	public boolean recibirDisparo(int fila, int columna) {
		// Si hay un barco en las coordenadas, marca un acierto
		if (tablero[fila][columna] == BARCO) {
			tablero[fila][columna] = ACIERTO; // Marca el acierto en la casilla
			return true; // El disparo fue un éxito
		} else if (tablero[fila][columna] == AGUA) {
			tablero[fila][columna] = FALLO; // Si la casilla estaba vacía, marca un fallo
		}
		return false; // Devuelve falso si el disparo no alcanzó un barco
	}
 
	// Método que marca un disparo del enemigo como acierto o fallo en el tablero
	public void marcarDisparoEnemigo(int fila, int columna, boolean acierto) {
		if (acierto) {
			tablero[fila][columna] = ACIERTO; // Marca el disparo como un acierto
		} else {
			tablero[fila][columna] = FALLO; // Marca el disparo como un fallo
		}
	}
 
	// Método para actualizar el tablero con un acierto en las coordenadas dadas
	public void actualizarConAcierto(int fila, int columna) {
		tablero[fila][columna] = ACIERTO; // Marca la casilla con un acierto
	}
 
	// Método para actualizar el tablero con un fallo en las coordenadas dadas
	public void actualizarConFallo(int fila, int columna) {
		tablero[fila][columna] = FALLO; // Marca la casilla con un fallo
	}
 
	// Método que muestra el tablero completo, revelando todos los barcos y
	// resultados
	public void mostrarTablero() {
		System.out.print("  ");
		for (int i = 0; i < TAMANNO; i++)
			System.out.print(i + " "); // Muestra los números de las columnas
		System.out.println();
 
		// Muestra las filas y el contenido del tablero
		for (int i = 0; i < TAMANNO; i++) {
			System.out.print(i + " "); // Muestra los números de las filas
			for (int j = 0; j < TAMANNO; j++) {
				System.out.print(tablero[i][j] + " "); // Muestra el estado de cada celda
			}
			System.out.println();
		}
	}
 
	// Método que muestra el tablero ocultando los barcos, mostrando solo agua,
	// aciertos y fallos
	public void mostrarTableroOculto() {
		System.out.print("  ");
		for (int i = 0; i < TAMANNO; i++)
			System.out.print(i + " "); // Muestra los números de las columnas
		System.out.println();
 
		// Muestra las filas y el contenido del tablero, ocultando los barcos
		for (int i = 0; i < TAMANNO; i++) {
			System.out.print(i + " "); // Muestra los números de las filas
			for (int j = 0; j < TAMANNO; j++) {
				if (tablero[i][j] == BARCO) {
					System.out.print(AGUA + " "); // Oculta los barcos mostrando agua
				} else {
					System.out.print(tablero[i][j] + " "); // Muestra los demás elementos
				}
			}
			System.out.println();
		}
	}
 
	// Método que verifica si todos los barcos han sido hundidos
	public boolean todosLosBarcosHundidos() {
		// Recorre todo el tablero para verificar si aún queda algún barco
		for (int i = 0; i < TAMANNO; i++) {
			for (int j = 0; j < TAMANNO; j++) {
				if (tablero[i][j] == BARCO) {
					return false; // Si hay algún barco, retorna falso
				}
			}
		}
		return true; // Si no hay barcos restantes, retorna verdadero
	}
 
	// Método que devuelve el tablero completo
	public char[][] getTablero() {
		return tablero; // Devuelve el tablero actual
	}
}