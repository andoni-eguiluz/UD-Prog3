package utils.verbos.expresiones;

/** Palabra gen�rica sin tratamiento espec�fico diferenciado
 */
public class Palabra extends UnidadTextual {
	public Palabra( String textoOriginal, int offsetInicio, int offsetFinal ) {
		super( textoOriginal, offsetInicio, offsetFinal );
	}
	public String toString() {
		return txtUnidad;
	}
}

