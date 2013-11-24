package tema5;
import java.util.*;

public class Persona implements Comparable<Persona> {
	private final String nombre, apellidos, dni;
    
	public Persona(String nombre, String apellidos, String dni) {
        if (nombre == null || apellidos == null || dni == null)
            throw new NullPointerException();
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.dni = dni.toUpperCase();  // Convertir a mayúscula
    }
	
    public String nombre() { return nombre; }
    public String apellidos()  { return apellidos;  }
    public String dni()  { return dni;  }
    
    public boolean equals(Object o) {
        if (!(o instanceof Persona)) return false;
        Persona n = (Persona) o;
        return n.dni.equals(dni);
    }

    public int hashCode() {
        // objetos iguales (equals) deben tener hash iguales
        return dni.hashCode() + 17*nombre.hashCode();
    }

    public String toString() {
        return dni + ": " + nombre + " " + apellidos;
    }

    public int compareTo(Persona n) {
        return dni.compareTo(n.dni);
    }
    
    public static void main( String s[] ) {
    	Persona n1, n2, n3, n4, n5;
    	// Uso 1 de ordenación: explícita con método (ALGORITMO) de Collections 
    	ArrayList<Persona> ln = new ArrayList<Persona>();
    	ln.add( n1 = new Persona( "Buzz", "Lightyear", "10000001S" ));
    	ln.add( n2 = new Persona( "Woody", "Allen", "20000002T" ));
    	ln.add( n3 = new Persona( "Tim", "Burton", "30000003V" ));
    	ln.add( n4 = new Persona( "Richard", "Marx", "40000004W" ));
    	ln.add( n5 = new Persona( "Groucho", "Marx", "50000005Z" ));
    	Collections.sort( ln );
    	System.out.println( ln );
    	// Uso 2 de ordenación: en estructura ordenada
    	TreeSet<Persona> sn = new TreeSet<Persona>();
    	sn.add( n1 );
    	sn.add( n2 );
    	sn.add( n3 );
    	sn.add( n4 );
    	sn.add( n5 );
    	System.out.println( sn );
    	// Uso 3 de ordenación: algoritmo con COMPARADOR diferente al "natural"
    	Collections.sort( ln, ORDEN_DE_Persona );
    	System.out.println( ln );
    }
    
    static final Comparator<Persona> ORDEN_DE_Persona =
        new Comparator<Persona>() {
			public int compare(Persona n1, Persona n2) {
				return n1.nombre.compareTo(n2.nombre);
			}
    	};

}

