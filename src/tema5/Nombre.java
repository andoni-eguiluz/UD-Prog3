package tema5;
import java.util.*;

public class Nombre implements Comparable<Nombre> {
	private final String nombre, apellidos;
    
	public Nombre(String nombre, String apellidos) {
        if (nombre == null || apellidos == null)
            throw new NullPointerException();
        this.nombre = nombre;
        this.apellidos = apellidos;
    }
	
    public String nombre() { return nombre; }
    public String apellidos()  { return apellidos;  }
    
    public boolean equals(Object o) {
        if (!(o instanceof Nombre)) return false;
        Nombre n = (Nombre) o;
        return n.nombre.equals(nombre) &&
               n.apellidos.equals(apellidos);
    }

    public int hashCode() {
        // objetos iguales (equals) deben tener hash iguales
        return 31*nombre.hashCode() + apellidos.hashCode();
    }

    public String toString() {
        return nombre + " " + apellidos;
    }

    public int compareTo(Nombre n) {
        int ultimaCmp = apellidos.compareTo(n.apellidos);
        return (ultimaCmp != 0 ? ultimaCmp :
                nombre.compareTo(n.nombre));
    }
    
    public static void main( String s[] ) {
    	Nombre n1, n2, n3, n4, n5;
    	// Uso 1 de ordenación: explícita con método (ALGORITMO) de Collections 
    	ArrayList<Nombre> ln = new ArrayList<Nombre>();
    	ln.add( n1 = new Nombre( "Buzz", "Lightyear" ));
    	ln.add( n2 = new Nombre( "Woody", "Allen" ));
    	ln.add( n3 = new Nombre( "Tim", "Burton" ));
    	ln.add( n4 = new Nombre( "Richard", "Marx" ));
    	ln.add( n5 = new Nombre( "Groucho", "Marx" ));
    	Collections.sort( ln );
    	System.out.println( ln );
    	// Uso 2 de ordenación: en estructura ordenada
    	TreeSet<Nombre> sn = new TreeSet<Nombre>();
    	sn.add( n1 );
    	sn.add( n2 );
    	sn.add( n3 );
    	sn.add( n4 );
    	sn.add( n5 );
    	System.out.println( sn );
    	// Uso 3 de ordenación: algoritmo con COMPARADOR diferente al "natural"
    	Collections.sort( ln, ORDEN_DE_NOMBRE );
    	System.out.println( ln );
    }
    
    // Clase sin nombre
    // (obsérvese el Nombre$1.class generado)
    static final Comparator<Nombre> ORDEN_DE_NOMBRE =
        new Comparator<Nombre>() {
			public int compare(Nombre n1, Nombre n2) {
				return n1.nombre.compareTo(n2.nombre);
			}
    	};

    // Otra manera de hacerlo (clase con nombre)
    static final Comparator<Nombre> ORDEN_DE_NOMBRE2 =
        new MiComparator();

}

// La clase con nombre para el comparador
class MiComparator implements Comparator<Nombre> {
	public MiComparator() {
	}
	public int compare(Nombre n1, Nombre n2) {
		return n1.nombre().compareTo(n2.nombre());
	}
}

