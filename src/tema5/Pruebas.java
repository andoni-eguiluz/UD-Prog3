package tema5;

import java.util.Vector;

public class Pruebas {
	public Vector<String> metodo( Vector<Double> v ) {
		return new Vector<String>();
	}
	
	public static void main(String[] args) {
		// Ejemplo vector gen�rico de strings
		Vector<String> v = new Vector<String>();
		v.add( "asdfsdf" );
		int lon = (v.get(0)).length();
		System.out.println( "Longitud primer string: " + lon );
		
		// Array y for each
		int[] miArray = { 1, 3, 5, 7 };
		int suma = 0;
		for (int i = 0; i<miArray.length; i++) {
			suma += miArray[i];
			i++;
		}
		for (int e : miArray) {
			suma += e;
		}
		System.out.println( suma );
		
		// Enumerados
		pruebaColores();
	}

	public static int COLOR_AZUL = 2;
	// Prueba con enumerados  (ver enum y clase abajo)
	private static void pruebaColores() {
		Ficha f1 = new Ficha();
		// Opci�n mala: codificaci�n
		f1.colorCodigo = COLOR_AZUL;  //�Qu� color era?
		System.out.println( f1.colorCodigo );
		for (int colores = 0; colores < 3; colores++) //�Cu�ntos colores tengo?
			System.out.print( colores + " " );
		System.out.println();
		// Opci�n mola: enumerado
		f1.color = ColorFicha.VERDE;   // Objeto creado autom�ticamente
		System.out.println( f1.color );
		for (ColorFicha c : ColorFicha.values()) // S� cu�ntos colores tengo
			System.out.print( c + " " );
		System.out.println();
		ColorFicha colorNuevo = ColorFicha.valueOf("AMARILLO");  
			// Y puedo convertir autom�ticamente del string al objeto
			// (Excepci�n impl�cita IllegalArgumentException)
		System.out.println( colorNuevo );
	}
}

enum ColorFicha {
	// public static ColorFicha ROJO = new ...
	ROJO, VERDE, AZUL, AMARILLO
}

class Ficha {
	int colorCodigo;  // 0 = rojo , 1 = ...
	ColorFicha color;
}