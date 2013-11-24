package tema5;

import java.util.Vector;

public class Pruebas {
	public Vector<String> metodo( Vector<Double> v ) {
		return new Vector<String>();
	}
	
	public static void main(String[] args) {
		// Ejemplo vector genérico de strings
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
		// Opción mala: codificación
		f1.colorCodigo = COLOR_AZUL;  //¿Qué color era?
		System.out.println( f1.colorCodigo );
		for (int colores = 0; colores < 3; colores++) //¿Cuántos colores tengo?
			System.out.print( colores + " " );
		System.out.println();
		// Opción mola: enumerado
		f1.color = ColorFicha.VERDE;   // Objeto creado automáticamente
		System.out.println( f1.color );
		for (ColorFicha c : ColorFicha.values()) // Sé cuántos colores tengo
			System.out.print( c + " " );
		System.out.println();
		ColorFicha colorNuevo = ColorFicha.valueOf("AMARILLO");  
			// Y puedo convertir automáticamente del string al objeto
			// (Excepción implícita IllegalArgumentException)
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