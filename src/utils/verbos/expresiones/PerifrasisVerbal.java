package utils.verbos.expresiones;
import utils.verbos.*;

/** PerifrasisVerbal.
 * Expresión que integra dos verbos simples: uno auxiliar y uno principal, en una estructuración de frase determinada
 * @author andoni
 */
public class PerifrasisVerbal extends Expresion implements VerboConjugado {
	private VerboConjugado verboAux = null;
	private VerboConjugado verboPrinc = null;
	
	/** pre: La expresión e tiene forma de perífrasis verbal. Hay un verbo auxiliar en la posición posVAux y uno principal en la posVPrin
	 * pre: posIni < posFin, y en medio están posVAux y posVPrin. Puede haber otros elementos auxiliares y separadores
	 * Si no se cumplen las precondiciones, se produce la excepción
	 * @param e
	 * @param posTCIni
	 * @param posTCFin
	 * @param posVAux
	 * @param posVPrin
	 */
	public PerifrasisVerbal( Expresion e, int posTCIni, int posTCFin, int posVAux, int posVPrin ) throws ExpresionVerboException {
		super( e.getTextoOriginalCompleto() );
		// Código de control de excepción:
			if (posTCIni < 0 || posTCIni >= e.numTCs()) throw new ExpresionVerboException( "Intento de crear PerifrasisVerbal con unidades no existentes: pos. de inicio " + posTCIni );
			if (posTCFin <= posTCIni || posTCFin >= e.numTCs()) throw new ExpresionVerboException( "Intento de crear PerifrasisVerbal con unidades no existentes: pos. de fin " + posTCFin );
			if (posVAux < posTCIni || posVAux > posTCFin || posVPrin < posTCIni || posVPrin > posTCFin )
				 throw new ExpresionVerboException( "Intento de crear PerifrasisVerbal con unidades no existentes: pos. interiores " + posVAux + "," + posVPrin );
			if (!(e.getTC(posVAux) instanceof VerboConjugado)) throw new ExpresionVerboException( "Intento de crear PerifrasisVerbal con auxiliar no verbal: " + e.getTC(posVAux) );
			if (!(e.getTC(posVPrin) instanceof VerboConjugado)) throw new ExpresionVerboException( "Intento de crear PerifrasisVerbal con principal no verbal: " + e.getTC(posVPrin) );
		verboAux = (VerboConjugado) (e.getTC(posVAux));
		verboPrinc = (VerboConjugado) (e.getTC(posVPrin));
		for (int inter = posTCIni; inter <= posTCFin; inter++) expresion.add( e.getTC(inter) );
	}
	
	public Conjugacion getConjugacion() {
		return verboAux.getConjugacion();
	}
	
	public String getInfinitivoVerboPrincipal() {
		return verboPrinc.getInfinitivoVerboPrincipal();
	}
	public boolean esGerundio() {
		return verboAux.esGerundio();
	}
	public boolean esInfinitivo() {
		return verboAux.esInfinitivo();
	}
	public boolean esParticipio() {
		return verboAux.esParticipio();
	}
	public boolean esConjugado() {
		return !(esGerundio() || esInfinitivo() || esParticipio());
	}
	public boolean esReflexivo() {
		return verboAux.esReflexivo();
	}
	public String toString() {
		return "<" + getTextoUnidad() + ">" + "[PV:" + ("<" + verboPrinc.getInfinitivoVerboPrincipal() + ">/") + (verboAux.getConjugacion().showReduc()) + "]";
	}
}

