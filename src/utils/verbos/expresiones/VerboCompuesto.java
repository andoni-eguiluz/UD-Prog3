package utils.verbos.expresiones;
import utils.verbos.*;

/** VerboCompuesto.
 * Expresión que integra dos verbos simples: una derivación de haber , y un participio
 * @author andoni
 *
 */
public class VerboCompuesto extends Expresion implements VerboConjugado {
	private VerboSimple verbo1 = null;
	private VerboSimple verbo2 = null;
	
	/** pre: La expresión e tiene un verbo haber conjugado en la posición posIni, y un participio en la posFin
	 * pre: posIni < posFin, y en medio no hay nada o hay solo separadores
	 * Si no se cumplen, se produce la excepción
	 * @param e
	 * @param posTCinicio
	 * @param posTCfin
	 */
	public VerboCompuesto( Expresion e, int posIni, int posFin ) throws ExpresionVerboException {
		super( e.getTextoOriginalCompleto() );
		// Código de control de excepción:
			if (posIni < 0 || posIni >= e.numTCs()) throw new ExpresionVerboException( "Intento de crear VerboCompuesto con unidades no existentes: pos. de inicio " + posIni );
			if (posFin <= posIni || posFin >= e.numTCs()) throw new ExpresionVerboException( "Intento de crear VerboCompuesto con unidades no existentes: pos. de fin " + posFin );
			if (!(e.getTC(posIni) instanceof VerboSimple)) throw new ExpresionVerboException( "Intento de crear VerboCompuesto con inicio no verbal: " + e.getTC(posIni) );
			if (!(e.getTC(posFin) instanceof VerboSimple)) throw new ExpresionVerboException( "Intento de crear VerboCompuesto con fin no verbal: " + e.getTC(posFin) );
		verbo1 = (VerboSimple) (e.getTC(posIni));
			if (!(verbo1.getInfinitivoVerboPrincipal().equals("haber"))) throw new ExpresionVerboException( "Intento de crear VerboCompuesto con verbo auxiliar no HABER: " + verbo1 );
		verbo2 = (VerboSimple) (e.getTC(posFin));
		expresion.add( verbo1 );
		for (int inter = posIni+1; inter < posFin; inter++) {
			if (!(e.getTC(inter) instanceof Separador))
				throw new ExpresionVerboException( "Intento de crear VerboCompuesto con contenidos entre haber y el verbo principal: " + e.getTC(inter) );
			expresion.add( e.getTC(inter) );
		}
		expresion.add( verbo2 );
	}
	
	public Conjugacion getConjugacionHaber() {
		return verbo1.getConjugacion();
	}

	public Conjugacion getConjugacion() {
		return verbo1.getConjugacion();
	}
	
	public String getInfinitivoVerboPrincipal() {
		return verbo2.getInfinitivoVerboPrincipal();
	}
	public boolean esGerundio() {
		return verbo1.esGerundio();
	}
	public boolean esInfinitivo() {
		return verbo1.esInfinitivo();
	}
	public boolean esParticipio() {
		return verbo1.esParticipio();
	}
	public boolean esConjugado() {
		return !(esGerundio() || esInfinitivo() || esParticipio());
	}
	public boolean esReflexivo() {
		return verbo2.esReflexivo();
	}
	public String toString() {
		return "<" + getTextoUnidad() + ">" + "[VC:" + ("<" + verbo2.getInfinitivoVerboPrincipal() + ">/") + (verbo1.getConjugacion().showReduc()) + "]";
	}
}

