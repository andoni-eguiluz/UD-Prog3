package utils.verbos.expresiones;
import utils.verbos.*;

/** ExpresionVerbal.
 * Expresión que contiene un verbo conjugado (de cualquier tipo: simple, compuesto, forma verbal o perífrasis verbal) 
 * con partículas adicionales no verbales (como el phrasal verb en inglés), con las 
 * que el verbo adopta un significado particular. Ejemplo: "darse por [adjetivo]", "ha dado por muerto", "lo hemos dado por muerto", "me lo dieron por perdido"
 * @author andoni
 */
public class ExpresionVerbal extends Expresion implements VerboConjugado {
	private VerboConjugado verbo = null;
	private boolean esReflexivo = false;
	
	/** pre: posIni <= posV <= posFin, y la expresión e tiene un verbo conjugado simple, compuesto o forma verbal, en la posición posV
	 * Si no se cumplen las precondiciones, se produce la excepción
	 * @param e
	 * @param posTCini
	 * @param posTCfin
	 * @param posV
	 */
	public ExpresionVerbal( Expresion e, int posTCIni, int posTCFin, int posV ) throws ExpresionVerboException {
		super( e.getTextoOriginalCompleto() );
		// Código de control de excepción:
			if (posTCIni < 0 || posTCIni >= e.numTCs()) throw new ExpresionVerboException( "Intento de crear ExpresionVerbal con unidades no existentes: pos. de inicio " + posTCIni );
			if (posTCFin <= posTCIni || posTCFin >= e.numTCs()) throw new ExpresionVerboException( "Intento de crear ExpresionVerbal con unidades no existentes: pos. de fin " + posTCFin );
			if (posV < posTCIni || posV > posTCFin ) throw new ExpresionVerboException( "Intento de crear ExpresionVerbal con unidad no existentes: pos. interior " + posV );
			if (!(e.getTC(posV) instanceof VerboConjugado)) throw new ExpresionVerboException( "Intento de crear ExpresionVerbal con contenido no verbal: " + e.getTC(posTCFin) );
		verbo = (VerboConjugado) e.getTC(posV);
		if (verbo.esReflexivo()) esReflexivo = true;
		for (int inter = posTCIni; inter <= posTCFin; inter++)
			expresion.add( e.getTC(inter) );
	}
	
	public Conjugacion getConjugacion() {
		return verbo.getConjugacion();
	}
	
	public String getInfinitivoVerboPrincipal() {
		return verbo.getInfinitivoVerboPrincipal();
	}
	
	public boolean esGerundio() {
		return verbo.esGerundio();
	}
	public boolean esInfinitivo() {
		return verbo.esInfinitivo();
	}
	public boolean esParticipio() {
		return verbo.esParticipio();
	}
	public boolean esConjugado() {
		return !(esGerundio() || esInfinitivo() || esParticipio());
	}
	public boolean esReflexivo() {
		return esReflexivo;
	}
	public String toString() {
		return "<" + getTextoUnidad() + ">" + "[EV:" + (verbo.getConjugacion().showReduc()) + "]";
	}
}

