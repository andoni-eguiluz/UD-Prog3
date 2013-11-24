package tests;

public class Excepcion extends RuntimeException
{
	/**
	 * Excepcion que se va a usar en todos los casos necesarios.
	 */
	
	private static final long serialVersionUID = 1115574599380238096L;
	
	String mensaje;
	
	public Excepcion()
	{
		mensaje = "";
	}
	public Excepcion(String s)
	{
		mensaje = s;
	}
	
}
