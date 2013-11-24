package utils.verbos.expresiones;
import utils.verbos.*;

public class VerboSimple extends UnidadTextual implements VerboConjugado {
	private Conjugacion conj = null;
	public VerboSimple( String textoOriginal, int offsetInicio, int offsetFinal, Conjugacion conjugacion ) {
		super( textoOriginal, offsetInicio, offsetFinal );
		conj = conjugacion;
	}
	public String getInfinitivoVerboPrincipal() {
		return conj.getInfinitivo();
	}
	public boolean esGerundio() {
		return conj.esGerundio();
	}
	public boolean esInfinitivo() {
		return conj.esInfinitivo();
	}
	public boolean esParticipio() {
		return conj.esParticipio();
	}
	public boolean esConjugado() {
		return !(esGerundio() || esInfinitivo() || esParticipio());
	}
	public Conjugacion getConjugacion() {
		return conj;
	}
	public boolean esReflexivo() {
		return conj.getReflexivo();
	}	
	public String toString() {
		return txtUnidad + "[VS:" + ((conj==null)?"null":conj.showReduc()) + "]";
	}
}

