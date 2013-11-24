package utils.verbos.expresiones;

public class Simbolo extends UnidadTextual {
	public Simbolo( String textoOriginal, int offsetInicio, int offsetFinal ) {
		super( textoOriginal, offsetInicio, offsetFinal );
	}
	public String toString() {
		return "[" + txtUnidad + "]";
	}
}

