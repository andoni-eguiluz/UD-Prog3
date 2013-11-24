package utils.verbos.expresiones;

public class Separador extends UnidadTextual {
	public Separador( String textoOriginal, int offsetInicio, int offsetFinal ) {
		super( textoOriginal, offsetInicio, offsetFinal );
	}
	public String toString() {
		return "_";
	}
}

