package tema5;

import java.util.*;

public class EjemploColeccionesTipicas {

	private static HashSet<Persona> miSet;  //set
	private static TreeSet<Persona> miSortedSet;  //Sorted set
	private static ArrayList<Persona> miList;  //List
	private static HashMap<String,Persona> miMap;  //Map
	private static TreeMap<String,Persona> miSortedMap;  //SortedMap
	private static LinkedList<Persona> miQueue;  //Queue (FIFO)
	private static PriorityQueue<Persona> miPriorityQueue;  //Priority queue

	public static void main(String[] args) {
		
    	Persona per1 = new Persona( "Groucho", "Marx", "50000005Z" );
    	Persona per2 = new Persona( "Tim", "Burton", "30000003V" );
    	Persona per3 = new Persona( "Buzz", "Lightyear", "10000001S" );
    	Persona per4 = new Persona( "Woody", "Allen", "20000002T" );
    	Persona per5 = new Persona( "Richard", "Marx", "40000004W" );
    	
		miSet = new HashSet<Persona>();
		miSortedSet = new TreeSet<Persona>();
		miList = new ArrayList<Persona>();
		miQueue = new LinkedList<Persona>();
		miPriorityQueue = new PriorityQueue<Persona>();
		miMap = new HashMap<String,Persona>();
		miSortedMap = new TreeMap<String,Persona>();

		// Llenamos todas las colecciones
    	Persona[] pers = { per1, per2, per3, per4, per5, per5 };

    	// No es recomendable usar arrays de tipos genéricos
    	// Java no permite CREAR arrays de tipos genéricos
    	//    OK:    ArrayList<Persona>[] pru;
    	//    ERROR: pru = new ArrayList<Persona>[5];
    	// Se permite crear el tipo RAW pero no es recomendable
    	// y de ahí el warning:
    	//    WARNING: pru = new ArrayList[5];
    	// En este caso sólo se usa para inicialización sencilla (con un warning):
		Collection[] col = { miSet, miSortedSet, miList, miQueue, miPriorityQueue };
		// Lo recomentable si necesitáramos esta estructura más allá sería
		// sustituir array por arraylist: 
		// ArrayList<Collection<Persona>> col = 
		//    new ArrayList<Collection<Persona>>();
		// col.add( miSet ); col.add( miSortedSet ); ...
		// Ver por ejemplo
		//   http://www.velocityreviews.com/forums/t755002-cannot-create-a-generic-array-of-type.html
		//   http://code.stephenmorley.org/articles/java-generics-type-erasure/

		for (Collection<Persona> c : col) {
	    	for (Persona p : pers) {
	    		c.add( p );
	    	}
		}
	    	// En vez de lo típico...
			// miSet.add( per1 );
			// miSet.add( per2 );
			// miSet.add( per3 );
			// miSet.add( per4 );
			// miSet.add( per5 );
			// miSortedSet.add( per1 );
			// ... etc etc ...

		System.out.println( "Las colecciones son:");
		for (Collection<Persona> c : col) {
			System.out.println( " " + c.getClass().getName() + " - " + c );
		}
		// Recorrido de la cola de prioridad (coge en orden de comparación):
		System.out.println( "Recorrido de cola de prioridad: " );
		while (!miPriorityQueue.isEmpty()) {
			Persona p = miPriorityQueue.remove();
			System.out.println( "  " + p );
		}
		
		// Llenamos los maps  (id. problema con el warning)
		Map[] map = { miMap, miSortedMap };
		for (Map<String,Persona> m : map) {
	    	for (Persona p : pers) {
	    		m.put( p.dni(), p );
	    	}
		}
		System.out.println( "Los mapas son:");
		for (Map<String,Persona> m : map) {
			System.out.println( " " + m.getClass().getName() + " - " + m );
		}
		System.out.println( "Busco el DNI 20000002T: " +
				miMap.get("20000002F"));
		
		Persona p5 = miMap.get("20000002F");
		if (p5 != null)
			System.out.println( p5.dni() );

		String[] lista = { "a", "b", "c" };
		System.out.println(
				Arrays.asList(lista).contains( "c" )
				);
		
	}
}
