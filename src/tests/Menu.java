package tests;

/*
 * Clase con los distintos metodos que se van a usar.
 * Posiblemente haya que modificar algun metodo cuando cree las ventanas.
 */

import java.io.*;
import java.util.LinkedList;
import java.util.StringTokenizer;

public class Menu
{
	public static void Inicio(LinkedList <RecetaPersonalizada> l, LinkedList <Receta> l2)throws Exception
	{
		FileInputStream fiRecetas = new FileInputStream("recetas.dat");
		ObjectInputStream oiRecetas = new ObjectInputStream(fiRecetas);
		RecetaPersonalizada r = (RecetaPersonalizada)oiRecetas.readObject();
		System.out.println( "===============" );
		while(r != null)
		{
			r.Visualizar();
			l.add(r);
//			Receta rr = (Receta)oiRecetas.readObject();
//			l2.add(rr);
			r = (RecetaPersonalizada)oiRecetas.readObject();
		}
		oiRecetas.close();
	}
	
	/*
	 * Metodo que añade una receta a la lista.
	 */
	
	public static void AñadirRecetaLista(LinkedList<RecetaPersonalizada> l) throws Exception
	{
		RecetaPersonalizada r = new RecetaPersonalizada();
		r.crearReceta();
		l.addFirst(r);
		System.out.println("Receta añadida.");
	}
	
	/*
	 * Metodo que elimina una receta de la lista.
	 */
	
	public static void EliminarRecetaDeLista(LinkedList <RecetaPersonalizada> l)
	{
		System.out.print("Introducir nombre de la receta a borrar: ");
		String s = Utilidades.leerCadena();
		int i = 0;
		boolean enc = false;
		while(i < l.size() && !enc)
		{
			Receta r = l.get(i);
			if(r.getNombre().equals(s))
			{
				enc = true;
				l.remove(i);
			}
			else
				i++;
		}
		if(!enc)
			System.out.println("La receta no ha sido encontrada, por lo que no se ha eliminado nada.");
		else
			System.out.println("Receta borrada.");
	}
	
	/*
	 * Metodo que modifica una receta, a partir del nombre de esta.
	 */
	
	public static void ModificarReceta(LinkedList <RecetaPersonalizada> l) throws Exception
	{
		System.out.print("Introducir nombre de la receta que desea modificar: ");
		String s = Utilidades.leerCadena();
		int i = 0;
		boolean enc = false;
		char opc = ' ';
		while(i < l.size() && !enc)
		{
			RecetaPersonalizada r = l.get(i);
			if(r.getNombre().equals(s))
			{
				enc = true;
				do
				{
					System.out.print("¿Desea modificar el nombre de la receta? ");
					opc = Utilidades.leerCaracter();
					if(opc == 's' || opc == 'S')
					{
						System.out.print("Introducir nuevo nombre de la receta: ");
						r.setNombre(Utilidades.leerCadena());
					}
				}
				while (opc != 's' && opc != 'n' && opc != 'S' && opc != 'N');
				opc = ' ';
				do
				{
					System.out.print("¿Desea modificar el numero de ingredientes? ");
					opc = Utilidades.leerCaracter();
					if(opc == 's' || opc == 'S')
					{
						System.out.print("Introducir numero de ingredientes de la receta: ");
						r.setNumeroIngredientes(Utilidades.leerEntero());
					}
				}
				while (opc != 's' && opc != 'n' && opc != 'S' && opc != 'N');
				opc = ' ';
				do
				{
					System.out.print("¿Desea modificar la lista de los ingredientes?");
					opc = Utilidades.leerCaracter();
					if(opc == 's' || opc == 'S')
					{
						System.out.print("Introducir nueva lista de ingredientes: ");
						r.setIngredientes(Utilidades.leerCadena());
					}
				}
				while (opc != 's' && opc != 'n' && opc != 'S' && opc != 'N');
				opc = ' ';
				do
				{
					System.out.print("¿Desea modificar el nombre del cocinero? ");
					opc = Utilidades.leerCaracter();
					if(opc == 's' || opc == 'S')
					{
						System.out.print("Introducir nombre del cocinero: ");
						r.setNombreCocinero(Utilidades.leerCadena());
					}
				}
				while (opc != 's' && opc != 'n' && opc != 'S' && opc != 'N');
				do
				{
					System.out.print("¿Desea modificar la dificultad de la receta? ");
					opc = Utilidades.leerCaracter();
					if(opc == 's' || opc == 'S')
					{
						r.introducirDificultad();
					}
				}
				while (opc != 's' && opc != 'n' && opc != 'S' && opc != 'N');
			}
			else
				i++;
		}
		if(!enc)
			System.out.println("La receta no ha sido encontrada.");
	}
	
