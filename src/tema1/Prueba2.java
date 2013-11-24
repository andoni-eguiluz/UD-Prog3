package tema1;

// REFERENCIAS VS TIPOS BASICOS

public class Prueba2 {
	private int miValor;
	/** Crea y devuelve un nuevo objeto Prueba2
	 * @param pMiValor	Valor de inicialización
	 */
	public Prueba2( int pMiValor ) {
		miValor = pMiValor;
	}
	public static Prueba2 nuevoObjeto5() {
		return new Prueba2( 5 );
	}
	
	/** Devuelve la repr. del objeto en forma de String
	 */
	public String toString() {
		return ""+miValor;
	}
	/**
	 * @return
	 */
	public int getValor() {
		return miValor;
	}
	
	public static void main( String s[]) {
		int i = 5;
		int i2 = i;
		i2 = i2 + 1;
		System.out.println( "i = " + i + " / i2 = " + i2 );
		Prueba2 pr2 = new Prueba2( 5 );
		System.out.println( "Mi objeto pr2 = " + pr2 );
		Prueba2 pr2b = new Prueba2(5); /*nuevoObjeto5();*/
		pr2.miValor++;
		pr2b.miValor++;
		System.out.println( "pr2 = " + pr2 + " / pr2b = " + pr2b );
		// Y ojo a la semántica referencial en comparación:
		if (i == i2) System.out.println( "i-i2 iguales" );
		if (pr2 == pr2b) System.out.println( "pr2-pr2b iguales" );
//		Prueba2 pr2c = null;
//		pr2c.visualizarValor( "");
	}
}
