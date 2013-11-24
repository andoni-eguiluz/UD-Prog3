package tema1;

/** Clase para gestionar objetos estelares
 * @author andoni.eguiluz
 */
public abstract class ObjetoEstelar {
	private static int numObjetosCreados;
	protected String nombre;
	protected long radio;
	public ObjetoEstelar( String nombre, long radio ) {
		this.nombre = nombre;
		this.radio = radio;
		numObjetosCreados++;
	}
	
	/** Calcula y devuelve la órbita del objeto estelar,
	 * dependiendo del tipo de objeto
	 * @return
	 */
	public abstract String calculaOrbita();
	
	public static int getNumObjetosCreados() {
		return numObjetosCreados;
	}
	public String getNombre() {
		return nombre;
	}
	/**
	 * @param nombre   Nombre nuevo que se quiere dar al objeto estelar
	 */
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public long getRadio() {
		return radio;
	}
	public void setRadio(long radio) {
		this.radio = radio;
	}	
	public static void main (String s[]) {
//		ObjetoEstelar o1 = new ObjetoEstelar( "Tierra", 6371000 );
//		o1.setNombre( "Urano" );
//		o1.setRadio( 25659000 );
//		System.out.println( o1.getNombre() + " - radio: " + o1.getRadio() );
	}
}
