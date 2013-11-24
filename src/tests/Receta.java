package tests;
import java.io.Serializable;

/*
 * Clase padre.
 */

public class Receta implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -7186174998510923264L;
	
	public String Nombre;
	public String NombreCocinero;
	
	public Receta()
	{
		Nombre = "";
		NombreCocinero = "";
	}
	public Receta(String n, String c)
	{
		Nombre = n;
		NombreCocinero = c;
	}
	
	public String getNombre()
	{
		return Nombre;
	}
	public void setNombre(String nombre)
	{
		Nombre = nombre;
	}
	public String getNombreCocinero()
	{
		return NombreCocinero;
	}
	public void setNombreCocinero(String nombreCocinero)
	{
		NombreCocinero = nombreCocinero;
	}
	
	public void v()
	{
		System.out.println("Nombre: " + Nombre);
		System.out.println("Cocinero: " + NombreCocinero);
	}
}
