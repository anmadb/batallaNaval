package bn;

import java.util.Random;

public class Tablero {
	static final int TAMANNO = 10;
	static final char AGUA = '~';
	static final char BARCO = 'S';
	static final char ACIERTO = 'X';
	static final char FALLO = 'O';
	private char[][] tablero;

	public Tablero() {
		tablero = new char[TAMANNO][TAMANNO];
		inicializarTablero();
	}

	public void inicializarTablero() {
		for (int i = 0; i < TAMANNO; i++) {
			for (int j = 0; j < TAMANNO; j++) {
				tablero[i][j] = AGUA;
			}
		}
	}

	public boolean puedeColocarBarco(int fila, int columna, int tamano, boolean horizontal) {
		if (horizontal) {
			if (columna + tamano > TAMANNO)
				return false;
			for (int i = 0; i < tamano; i++) {
				if (tablero[fila][columna + i] != AGUA)
					return false;
			}
		} else {
			if (fila + tamano > TAMANNO)
				return false;
			for (int i = 0; i < tamano; i++) {
				if (tablero[fila + i][columna] != AGUA)
					return false;
			}
		}
		return true;
	}

	public boolean colocarBarco(int fila, int columna, int tamano, boolean horizontal) {
		if (!puedeColocarBarco(fila, columna, tamano, horizontal)) {
			return false;
		}

		for (int i = 0; i < tamano; i++) {
			if (horizontal) {
				tablero[fila][columna + i] = BARCO;
			} else {
				tablero[fila + i][columna] = BARCO;
			}
		}
		return true;
	}

	public boolean colocarBarcoAleatorio(int tamano) {
		Random random = new Random();
		boolean horizontal;
		int fila, columna;
		boolean colocado = false;

		while (!colocado) {
			fila = random.nextInt(TAMANNO);
			columna = random.nextInt(TAMANNO);
			horizontal = random.nextBoolean();

			if (puedeColocarBarco(fila, columna, tamano, horizontal)) {
				colocarBarco(fila, columna, tamano, horizontal);
				colocado = true;
			}
		}
		return colocado;
	}

	public boolean recibirDisparo(int fila, int columna) {
		if (tablero[fila][columna] == BARCO) {
			tablero[fila][columna] = ACIERTO;
			return true;
		} else if (tablero[fila][columna] == AGUA) {
			tablero[fila][columna] = FALLO;
		}
		return false;
	}

	public void marcarDisparoEnemigo(int fila, int columna, boolean acierto) {
		if (acierto) {
			tablero[fila][columna] = ACIERTO;
		} else {
			tablero[fila][columna] = FALLO;
		}
	}

	public void actualizarConAcierto(int fila, int columna) {
		tablero[fila][columna] = ACIERTO;
	}

	public void actualizarConFallo(int fila, int columna) {
		tablero[fila][columna] = FALLO;
	}

	public void mostrarTablero() {
		System.out.print("  ");
		for (int i = 0; i < TAMANNO; i++)
			System.out.print(i + " ");
		System.out.println();

		for (int i = 0; i < TAMANNO; i++) {
			System.out.print(i + " ");
			for (int j = 0; j < TAMANNO; j++) {
				System.out.print(tablero[i][j] + " ");
			}
			System.out.println();
		}
	}

	public void mostrarTableroOculto() {
		System.out.print("  ");
		for (int i = 0; i < TAMANNO; i++)
			System.out.print(i + " ");
		System.out.println();

		for (int i = 0; i < TAMANNO; i++) {
			System.out.print(i + " ");
			for (int j = 0; j < TAMANNO; j++) {
				if (tablero[i][j] == BARCO) {
					System.out.print(AGUA + " "); // Oculta el barco
				} else {
					System.out.print(tablero[i][j] + " ");
				}
			}
			System.out.println();
		}
	}

	public boolean todosLosBarcosHundidos() {
		for (int i = 0; i < TAMANNO; i++) {
			for (int j = 0; j < TAMANNO; j++) {
				if (tablero[i][j] == BARCO) {
					return false;
				}
			}
		}
		return true;
	}

	public char[][] getTablero() {
		return tablero;
	}
}
