package tema1;

public class Planeta extends ObjetoEstelar implements Habitable {
	private long distanciaMediaAlSolEnMetros;
	static private int numPlanetas;

	public Planeta() {
		// constructor de la clase padre
		super( "", 0L );
		numPlanetas++;
	}
	public Planeta( String nombre, long radio, long pDistMediaAlSolEnMetros ) {
		super( nombre, radio );
		distanciaMediaAlSolEnMetros = pDistMediaAlSolEnMetros;
		numPlanetas++;
	}

	public static int getNumPlanetas() {
		return numPlanetas;
	}

	public static void setNumPlanetas(int numPlanetas) {
		Planeta.numPlanetas = numPlanetas;
	}

	@Override
	public boolean equals( Object arg0 ) {
		if (arg0 instanceof Planeta) {
			// return this.nombre.equals( ((Planeta)arg0).nombre );
			Planeta temp = (Planeta) arg0;
			return this.nombre.equals( temp.nombre );
			// ERROR: return this.nombre ==  temp.nombre;
		}
		else
			return false;
	}
	
	@Override
	public String toString() {
		return nombre + " (dist. media al sol = " + distanciaMediaAlSolEnMetros + " m.)"; 
		// + super.toString(); // si quisiéramos aprovechar el método ya existente en el padre
	}

	public String calculaOrbita() {
		// cálculos
		return "órbita del planeta";
	}

	public long getNumHabitantes() {
		// Cálculo, atributo... lo hacemos simplificando
		return 7000000000L;
	}
	
}
