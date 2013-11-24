package tests;

import java.io.Serializable;

public class Paso implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 7500878891704281910L;
	
	public String Nombre;
	public String Paso;
	public int Numero;
	
	public Paso()
	{
		Numero = 0;
		Paso = "";
		Nombre = "";
	}
	public Paso(String n, String p, int nu)
	{
		Numero = nu;
		Paso = p;
		Nombre = n;
	}
	
	public int getNumeroPaso()
	{
		return Numero;
	}
	public void setNumeroPaso(int numeroPaso)
	{
		Numero = numeroPaso;
	}
	public String getPaso()
	{
		return Paso;
	}
	public void setPaso(String paso)
	{
		Paso = paso;
	}
	public String getNombrePaso()
	{
		return Nombre;
	}
	public void setNombrePaso(String nombrePaso)
	{
		Nombre = nombrePaso;
	}
	
	/*
	 * Metodo que crea el paso a partir de los datos introducidos por teclado.
	 */
	
	public void crearPaso(int i)
	{
		Numero = i;
		System.out.print("Introducir nombre del paso " + i + ": ");
		Nombre = Utilidades.leerCadena();
		System.out.print("Introducir descripción del paso: ");
		Paso = Utilidades.leerCadena();
	}
	
	/*
	 * Metodo que visualiza en pantalla el paso.
	 */
	
	public void visualizarPaso()
	{
		System.out.println(Numero + "º: " + Nombre);
		System.out.println(Paso);
	}
	
	public static void main(String[] args)
	{
		Paso p = new Paso();
		System.out.print("Introducir numero del paso: ");
		p.crearPaso(Utilidades.leerEntero());
		p.visualizarPaso();
	}

}
