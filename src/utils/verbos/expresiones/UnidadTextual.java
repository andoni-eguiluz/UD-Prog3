package utils.verbos.expresiones;
import utils.verbos.*;

public class UnidadTextual implements TextoConceptual {
	private int offInicio;  // offset de inicio con respecto al texto original
	private int offFinal;  // offset de final (+1) con respecto al texto original
	protected String txtOriginal;  // referencia al texto original completo
	protected String txtUnidad;  // copia del texto de la unidad (parte del original)
	protected String txtMinusc;  // mismo texto en minúsculas (orientado a búsqueda)
	protected String txtSinTildes;  // mismo texto en minúsculas sin tildes (orientado a búsqueda)

	/** Crea una nueva unidad textual
	 * @param textoOriginal	Referencia al texto de origen
	 * @param offsetInicio	Offset del inicio de la unidad textual
	 * @param offsetFinal	Offset de final de la unidad textual (+1) (Referencia al primer carácter siguiente a esta unidad)
	 * 	(es decir, offsetFinal - offsetInicio = longitud de la unidad textual)
	 */
	public UnidadTextual( String textoOriginal, int offsetInicio, int offsetFinal ) {
		txtOriginal = textoOriginal; offInicio = offsetInicio; offFinal = offsetFinal;
		txtUnidad = txtOriginal.substring( offsetInicio, offsetFinal );
		txtMinusc = txtUnidad.toLowerCase();
		txtSinTildes = UtilsVerbos.quitarTildes( txtMinusc );
	}
	
	public String getTextoOriginalCompleto() { return txtOriginal; }
	public String getTextoUnidad() { return txtUnidad; }
	public String getTextoMinusc() { return txtMinusc; }
	public String getTextoSinTildes() { return txtSinTildes; }
	public int getOffsetIni() { return offInicio; }
	public int getOffsetFin() { return offFinal; }

	public String toString() {
		return txtUnidad + "[" + getClass().getSimpleName().substring(0,3).toLowerCase() + "]";
	}
	
}

// Ejemplo
//     "Me lo dices    si aciertas   , de repente y con valentía, a vivírmela con la intensidad de haber    estado   allí 1.322 veces"
//  ->  pr_ad_v<decir>_p _v<acertar>_s_p _p      _p_p  _p       s_p_v<vivir> _p  _ad_p         _p _v<haber>_v<estar>_p   _n    _p
//  ->  [--v<decir>--]_p _[--perif<vivir>-----------------------------------]_p  _ad_p         _p _[--vc<estar>----]_p   _n    _p

//  Jerarquía descendiente de UnidadTextual:
// s = Símbolo
// _ = Separador
// n = Numero
// pr = ParticulaReflexiva    { "me", "te", "se", "nos", "os" }
// ad = ArticuloDeterminado   { "la", "las", "le", "les", "lo", "los" }
// p = Palabra   (cualquier otra palabra sin sentido de cara al análisis verbal)
// v = VerboSimple  -- p ej. "estado", "vivírmela"  |  Implementa VerboConjugado | VerboPrincipal

// e = Expresión - varias palabras. Subclases de expresión:
//     f = Frase  -- toda la frase referenciada
//     vc = VerboCompuesto  -- p ej. "ha estado"  |  Implementa VerboConjugado | VerboPrincipal
//     fv = FormaVerbal  -- p ej. "me lo dijo"  |  Implementa VerboConjugado | VerboPrincipal
//     perif = PerifrasisVerbal  -- p ej. "aciertas, de repente y con valentía, a vivírmela"  |  Implementa VerboPrincipal
//     ev = ExpresionVerbal  -- p ej. "doy crédito a"  |  Implementa VerboPrincipal
//   
