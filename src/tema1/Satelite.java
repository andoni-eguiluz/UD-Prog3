package tema1;

public class Satelite extends ObjetoEstelar {
	private Planeta planetaDelQueOrbito;
	public Planeta getPlanetaDelQueOrbito() {
		return planetaDelQueOrbito;
	}
	public void setPlanetaDelQueOrbito(Planeta planetaDelQueOrbito) {
		this.planetaDelQueOrbito = planetaDelQueOrbito;
	}
	public Satelite( String nombre, long radio, Planeta planetaDelQueOrbito ) {
		super( nombre, radio );
		this.planetaDelQueOrbito = planetaDelQueOrbito;
	}
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return nombre;
	}
	public String calculaOrbita() {
		// cálculos
		return "órbita del satélite";
	}
	
}
