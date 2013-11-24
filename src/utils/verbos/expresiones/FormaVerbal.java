package utils.verbos.expresiones;
import utils.verbos.*;

/** FormaVerbal (de prefijo).
 * Expresión que integra un verbo conjugado (simple o compuesto) con partículas prefijas de artículo determinado y/o partícula reflexiva.
 * Por ejemplo: me dijo, lo sabe, te la paso, me lo ha dicho...
 * @author andoni
 */
public class FormaVerbal extends Expresion implements VerboConjugado {
	private VerboConjugado verbo = null;  // VerboSimple o VerboConjugado
	private TextoConceptual tc1 = null;
	private TextoConceptual tc2 = null;
	private boolean esReflexivo = false;
	
	/** pre: La expresión e tiene un verbo simple conjugado en la posición posFin, y entre posIni y posFin hay:
	 * (1): un ArticuloDeterminado (2): una ParticulaReflexiva (3): (2)+(1)
	 * pre: posIni < posFin, y entre unidades hay nada o solo separadores
	 * Si no se cumplen, se produce la excepción
	 * @param e
	 * @param posTCinicio
	 * @param posTCfin
	 */
	public FormaVerbal( Expresion e, int posIni, int posFin ) throws ExpresionVerboException {
		super( e.getTextoOriginalCompleto() );
		// Código de control de excepción:
			if (posIni < 0 || posIni >= e.numTCs()) throw new ExpresionVerboException( "Intento de crear FormaVerbal con unidades no existentes: pos. de inicio " + posIni );
			if (posFin <= posIni || posFin >= e.numTCs()) throw new ExpresionVerboException( "Intento de crear FormaVerbal con unidades no existentes: pos. de fin " + posFin );
			if (!(e.getTC(posFin) instanceof VerboSimple || e.getTC(posFin) instanceof VerboCompuesto)) throw new ExpresionVerboException( "Intento de crear FormaVerbal con fin no verbal: " + e.getTC(posFin) );
			verbo = (VerboConjugado) e.getTC(posFin);
			int pos1 = posIni;  // pos1 debe estar en el primer elemento significativo
			tc1 = e.getTC(pos1);
			int pos2 = pos1 + 1;
			while (pos2 < posFin && e.getTC(pos2) instanceof Separador) pos2++;   // pos2 acaba en el segundo elemento significativo o en posFin
			tc2 = (pos2<posFin)?e.getTC(pos2):null;
			int posInt = pos2 + 1;
			while (posInt < posFin && e.getTC(posInt) instanceof Separador) posInt++;   // posInt acaba en el tercer elemento significativo si lo hay (error)
			if (posInt < posFin) throw new ExpresionVerboException( "Intento de crear FormaVerbal con demasiados elementos intermedios: " + e.getTC(posInt) );
			if ((tc2 != null) && !((tc2 instanceof ArticuloDeterminado) && (tc1 instanceof ParticulaReflexiva)))
				throw new ExpresionVerboException( "Intento de crear FormaVerbal con dos partículas mal estructuradas: " + tc1 + " / " + tc2 );
			if ((tc2 == null) && !(tc1 instanceof ArticuloDeterminado || tc1 instanceof ParticulaReflexiva))
				throw new ExpresionVerboException( "Intento de crear FormaVerbal con partícula incorrecta: " + tc1 );
		if (tc2!=null || tc1 instanceof ParticulaReflexiva) esReflexivo = true;
		if (verbo.esReflexivo()) esReflexivo = true;
		// TODO: Aquí podría comprobarse una formación incorrecta si hay reflexivo como prefijo y como sufijo (pero no estamos valorando escritura incorrecta)
		for (int inter = posIni; inter <= posFin; inter++)
			expresion.add( e.getTC(inter) );
	}
	
	public Conjugacion getConjugacion() {
		return verbo.getConjugacion();
	}
	
	public String getInfinitivoVerboPrincipal() {
		return verbo.getInfinitivoVerboPrincipal();
	}
	
	/** Devuelve el artículo previo, si existe
	 * @return	Artículo o null
	 */
	public ArticuloDeterminado getArticulo() {
		if (tc2 != null && tc2 instanceof ArticuloDeterminado) return (ArticuloDeterminado) tc2;
		if (tc1 != null && tc1 instanceof ArticuloDeterminado) return (ArticuloDeterminado) tc1;
		return null;
	}

	/** Devuelve el reflexivo previo, si existe
	 * @return	Reflexivo o null
	 */
	public ParticulaReflexiva getReflexivo() {
		if (tc1 != null && tc1 instanceof ParticulaReflexiva) return (ParticulaReflexiva) tc1;
		return null;
	}
	
	public boolean esGerundio() {
		// TODO: Aquí podría comprobarse una formación incorrecta si hay gerundio (no admite partículas) (pero no estamos valorando escritura incorrecta)
		return verbo.esGerundio();
	}
	public boolean esInfinitivo() {
		// TODO: Aquí podría comprobarse una formación incorrecta si hay infinitivo (no admite partículas) (pero no estamos valorando escritura incorrecta)
		return verbo.esInfinitivo();
	}
	public boolean esParticipio() {
		// TODO: Aquí podría comprobarse una formación incorrecta si hay participio (no admite partículas) (pero no estamos valorando escritura incorrecta)
		return verbo.esParticipio();
	}
	public boolean esConjugado() {
		return !(esGerundio() || esInfinitivo() || esParticipio());
	}
	public boolean esReflexivo() {
		return esReflexivo;
	}
	public String toString() {
		return "<" + getTextoUnidad() + ">" + "[FV:" + 
			(tc1.getTextoMinusc() + ((tc2==null)?"":("/"+tc2.getTextoMinusc())) + "/") + (verbo.getConjugacion().showReduc()) + "]";
	}
}

