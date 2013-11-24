package tests;

/*
 * Clase hija con todos los atributos y los metodos a utiliazar.
 */

import java.io.Serializable;
import java.util.LinkedList;
import java.util.StringTokenizer;

public class RecetaPersonalizada extends Receta implements Serializable
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 3710310510610946111L;
	
	
	public String Ingredientes;
	public int NumeroIngredientes;
	public Dificultades Dificultad;
	public Tipos Tipo;
	public LinkedList <Paso> Pasos;
	
	public RecetaPersonalizada()
	{
		super();
		Ingredientes = "";
		NumeroIngredientes = 0;
		Dificultad = null;
		Tipo = null;
		Pasos = null;
	}
	public RecetaPersonalizada(String n, String i, String c, Dificultades d, int ni, Tipos t, LinkedList <Paso> p)
	{
		super(n, c);
		Ingredientes = i;
		Dificultad = d;
		NumeroIngredientes = ni;
		Tipo = t;
		Pasos = p;
	}
	
	public LinkedList <Paso> getPasos()
	{
		return Pasos;
	}
	public void setPasos(LinkedList <Paso>  pasos)
	{
		Pasos = pasos;
	}
	public String getIngredientes()
	{
		return Ingredientes;
	}
	public void setIngredientes(String ingredientes)
	{
		Ingredientes = ingredientes;
		comprobarIngredientes();
	}
	public Dificultades getDificultad()
	{
		return Dificultad;
	}
	public void setDificultad(Dificultades dificultad)
	{
		Dificultad = dificultad;
	}
	public int getNumeroIngredientes()
	{
		return NumeroIngredientes;
	}
	public void setNumeroIngredientes(int numeroIngredientes)
	{
		NumeroIngredientes = numeroIngredientes;
		comprobarIngredientes();
	}
	public Tipos getTipo()
	{
		return Tipo;
	}
	public void setTipo(Tipos tipo)
	{
		this.Tipo = tipo;
	}
	
	
	/*
	 * Metodos para introducir el tipo y la dificultad de la receta.
	 */
	
	public void introducirDificultad() throws Exception
	{
		int d = 0;
		try
		{
		System.out.print("Introducir la dificultad de la receta (1 facilísimo - 5 dificilísimo): ");
		d = Utilidades.leerEntero();
		if(d < 1 || d >5)
			throw new Excepcion("Dificultad incorrecta.");
		}
		catch(Excepcion e)
		{
			System.out.println();
			System.out.println(e.mensaje);
			introducirDificultad();
		}
		switch(d)
		{
			case 1:
				Dificultad = Dificultades.facilisimo;
			break;
			case 2:
				Dificultad = Dificultades.facil;
			break;
			case 3:
				Dificultad = Dificultades.normal;
			break;
			case 4:
				Dificultad = Dificultades.dificil;
			break;
			case 5:
				Dificultad = Dificultades.dificilisimo;
			break;
		}
	}
	public void introducirTipo() throws Exception
	{
		int d = 0;
		try
		{
		System.out.print("Introducir tipo de la receta (1- ensalada, 2- pasta, 3- entrante, 4- carne, 5- pescado, 6- postre): ");
		d = Utilidades.leerEntero();
		if(d < 1 || d >6)
			throw new Excepcion("Tipo incorrecto.");
		}
		catch(Excepcion e)
		{
			System.out.println();
			System.out.println(e.mensaje);
			introducirTipo();
		}
		switch(d)
		{
			case 1:
				Tipo = Tipos.ensalada;
			break;
			case 2:
				Tipo = Tipos.pasta;
			break;
			case 3:
				Tipo = Tipos.entrante;
			break;
			case 4:
				Tipo = Tipos.carne;
			break;
			case 5:
				Tipo = Tipos.pescado;
			break;
			case 6:
				Tipo = Tipos.postre;
			break;
		}
	}
	
	public int devolverTipo()
	{
		int d = 0;
		switch(Tipo)
		{
			case ensalada:
				d = 0;
			break;
			case pasta:
				d = 1;
			break;
			case entrante:
				d = 2;
			break;
			case carne:
				d = 3;
			break;
			case pescado:
				d = 4;
			break;
			case postre:
				d = 5;
			break;
		}
		return d;
	}
	
	public int devolverDificultad()
	{
		int d = 0;
		switch(Dificultad)
		{
			case facilisimo:
				d = 0;
			break;
			case facil:
				d = 1;
			break;
			case normal:
				d = 2;
			break;
			case dificil:
				d = 3;
			break;
			case dificilisimo:
				d = 4;
			break;
		}
		return d;
	}
	
	/*
	 * Metodo que comprueba que el numero de ingredientes es correcto.
	 */
	
	private void comprobarIngredientes()
	{
		StringTokenizer st = new StringTokenizer(Ingredientes, ", ");
		int i = st.countTokens();
		if(i != NumeroIngredientes)
			introducirIngredientes();
	}
	
	/*
	 * Metodo solicita los pasos que son necesarios para elaborar la receta.
	 */
	
	public void introducirPasos()
	{
		Pasos = new LinkedList<Paso> ();
		System.out.print("Introducir numero de pasos: ");
		int h = 0;
		try
		{
			h = Utilidades.leerEntero();
			if(h <= 0)
				throw new Excepcion("Numero de pasos incorrecto");
		}
		catch(Excepcion e)
		{
			System.out.println();
			System.out.println(e.mensaje);
			introducirPasos();
		}
		for(int i = 0; i < h; i++)
		{
			Paso p = new Paso();
			System.out.print("Nombre del paso " + (i+1) + ": ");
			p.setNombrePaso(Utilidades.leerCadena());
			System.out.print("Descripcion del paso " + (i+1) + ": ");
			p.setPaso(Utilidades.leerCadena());
			p.setNumeroPaso(i+1);
			Pasos.add(i, p);
		}
		
	}
	
	/*
	 * Metodo que solicita los ingredientes.
	 */
	
	public void introducirIngredientes()
	{
		System.out.print("Introducir numero de ingredientes: ");
		NumeroIngredientes = Utilidades.leerEntero();
		System.out.print("Introducir Ingredientes de la receta, todos en minúsculas y separados por comas: ");
		Ingredientes = Utilidades.leerCadena();
		comprobarIngredientes();
	}
	
	/*
	 * Metodo para visualizar la receta.
	 */
	
	public void Visualizar()
	{
		System.out.println("            " + Nombre);
		System.out.println("Ingredientes: " + Ingredientes);
		System.out.println("Dificultad: " + Dificultad);
		System.out.println("Tipo: " + Tipo);
		System.out.println();
		System.out.println("Pasos:");
		for(int i = 0; i < Pasos.size(); i++)
		{
			Paso p = Pasos.get(i);
			p.visualizarPaso();
		}
		System.out.println("Receta creada por: " + NombreCocinero);
	}
	
	/*
	 * Metodo que pide los pasos progresivamente.
	 */
	
	public void crearReceta() throws Exception
	{
		System.out.print("Introducir nombre de la receta: ");
		setNombre(Utilidades.leerCadena());
		introducirIngredientes();
		System.out.print("Nombre del cocinero: ");
		setNombreCocinero(Utilidades.leerCadena());
		introducirDificultad();
		introducirPasos();
		introducirTipo();
	}
	
	/*
	 * Convierte los pasos en un solo String.
	 */
	
	public String aString()
	{
		String s = "";
		for(int i = 0; i < Pasos.size(); i++)
		{
			Paso p = Pasos.get(i);
			s = s + "\n" + p.getPaso();
		}
		return s;
	}
	
	public static void main (String []args) throws Exception
	{
		RecetaPersonalizada r= new RecetaPersonalizada();
		r.crearReceta();
		r.Visualizar();
		System.out.println(r.aString());
	}
}
