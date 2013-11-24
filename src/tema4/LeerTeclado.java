package tema4;
import java.io.*;

class LeerTeclado {
	static BufferedReader inp = new BufferedReader( new
	InputStreamReader(System.in));

	public static String leerLinea()
	{
		String s = "";
		try {
			s = inp.readLine();
		} catch (java.io.IOException e) { }
		return s;
	}

	public static String leerLinea( String mens )
	{
		// con prompt
		System.out.print( mens );
		return leerLinea();
	}

/* leerDoble() Devuelve NaN en caso de error */
	public static double leerDoble()
	{
		String s = leerLinea();
		double d;
		try {
			d = Double.valueOf( s ).doubleValue();
		}
		catch (java.lang.NumberFormatException e) 
		{
			d = Double.NaN;
		}
		return d;
	}
	public static double leerDoble( String mens )
	{
		// con prompt
		System.out.print( mens );
		return leerDoble();
	}
	
	/** Lee un entero de teclado y lo devuelve<p>
	 * Si la lectura es incorrecta (no es un entero válido), devuelve MAX_VALUE
	 * @return
	 */
	public static int leerEntero()
	{
		String s = leerLinea();
		int nuevoInt;
		try {
			nuevoInt = Integer.valueOf( s ).intValue();
		}
		catch (java.lang.NumberFormatException e) 
		{
			nuevoInt = Integer.MAX_VALUE;
		}
		return nuevoInt;
	}
	
	
	
	public static void main (String[] a)
	{
		double d1 = leerDoble( "Introduce un número real: " );
		double d2 = leerDoble( "Introduce otro: " );
		System.out.println( "La suma de ambos es: " + (d1+d2) );
	}
}