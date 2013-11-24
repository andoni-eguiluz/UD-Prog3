package tema1;

/**
 * Clase para pruebas de conceptos de introducci�n de Java
 * @author andoni
 *
 */
public class PruebasTema1 {
	private int i;
	public PruebasTema1( int val ) {
		i = val;
	}
	
	/** M�todo principal para pruebas
	 * @param s
	 */
	public static void main (String s[]) {
		
		// Sintaxis b�sica de Java (3/6)
		double x = 5 / 2;
		System.out.println( x );

		// Sintaxis b�sica de Java (4/6)
		// String miString;  // Error (local no se inicializa por defecto)
		String miString = null;  // Correcci�n
		System.out.println( miString );
		
		// Constructores (1/2)
		// PruebasTema1 p = new PruebasTema1();
		int i1 = 3;
		int i2 = i1;
		PruebasTema1 pt1 = new PruebasTema1( 3 );
		PruebasTema1 pt2 = pt1;  // Asignaci�n = copia superficial
		System.out.println( i2 );
		System.out.println( pt2 );
	}
	
}


/*public*/ class Circulo
{
		double x,y;
		double radio;
		public double perimetro()
		{
			return 2*3.1416*radio;
		}
		public double area()
		{
			return 3.1416*radio*radio;
		}
}

