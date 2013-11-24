package tema4;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class Fraccion {

	private int numerador;
	private int denominador;

	public Fraccion(int num, int den)
	{
		numerador = num;
		denominador = den;
	}

	/** Devuelve el numerador de la fracción
	 * @return numerador
	 */
	public int getNumerador()
	{ 
		return numerador;  
	}

	/** Devuelve el denominador de la fracción
	 * @return denominador
	 */
	public int getDenominador()
	{  
		return denominador; 
	}

	/** Devuelve el valor real de la fracción
	 * @return valor numerador / denominador
	 */
	public double getValor()
	{  
		return 1.0D*numerador/denominador; 
	}

	/** Devuelve una fracción resultado de la suma entre this y f
	 * @param f - Fracción sumando
	 * @return Nueva fracción resultado
	 */
	public Fraccion sumar(Fraccion f)
	{
		int num = (this.numerador * f.denominador) + (f.numerador * this.denominador);
		int den = this.denominador * f.denominador;
		return new Fraccion(num,den);
	}

	/** Devuelve una fracción resultado de la resta entre this y f
	 * @param f - Fracción sustraendo
	 * @return Nueva fracción resultado
	 */
	public Fraccion restar(Fraccion f)
	{
		int num = (this.numerador * f.denominador) - (f.numerador * this.denominador);
		int den = this.denominador * f.denominador;
		return new Fraccion(num,den);
	}

	/** Devuelve una fracción resultado de la multiplicación entre this y f
	 * @param f - Fracción multiplicando
	 * @return Nueva fracción resultado
	 */
	public Fraccion multiplicar(Fraccion f)
	{
		int num = this.numerador * f.numerador;
		int den = this.denominador * f.denominador;
		return new Fraccion(num,den);
	}

	/** Devuelve una fracción resultado de la división entre this y f
	 * @param f - Fracción divisor
	 * @return Nueva fracción resultado
	 */
	public Fraccion dividir(Fraccion f)
	{  
		int num = this.numerador * f.denominador;
		int den = this.denominador * f.numerador;
		return new Fraccion(num,den);
	}

	/** Visualiza la fracción en la salida estándar de consola
	 */
	public void visualizar()
	{
		System.out.println( toString() );
	}
	
	/* Conversión estandar a String:  "num/den"
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return numerador + "/" + denominador;
	}

	/** Devuelve true si el valor real del objeto (this) es menor que v2
	 * @param v2 Segundo valor de la comparación
	 * @return true si valor real de this < v2
	 */
	public boolean esMenor( Fraccion v2 ) {
		return (getValor() < v2.getValor());
	}
	/** Devuelve true si el valor real del objeto (this) es mayor que v2
	 * @param v2 Segundo valor de la comparación
	 * @return true si valor real de this > v2
	 */
	public boolean esMayor( Fraccion v2 ) {
		return (getValor() > v2.getValor());
	}
	/** Devuelve true si el valor real del objeto (this) es igual que v2
	 * @param v2 Segundo valor de la comparación
	 * @return true si valor real de this = v2
	 */
	public boolean esIgual( Fraccion v2 ) {
		return (getValor() == v2.getValor());
	}

	
			static BufferedReader inp = new BufferedReader( new InputStreamReader(System.in));
			/** Lee un string de teclado (entrada estándar)
			 * @return	String leido
			 */
			private static String leerLinea()
			{
				String s = "";
				try {
					s = inp.readLine();
				} catch (java.io.IOException e) { }
				return s;
			}
	

	
	
	
	// Lee una fracción por teclado y la devuelve
	public static Fraccion leerFraccionDeTeclado() {
		System.out.print( "Introduce fracción. Numerador: " );
		int num = LeerTeclado.leerEntero();
		System.out.print( "Denominador: " );
		int den = LeerTeclado.leerEntero();
		Fraccion f1 = new Fraccion(num, den);
		return f1;
	}
	
	// Prueba de lectura de una fracción por teclado
	private static Fraccion pruebaLecturaYProductoFracciones() {
		Fraccion f1 = leerFraccionDeTeclado();
		System.out.println( "Se ha leido la fracción " + f1 );
		Fraccion f2 = leerFraccionDeTeclado();
		System.out.println( "Se ha leido la fracción " + f2 );
		Fraccion f3 = f1.sumar(f2);
		System.out.println( "Resultado de su suma: " + f3 );
		return f3;
	}
	
	/** Programa de prueba de la clase Fraccion
	 * @param args - No se usa
	 */
	public static void main(String[] args)
	{
		Fraccion f = pruebaLecturaYProductoFracciones();
		System.out.print( "El valor de la fracción resultado es: " );
		System.out.println( f.getNumerador() / f.getDenominador() );

		
		
		
		
		
		
		System.exit(0);
		// Resto de test
		Fraccion f1,f2,f3;
		f1 = new Fraccion(3,2);
		f2 = new Fraccion(0,2);
		f3 = f1.sumar(f2);
		f3.visualizar();
		f3 = f1.restar(f2);
		f3.visualizar();
		f3 = f1.multiplicar(f2);
		f3.visualizar();
		f3 = f1.dividir(f2);
		f3.visualizar();
		f1 = new Fraccion(4,3);
		f2 = new Fraccion(1,2);
		f3 = f1.sumar(f2);
		f3.visualizar();
		f3 = f1.restar(f2);
		f3.visualizar();
		f3 = f1.multiplicar(f2);
		f3.visualizar();
		f3 = f1.dividir(f2);
		f3.visualizar();
		System.out.println( "Valor de fracción 1/9: " + new Fraccion(1,9).getValor() );
		System.out.println( "El valor " + f1 + (f1.esMayor(f2)?" SI ":" NO ") + "es mayor que " + f2 );
		System.out.println( "El valor " + f1 + (f1.esMenor(f2)?" SI ":" NO ") + "es menor que " + f2 );
		System.out.println( "El valor " + f1 + (f1.esIgual(f2)?" SI ":" NO ") + "es igual a " + f2 );
	} 
}