	/*
	 * Metodo que busca una receta por el nombre.
	 */
	
	public static void BuscadorNombre(LinkedList <RecetaPersonalizada> l)
	{
		boolean enc = false;
		int i = 0;
		System.out.print("Introducir nombre de la receta: ");
		String s = Utilidades.leerCadena();
		while(i < l.size() && !enc)
		{
			RecetaPersonalizada r = l.get(i);
			if(r.getNombre().equals(s))
			{
				r.Visualizar();
				enc = true;
			}
			else
				i++;
		}
		if (!enc)
			System.out.println("No hay ninguna receta con ese nombre.");
	}
	
	/*
	 * Metodo que devuelve todas las recetas de un determinado cocinero.
	 */
	
	public static void BuscadorCocinero(LinkedList <RecetaPersonalizada> l)
	{
		System.out.print("Introducir cocinero del que desea ver las recetas: ");
		String s = Utilidades.leerCadena();
		int i = 0;
		int h = 0;
		while(i < l.size())
		{
			RecetaPersonalizada r = l.get(i);
			if(r.getNombreCocinero().equals(s))
			{
				h++;
				System.out.println("Receta " + h + ": " + r.getNombre());
			}
			i++;
		}
		if(h == 0)
			System.out.println(s + " no tiene ninguna receta en la base de datos.");
	}
	
	/*
	 * Metodo que devuelve todas las recetas que requieran un ingrediente concreto.
	 */
	
	public static void BuscadorIngredientes(LinkedList <RecetaPersonalizada> l)
	{
		System.out.print("Introducir ingrediente en minusculas: ");
		String s = Utilidades.leerCadena();
		int i = 0;
		int h = 0;
		while(i < l.size())
		{
			RecetaPersonalizada r = l.get(i);
			if(CompararIngredientes(s, r.getIngredientes()) == 1)
			{
				h++;
				System.out.println("Receta " + h + ": " + r.getNombre());
			}
			i++;
		}
		if(h == 0)
			System.out.println("No hay ninguna receta que requiera " + s + " para su elaboracion.");
	}
	
	/*
	 * Metodo que guarda la lista de recetas en el fichero.
	 */
	
	public static void PasarAlFichero(LinkedList <RecetaPersonalizada> l)throws Exception
	{
		vaciarFichero2();
		FileOutputStream foRecetas = new FileOutputStream("recetas.dat");
		ObjectOutputStream ooRecetas = new ObjectOutputStream(foRecetas);
		for(int i = 0; i < l.size(); i++)
		{
			RecetaPersonalizada r = (RecetaPersonalizada)l.get(i);
			ooRecetas.writeObject(r);
		}
		ooRecetas.writeObject(null);
		ooRecetas.close();
	}
	
	/*
	 * Metodo que crea una copia de seguridad del fichero.
	 */
	
