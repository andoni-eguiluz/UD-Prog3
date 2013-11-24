package tests;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;

public class ControlTemperaturas {
	static int NUM_FILAS = 4;
	static int NUM_COLS = 3;
	
	public static void main( String[] s) {
		double[][] tablaTemperaturas = cargarMatrizTemperaturas();
		visualizarMatrizTemperaturas(tablaTemperaturas);
		double[] temperaturasMedias = calcularValoresMedios(tablaTemperaturas);
		visualizarTemperaturasMedias(temperaturasMedias);
	}

	public static double[][] cargarMatrizTemperaturas()
	{
		double[][] matriz = new double[NUM_FILAS][3];
		for (int fila = 0; fila < NUM_FILAS; fila++) {
			System.out.println( "Introducir la " + (fila+1) + " toma de temperaturas" );
			for (int columna = 0; columna < NUM_COLS; columna++) {
				System.out.print( "Temperatura del " + (columna+1) + " desagüe: " );
				matriz[fila][columna] = leerReal();
			}
			System.out.println();
		}
		return matriz;
	}
	
	public static void visualizarMatrizTemperaturas(double[][]matriz)
	{
		System.out.println( "Registro de las temperaturas bla bla");
		for (int fila=0; fila < matriz.length; fila++) {
			System.out.print( "   " + (fila+1) + "     " );
			for (int col=0; col < matriz[fila].length; col++ ) {
				System.out.print( matriz[fila][col] + "  " );
			}
			System.out.println();
		}
	}
	
	public static double[] calcularValoresMedios(double[][]matriz)
	{
		double[] medias = new double[NUM_FILAS];
		for (int fila = 0; fila < NUM_FILAS; fila++) {
			double suma = 0;
			for( int columna = 0; columna < NUM_COLS; columna++ ) {
				suma = suma + matriz[fila][columna];
			}
			// código para convertir infor a el índice en el que guardarlo
			medias[fila] = suma / NUM_COLS;
		}
		return medias; 
	}

	public static void visualizarTemperaturasMedias(double[]media)
	{
		System.out.println( "blab");
		for (int fila=0; fila < media.length; fila++) {
			System.out.print( "   " + media[fila] );
		}
		System.out.println();
	}

	public static double leerReal() {
		double real;
		do { 
			try {
				InputStreamReader isrTeclado = new InputStreamReader( System.in );
				BufferedReader brTeclado = new BufferedReader( isrTeclado ); 
				String lectura = brTeclado.readLine();
				real = Double.parseDouble( lectura );
			} catch (Exception e) {
				System.out.println( "ERROR EN ENTRADA");
				real = Double.MAX_VALUE;
			}
		} while (real == Double.MAX_VALUE);
		return real;
	}
	
}
	
