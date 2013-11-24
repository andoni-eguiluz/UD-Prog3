package utils.verbos.expresiones;

/** Palabra genérica sin tratamiento específico diferenciado
 */
public class Palabra extends UnidadTextual {
	public Palabra( String textoOriginal, int offsetInicio, int offsetFinal ) {
		super( textoOriginal, offsetInicio, offsetFinal );
	}
	public String toString() {
		return txtUnidad;
	}
}

