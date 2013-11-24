package utils.verbos.expresiones;
import utils.verbos.*;

public interface VerboConjugado extends VerboPrincipal {
	// public String getInfinitivoVerboPrincipal();  // de VerboPrincipal: Devuelve el infinitivo del verbo principal conjugado
	public boolean esInfinitivo();  // true si toda la unidad representa a un infinitivo (ej: "beber", "haber bebido", "beb�rtelo")
	public boolean esGerundio();  // true si toda la unidad representa a un gerundio (ej: "bebiendo", "habiendo bebido", "bebi�ndotelo")
	public boolean esParticipio();  // true si toda la unidad representa a un participio (ej: "bebido")
	public boolean esConjugado(); // true si no es infinitivo, gerundio ni participio
	public boolean esReflexivo();  // true si la unidad es una conjugaci�n de un verbo reflexivo (ej: "se lo come", "c�meselo")
	public Conjugacion getConjugacion();  // devuelve la conjugaci�n del verbo conjugado
	public int getOffsetIni();  // Devuelve el offset de inicio de la unidad conceptual
	public int getOffsetFin();  // Devuelve el offset de fin de la unidad conceptual
	public String getTextoUnidad();
}