	public static void CrearCopiaSeguridad()throws Exception
	{
		FileInputStream fiRecetas = new FileInputStream("recetas.dat");
		ObjectInputStream oiRecetas = new ObjectInputStream(fiRecetas);
		vaciarFichero();
		FileOutputStream foRecetas = new FileOutputStream("copiarecetas.dat");
		ObjectOutputStream ooRecetas = new ObjectOutputStream(foRecetas);
		RecetaPersonalizada r = (RecetaPersonalizada)oiRecetas.readObject();
		while(r != null)
		{
			ooRecetas.writeObject(r);
			r = (RecetaPersonalizada)oiRecetas.readObject();
		}
		ooRecetas.writeObject(null);
		ooRecetas.close();
	}
	
	/*
	 * Metodos que vacian los ficheros antes de guardar nada.
	 */
	
	public static void vaciarFichero()throws Exception
	{
		FileInputStream fiRecetas = new FileInputStream("copiarecetas.dat");
		ObjectInputStream oiRecetas = new ObjectInputStream(fiRecetas);
		oiRecetas.close();
	}
	private static void vaciarFichero2()throws Exception
	{
		FileInputStream fiRecetas = new FileInputStream("recetas.dat");
		ObjectInputStream oiRecetas = new ObjectInputStream(fiRecetas);
		oiRecetas.close();
	}
	
	/*
	 * Metodo que compara ingredientes.
	 */
	
	public static int CompararIngredientes(String s, String i)
	{
		StringTokenizer st = new StringTokenizer(i, ", ");
		int enc = 0;
		while (st.hasMoreTokens() && enc == 0)
		{
			if(st.nextToken().equals(s))
				enc = 1;
		}
		return enc;
	}
	
	public static void main (String args[]) throws Exception
	{
		LinkedList<Paso> p = new LinkedList<Paso>();
		Paso paso1 = new Paso("Limpiar.", "Limpiamos bien la lechuga y el tomate. Pelamos el huevo cocido y abrimos la lata de atun.", 1);
		Paso paso2 = new Paso("Preparación.", "Troceamos el tomate, el huevo, el atun y el queso. Servimos en una fuente grande con unas cuantas hojas de lechuga. Añadir una pizca de sal, y por cada 3 partes de aceite una de vinagre.", 2);
		p.add(paso1);
		p.add(paso2);
		RecetaPersonalizada r1 = new RecetaPersonalizada("Ensalada cesar", "lechuga, tomate, salsa, huevo, atun, queso, aceite, vinagre, sal", "Argiñano", Dificultades.facil, 9, Tipos.ensalada, p);
		r1.Visualizar();
		System.out.println();
		
		LinkedList<Paso> p1 = new LinkedList<Paso>();
		Paso paso3 = new Paso("Asar.", "Asar las chuletillas a la parrilla hasta que queden bien doraditas, pero sin pasarse.", 1);
		Paso paso4 = new Paso("Servir.", "Servir con una ensalada de guarnicion, o con unas patatas.", 2);
		p1.add(paso3);
		p1.add(paso4);
		RecetaPersonalizada r2 = new RecetaPersonalizada("Chuletillas", "chuletillas, sal", "Argiñano", Dificultades.normal, 2, Tipos.carne, p1);
		r2.Visualizar();
		System.out.println();
		
		Receta h = (Receta)r1;
		h.v();
		System.out.println();
		
		Receta h2 = (Receta)r2;
		h2.v();
		System.out.println();
		
		FileOutputStream a = new FileOutputStream("recetas.dat");
		ObjectOutputStream b = new ObjectOutputStream(a);
		b.writeObject(r1);
		b.writeObject(h);
		b.writeObject(r2);
		b.writeObject(h2);
		b.writeObject(null);
		b.close();
		System.out.println("Objetos escritos");
		System.out.println();
		
		LinkedList <RecetaPersonalizada> l = new LinkedList<RecetaPersonalizada>();
		LinkedList <Receta> l2 = new LinkedList<Receta>();
		Inicio(l, l2);
		RecetaPersonalizada r =  l.get(0);
		r.setNombre(h.getNombre());
		r.setNombreCocinero(h.getNombreCocinero());
		r.Visualizar();
	}
	
}
