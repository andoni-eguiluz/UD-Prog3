package tema1;

public class DeathStar extends ObjetoEstelar implements Habitable {
	private long potenciaDestruccion;
	
	public DeathStar( String nombre, long radio, long potenciaDestruccion ) {
		super( nombre, radio );
		this.potenciaDestruccion = potenciaDestruccion;
	}
	
	public String calculaOrbita() {
		return "";
	}

	public long getNumHabitantes() {
		// Esto habría que hacerlo bien: Cálculo, atributo... 
		// Lo hacemos simplificando (para ilustrar el uso de un interfaz)
		return 15000L;
	}
	
	@Override
	public String toString() {
		return super.toString() + " (potencia de destrucción = " + potenciaDestruccion + ")";
	}

}
